# StudyHub

## Purpose

StudyHub is a campus study-group finder for university students. Students browse the courses offered each semester, find study groups that meet for those courses, and join a group that fits their schedule. Any student can create a group for a course they are taking and act as its leader: managing membership, posting announcements, and keeping the meeting time and location up to date. Administrators can moderate groups and users to keep the platform safe. The goal is to make it easy for students to find classmates to study with instead of relying on word of mouth or luck.

## Value

I chose this project because I am interested in building tools that help college students connect with each other on campus. Study groups are one of the most useful things a student can be part of, but forming one usually depends on already knowing people in the class. A simple app that removes that barrier is something I would actually want to use, and it is a problem I understand from experience as a student. It is also a good fit for a semester-long project: the core idea is small enough to build, but there is a clear path of features (accounts, groups, announcements, moderation) to grow into as the course goes on.

## Project structure

- `code/` – application source code (Maven Java project)
- `documentation/` – project documentation, including the product backlog

## Development setup

Use JDK 17 and Maven. From the repository root, launch the application:

```bash
mvn -f code/studyhub-app/pom.xml javafx:run
```

The Project 1 MVP offers a labeled demo course catalog, course selection and code search, group creation, read-only meeting details, and confirmed deletion. It uses three JavaFX windows. Select a group to enable View details and Delete group.

Group data is in memory and resets when the app closes. The broader product vision above includes future features; accounts, joining groups, announcements, persistence and moderation are outside this MVP.

See documentation/Project-1-MVP-Scope.md and documentation/Project-1-Sprint-Backlog.md for the scope and tasks.

## Test setup

Run the build, unit tests, course Checkstyle and coverage gate:

```bash
mvn -f code/studyhub-app/pom.xml clean verify
```

Coverage must reach at least 95% lines in each included package. Only Main and FXML codebehind classes are excluded. The HTML coverage report is generated at code/studyhub-app/target/site/jacoco/index.html. See documentation/Project-1-Verification.md for observed results.
