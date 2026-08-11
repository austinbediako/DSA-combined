# Contributing

Short guidance for the 14-member team working in this repository.

## Before starting work

1. Pull the latest `main`:
   ```bash
   git checkout main
   git pull
   ```
2. Create a branch named after what you're doing, e.g.:
   - `feature/custom-queue`
   - `feature/dijkstra`
   - `docs/report-outline`
   - `test/binary-search`

## While working

- Make small, focused commits using a prefix that describes the change:
  - `feat:` — new functionality (e.g. `feat: implement circular queue`)
  - `test:` — tests (e.g. `test: add boundary cases for BST`)
  - `docs:` — documentation only
  - `fix:` — bug fixes
- Do not replace assessed data structures with Java's built-in
  `HashMap`, `TreeMap`, `PriorityQueue`, `Stack`, `ArrayDeque`, or
  similar collection classes — see `Joint_DSA_Project_Brief.md` §8.
  Built-in utilities are fine for file I/O, JDBC, and test scaffolding.

## Opening a pull request

- Open a PR into `main` and request review from at least one teammate.
- **No direct pushes to `main`.** All changes go through a reviewed PR.
- A task is not considered complete until the PR includes:
  1. The implementation code.
  2. Relevant unit tests (normal, boundary, and invalid-input cases for
     data structures — see `docs/TESTING_AND_EVIDENCE.md`).
  3. A short complexity note where applicable (time/space Big-O for the
     structure or algorithm added).
  4. Any evidence produced (trace table, screenshot, benchmark CSV)
     saved under `evidence/` and referenced in the PR description.

## Questions about scope or schema

Check `Joint_DSA_Project_Brief.md` first, then `docs/PROJECT_SCOPE.md`
and `docs/ARCHITECTURE.md`. If still unclear, raise it with the team
before implementing — schema and interface changes affect everyone.
