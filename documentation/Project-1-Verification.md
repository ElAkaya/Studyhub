# Project 1 verification

Verified on October 11, 2026 for the completed US1–US6 implementation.

## Build and tests

The student ran `mvn -f code/studyhub-app/pom.xml clean verify` on macOS with Java 17 and Maven 3.10.0. The development environment also verified the build with OpenJDK 17 and Maven 3.9.9.

- Compilation: BUILD SUCCESS.
- JUnit: 95 tests, zero failures, errors, or skipped tests.
- Course Checkstyle: zero violations; the course devops_checks.xml remains unchanged.
- JaCoCo included code: 75/75 lines and 32/32 branches covered (100%) in the development build.
- Required package line coverage gate: 95%.
- Coverage exclusions: Main.class and view/codebehind/** only.
- Tested classes: Course, CourseCatalog, StudyGroup, and GroupManager.
- Tests cover invalid constructor inputs, whitespace normalization, catalog membership, capacity parsing and boundaries, combined filtering, immutable snapshots, and deletion behavior.

JavaFX dependency metadata and an optional repository certificate warning appeared on the student's machine; they did not prevent the verified build from succeeding.

## Window and interaction checks

The student launched the application on macOS using `mvn -f code/studyhub-app/pom.xml javafx:run` and confirmed:

| Check | Observed result |
| --- | --- |
| Main window starts | Course selector, search, empty state and actions display |
| Invalid capacity abc | Error shown; form stays open with entered values preserved |
| Correct capacity to 5 and save | Create window closes; new group appears in main list |
| Open selected group's details | Name, code, description, time, location and capacity are correct |
| Lowercase course-code search | Matching group appears |
| Unknown course-code search | Empty result state appears |
| Clear search | Group returns |
| Select MATH101, then CS101 | Other-course group hidden; matching group returns |
| Show all | Selection/search reset and group appears |
| Cancel deletion | Group remains |
| Confirm deletion | Group removed and count becomes zero |
| Cancel partially entered create form | No group added |

An additional temporary JavaFX interaction harness in the development environment loaded all three FXML windows and exercised creation validation, preserved input, Cancel, search/filter combination, every details field, and both deletion choices. Those checks passed. The temporary harness and its headless toolkit are not application dependencies.

## GitHub and submission status

- Feature PRs #3, #4, #5 and #6 targeted dev, passed the Java CI with Maven build check, and were merged.
- CI is configured for pushes to dev and pull requests targeting dev or release.
- The release protection settings were inspected during repository preparation: pull request required, one approving review, build check required, branches up to date, no bypass, no force pushes or branch deletion.
- Final release review and submission packaging must be completed after this documentation update.
- The submission must include genuine release-protection and final dev-to-release PR screenshots, plus a ZIP of the complete local repository folder after Maven clean, inside one submission ZIP.
