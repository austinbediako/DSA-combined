# Evidence

Correctness and performance evidence required by the brief (§8.6, §10)
lives here. See `docs/TESTING_AND_EVIDENCE.md` and
`docs/PERFORMANCE_EXPERIMENTS.md` for the checklists these files
support.

## Subdirectories and naming conventions

- `screenshots/` — console demo runs, database state, test results.
  Name as `<area>_<what>.png`, e.g. `dispatch_priority_demo.png`,
  `db_service_requests_table.png`.
- `trace-tables/` — trace tables for the six required algorithms
  (binary search, insertion sort, merge/quicksort, Dijkstra,
  Kruskal/Prim, DP). Name as `<algorithm>_trace.md` or `.csv`, e.g.
  `dijkstra_trace.md`.
- `benchmarks/` — raw CSV timing/memory results from performance
  experiments. Name as `<experiment>.csv`, matching
  `docs/PERFORMANCE_EXPERIMENTS.md`, e.g. `sorting_comparison.csv`.
  Columns: `algorithm_name,input_size,run_number,time_ns,memory_kb,date_run`.
- `graphs/` — plotted line graphs from benchmark CSVs. Name as
  `<experiment>.png`, matching the corresponding CSV filename.

## Rules

- Local data referenced in screenshots/traces must use Ghana / University
  of Ghana names, per `data/README.md`, and must not expose personal
  data.
- Each benchmark CSV must record how it was produced (which experiment,
  which machine — see `docs/PERFORMANCE_EXPERIMENTS.md` machine spec
  table) so results are reproducible and traceable to the team dataset.
- Do not hand-edit benchmark CSVs to "smooth" results — if a run looks
  anomalous, re-run and note it in `docs/DEVELOPMENT_LOG.md` instead.
