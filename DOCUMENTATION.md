# DOCUMENTATION.md — Draft Source Material for the Final Report

This file consolidates everything already built and verified in the
repository into one place, structured around the exact 12-section
report format required by the brief (§11). It is a **draft source
document**, not the final report — the Documentation & Presentation
team (Appiah Ebo Acquah, Joy Oforiwaa Ampadu, Abena Serwaa Dwamena,
Kenny Idan) should expand this into the formatted DOCX/PDF submission,
filling in the sections marked `[TEAM TO FILL IN]`.

Every factual claim below (counts, file paths, test results) has been
verified against the actual code and CSVs in this repository, not
just described from memory — where a section says "see X," X is a
real file you can open right now.

---

## 1. Cover Page

`[TEAM TO FILL IN]`

- Title: Ghana Smart Campus Service Operations Optimizer
- Local context: University of Ghana, Legon campus service operations
- Team members and index numbers
- Course: DCIT 204/308, Data Structures and Algorithms I & II
- Date of submission

---

## 2. Problem Statement, Assumptions, Input-Output Definitions, System Boundaries

### Problem statement

The University of Ghana Legon campus needs a system to manage
maintenance and shuttle service requests across campus: prioritising
and dispatching them to available resources (technicians, porters,
shuttles), computing routes between locations, monitoring which
locations are reachable from a given point, and evaluating the
performance of the underlying algorithms as the dataset grows. See
`docs/PROJECT_SCOPE.md` for the full in-scope user journey.

### Core questions answered (brief §3)

1. Which service request should be handled next under FIFO, urgency,
   and priority-based rules? — answered by `SchedulingEngine`
   (`src/main/java/gh/edu/ug/dsaoptimizer/service/SchedulingEngine.java`).
2. What is the fastest route from one location to another under
   weighted road conditions? — answered by `RouteService` using
   Dijkstra (`Graph.dijkstra`/`Graph.shortestPath`).
3. Which locations are reachable from the current dispatch point? —
   answered by `RouteService.reachableFrom` using BFS.
4. Which subset of requests/resources can be selected under a
   budget/capacity constraint? — answered by `Knapsack` (0/1 DP) and
   `GreedyAssignment`.
5. How do alternative data structures/algorithms perform as the
   dataset grows? — answered by the 6 performance experiments in
   `evidence/benchmarks/` and `evidence/graphs/`.
6. How can the system store records permanently and reload them? —
   answered by the persistence layer
   (`src/main/java/gh/edu/ug/dsaoptimizer/persistence/`).

### Assumptions

- Roads are physically bidirectional (a walkway usable in either
  direction), even though each is stored once in the seed data — the
  system adds each road edge in both directions when building the
  graph (`RouteService`).
- Service request urgency has 4 levels (LOW, MEDIUM, HIGH, CRITICAL),
  used both for priority dispatch ordering and DP/greedy weighting.
- Resource assignment cost is modelled as road-network distance from
  the resource's home location to the request's source location.
- `[TEAM TO FILL IN]`: any additional domain assumptions specific to
  your local-context write-up (e.g. operating hours, staff shift
  patterns) — see `docs/PROJECT_SCOPE.md` for prompts to fill in.

### System boundaries

Out of scope (see `docs/PROJECT_SCOPE.md` for the full list): real-time
GPS/live traffic, authentication/user accounts, integration with real
university IT systems, any personally identifiable data.

---

## 3. Dataset Description, Data Dictionary, Database Schema

### Dataset (brief §4 minimums — all met)

| Entity | Required minimum | Actual | Source file |
|---|---|---|---|
| Locations | 50 | 50 | `data/processed/locations.csv` |
| Roads | 100 | 101 | `data/processed/roads.csv` |
| Service requests | 300 | 300 | `data/processed/service_requests.csv` |
| Resources | 30 | 30 | `data/processed/resources.csv` |
| Algorithm runs | 30 | 91 | `data/processed/algorithm_runs.csv` |

Full field-level data dictionary: `docs/DATA_DICTIONARY.md`.
Provenance (how each dataset was constructed, and what's AI-estimated
vs. measured): `data/README.md`.

### Database schema

Full DDL: `database/schema.sql` (SQLite). Six tables: `locations`,
`roads`, `service_requests`, `resources`, `algorithm_runs`,
`audit_events`. Foreign keys enforced (`PRAGMA foreign_keys = ON`).

