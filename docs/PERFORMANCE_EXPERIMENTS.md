# Performance Experiments

From the brief §9. Run each experiment at least **three times** and
report the **average runtime**. Use the **same machine** for all
experiments and record its specification below. Export raw results to
CSV (`evidence/benchmarks/`) and plot line graphs (`evidence/graphs/`).

## Machine specification (fill in before running experiments)

| Field | Value |
|---|---|
| CPU | Intel(R) Core(TM) i3-10110U CPU @ 2.10GHz |
| RAM | 7.84 GB |
| OS | Windows 11 |
| Java version | OpenJDK 25.0.3 Temurin |
| Date of experiments | August 15, 2026 |
## Required experiments

| # | Experiment | Minimum input sizes | Expected graph | CSV file | Graph file | Done? |
|---|---|---|---|---|---|---|
| 1 | Search comparison | 100, 500, 1,000, 5,000, 10,000 records | linear vs binary search runtime | `evidence/benchmarks/search_comparison.csv` | `evidence/graphs/search_comparison.png` | ☐ |
| 2 | Sorting comparison | 100, 500, 1,000, 5,000, 10,000 requests | selection, insertion, merge, quicksort | `evidence/benchmarks/sorting_comparison.csv` | `evidence/graphs/sorting_comparison.png` | ☐ |
| 3 | Hash table load factor | 100 to 20,000 keys, varying table sizes | load factor vs collision count/time | `evidence/benchmarks/hash_load_factor.csv` | `evidence/graphs/hash_load_factor.png` | ☐ |
| 4 | BST vs balanced tree | insert/search at multiple sizes | height and search time comparison | `evidence/benchmarks/bst_vs_balanced.csv` | `evidence/graphs/bst_vs_balanced.png` | ☐ |
| 5 | Heap priority dispatch | 100 to 20,000 requests | insert/extract operation time | `evidence/benchmarks/heap_dispatch.csv` | `evidence/graphs/heap_dispatch.png` | ☐ |
| 6 | Graph algorithms | 50, 100, 200, 500 locations/edges | BFS/DFS/Dijkstra/MST runtime | `evidence/benchmarks/graph_algorithms.csv` | `evidence/graphs/graph_algorithms.png` | ☐ |

## Rules

1. Each experiment: run **3+ times**, report the average.
2. Use the **same machine** for all runs; record its spec above.
3. Export results to CSV; plot line graphs (Excel, Python, or a Java
   plotting library are all acceptable).
4. Explain any mismatch between theoretical complexity (Big-O) and
   observed runtime in the final report's performance analysis section.

## CSV format convention

Each benchmark CSV should have at minimum these columns:

```
algorithm_name,input_size,run_number,time_ns,memory_kb,date_run
```

This mirrors the `algorithm_runs` table in `database/schema.sql` so
results can be loaded into the database directly if desired.
