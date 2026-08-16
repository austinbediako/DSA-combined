# Trace table — Binary Search

Algorithm: `BinarySearch.search(int[] array, int target)`
Precondition: `array` must be sorted ascending (see counterexample below
for what happens when this precondition is violated).

## Found case

Input: `array = [2, 3, 4, 8, 15, 47, 99]`, `target = 47`

| Step | low | high | mid | array[mid] | Comparison | Action |
|---|---|---|---|---|---|---|
| 1 | 0 | 6 | 3 | 8  | 8 == 47 → false, 8 < 47 → true  | low = mid + 1 = 4 |
| 2 | 4 | 6 | 5 | 47 | 47 == 47 → true                | return mid = 5 |

Result: index 5. Total comparisons: 2 (matches ⌈log2(7)⌉ = 3 worst case,
found early here).

## Not-found case

Input: `array = [2, 3, 4, 8, 15, 47, 99]`, `target = 10`

| Step | low | high | mid | array[mid] | Comparison | Action |
|---|---|---|---|---|---|---|
| 1 | 0 | 6 | 3 | 8  | 8 < 10  | low = mid + 1 = 4 |
| 2 | 4 | 6 | 5 | 47 | 47 > 10 | high = mid - 1 = 4 |
| 3 | 4 | 4 | 4 | 15 | 15 > 10 | high = mid - 1 = 3 |
| — | 4 | 3 | — | — | low > high | loop ends, return -1 |

Result: -1 (not found). Total comparisons: 3, consistent with O(log n)
for n = 7 (⌈log2(7)⌉ = 3).

Loop invariant used in the proof sketch: before every iteration, if
`target` exists in `array`, it lies within `array[low..high]`. Each
iteration either finds it or halves the range. When `low > high`, the
range is empty, so the invariant guarantees `target` is absent —
justifying the `return -1`.

## Counterexample — unsorted input (precondition violation)

Binary search's correctness depends entirely on the array being
sorted. This implementation does not validate that precondition (doing
so cheaply isn't possible without an O(n) pre-scan, which would defeat
the point of an O(log n) search), so running it on unsorted data
silently gives an unreliable answer instead of throwing.

Input: `array = [99, 2, 47, 3, 15, 8, 4]` (unsorted), `target = 99`
(present at index 0)

| Step | low | high | mid | array[mid] | Comparison | Action |
|---|---|---|---|---|---|---|
| 1 | 0 | 6 | 3 | 3 | 3 < 99 | low = mid + 1 = 4 |
| 2 | 4 | 6 | 5 | 8 | 8 < 99 | low = mid + 1 = 6 |
| 3 | 6 | 6 | 6 | 4 | 4 < 99 | low = mid + 1 = 7 |
| — | 7 | 6 | — | — | low > high | loop ends, return -1 |

Result: **-1 (WRONG — target 99 is actually at index 0)**. The
comparisons at each step discard the half of the array containing the
target, because the array isn't sorted and the algorithm's "discard
half" logic assumes it is. This is exactly why the sorted precondition
must be documented, tested, and upheld by every caller — see
`BinarySearchTest.unsortedInputViolatesPreconditionAndGivesWrongAnswer`
for the corresponding automated test.