Note for the report: `request_id` and `resource_id` are `TEXT`
(e.g. `"REQ-001"`, `"RES-001"`) rather than auto-incrementing
integers — a deliberate choice for readability in demos/reports,
documented in `database/schema.sql`'s comments.

---

## 4. System Architecture and Module Design

Full detail: `docs/ARCHITECTURE.md`. Summary:

```
ui       -> service -> algorithms -> structures
                     -> persistence -> model
algorithms -> model
structures -> (no dependencies on other layers)
```

- **`model/`**: `Location`, `Road`, `Resource`, `ServiceRequest`,
  `AlgorithmRun`, `AuditEvent` — plain domain classes.
- **`structures/`**: every custom data structure (see section 5).
- **`algorithms/`**: every custom algorithm (see section 6).
- **`persistence/`**: `Database` (connection + schema management) and
  one repository per entity (insert/findAll/CSV-load/targeted
  updates).
- **`service/`**: `SchedulingEngine` (dispatch rules), `RouteService`
  (routing/reachability), `ResourceAssignmentService` (greedy
  assignment).
- **`ui/`**: `ConsoleMenu` — the single entry point an examiner
  interacts with.
- **`benchmark/`**: `PerformanceExperiments` — all 6 required
  experiments.
- **`util/`**: `LineChart` — native-Java PNG chart renderer (no
  external plotting dependency).

---

## 5. Data Structure Implementation (brief §6 — all 13 done)

| Structure | File | Notes for the report |
|---|---|---|
| Dynamic array | `structures/DynamicArray.java` | Object[]-backed, doubling growth, shrinks on removal below 1/4 full |
| Linked list (doubly) | `structures/DoublyLinkedList.java` | O(1) add/remove at both ends, `toArray()` for interop |
| Stack | `structures/Stack.java` | Built on DoublyLinkedList |
| Queue / circular queue | `structures/Queue.java`, `structures/CircularQueue.java` | Circular queue demonstrates front/rear wrap-around |
| Deque | `structures/Deque.java` | Used by `SchedulingEngine` for urgent-request queue-jumping |
| Priority queue / heap | `structures/PriorityQueueHeap.java` | Array-backed binary min-heap, used by Dijkstra/Prim and priority dispatch |
| Hash table | `structures/HashTable.java` | Separate chaining, auto-resize at 0.75 load factor (toggleable for the load-factor experiment) |
| BST | `structures/BSTMap.java` | Unbalanced; degenerates to O(n) height on sorted input (see section 8) |
| Red-black tree | `structures/RedBlackTreeMap.java` | Real rotations + recolouring on insert; stays balanced on the same sorted input that degenerates the BST |
| B-tree | `structures/BTreeMap.java` | Minimum-degree-t, models database index pages |
| Disjoint set | `structures/DisjointSet.java` | Union by rank + path compression; used by Kruskal for cycle detection |
| Set / map | `structures/HashSetWrapper.java`, `TreeSetWrapper.java`, `HashMapWrapper.java`, `TreeMapWrapper.java` | Built on top of HashTable/RedBlackTreeMap per brief's "on top of hash table or BST" requirement |
| Graph | `structures/Graph.java` (adjacency list), `structures/GraphMatrix.java` (adjacency matrix) | Both representations implemented; Dijkstra included on both |

**No built-in Java collections** (`HashMap`, `TreeMap`,
`PriorityQueue`, `Stack`, `ArrayDeque`, `LinkedList`, `ArrayList`) are
used for any of the above — verified by repository grep at every
merge. Diagrams: `[TEAM TO FILL IN — draw box/pointer diagrams for
linked list, tree rotation before/after, hash collision chaining]`.

---

## 6. Algorithm Implementation (brief §7 — all 12 done)

