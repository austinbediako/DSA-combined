# Trace table — Selection Sort

Algorithm: `SelectionSort.sort(int[] array)` (ascending)
Input: `array = [29, 10, 14, 37, 13]`

| Pass (i) | Unsorted region scanned (j) | minIndex found | Swap | Array after pass |
|---|---|---|---|---|
| 0 | j = 1..4, compares 10,14,37,13 against current min | 1 (value 10) | swap array[0], array[1] | [10, 29, 14, 37, 13] |
| 1 | j = 2..4, compares 14,37,13 against current min | 4 (value 13) | swap array[1], array[4] | [10, 13, 14, 37, 29] |
| 2 | j = 3..4, compares 37,29 against current min | 2 (value 14, already min) | no swap needed | [10, 13, 14, 37, 29] |
| 3 | j = 4, compares 29 against current min | 4 (value 29) | swap array[3], array[4] | [10, 13, 14, 29, 37] |

Result: `[10, 13, 14, 29, 37]`. n = 5, so passes = n-1 = 4, total
comparisons = 4+3+2+1 = 10 (matches n(n-1)/2), swaps = 3 (≤ n-1 = 4).

Loop invariant used in the proof sketch: after pass i completes,
`array[0..i]` is sorted and every value in it is ≤ every value in
`array[i+1..n-1]`.
