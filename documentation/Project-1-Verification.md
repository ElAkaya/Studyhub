# Preparation verification

Verified locally on October 10, 2026 against original dev commit 349e8a31dca71458721a0ebaeab03133f16469c0 plus the supplied preparation patch.

- Java: OpenJDK 17; Maven: 3.9.9.
- Maven clean verify: BUILD SUCCESS.
- JUnit: 9 tests, zero failures, errors or skips.
- Course Checkstyle: zero violations; devops_checks.xml unchanged.
- JaCoCo included application code: StudyGroup only, 19/19 lines and 10/10 branches covered (100%).
- Required package line coverage gate: 95%.
- Coverage exclusions: Main.class and view/codebehind/** only. No entire root package exemption.
- Application Java/FXML unchanged. The proposed new features are not implemented or tested yet.
- No graphical application smoke test performed in this environment.
- GitHub branches dev and release exist; clone default is dev. PR #1 was open with title “Release: StudyHub CI pipeline and sample project”.
- GitHub branch protection was not inspected or changed. CI success on GitHub after applying this patch remains to be confirmed.
- No instructor approval, fabricated screenshots, fabricated progress records, final implementation or final submission archive is supplied.

The runtime needed a temporary Maven proxy setting to fetch dependencies. That environment-only setting is not part of the patch or user setup.