| Algorithm | File | Notes for the report |
|---|---|---|
| Linear search | `algorithms/LinearSearch.java` | O(n), no precondition |
| Binary search | `algorithms/BinarySearch.java` | O(log n), requires sorted input — see counterexample in section 7 |
| Selection sort | `algorithms/SelectionSort.java` | O(n²) always, ≤n-1 swaps |
| Insertion sort | `algorithms/InsertionSort.java` | O(n²) worst case, O(n) best case (already sorted), stable |
| Merge sort | `algorithms/MergeSort.java` | O(n log n) always, not data-dependent |
| Quicksort | `algorithms/QuickSort.java` | O(n log n) average, O(n²) worst case (already-sorted input with last-element pivot) |
| Greedy assignment | `algorithms/GreedyAssignment.java` | See required counterexample in section 7 |
| Dynamic programming | `algorithms/Knapsack.java` | 0/1 knapsack, tabulation + reconstruction |
| BFS | `algorithms/BFS.java` | Reachability, level-order traversal |
| DFS | `algorithms/DFS.java` | Iterative (custom Stack), depth-first traversal |
| Dijkstra | `structures/Graph.java` (`dijkstra`/`shortestPath` methods) | Shortest path, non-negative weights |
| Prim | `algorithms/Prim.java` | MST, grows from a start node |
| Kruskal | `algorithms/Kruskal.java` | MST, sorts all edges (reuses MergeSort) + DisjointSet for cycle detection |

Pseudocode: `[TEAM TO FILL IN — brief §5 M1 asks for pseudocode/
flowcharts for at least 5 major operations; suggest: binary search,
merge sort, Dijkstra, Kruskal, knapsack]`.

---

## 7. Correctness Evidence

### Trace tables (6 required, all done) — `evidence/trace-tables/`

1. `binary-search-trace.md`
2. `insertion-sort-trace.md`
3. `merge-sort-trace.md`
4. `dijkstra-trace.md`
5. `kruskal-trace.md`
6. `knapsack-dp-trace.md` (verified against actual code output, not just hand-derived)

### Proof sketches (3 required, all done) — `evidence/proof-sketches.md`

1. Loop invariant — binary search
2. Induction/recursion proof — merge sort
3. Greedy correctness — Kruskal's cut property, explicitly contrasted
   with the greedy failure below

### Counterexamples (2 required, both done)

1. **Greedy failure**: `GreedyAssignmentTest.greedyFailsToFindTheOptimalAssignment`
   — a 2-request/2-resource scenario where greedy produces cost 101
   vs. the true optimum of 4.
2. **Invalid precondition**: `BinarySearchTest.unsortedInputViolatesPreconditionAndGivesWrongAnswer`
   — binary search silently gives a wrong answer on unsorted input.

### Edge cases covered

Empty structure, single element, duplicate keys, disconnected graph
(`KruskalTest.disconnectedGraphProducesSpanningForestNotSingleTree`,
`BFSTest`/`DFSTest` disconnected-graph tests), unreachable path
(`BFS.isReachable`/`DFS.isReachable` tests), queue full/empty
(`CircularQueueTest`, `SchedulingEngineTest.occupyingBeyondCapacityThrows`),
hash collision (`HashTableTest.testCollisionCountIncreasesAsTableFillsUpWithAutoResizeDisabled`).

### Unit tests

**172 tests, all passing** as of the last merge to `main`. Full
per-area breakdown: `docs/TESTING_AND_EVIDENCE.md`.

---

## 8. Performance Analysis (brief §9 — all 6 experiments done)

Machine: Apple M4, 16 GB RAM, macOS 26.5.1, OpenJDK 21.0.8 (all 6
experiments run on the same machine — see `docs/PERFORMANCE_EXPERIMENTS.md`
for the reconciliation history).

| Experiment | CSV | Graph | Key finding |
|---|---|---|---|
| Search comparison | `evidence/benchmarks/search_comparison.csv` | `evidence/graphs/search_comparison.png` | Linear search grows with n; binary search stays near-constant |
| Sorting comparison | `sorting_comparison.csv` | `sorting_comparison.png` | Selection/insertion sort show clear O(n²); merge/quick stay flat (O(n log n)) even at n=10,000 |
| Hash table load factor | `hash_load_factor.csv` | `hash_load_factor.png` | Load factors ≤1.0 show zero collisions (sequential integer keys); 1.5/2.0 show clear linear growth |
| BST vs balanced tree | `bst_vs_balanced.csv` | `bst_vs_balanced.png` | BST height hits 9999 at n=10,000 (fully degenerate); red-black tree height stays under 14 |
| Heap priority dispatch | `heap_dispatch.csv` | `heap_dispatch.png` | Insert/extract both scale as expected for a binary heap |
| Graph algorithms | `graph_algorithms.csv` | `graph_algorithms.png` | BFS/DFS stay flatter than Dijkstra/Kruskal as node count grows |

### Explained mismatches (brief §9.4 — required)

- **n=100/n=50 dips** in the search and graph-algorithms experiments:
  JIT warmup noise on the very first measurement, not a real
  complexity effect — documented in `docs/PERFORMANCE_EXPERIMENTS.md`.
