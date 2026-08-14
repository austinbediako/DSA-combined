# Trace table — Linear Search

Algorithm: `LinearSearch.search(int[] array, int target)`
Input: `array = [12, 45, 3, 27, 45, 9]`, `target = 27`

| Step (i) | array[i] | Comparison (array[i] == target?) | Action |
|---|---|---|---|
| 0 | 12 | 12 == 27 → false | continue |
| 1 | 45 | 45 == 27 → false | continue |
| 2 | 3  | 3 == 27 → false | continue |
| 3 | 27 | 27 == 27 → true  | return i = 3 |

Result: index 3 (first match). Loop invariant: before iteration i, no
element in `array[0..i-1]` equals the target — used in the proof
sketch for correctness. Total comparisons: 4.

## Not-found case

Input: `array = [12, 45, 3, 27, 45, 9]`, `target = 99`

| Step (i) | array[i] | Comparison | Action |
|---|---|---|---|
| 0 | 12 | false | continue |
| 1 | 45 | false | continue |
| 2 | 3  | false | continue |
| 3 | 27 | false | continue |
| 4 | 45 | false | continue |
| 5 | 9  | false | continue |
| — | — | i == array.length | return -1 |

Total comparisons: 6 (worst case, n comparisons for n elements).
