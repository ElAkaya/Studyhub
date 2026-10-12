# Project 1 MVP scope

Status: Instructor approval reported by Moria El Akaya on October 11, 2026. US1–US6 are implemented and verified. Final release review and submission packaging are being completed.

## Product and goal
StudyHub is a campus study-group finder. This MVP lets a student find a study group for a course and inspect its meeting information using a local JavaFX desktop application.

## Included stories and acceptance criteria
Story references below use the row numbers of the existing Product Backlog.xlsx (row 1 is the header). The original product backlog remains unchanged.

| ID | Source row | Included behavior | Acceptance criteria |
| --- | --- | --- | --- |
| US1 | 2 | Browse courses | Show a small, explicitly labeled demonstration semester course catalog. Selecting a course filters the groups. No university integration is implied. |
| US2 | 3 | Browse groups for a course | Show only the selected course's groups, with a clear empty state and an option to show all groups. |
| US3 | 4 | View group details | Open a separate details window showing name, course, description, meeting time, location, and capacity. Require a selected group before opening. |
| US4 | 5 | Search by course code | Trim input and match case-insensitively. Empty input restores the current course's full list; no matches shows an empty state. Course selection and search combine. |
| US5 | 12, 16 | Create a group with capacity | Keep a separate create window. Require a catalog course, nonblank name/description/time/location and positive integer capacity. Invalid input preserves the form and reports the problem. Saving updates the shared group collection; Cancel changes nothing. |
| US6 | 14 | Delete a group | Confirm before removal. Cancel preserves the group, and the displayed list refreshes after confirmation. This local demonstration has no leader authorization. |

## Windows and responsibilities
1. Main window: course selector, course-code search, group list, View Details, Create, Delete.
2. Create window: validated input fields, Save and Cancel.
3. Details window: read-only selected group information and Close.

Domain and service classes own validation, filtering and collection changes. FXML codebehind only reads controls, delegates work, displays results/errors and opens windows. Use JavaFX, Maven, JUnit 5 and the existing course Checkstyle file.

## Explicit limitations
Single-user local demonstration with in-memory data. Changes reset when the application closes. Meeting time is descriptive text, not a scheduling or conflict-detection engine. Demo course data is not an official university catalog. Authentication, accounts, joining/leaving, announcements, editing, invitations, moderation, persistence, networking and deployment are deferred. Instructor approval must explicitly accept these boundaries, particularly the demonstration catalog and absence of leader authorization.

## Definition of done
- Instructor approves this scope and the sprint backlog before feature coding begins.
- Each approved task uses a feature branch created from dev and merged into dev, with scoped commits using a roughly 50-character subject and explanatory body.
- Maven clean verify succeeds: compilation, all tests, unchanged course Checkstyle rules, at least 95% line coverage in each included package.
- Only Main and view/codebehind classes are excluded from coverage. All significant domain/service logic has tests, including invalid inputs and boundary cases.
- Manual window/navigation/error checks are recorded honestly.
- CI runs for pushes to dev and PRs targeting release; release protection blocks direct pushes and requires the build check.
- Final dev-to-release PR describes delivered scope and verification evidence.
- Submission contains real GitHub screenshots and an archive of the local repository folder after mvn clean.

## Approval record
Instructor decision: approved, as confirmed by Moria El Akaya on October 11, 2026.
Approved scope/version: the proposed US1–US6 scope and associated sprint backlog.
Approval evidence: the instructor's original approval communication is not attached to this repository.
Changes requested: no scope changes were reported in the approval confirmation.

