'use strict';
// Node.js 24 includes SQLite; this app needs no third-party packages.
const http = require('node:http');
const { DatabaseSync } = require('node:sqlite');
const { join } = require('node:path');
const { readFileSync } = require('node:fs');
const {
  escapeHtml, parseCredentials, registerAccount, authenticateAccount,
  parseSessionId, createSessionStore
} = require('./helpers');

function page(title, content) {
  return `<!doctype html><html lang="en"><head><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>${title} | StudyHub</title><link rel="stylesheet" href="/styles.css"></head><body><header><a class="brand" href="/">StudyHub<span>ACCOUNT PORTAL</span></a><span class="tag">Lab 6 · Node.js + SQLite</span></header><main>${content}</main><footer>A local classroom application · Moria El Akaya</footer></body></html>`;
}
function formPage(register, error = '', username = '') {
  const title = register ? 'Create your account' : 'Welcome back';
  return page(title, `<section class="intro"><p class="eyebrow">YOUR NEXT STUDY SESSION STARTS HERE</p><h1>A place to<br>learn together.</h1><p>Create an account or sign in to your StudyHub account portal.</p><div class="mark" aria-hidden="true">S<span>H</span></div></section><section class="card"><p class="eyebrow">${register ? 'GET STARTED' : 'SIGN IN'}</p><h2>${title}</h2><p>${register ? 'Choose a name and password to get started.' : 'Enter your credentials to continue.'}</p>${error ? `<div class="error" role="alert">${escapeHtml(error)}</div>` : ''}<form method="post" action="/${register ? 'register' : 'login'}"><label for="username">Name / username</label><input id="username" name="username" type="text" autocomplete="username" required maxlength="60" value="${escapeHtml(username)}" aria-describedby="name-help"><small id="name-help">Use 1–60 characters. Spaces within your name are allowed.</small><label for="password">Password</label><input id="password" name="password" type="password" autocomplete="${register ? 'new-password' : 'current-password'}" required maxlength="128" aria-describedby="password-help"><small id="password-help">${register ? 'Use 1–128 characters; not only spaces.' : 'Passwords are case-sensitive.'}</small><button type="submit">${register ? 'Create account' : 'Log in'} <span aria-hidden="true">→</span></button></form><p class="switch">${register ? 'Already have an account? <a href="/login">Log in</a>' : 'New to StudyHub? <a href="/register">Create an account</a>'}</p></section>`);
}
async function readForm(req) {
  let body = '';
  for await (const chunk of req) {
    body += chunk;
    if (Buffer.byteLength(body) > 8192) throw new Error('FORM_TOO_LARGE');
  }
  return parseCredentials(body);
}

function createApp(dbPath = join(__dirname, 'users.db')) {
  const db = new DatabaseSync(dbPath);
  // Exactly the two columns requested by the assignment.
  db.exec('CREATE TABLE IF NOT EXISTS users (username TEXT PRIMARY KEY COLLATE NOCASE NOT NULL, password TEXT NOT NULL)');
  const findUser = db.prepare('SELECT username, password FROM users WHERE username = ?');
  const addUser = db.prepare('INSERT INTO users (username, password) VALUES (?, ?)');
  const store = {
    findUser: username => findUser.get(username),
    addUser: (username, hash) => addUser.run(username, hash)
  };
  const sessions = createSessionStore();
  const session = req => sessions.get(parseSessionId(req.headers.cookie));
  const server = http.createServer(async (req, res) => {
    res.setHeader('Cache-Control', 'no-store');
    res.setHeader('X-Content-Type-Options', 'nosniff');
    res.setHeader('Content-Security-Policy', "default-src 'self'; form-action 'self'; frame-ancestors 'none'; base-uri 'none'");
    const send = (status, html) => { res.writeHead(status, {'Content-Type':'text/html; charset=utf-8'}); res.end(html); };
    const redirect = target => { res.writeHead(303, {Location: target}); res.end(); };
    const path = new URL(req.url, 'http://localhost').pathname;
    try {
      if (req.method === 'GET' && path === '/styles.css') {
        res.writeHead(200, {'Content-Type':'text/css; charset=utf-8'});
        return res.end(readFileSync(join(__dirname, 'public/styles.css')));
      }
      if (req.method === 'GET' && path === '/') return redirect(session(req) ? '/landing' : '/login');
      if (req.method === 'GET' && ['/login', '/register'].includes(path)) return send(200, formPage(path === '/register'));
      if (req.method === 'GET' && path === '/landing') {
        const user = session(req);
        if (!user) return redirect('/login');
        return send(200, page('Your account', `<section class="card welcome"><p class="eyebrow">YOU'RE SIGNED IN</p><div class="check" aria-hidden="true">✓</div><h1>Welcome, <span>${escapeHtml(user.username)}</span>!</h1><p>You have successfully signed in to StudyHub.</p><div class="account"><small>YOUR USERNAME</small><strong>${escapeHtml(user.username)}</strong><p>Your account is saved in SQLite.</p></div><form method="post" action="/logout"><button type="submit">Log out</button></form></section>`));
      }
      if (req.method === 'POST' && path === '/logout') {
        const user = session(req);
        if (user) sessions.destroy(user.id);
        res.setHeader('Set-Cookie', 'sid=; HttpOnly; SameSite=Lax; Path=/; Max-Age=0');
        return redirect('/login');
      }
      if (req.method === 'POST' && ['/login', '/register'].includes(path)) {
        const register = path === '/register';
        if (!(req.headers['content-type'] || '').startsWith('application/x-www-form-urlencoded')) return send(415, formPage(register, 'Please submit the form on this page.'));
        const {username, password} = await readForm(req);
        const result = register
          ? registerAccount(store, username, password)
          : authenticateAccount(store, username, password);
        if (!result.ok) return send(result.status, formPage(register, result.error, username.slice(0, 60)));
        const previous = session(req);
        if (previous) sessions.destroy(previous.id);
        const id = sessions.create(result.username);
        res.setHeader('Set-Cookie', `sid=${id}; HttpOnly; SameSite=Lax; Path=/; Max-Age=3600`);
        return redirect('/landing');
      }
      send(404, page('Not found', '<section class="card"><h1>Page not found</h1><a href="/">Return home</a></section>'));
    } catch (error) {
      if (error.message === 'FORM_TOO_LARGE') return send(413, formPage(path === '/register', 'The form is too large. Please try again.'));
      console.error('Request failed:', error.message);
      send(500, formPage(path === '/register', 'Something went wrong. Please try again.'));
    }
  });
  server.on('close', () => db.close());
  return {server, db};
}
if (require.main === module) {
  const {server} = createApp();
  server.listen(Number(process.env.PORT || 8080), '127.0.0.1', () => console.log(`StudyHub Lab 6: http://localhost:${server.address().port}\nPress Control+C to stop.`));
  server.on('error', error => { console.error(error.code === 'EADDRINUSE' ? 'Port is busy. Stop the previous app or run PORT=8081 node app.js.' : error.message); process.exitCode = 1; });
  for (const signal of ['SIGINT', 'SIGTERM']) process.on(signal, () => server.close());
}
module.exports = {createApp};
