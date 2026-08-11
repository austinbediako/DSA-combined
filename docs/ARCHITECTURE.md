# Architecture

## Package boundaries

The system follows a simple layered structure, all under
`gh.edu.ug.dsaoptimizer`:

```
ui            -> calls service
service       -> uses algorithms + structures, calls persistence
algorithms    -> operates on structures and model data; no I/O
structures    -> standalone custom data structures; no dependencies on other layers
persistence   -> loads/saves model data via JDBC/SQLite; no algorithm logic
model         -> plain domain records/classes shared by every layer
util          -> small cross-cutting helpers (e.g. timing utilities for benchmarks)
```

Dependency direction (arrows point to "depends on"):

```
ui -> service -> algorithms -> structures
              -> persistence -> model
algorithms -> model
structures -> (nothing else in the project)
```

Rules of thumb:

- `structures` must not depend on `model`, `service`, or `persistence` —
  they should be generic/reusable (e.g. a `LinkedList<T>`, `MinHeap<T>`).
- `algorithms` may depend on `structures` and `model`, but not on
  `persistence` or `ui`.
- `persistence` reads/writes `model` objects to/from SQLite; it must not
  contain algorithmic logic (sorting, graph traversal, etc.).
- `service` is the orchestration layer: it loads data via `persistence`,
  runs it through `structures`/`algorithms`, and returns results to `ui`.
- `ui` is a console menu only — no business logic, no direct database or
  algorithm calls.

## Shared domain entities (`model`)

These five records/classes are used across the system and map directly
to the database tables in `database/schema.sql`:

| Class | Corresponds to table | Key fields |
|---|---|---|
| `Location` | `locations` | id, name, area, type, latitude, longitude |
| `Road` | `roads` | id, fromLocationId, toLocationId, distance, travelTime, roadConditionWeight |
| `ServiceRequest` | `service_requests` | id, sourceLocationId, destinationLocationId, category, urgency, timeSubmitted, deadline, status |
| `Resource` | `resources` | id, type, homeLocationId, capacity, availabilityStatus |
| `AlgorithmRun` | `algorithm_runs` | id, algorithmName, inputSize, timeNs, memoryKb, dateRun |

> `AuditEvent` (mapping to `audit_events`) belongs in `model` too, even
> though it wasn't listed among the five "shared" entities above — it's
> used by both `service` (to write events) and `persistence`.

## Where things go

- Custom `LinkedList`, `Stack`, `Queue`/`CircularQueue`, `Deque`,
  `PriorityQueue`/`Heap`, `BST`, red-black tree, B-tree, `HashTable`,
  `Set`/`Map`, `DisjointSet`, `Graph` → `structures/`
- Search (`linearSearch`, `binarySearch`), sort (`selectionSort`,
  `insertionSort`, `mergeSort`, `quickSort`), graph algorithms (`bfs`,
  `dfs`, `dijkstra`, `prim`, `kruskal`), greedy and DP optimisation →
  `algorithms/`
- Dispatch rules, route-finding orchestration, resource assignment,
  experiment running → `service/`
- SQLite connection management, CRUD/load/save for each entity, CSV
  import → `persistence/`
- Console menu, input parsing, result printing → `ui/`

> Fill in: once implementation starts, record any deviations from this
> layout here so the report's architecture section stays accurate.
