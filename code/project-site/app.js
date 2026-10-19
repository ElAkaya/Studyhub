const express = require('express');
const path = require('path');
const app = express();

app.set('view engine', 'ejs');
app.set('views', path.join(__dirname, 'views'));

app.use(express.static(path.join(__dirname, 'public')));

// Main pages
app.get('/', (req, res) => {
  res.render('page/index');
});

app.get('/documentation', (req, res) => {
  res.render('page/documentation');
});

app.get('/quickstart', (req, res) => {
  res.render('page/quickstart');
});

// One page for each implemented StudyHub feature
const features = [
  'browse-courses',
  'browse-groups',
  'view-details',
  'search',
  'create-group',
  'delete-group'
];

features.forEach((feature) => {
  app.get('/features/' + feature, (req, res) => {
    res.render('page/' + feature);
  });
});

app.use((req, res) => {
  res.status(404).send('Page not found. Return to <a href="/">StudyHub home</a>.');
});

app.listen(8080, () => {
  console.log('StudyHub project site: http://localhost:8080');
});
