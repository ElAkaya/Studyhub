# Project 1 sprint backlog

Status as of October 11, 2026: instructor approval confirmed by the student; T1–T10 complete. T11 documentation is updated; final release review and submission packaging remain.

Estimates are planning hours, not time already spent. Weekly remaining-work fields remain blank because no weekly checkpoint observations were recorded. Completion below records verified outcomes, not retrospective time estimates. This follows the supplied template's story/task/initial-estimate/weekly-remaining structure.

| Task | Story | Task description | Initial hours | Dependencies | Week 1 remaining | Week 2 remaining | Week 3 remaining | Week 4 remaining |
| --- | --- | --- | ---: | --- | ---: | ---: | ---: | ---: |
| P1 | All | Review scope and obtain instructor approval | 0.5 | None | | | | |
| T1 | US1 | Add course catalog model/service and unit tests | 1.5 | P1 | | | | |
| T2 | US3, US5 | Extend group details and constructor validation with tests | 2 | P1 | | | | |
| T3 | US2, US4 | Implement shared group collection and combined course/search filtering with tests | 2 | T1, T2 | | | | |
| T4 | US5, US6 | Implement create/delete service operations and invalid-input tests | 2 | T2, T3 | | | | |
| T5 | US1, US2, US4 | Wire main FXML course selector, search and group list | 2 | T3 | | | | |
| T6 | US5 | Wire create window to service, preserve form on errors, refresh main list | 1.5 | T4, T5 | | | | |
| T7 | US3 | Add details FXML/window and selected-group navigation | 1 | T2, T5 | | | | |
| T8 | US6 | Wire confirmation and deletion with selection guards | 0.5 | T4, T5 | | | | |
| T9 | All | Run regression tests and close meaningful coverage gaps to at least 95% | 1.5 | T6, T7, T8 | | | | |
| T10 | All | Manually verify all windows, empty states, cancel flows and validation | 1 | T9 | | | | |
| T11 | All | Update README, record actual sprint progress, create final release PR and submission | 1 | T10 | | | | |
| Total | | | 16.5 | | | | | |

## Working process
Before coding each task, create feature/<task-name> from current dev. Write relevant tests incrementally. Run mvn clean verify before merging back to dev. Commit one coherent task at a time; use a body to explain why/how and reference the prior commit when correcting it. Record actual remaining hours at each weekly checkpoint, then chart total remaining hours if a burndown is requested. Estimates may change; record the reason instead of altering past observations.

## Preparation already supplied
Existing base app and product backlog preserved. Proposed scope and backlog added. Coverage gate increased from 90% to 95%, exact Main/codebehind exclusions applied, and Java compilation aligned with the CI JDK 17. These preparation changes do not represent implementation of T1–T11.

## Completion record — October 11, 2026

| Task | Status | Evidence |
| --- | --- | --- |
| P1 | Approval confirmed by student | See scope approval record; original communication is separate |
| T1 | Complete | Course catalog and tests; PR #3 merged into dev |
| T2 | Complete | Meeting details and validation tests; PR #4 merged into dev |
| T3, T4 | Complete | GroupManager, search/filter/create/delete tests; PR #5 merged into dev |
| T5, T6, T7, T8 | Complete | Main, create and details windows; PR #6 merged into dev |
| T9 | Complete | 95 passing tests, zero Checkstyle violations, 100% included line coverage |
| T10 | Complete | macOS window and interaction checks recorded in Project-1-Verification.md |
| T11 | In progress | README and documentation updated; final dev-to-release PR review and local ZIP packaging remain |

Feature commits are scoped to the catalog, meeting details, group management, and connected windows. Tests were added with each domain feature and verified before its merge. No actual hours or historical weekly progress have been fabricated.
