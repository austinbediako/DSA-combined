# Trace table — Merge Sort

Algorithm: `MergeSort.sort(T[] array, Comparator<? super T> comparator)`
Input: `array = [38, 27, 43, 3, 9, 82, 10]` (indices 0..6)

## Split phase (recursion down to single elements)

Each call `mergeSort(array, left, right)` splits at
`mid = left + (right - left) / 2` until `left >= right` (base case: a
single element, already "sorted").

| Call | Range (indices) | Elements | Splits into |
|---|---|---|---|
| mergeSort(0,6) | [0..6] | [38,27,43,3,9,82,10] | mergeSort(0,3) + mergeSort(4,6) |
| mergeSort(0,3) | [0..3] | [38,27,43,3] | mergeSort(0,1) + mergeSort(2,3) |
| mergeSort(0,1) | [0..1] | [38,27] | mergeSort(0,0) + mergeSort(1,1) |
| mergeSort(0,0) | [0..0] | [38] | base case |
| mergeSort(1,1) | [1..1] | [27] | base case |
| mergeSort(2,3) | [2..3] | [43,3] | mergeSort(2,2) + mergeSort(3,3) |
| mergeSort(2,2) | [2..2] | [43] | base case |
| mergeSort(3,3) | [3..3] | [3] | base case |
| mergeSort(4,6) | [4..6] | [9,82,10] | mergeSort(4,5) + mergeSort(6,6) |
| mergeSort(4,5) | [4..5] | [9,82] | mergeSort(4,4) + mergeSort(5,5) |
| mergeSort(4,4) | [4..4] | [9] | base case |
| mergeSort(5,5) | [5..5] | [82] | base case |
| mergeSort(6,6) | [6..6] | [10] | base case |

## Merge phase (combining back up)

| Merge call | Left (sorted) | Right (sorted) | Comparisons | Result |
|---|---|---|---|---|
| merge(0,0,1) | [38] | [27] | 38 ≤ 27? no → take 27, then 38 | [27, 38] |
| merge(2,2,3) | [43] | [3] | 43 ≤ 3? no → take 3, then 43 | [3, 43] |
| merge(0,1,3) | [27, 38] | [3, 43] | 27≤3? no→3; 27≤43? yes→27; 38≤43? yes→38; remaining→43 | [3, 27, 38, 43] |
| merge(4,4,5) | [9] | [82] | 9 ≤ 82? yes → take 9, then 82 | [9, 82] |
| merge(4,5,6) | [9, 82] | [10] | 9≤10? yes→9; 82≤10? no→10; remaining→82 | [9, 10, 82] |
| merge(0,3,6) | [3,27,38,43] | [9,10,82] | 3≤9→3; 27≤9? no→9; 27≤10? no→10; 27≤82→27; 38≤82→38; 43≤82→43; remaining→82 | [3, 9, 10, 27, 38, 43, 82] |

Final result: `[3, 9, 10, 27, 38, 43, 82]` — matches
`MergeSortTest.testNormalUnsortedArray`.

## Recurrence relation (brief §7)

`T(n) = 2T(n/2) + O(n)` — two recursive calls on halves of the input,
plus O(n) work to merge the two sorted halves back together. By the
Master Theorem (case 2: `f(n) = O(n^log_b(a))` with `a=2, b=2`), this
resolves to **T(n) = O(n log n)** for best, average, and worst case —
unlike selection/insertion sort, merge sort's runtime does not depend
on the input's initial ordering, only on `n`.

Space complexity is O(n): the `merge` step allocates temporary `L`/`R`
arrays proportional to the size of the range being merged.

Loop invariant for `merge` (proof sketch, brief §10): at the start of
each iteration of the main merge loop, `array[left..k-1]` contains the
`k - left` smallest elements of `L` and `R` combined, in sorted order.
Combined with the base case (before any iteration, this is trivially
true for an empty prefix) and the fact that each iteration either
extends this prefix with `L[i]` or `R[j]` — whichever is smaller —
correctness of the full merge follows by induction on `k`.
