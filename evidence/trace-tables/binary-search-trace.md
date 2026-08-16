# Trace table — Binary Search



Algorithm: `BinarySearch.search(int[] array, int target)`

Input: `array = [3, 8, 12, 17, 21, 26, 31, 45, 52]`, `target = 26`



| Step | low | high | mid | array[mid] | Comparison | Action |

|---|---:|---:|---:|---:|---|---|

| 1 | 0 | 8 | 4 | 21 | 21 < 26 | search right half |

| 2 | 5 | 8 | 6 | 31 | 31 > 26 | search left half |

| 3 | 5 | 5 | 5 | 26 | 26 == 26 | return index 5 |



Result: index 5. Total comparisons: 3.



Loop invariant: at the start of each iteration, if the target

exists in the array, it must be within the current search interval

`array[low..high]`. Each comparison eliminates half of the remaining

search interval.



## Not-found case



Input: `array = [3, 8, 12, 17, 21, 26, 31, 45, 52]`, `target = 30`



| Step | low | high | mid | array[mid] | Comparison | Action |

|---|---:|---:|---:|---:|---|---|

| 1 | 0 | 8 | 4 | 21 | 21 < 30 | search right half |

| 2 | 5 | 8 | 6 | 31 | 31 > 30 | search left half |

| 3 | 5 | 5 | 5 | 26 | 26 < 30 | search right half |

| — | 6 | 5 | — | — | low > high | return -1 |



Result: index -1 (target not found). Total comparisons: 3.



Precondition: the input array must be sorted in ascending order.

Time complexity: O(log n) in the worst case.

Space complexity: O(1) for the iterative implementation.

