# StudyHub Project 1: next steps

This package is the approval-preparation stage, not the final project submission. Its patch applies to repository commit 349e8a31dca71458721a0ebaeab03133f16469c0. Existing application source and FXML are unchanged.

## 1. Apply the preparation patch to your own local repository
Install/use JDK 17 and Maven. Check `java -version` and `mvn -version`; Maven must report JDK 17. Use your existing clone when available. Otherwise run:

```bash
git clone https://github.com/ElAkaya/Studyhub.git
cd Studyhub
```

Before continuing, run `git status` and preserve any uncommitted work. Do not overwrite or reset local work. Then:

```bash
git switch dev
git pull --ff-only origin dev
git switch -c chore/project1-preparation
```

Copy the downloaded `project1-preparation.patch` into the parent directory of Studyhub, then run from Studyhub:

```bash
git apply --check ../project1-preparation.patch
git apply ../project1-preparation.patch
mvn -f code/studyhub-app/pom.xml clean verify
```

If the patch check fails, stop and resolve the changed baseline rather than forcing the patch. If verification fails, correct the reported problem before committing. Then:

```bash
git add code/studyhub-app/pom.xml
git commit -m "Raise Project 1 coverage gate to 95 percent" -m "Align compilation with CI JDK 17 and exclude only Main and codebehind classes. Retain the existing course Checkstyle configuration."
git add documentation/Project-1-MVP-Scope.md documentation/Project-1-Sprint-Backlog.md documentation/Project-1-GitHub-Steps.md documentation/Project-1-Verification.md
git commit -m "Document proposed Project 1 scope and tasks" -m "Map the MVP to existing product stories and provide an estimated sprint backlog for instructor approval before feature implementation."
git push -u origin chore/project1-preparation
```

On GitHub, create a PR with base `dev`, compare `chore/project1-preparation`. Review the changes and passing build, then merge into dev. This is a preparation PR, not the final dev-to-release PR.

## 2. Confirm GitHub settings before asking the instructor
Repository Settings: confirm `dev` is the default branch. Both `dev` and `release` already exist.

Under branch protection/rules, protect `release`. Require a pull request before merging, require the CI build status check, apply enforcement to administrators/bypass users where available, disable force pushes and branch deletions, and leave no direct-push bypass. Do not use a rule that blocks all updates, because it can also block PR merges. Do not require another person's approval unless the course requires it and a reviewer is available.

The existing workflow is named `Java CI with Maven` and its job is `build`. Select the actual status check shown by GitHub after a successful workflow run. A required check is stronger than merely running CI. Confirm a push to dev and a PR targeting release both trigger it. Save a screenshot of the actual saved rule and its branch target. No screenshot has been fabricated or included here.

## 3. Request instructor scope approval
Provide the repository link and the two new documents under documentation. Ask the instructor to approve the proposed features, local-only limitations and estimated tasks. If approval already exists, use that approved scope instead and supply it before implementing. Record the actual approval in the scope document. Do not represent a generated proposal as approved.

## 4. After approval: implement feature branches
These commands illustrate the required process; run them separately for each task while actually doing that task, not as an empty batch:

```bash
git switch dev
git pull --ff-only origin dev
git switch -c feature/course-catalog
# Implement the approved task and its tests.
mvn -f code/studyhub-app/pom.xml clean verify
git add <specific-files-for-this-task>
git commit -m "Add course catalog and validation tests" -m "Explain the actual behavior, design decisions and tests here."
git push -u origin feature/course-catalog
```

Open and merge a feature-to-dev PR after checks pass. Repeat for the actual remaining tasks. Avoid implementing everything first and inventing a history later.

## 5. Run and verify the completed application
From the repository root:

```bash
mvn -f code/studyhub-app/pom.xml clean verify
mvn -f code/studyhub-app/pom.xml javafx:run
```

Inspect `code/studyhub-app/target/site/jacoco/index.html`. Exercise the scope's acceptance criteria and record actual results. Current preparation package still contains the original create/remove application, not the proposed MVP.

## 6. Final release PR and submission — only after implementation
Push the completed dev branch. Create or update a PR with base `release`, compare `dev`, describing actual implemented scope, test results, coverage and manual checks. PR #1 was already open when inspected; check its current state and update/reuse it if it is the same dev-to-release PR. Do not claim it proves completion before features are implemented. Follow the instructor's direction about whether to leave the final PR open or merge it; the assignment explicitly requires setting up the PR. Capture the real PR page with branch names and checks visible.

For the final archive on macOS, use your actual local folder (including .git history). From inside Studyhub, run:

```bash
mvn -f code/studyhub-app/pom.xml clean
```

Only continue if clean succeeds. Move one directory above Studyhub. Ensure it contains no credentials, personal data, IDE secrets, or unrelated files. Then:

```bash
mkdir Project1-Submission
zip -r Project1-Submission/Studyhub-local-repository.zip Studyhub -x '*/.DS_Store'
```

Place your real `branch-protection.png` and `dev-to-release-pr.png` screenshots in Project1-Submission. From its parent directory:

```bash
zip -r Project1-Submission.zip Project1-Submission
unzip -l Project1-Submission.zip
```

Inspect both archives. The inner archive must contain the enclosing Studyhub directory, source, documentation and .git. The outer archive must contain the inner ZIP plus both screenshots. Do not submit this preparation download as the completed assignment.
