# Development Log

Required by the brief (§15.4): a short development log showing weekly
progress, challenges, and decisions. Add a new entry per session/week.
Convert relative dates to absolute dates.

## Template (copy for each new entry)

### YYYY-MM-DD — Week N

| Field | Notes |
|---|---|
| Completed work | |
| Decisions made | |
| Blockers | |
| Evidence captured | (link to files in `evidence/`) |
| Next action | |

---

## Log entries

### 2026-08-11 — Week 0

| Field | Notes |
|---|---|
| Completed work | Repository scaffold created: Maven project structure, package layout, draft database schema, documentation templates. |
| Decisions made | Selected local context: University of Ghana Smart Campus Service Operations Optimizer. Java 17 + Maven + SQLite + JUnit 5. |
| Blockers | None yet — team assignments and dataset construction still pending. |
| Evidence captured | None yet. |
| Next action | Assign team members in `docs/TEAM_TASKS.md`, finalise `database/schema.sql`, begin dataset construction per `docs/DATA_DICTIONARY.md`. |

### 2026-08-14 — Week 1

| Field | Notes |
|---|---|
| Completed work | Team roster filled (15/14 members assigned across all 5 teams). Full seed dataset complete and merged: 50 locations, 101 roads, 30 resources, 300 service requests — all validated programmatically and confirmed to load into the live SQLite schema. First code contributions reviewed: PR #17 (Selase — LinearSearch, SelectionSort, ServiceRequest model, 23 tests, all passing) merged to main. |
| Decisions made | roads.csv adopted as the canonical road/edge dataset over a duplicate submission (PR #10) that only covered 41 of 50 locations and left 9 isolated, including 3 shuttle stops — closed with review notes. request_id/resource_id changed from INTEGER AUTOINCREMENT to TEXT in schema.sql to match the team's readable id convention (REQ-001, RES-001). |
| Blockers | PR #16 (Amankwah — assigned_resource_id FK) has a column-order bug that would NULL out `status` on all 300 existing service_requests rows on import; needs the new column moved after `status`. PR #18 (Aidan — full Core Structures set: DynamicArray, DoublyLinkedList, Stack, Queue, CircularQueue, Deque, HashTable, PriorityQueueHeap, Graph, 36 tests all passing) is otherwise strong but `Graph.java` uses `java.util.HashMap`/`java.util.LinkedList` internally, which violates the brief's ban on built-in collections for assessed core logic — needs to be swapped for the custom HashTable/DoublyLinkedList already in the same PR. Graph is also still adjacency-list only; adjacency matrix still required. Both PRs have review comments and are waiting on fixes before merge. |
| Evidence captured | `data/README.md` provenance log (all 4 seed CSVs); `evidence/trace-tables/linear-search-trace.md`, `evidence/trace-tables/selection-sort-trace.md` (from PR #17). |
| Next action | Amankwah to fix PR #16 column order; Aidan to swap Graph's internals to custom structures and add adjacency matrix; re-review both and merge. Continue chasing the remaining open Core Structures/Graph & Network/Data & Integration seats and outstanding module work (binary search branch already opened by Selase). |