- **Hash load factor ≤1.0 showing zero collisions**: specific to this
  experiment's use of consecutive integer keys, whose `hashCode()` is
  the value itself — not a general property of separate-chaining hash
  tables. Documented in the same file.

---

## 9. Database Integration Evidence

- Schema: `database/schema.sql`.
- Sample records: any of the 5 seed CSVs under `data/processed/`.
- Run logs: `PersistenceIntegrationTest` loads the real schema and
  real seed CSVs end-to-end in an automated test (not just manually) —
  see `src/test/java/gh/edu/ug/dsaoptimizer/persistence/PersistenceIntegrationTest.java`.
- Screenshots: `[TEAM TO FILL IN — run `mvn exec:java
  -Dexec.mainClass="gh.edu.ug.dsaoptimizer.App"` and screenshot the
  console menu, a search result, and the MST demo output; save under
  `evidence/screenshots/`]`.

---

## 10. Responsible Algorithm Selection

When each algorithm is appropriate, and when it isn't:

- **Binary search**: only correct on sorted data — see the
  counterexample. Linear search is the fallback when data isn't
  sorted and sorting it first isn't worth the cost for a one-off
  lookup.
- **BST vs red-black tree**: a plain BST is simpler and fine for
  random insert order, but degrades to a linked list (O(n) operations)
  on sorted/adversarial input — see section 8's measured result. Use
  the red-black tree whenever insert order can't be guaranteed random.
- **Greedy assignment**: fast (single pass) but not guaranteed
  optimal — see the counterexample. Appropriate when speed matters
  more than exact optimality, or when the problem has matroid
  structure (like MST) where greedy IS provably correct (see Kruskal's
  proof sketch).
- **Dynamic programming (knapsack)**: guaranteed optimal but O(n ×
  capacity) time/space — appropriate when capacity is bounded and
  small enough, inappropriate for very large budgets where the table
  becomes too large.
- **Selection/insertion sort**: only appropriate for small n or
  nearly-sorted data (insertion sort's O(n) best case); merge/quicksort
  for anything larger.
- **Quicksort vs merge sort**: quicksort is faster in practice
  (smaller constant factor, in-place) but has an O(n²) worst case on
  adversarial/sorted input; merge sort guarantees O(n log n) always at
  the cost of O(n) extra space — pick based on whether worst-case
  guarantees matter more than average-case speed.

---

## 11. Individual Contribution Statement and Oral Defense Prep

**Who wrote what** (for defense prep — every member must be able to
explain and modify their own code live if asked):

| Member | Contribution |
|---|---|
| Aidan Be-ir | DynamicArray, DoublyLinkedList, Stack, Queue, CircularQueue, Deque, PriorityQueueHeap, HashTable, Graph (list + matrix, with Dijkstra), BSTMap, RedBlackTreeMap, BTreeMap, DisjointSet, Set/Map wrappers |
| Selase Akusika Akumah | LinearSearch, SelectionSort |
| Cyril-Devon Nyamful | MergeSort |
| Boanu Samuel Kyere | BinarySearch |
| Amankwah John Adjei | Database schema (assigned_resource_id fix) |
| Nicole Eshun | Search comparison performance experiment (original) |
| Appiah Ebo Acquah | Performance experiments documentation |
| Austin Bediako | InsertionSort, QuickSort, GreedyAssignment, Knapsack, BFS, DFS, Prim, Kruskal, persistence layer, service layer, console UI, remaining performance experiments, algorithm_runs seed data |

**Flagged for the team**: Acquah Isaac Junior and Joseph Akondoh-Tetteh
jnr were originally assigned insertion sort/quicksort and greedy/DP/
scheduling respectively; that code was written by Austin Bediako to
keep the project moving. If either is asked to defend that code at
oral defense, they should review `InsertionSort.java`, `QuickSort.java`,
`GreedyAssignment.java`, `Knapsack.java`, or `SchedulingEngine.java`
beforehand.

`[TEAM TO FILL IN]`: each member's own 2-3 sentence contribution
statement, and which specific structure + algorithm they'll defend.

---

## 12. References and Appendices

`[TEAM TO FILL IN]` — suggested references already listed in
`Joint_DSA_Project_Brief.md` §16 (CLRS, Sedgewick & Wayne, Goodrich
et al., etc.). Appendices: full CSV samples, full trace tables (already
written, just attach), any additional screenshots.
