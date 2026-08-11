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

## Workflow

1. **Load local data** — CSV seed files in `data/` are imported into the
   SQLite database (`database/schema.sql`).
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

# Run the console application
mvn exec:java -Dexec.mainClass="gh.edu.ug.dsaoptimizer.App"
```

## Important note on data structures

**Assessed data structures (dynamic array, linked list, stack, queue,
deque, priority queue/heap, BST, red-black tree, B-tree, hash table,
set/map, disjoint set, graph) must be implemented by the students.**
Built-in Java collection classes such as `HashMap`, `TreeMap`,
`PriorityQueue`, `Stack`, and `ArrayDeque` must **not** be used as
substitutes for this assessed core logic. Built-in utilities are
allowed for file I/O, JDBC, and unit-test scaffolding only. See
`CONTRIBUTING.md` and `Joint_DSA_Project_Brief.md` §8.

## Project documents

- [`Joint_DSA_Project_Brief.md`](Joint_DSA_Project_Brief.md) — the
  official project brief (source of truth).
- [`docs/PROJECT_SCOPE.md`](docs/PROJECT_SCOPE.md) — selected context,
  in-scope journey, MVP demo.
- [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) — package boundaries
  and shared entities.
- [`docs/DATA_DICTIONARY.md`](docs/DATA_DICTIONARY.md) — entity field
  definitions and minimum dataset sizes.
- [`docs/TEAM_TASKS.md`](docs/TEAM_TASKS.md) — task board for the
  14-member team.
- [`docs/TESTING_AND_EVIDENCE.md`](docs/TESTING_AND_EVIDENCE.md) —
  testing/evidence requirements and checklists.
- [`docs/PERFORMANCE_EXPERIMENTS.md`](docs/PERFORMANCE_EXPERIMENTS.md) —
  required benchmarking experiments.
- [`docs/DEVELOPMENT_LOG.md`](docs/DEVELOPMENT_LOG.md) — weekly
  progress log.
