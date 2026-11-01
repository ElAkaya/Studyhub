// Route-level tests: start the real server against a temporary SQLite file.
const { mkdtempSync, rmSync } = require('node:fs');
const { tmpdir } = require('node:os');
const { join } = require('node:path');
const { createApp } = require('../app');

let dir, file, app, base;

async function start() {
  app = createApp(file);
  await new Promise(resolve => app.server.listen(0, '127.0.0.1', resolve));
  base = `http://127.0.0.1:${app.server.address().port}`;
}
const stop = () => new Promise(resolve => app.server.close(resolve));
const get = (path, cookie = '') => fetch(base + path, { redirect: 'manual', headers: { cookie } });
const post = (path, username, password, cookie = '') => fetch(base + path, {
  method: 'POST', redirect: 'manual',
  headers: { 'content-type': 'application/x-www-form-urlencoded', cookie },
  body: new URLSearchParams({ username, password })
});
const count = () => app.db.prepare('SELECT count(*) AS n FROM users').get().n;
const cookieFrom = res => res.headers.get('set-cookie').split(';')[0];

beforeAll(async () => {
  dir = mkdtempSync(join(tmpdir(), 'login-app-'));
  file = join(dir, 'test.db');
  await start();
});
afterAll(async () => {
  await stop();
  rmSync(dir, { recursive: true, force: true });
});

describe('login application routes', () => {
  test('users table has exactly username and password columns', () => {
    const cols = app.db.prepare('PRAGMA table_info(users)').all().map(c => c.name);
    expect(cols).toEqual(['username', 'password']);
  });

  test('login and register pages link to each other', async () => {
    for (const [path, link] of [['/login', '/register'], ['/register', '/login']]) {
      const res = await get(path);
      expect(res.status).toBe(200);
      const html = await res.text();
      expect(html).toContain(`href="${link}"`);
      expect(html).toContain('type="password"');
    }
  });

  test('landing page redirects to login when not signed in', async () => {
    expect((await get('/landing')).headers.get('location')).toBe('/login');
  });

  test('invalid account creation shows an error and writes nothing', async () => {
    const res = await post('/register', '', '');
    expect(res.status).toBe(400);
    expect(await res.text()).toContain('class="error"');
    expect(count()).toBe(0);
  });

  test('valid account creation saves to DB and redirects to landing', async () => {
    const res = await post('/register', 'Alex Student', 'Study123!');
    expect(res.status).toBe(303);
    expect(res.headers.get('location')).toBe('/landing');
    expect(count()).toBe(1);
    const landing = await (await get('/landing', cookieFrom(res))).text();
    expect(landing).toMatch(/Welcome, <span>Alex Student/);
  });

  test('duplicate account shows an error and does not change DB', async () => {
    const res = await post('/register', 'alex student', 'other');
    expect(res.status).toBe(409);
    expect(await res.text()).toMatch(/already exists/);
    expect(count()).toBe(1);
  });

  test('failed login stays on the login page with an error', async () => {
    const res = await post('/login', 'Alex Student', 'wrong');
    expect(res.status).toBe(401);
    expect(await res.text()).toMatch(/Incorrect username or password/);
    expect(res.headers.get('set-cookie')).toBeNull();
  });

  test('successful login redirects to landing showing the user name', async () => {
    const res = await post('/login', '  alex student ', 'Study123!');
    expect(res.status).toBe(303);
    const landing = await (await get('/landing', cookieFrom(res))).text();
    expect(landing).toContain('Alex Student');
  });

  test('logout ends the session', async () => {
    const cookie = cookieFrom(await post('/login', 'Alex Student', 'Study123!'));
    expect((await post('/logout', '', '', cookie)).status).toBe(303);
    expect((await get('/landing', cookie)).headers.get('location')).toBe('/login');
  });

  test('user names are HTML-escaped on the landing page', async () => {
    const name = "<script>alert('x')</script>";
    const res = await post('/register', name, 'password');
    const html = await (await get('/landing', cookieFrom(res))).text();
    expect(html).not.toContain(name);
    expect(html).toContain('&lt;script&gt;');
  });

  test('unknown routes return 404', async () => {
    expect((await get('/missing')).status).toBe(404);
  });

  test('accounts persist after the server restarts', async () => {
    await stop();
    await start();
    expect((await post('/login', 'Alex Student', 'Study123!')).status).toBe(303);
  });
});
