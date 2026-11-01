const {
  MESSAGES, escapeHtml, parseCredentials, validateCredentials,
  hashPassword, verifyPassword, registerAccount, authenticateAccount,
  parseSessionId, createSessionStore
} = require('../helpers');

// Simple in-memory stand-in for the SQLite users table.
function fakeStore(initial = []) {
  const rows = new Map(initial.map(u => [u.username.toLowerCase(), u]));
  return {
    rows,
    findUser: jest.fn(name => rows.get(name.toLowerCase())),
    addUser: jest.fn((name, hash) => rows.set(name.toLowerCase(), { username: name, password: hash }))
  };
}

describe('escapeHtml', () => {
  test('escapes all HTML special characters', () => {
    expect(escapeHtml(`<a href="x">'&'</a>`)).toBe('&lt;a href=&quot;x&quot;&gt;&#39;&amp;&#39;&lt;/a&gt;');
  });
  test('converts non-strings to text', () => {
    expect(escapeHtml(42)).toBe('42');
  });
});

describe('parseCredentials', () => {
  test('trims the username but keeps the password exact', () => {
    expect(parseCredentials('username=%20%20Alex%20&password=%20pw%20'))
      .toEqual({ username: 'Alex', password: ' pw ' });
  });
  test('missing fields become empty strings', () => {
    expect(parseCredentials('')).toEqual({ username: '', password: '' });
    expect(parseCredentials(undefined)).toEqual({ username: '', password: '' });
  });
});

describe('validateCredentials', () => {
  test('accepts a normal name and password', () => {
    expect(validateCredentials('Alex Student', 'Study123!')).toBeNull();
  });
  test('accepts the boundary lengths', () => {
    expect(validateCredentials('a'.repeat(60), 'p'.repeat(128))).toBeNull();
  });
  test.each([
    ['', 'password'],
    ['Name', ''],
    ['Name', '   '],
    ['a'.repeat(61), 'password'],
    ['Name', 'p'.repeat(129)],
    ['Bad\nName', 'password'],
    [undefined, 'password']
  ])('rejects username=%j password=%j', (u, p) => {
    expect(validateCredentials(u, p)).toBe(MESSAGES.invalid);
  });
});

describe('hashPassword / verifyPassword', () => {
  test('hash never contains the plain password and uses salt:hash format', () => {
    const stored = hashPassword('Study123!');
    expect(stored).not.toContain('Study123!');
    expect(stored).toMatch(/^[a-f0-9]{32}:[a-f0-9]{128}$/);
  });
  test('same password gets a different salt each time', () => {
    expect(hashPassword('same')).not.toBe(hashPassword('same'));
  });
  test('same salt gives the same hash', () => {
    expect(hashPassword('same', 'abc')).toBe(hashPassword('same', 'abc'));
  });
  test('verifies correct password and rejects wrong or differently-cased ones', () => {
    const stored = hashPassword('Study123!');
    expect(verifyPassword('Study123!', stored)).toBe(true);
    expect(verifyPassword('study123!', stored)).toBe(false);
    expect(verifyPassword('wrong', stored)).toBe(false);
  });
  test('malformed stored values are rejected instead of throwing', () => {
    expect(verifyPassword('x', undefined)).toBe(false);
    expect(verifyPassword('x', 'no-colon')).toBe(false);
  });
});

describe('registerAccount', () => {
  test('creates a new account with a hashed password', () => {
    const store = fakeStore();
    const result = registerAccount(store, 'Alex', 'Study123!');
    expect(result).toEqual({ ok: true, status: 303, username: 'Alex' });
    expect(store.addUser).toHaveBeenCalledTimes(1);
    const saved = store.rows.get('alex');
    expect(saved.password).not.toBe('Study123!');
    expect(verifyPassword('Study123!', saved.password)).toBe(true);
  });
  test('invalid input is rejected without touching the database', () => {
    const store = fakeStore();
    expect(registerAccount(store, '', 'pw')).toEqual({ ok: false, status: 400, error: MESSAGES.invalid });
    expect(store.findUser).not.toHaveBeenCalled();
    expect(store.addUser).not.toHaveBeenCalled();
  });
  test('duplicate username (any case) is rejected and not saved', () => {
    const store = fakeStore([{ username: 'Alex', password: hashPassword('a') }]);
    expect(registerAccount(store, 'ALEX', 'other')).toEqual({ ok: false, status: 409, error: MESSAGES.duplicate });
    expect(store.addUser).not.toHaveBeenCalled();
  });
});

describe('authenticateAccount', () => {
  const store = fakeStore([{ username: 'Alex Student', password: hashPassword('Study123!') }]);

  test('correct credentials succeed and return the stored name', () => {
    expect(authenticateAccount(store, 'alex student', 'Study123!'))
      .toEqual({ ok: true, status: 303, username: 'Alex Student' });
  });
  test('wrong password fails with a login error', () => {
    expect(authenticateAccount(store, 'Alex Student', 'nope'))
      .toEqual({ ok: false, status: 401, error: MESSAGES.badLogin });
  });
  test('unknown user gets the same error as a wrong password', () => {
    expect(authenticateAccount(store, 'Nobody', 'Study123!'))
      .toEqual({ ok: false, status: 401, error: MESSAGES.badLogin });
  });
  test('blank fields fail validation', () => {
    expect(authenticateAccount(store, 'Alex Student', '').status).toBe(400);
  });
});

describe('parseSessionId', () => {
  const id = 'a'.repeat(64);
  test('finds sid among other cookies', () => {
    expect(parseSessionId(`theme=dark; sid=${id}; other=1`)).toBe(id);
  });
  test('returns null for missing or malformed cookies', () => {
    expect(parseSessionId(undefined)).toBeNull();
    expect(parseSessionId('sid=short')).toBeNull();
    expect(parseSessionId(`notsid=${id}`)).toBeNull();
  });
});

describe('createSessionStore', () => {
  test('create, get, and destroy a session', () => {
    const sessions = createSessionStore();
    const id = sessions.create('Alex');
    expect(id).toMatch(/^[a-f0-9]{64}$/);
    expect(sessions.get(id)).toEqual({ id, username: 'Alex' });
    sessions.destroy(id);
    expect(sessions.get(id)).toBeNull();
  });
  test('unknown or empty ids return null', () => {
    const sessions = createSessionStore();
    expect(sessions.get(null)).toBeNull();
    expect(sessions.get('b'.repeat(64))).toBeNull();
  });
  test('sessions expire after the time limit', () => {
    let time = 1000;
    const sessions = createSessionStore({ ttlMs: 500, now: () => time });
    const id = sessions.create('Alex');
    time = 1500;
    expect(sessions.get(id)).not.toBeNull();
    time = 1501;
    expect(sessions.get(id)).toBeNull();
  });
  test('expired sessions are cleaned up when a new one is created', () => {
    let time = 0;
    const sessions = createSessionStore({ ttlMs: 10, now: () => time });
    sessions.create('Old');
    time = 100;
    sessions.create('New');
    expect(sessions.size()).toBe(1);
  });
});
