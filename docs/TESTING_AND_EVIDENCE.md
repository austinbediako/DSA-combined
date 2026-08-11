# Testing and Evidence

Minimum requirements from the brief (§10):

| Evidence type | Minimum | Tracked here |
|---|---|---|
| Unit tests | 40 | see checklist below |
| Trace tables | 6 | see checklist below |
| Proof sketches | 3 | see checklist below |
| Counterexamples | 2 | see checklist below |
| Edge cases | (see list) | see checklist below |

## Unit test checklist (target: 40+)

Track counts per area; every custom structure needs normal, boundary,
and invalid-input cases (brief §8.3).

| Area | Tests written | Notes |
|---|---|---|
| Dynamic array | 0 | |
| Linked list | 0 | |
| Stack | 0 | |
| Queue / circular queue | 0 | |
| Deque | 0 | |
| Priority queue / heap | 0 | |
| BST | 0 | |
| Red-black tree | 0 | |
| B-tree | 0 | |
| Hash table | 0 | |
| Set / map | 0 | |
| Disjoint set | 0 | |
| Graph (adjacency list/matrix) | 0 | |
| Search (linear/binary) | 0 | |
| Sort (selection/insertion/merge/quick) | 0 | |
| Greedy algorithm | 0 | |
| Dynamic programming | 0 | |
| BFS / DFS | 0 | |
| Dijkstra | 0 | |
| Prim / Kruskal | 0 | |
| **Total** | **0 / 40** | |

## Trace tables (target: 6, brief §10)

Required: binary search, insertion sort, merge sort OR quicksort,
Dijkstra, Kruskal OR Prim, and one DP algorithm.

| # | Algorithm | Trace table location | Done? |
|---|---|---|---|
| 1 | Binary search | `evidence/trace-tables/` | ☐ |
| 2 | Insertion sort | `evidence/trace-tables/` | ☐ |
| 3 | Merge sort or quicksort | `evidence/trace-tables/` | ☐ |
| 4 | Dijkstra | `evidence/trace-tables/` | ☐ |
| 5 | Kruskal or Prim | `evidence/trace-tables/` | ☐ |
| 6 | Dynamic programming | `evidence/trace-tables/` | ☐ |

## Proof sketches (target: 3, brief §10)

| # | Type | Algorithm | Done? |
|---|---|---|---|
| 1 | Loop invariant for search/sort | | ☐ |
| 2 | Induction/recursion proof | | ☐ |
| 3 | Greedy or DP correctness idea | | ☐ |

## Counterexamples (target: 2, brief §10)

| # | Type | Description | Done? |
|---|---|---|---|
| 1 | Greedy failure | Show a case where the chosen greedy strategy gives a suboptimal result | ☐ |
| 2 | Invalid precondition | e.g. binary search on unsorted input | ☐ |

## Required edge cases (brief §10)

Confirm each of these is covered by at least one test:

- ☐ Empty structure
- ☐ Single element
- ☐ Duplicate keys
- ☐ Disconnected graph
- ☐ Unreachable path
- ☐ Queue full / empty
- ☐ Hash collision

## Where evidence lives

- Trace tables → `evidence/trace-tables/`
- Proof sketches → embed in the final report (`docs/` or report doc),
  reference from here
- Screenshots (test runs, DB state, console demo) → `evidence/screenshots/`
- Raw benchmark CSVs → `evidence/benchmarks/`
- Performance graphs → `evidence/graphs/`

See `evidence/README.md` for naming conventions.
