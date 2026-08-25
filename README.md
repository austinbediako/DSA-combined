# Ghana Smart Campus Service Operations Optimizer

DCIT 204/308 Joint DSA Semester Project — University of Ghana, Department
of Computer Science.

## Context

The system models service operations across the University of Ghana
campus: hostels, lecture halls, labs, and shuttle stops as locations
connected by a weighted road network. It receives maintenance and
shuttle service requests, prioritises and dispatches them to resources
(technicians, porters, shuttles), computes routes and reachability
across campus, and evaluates the performance of the underlying
algorithms and data structures as the dataset grows.

## Status

The system is functionally complete: every required custom data
structure and algorithm is implemented and tested, the database is
fully wired into the running system (not just static seed files), and
a console menu lets an examiner run every required demonstration
without touching source code. See `docs/DEVELOPMENT_LOG.md` for the
week-by-week history and `docs/TESTING_AND_EVIDENCE.md` for exact
coverage counts.

- **Data structures** (brief §6): dynamic array, linked list, stack,
  queue/circular queue, deque, priority queue/heap, hash table, BST,
  red-black tree, B-tree, disjoint set, set/map, graph (adjacency list
  *and* matrix) — all 13 done.
- **Algorithms** (brief §7): linear/binary search, selection/insertion/
  merge/quicksort, greedy assignment (with counterexample), dynamic
  programming (0/1 knapsack), BFS, DFS, Dijkstra, Prim, Kruskal — all
  12 done.
- **Database & persistence** (M2): schema, seed data for all 5 minimum
  entities (locations, roads, resources, service requests, algorithm
  runs), and a full JDBC layer that actually reads/writes through the
  database at runtime.
- **Service layer & console UI** (M5, §8.4): a scheduling engine
  modelling FIFO/priority/circular/deque dispatch rules, route and
  resource-assignment services, and an interactive console menu tying
  it all together.
- **Testing & evidence** (§10): 172+ unit tests, all 6 required trace
  tables, all 3 required proof sketches, both required counterexamples.
- **Performance experiments** (§9): all 6 required experiments, with
  real measured data, committed CSVs and graphs, and honest
  interpretation notes for anything that didn't match theory cleanly.

What's left is report assembly, individual defense prep, and the demo
video — not code. See `docs/DEVELOPMENT_LOG.md` for the current punch
list.

## Workflow

1. **Load local data** — CSV seed files in `data/` are imported into
   the SQLite database (`database/schema.sql`) automatically on first
   run against an empty database.
2. **Store/reload through the database** — the persistence layer reads
   and writes `locations`, `roads`, `service_requests`, `resources`,
   `algorithm_runs`, and `audit_events` via JDBC.
3. **Use custom structures** — data is loaded into student-implemented
   linked lists, stacks, queues, heaps, trees, hash tables, disjoint
   sets, and graphs (see `src/main/java/gh/edu/ug/dsaoptimizer/structures/`).
4. **Run algorithms** — search, sort, graph traversal/shortest-path/MST,
   greedy, and dynamic-programming algorithms operate on those
   structures (`src/main/java/gh/edu/ug/dsaoptimizer/algorithms/`).
5. **Test** — unit tests, trace tables, proof sketches, and
   counterexamples verify correctness (see `docs/TESTING_AND_EVIDENCE.md`).
6. **Benchmark** — runtime/memory are measured across increasing input
   sizes (see `docs/PERFORMANCE_EXPERIMENTS.md`).
7. **Save evidence** — screenshots, trace tables, benchmark CSVs, and
   graphs are stored under `evidence/`.

## Technology

- Java 17
- Maven
- SQLite (via `org.xerial:sqlite-jdbc`)
- JUnit 5

## Setup and run

```bash
# Compile and run the test suite
mvn test

# Run the interactive console application
mvn exec:java -Dexec.mainClass="gh.edu.ug.dsaoptimizer.App"
```

On first run against an empty database, the app automatically loads
all seed data from `data/processed/`. The console menu then lets you
reload data, search locations/requests, run dispatch/route/MST demos,
run sorting/searching performance comparisons live, and run the greedy
resource-assignment demo — everything an examiner needs, in one menu.

## Important note on data structures

**Assessed data structures (dynamic array, linked list, stack, queue,
deque, priority queue/heap, BST, red-black tree, B-tree, hash table,
set/map, disjoint set, graph) must be implemented by the students.**
Built-in Java collection classes such as `HashMap`, `TreeMap`,
`PriorityQueue`, `Stack`, and `ArrayDeque` must **not** be used as
substitutes for this assessed core logic. Built-in utilities are
allowed for file I/O, JDBC, plotting export, and unit-test scaffolding
only. See `CONTRIBUTING.md` and `Joint_DSA_Project_Brief.md` §8.

## Project documents

- [`Joint_DSA_Project_Brief.md`](Joint_DSA_Project_Brief.md) — the
  official project brief (source of truth).
- [`docs/PROJECT_SCOPE.md`](docs/PROJECT_SCOPE.md) — selected context,
  in-scope journey, MVP demo.
- [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) — package boundaries
  and shared entities.
- [`docs/DATA_DICTIONARY.md`](docs/DATA_DICTIONARY.md) — entity field
  definitions and minimum dataset sizes (all done).
- [`docs/TEAM_TASKS.md`](docs/TEAM_TASKS.md) — task board for the
  team.
- [`docs/TESTING_AND_EVIDENCE.md`](docs/TESTING_AND_EVIDENCE.md) —
  testing/evidence requirements and checklists (all done).
- [`docs/PERFORMANCE_EXPERIMENTS.md`](docs/PERFORMANCE_EXPERIMENTS.md) —
  required benchmarking experiments (all done).
- [`docs/DEVELOPMENT_LOG.md`](docs/DEVELOPMENT_LOG.md) — weekly
  progress log and current punch list.
- [`evidence/`](evidence/) — trace tables, proof sketches, benchmark
  CSVs, and graphs.
