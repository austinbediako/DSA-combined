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
| Dynamic array | 6 | `DynamicArrayTest` |
| Linked list | 8 | `DoublyLinkedListTest` |
| Stack | 3 | `StackTest` |
| Queue / circular queue | 7 | `QueueTest` (3) + `CircularQueueTest` (4) |
| Deque | 4 | `DequeTest` |
| Priority queue / heap | 4 | `PriorityQueueHeapTest` |
| BST / red-black tree / B-tree / set / map | 5 | `TreeAndMapTests` (BSTMap, RedBlackTreeMap, BTreeMap, HashMapWrapper/HashSetWrapper, TreeMapWrapper/TreeSetWrapper -- one test per structure) |
| Hash table | 4 | `HashTableTest` |
| Disjoint set | 1 | `DisjointSetTest` |
| Graph (adjacency list/matrix) | 5 | `GraphTest` (3) + `GraphMatrixTest` (2) |
| Search (linear/binary) | 23 | `LinearSearchTest` (11) + `BinarySearchTest` (12, includes the unsorted-input counterexample) |
| Sort (selection/insertion/merge/quick) | 43 | `SelectionSortTest` (12) + `InsertionSortTest` (10) + `MergeSortTest` (11) + `QuickSortTest` (10) |
| Greedy algorithm | 5 | `GreedyAssignmentTest` (includes the required counterexample) |
| Dynamic programming | 8 | `KnapsackTest` |
| BFS / DFS | 11 | `BFSTest` (5) + `DFSTest` (6) |
| Dijkstra | -- | covered within `GraphTest`/`GraphMatrixTest`, no separate file |
| Prim / Kruskal | 9 | `PrimTest` (4) + `KruskalTest` (5) |
| Persistence (M2 -- database load/save) | 3 | `PersistenceIntegrationTest`, loads the real schema + real seed CSVs end-to-end |
| Service layer (M5 -- scheduling/routing/assignment) | 11 | `SchedulingEngineTest` (7) + `RouteServiceTest` (3) + `ResourceAssignmentServiceTest` (1) |
| **Total** | **160 / 40** | |

## Trace tables (target: 6, brief §10)

Required: binary search, insertion sort, merge sort OR quicksort,
Dijkstra, Kruskal OR Prim, and one DP algorithm.

| # | Algorithm | Trace table location | Done? |
|---|---|---|---|
| 1 | Binary search | `evidence/trace-tables/binary-search-trace.md` | Done |
| 2 | Insertion sort | `evidence/trace-tables/insertion-sort-trace.md` | Done |
| 3 | Merge sort or quicksort | `evidence/trace-tables/merge-sort-trace.md` | Done |
| 4 | Dijkstra | `evidence/trace-tables/dijkstra-trace.md` | Done |
| 5 | Kruskal or Prim | `evidence/trace-tables/kruskal-trace.md` | Done |
| 6 | Dynamic programming | `evidence/trace-tables/knapsack-dp-trace.md` | Done |

## Proof sketches (target: 3, brief §10)

| # | Type | Algorithm | Done? |
|---|---|---|---|
| 1 | Loop invariant for search/sort | Binary search | Done -- `evidence/proof-sketches.md` |
| 2 | Induction/recursion proof | Merge sort | Done -- `evidence/proof-sketches.md` |
| 3 | Greedy or DP correctness idea | Kruskal (cut property) | Done -- `evidence/proof-sketches.md` |

## Counterexamples (target: 2, brief §10)

| # | Type | Description | Done? |
|---|---|---|---|
| 1 | Greedy failure | `GreedyAssignmentTest.greedyFailsToFindTheOptimalAssignment` -- cost 101 vs true optimum 4 | Done |
| 2 | Invalid precondition | `BinarySearchTest.unsortedInputViolatesPreconditionAndGivesWrongAnswer` | Done |

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
