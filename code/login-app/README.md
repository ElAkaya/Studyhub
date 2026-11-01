# StudyHub Login App — Lab 7 (CI and Testing with Node.js and Jest)

Login and account creation for StudyHub (Node.js + built-in SQLite), refactored
so the login logic lives in testable helper functions, with Jest tests and a
GitHub Actions CI pipeline.

## Versions (must match CI exactly)

- Node.js 24.13.0
- npm 11.14.0

These are pinned in `package.json` (`engines`) and in
`.github/workflows/login-app-ci.yml`. `.npmrc` sets `engine-strict=true`, so
`npm install` / `npm ci` fail if a different Node or npm version is used.

## Run

    npm install
    npm start

Open http://localhost:8080. Press Control+C to stop.
If port 8080 is busy: `PORT=8081 npm start`.

## Test

    npm test

Tests never touch `users.db`; the route tests use a temporary database.

## Structure

| File | Purpose |
|---|---|
| `app.js` | HTTP server, routes, and HTML pages |
| `helpers.js` | Login logic: input parsing, validation, password hashing/verification, account registration, authentication, session cookie parsing, session store |
| `test/helpers.test.js` | Unit tests for every helper (no server, no real DB; uses a fake user store and a fake clock) |
| `test/app.test.js` | Route tests: real server on a random port with a temporary SQLite file |
| `users.db` | SQLite database, table `users(username, password)` |

## Refactoring summary

Logic that previously lived inside the request handler in `app.js` was moved
into `helpers.js`:

- `parseCredentials(body)` – reads and trims form input
- `validateCredentials(username, password)` – length/blank/control-character rules
- `hashPassword` / `verifyPassword` – salted scrypt hashing
- `registerAccount(store, username, password)` – validation + duplicate check + insert
- `authenticateAccount(store, username, password)` – validation + lookup + password check
- `parseSessionId(cookieHeader)` and `createSessionStore()` – session handling with expiry
- `escapeHtml(value)` – output escaping

`registerAccount` and `authenticateAccount` take a small `store` object
(`findUser`, `addUser`) instead of the database directly, so tests can pass an
in-memory fake. `createSessionStore` accepts a `now` function so expiry can be
tested without waiting.

## CI

`.github/workflows/login-app-ci.yml` runs on every push and on pull requests
to `dev` and `release` when files under `code/login-app/` change. It installs
Node 24.13.0 and npm 11.14.0, verifies both versions, runs `npm ci`, then
`npm test`.
