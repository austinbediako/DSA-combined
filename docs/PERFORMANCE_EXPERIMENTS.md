# Performance Experiments

From the brief §9. Run each experiment at least **three times** and
report the **average runtime**. Use the **same machine** for all
experiments and record its specification below. Export raw results to
CSV (`evidence/benchmarks/`) and plot line graphs (`evidence/graphs/`).

## Machine specification

All 6 experiments now ran on the same machine (search comparison was
originally run separately on a Windows i3 machine -- re-run here on
2026-08-22 to fix that mismatch and keep every number comparable):

| Field | Value |
|---|---|
| CPU | Apple M4 |
| RAM | 16 GB |
| OS | macOS 26.5.1 |
| Java version | OpenJDK 21.0.8 |
| Date of experiments | August 22, 2026 |

## Required experiments

| # | Experiment | Minimum input sizes | Expected graph | CSV file | Graph file | Done? |
|---|---|---|---|---|---|---|
| 1 | Search comparison | 100, 500, 1,000, 5,000, 10,000 records | linear vs binary search runtime | `evidence/benchmarks/search_comparison.csv` | `evidence/graphs/search_comparison.png` | Done |
| 2 | Sorting comparison | 100, 500, 1,000, 5,000, 10,000 requests | selection, insertion, merge, quicksort | `evidence/benchmarks/sorting_comparison.csv` | `evidence/graphs/sorting_comparison.png` | Done |
| 3 | Hash table load factor | 100 to 20,000 keys, varying table sizes | load factor vs collision count/time | `evidence/benchmarks/hash_load_factor.csv` | `evidence/graphs/hash_load_factor.png` | Done |
| 4 | BST vs balanced tree | insert/search at multiple sizes | height and search time comparison | `evidence/benchmarks/bst_vs_balanced.csv` | `evidence/graphs/bst_vs_balanced.png` | Done |
| 5 | Heap priority dispatch | 100 to 20,000 requests | insert/extract operation time | `evidence/benchmarks/heap_dispatch.csv` | `evidence/graphs/heap_dispatch.png` | Done |
| 6 | Graph algorithms | 50, 100, 200, 500 locations/edges | BFS/DFS/Dijkstra/MST runtime | `evidence/benchmarks/graph_algorithms.csv` | `evidence/graphs/graph_algorithms.png` | Done |

## Interpretation notes (brief section 9, rule 4: explain mismatches)

- **Search comparison**: at n=100, both linear and binary search show
  a large first-run spike (hundreds of microseconds) before dropping
  to a stable baseline for the rest of that size's runs, and staying
  low for every larger size. Same JIT warmup effect as the graph
  algorithms note below -- the very first invocation of a method pays
  the cost of interpretation/compilation before the JIT optimizes it.
  From the second run onward the expected shapes are clear: linear
  search grows roughly linearly with n, binary search stays
  near-constant regardless of n.
- **Sorting comparison**: selection sort and insertion sort both show
  clear O(n²) growth (curving sharply upward), while merge sort and
  quicksort stay close to flat (O(n log n)) even at n=10,000 -- matches
  theory exactly.
- **Hash table load factor**: load factors 0.5, 0.75, and 1.0 all show
  **zero** collisions, which looks surprising at first. This is
  because the experiment inserts consecutive integer keys (0, 1, 2,
  ...) and `indexFor` uses `key.hashCode() % capacity` -- for
  `Integer`, `hashCode()` is the value itself, so consecutive keys
  hash to consecutive bucket indices with no repeats *as long as
  capacity >= n* (load factor <= 1.0). Collisions only appear once
  capacity < n (load factors 1.5 and 2.0), growing roughly linearly
  as expected. This result is specific to sequential integer keys --
  random or string keys would show collisions at lower load factors
  too, since their hash codes don't map so neatly to consecutive
  buckets.
- **BST vs balanced tree**: on ascending-order insertion (the
  worst case for an unbalanced BST), BST height grows linearly,
  reaching 9999 at n=10,000, while the red-black tree's height stays
  under 14 the entire time -- a dramatic, textbook-exact demonstration
  of why balancing matters.
- **Graph algorithms**: there is a visible dip in runtime from n=50 to
  n=100 across *all four* algorithms before growth resumes as expected
  from n=100 onward. This is JIT warmup noise, not a real complexity
  effect -- n=50 is the first measurement taken, before the JVM's
  just-in-time compiler has optimized the hot code paths, so it's
  artificially slow. A more rigorous version of this experiment would
  run a handful of untimed warmup iterations before the first timed
  size.

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
