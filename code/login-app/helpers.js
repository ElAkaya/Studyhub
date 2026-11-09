'use strict';
// Login and account-creation logic, separated from the HTTP server so it can
// be unit tested without starting the app or opening the real database.
const { randomBytes, scryptSync, timingSafeEqual } = require('node:crypto');

const USERNAME_MAX = 60;
const PASSWORD_MAX = 128;
const SESSION_TTL_MS = 60 * 60 * 1000;

const MESSAGES = Object.freeze({
  invalid: 'Enter a name (1–60 characters) and a password (1–128 characters). Neither can be blank.',
  duplicate: 'That username already exists. Choose another name or log in.',
  badLogin: 'Incorrect username or password. Please try again.'
});

const HTML_ESCAPES = { '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' };
function escapeHtml(value) {
  return String(value).replace(/[&<>"']/g, c => HTML_ESCAPES[c]);
}

// Turns a URL-encoded form body into trimmed credentials.
// The password is intentionally not trimmed (passwords are exact).
function parseCredentials(body) {
  const values = new URLSearchParams(body || '');
  return {
    username: (values.get('username') || '').trim(),
    password: values.get('password') || ''
  };
}

// Returns an error message when the credentials are not acceptable, otherwise null.
function validateCredentials(username, password) {
  if (typeof username !== 'string' || typeof password !== 'string') return MESSAGES.invalid;
  if (!username || username.length > USERNAME_MAX) return MESSAGES.invalid;
  if (/[\x00-\x1f\x7f]/.test(username)) return MESSAGES.invalid;
  if (!password.trim() || password.length > PASSWORD_MAX) return MESSAGES.invalid;
  return null;
}

function hashPassword(password, salt = randomBytes(16).toString('hex')) {
  return salt + ':' + scryptSync(password, salt, 64).toString('hex');
}

function verifyPassword(password, stored) {
  if (typeof stored !== 'string' || !stored.includes(':')) return false;
  const [salt, hash] = stored.split(':');
  const expected = Buffer.from(hash, 'hex');
  const actual = scryptSync(password, salt, 64);
  return expected.length === actual.length && timingSafeEqual(expected, actual);
}

// store = { findUser(username) -> {username, password} | undefined, addUser(username, hash) }
function registerAccount(store, username, password) {
  const error = validateCredentials(username, password);
  if (error) return { ok: false, status: 400, error };
  if (store.findUser(username)) return { ok: false, status: 409, error: MESSAGES.duplicate };
  store.addUser(username, hashPassword(password));
  return { ok: true, status: 303, username };
}

function authenticateAccount(store, username, password) {
  const error = validateCredentials(username, password);
  if (error) return { ok: false, status: 400, error };
  const user = store.findUser(username);
  if (!user || !verifyPassword(password, user.password)) {
    return { ok: false, status: 401, error: MESSAGES.badLogin };
  }
  return { ok: true, status: 303, username: user.username };
}

// Reads the session id out of a Cookie header, or returns null.
function parseSessionId(cookieHeader) {
  const match = /(?:^|;\s*)sid=([a-f0-9]{64})(?:;|$)/.exec(cookieHeader || '');
  return match ? match[1] : null;
}

// In-memory sessions with expiry. `now` can be replaced in tests.
function createSessionStore({ ttlMs = SESSION_TTL_MS, now = () => Date.now() } = {}) {
  const sessions = new Map();
  return {
    create(username) {
      for (const [id, value] of sessions) if (value.expires < now()) sessions.delete(id);
      const id = randomBytes(32).toString('hex');
      sessions.set(id, { username, expires: now() + ttlMs });
      return id;
    },
    get(id) {
      const entry = id ? sessions.get(id) : undefined;
      if (!entry) return null;
      if (entry.expires < now()) { sessions.delete(id); return null; }
      return { id, username: entry.username };
    },
    destroy(id) { sessions.delete(id); },
    size() { return sessions.size; }
  };
}

module.exports = {
  USERNAME_MAX, PASSWORD_MAX, SESSION_TTL_MS, MESSAGES,
  escapeHtml, parseCredentials, validateCredentials,
  hashPassword, verifyPassword,
  registerAccount, authenticateAccount,
  parseSessionId, createSessionStore
};
