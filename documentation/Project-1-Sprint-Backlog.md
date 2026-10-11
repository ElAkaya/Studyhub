# Project 1 sprint backlog — proposed

Status: Not started. Instructor approval required before T1–T11.

Estimates are planning hours, not time already spent. Weekly remaining-work fields are intentionally blank until observed. Do not invent retrospective progress or mark tasks complete before verification. This follows the supplied template's story/task/initial-estimate/weekly-remaining structure.

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
