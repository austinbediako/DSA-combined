# Trace table — Insertion Sort

Algorithm: `InsertionSort.sort(T[] array, Comparator<T> comparator)`
Input: `array = [29, 10, 14, 37, 13]` (same input as
`selection-sort-trace.md`, for direct comparison)

| Pass (i) | key | Shifts performed | Array after pass |
|---|---|---|---|
| 1 | 10 | 29 shifts right (29 > 10) | [10, 29, 14, 37, 13] |
| 2 | 14 | 29 shifts right (29 > 14); 10 does not (10 ≤ 14) | [10, 14, 29, 37, 13] |
| 3 | 37 | none — 29 ≤ 37, key stays in place | [10, 14, 29, 37, 13] |
| 4 | 13 | 37, 29, 14 all shift right; 10 does not (10 ≤ 13) | [10, 13, 14, 29, 37] |

Result: `[10, 13, 14, 29, 37]`. n = 5, so worst-case comparisons =
n(n-1)/2 = 10; this run made 1+2+1+4 = 8 comparisons (pass 3 exits
after a single comparison since the array prefix is already ordered
relative to the key) — fewer than worst case because the input is not
fully reverse-sorted.

Loop invariant used in the proof sketch: before each pass i,
`array[0..i-1]` is sorted. The pass extends this by inserting `key`
(originally at index i) into its correct position within that sorted
prefix, shifting larger elements right to make room — so after the
pass, `array[0..i]` is sorted. Base case: `array[0..0]` (a single
element) is trivially sorted.

## Comparison with Selection Sort

Both produce the same sorted output on this input, but insertion sort
needed only 8 comparisons here versus selection sort's fixed 10
(`selection-sort-trace.md`) — insertion sort's comparison count is
data-dependent (best case O(n) on already-sorted input), while
selection sort always scans the full remaining unsorted region
regardless of order (always O(n²) comparisons). This is the empirical
difference the sorting-comparison performance experiment
(`docs/PERFORMANCE_EXPERIMENTS.md`) is expected to show at scale.
