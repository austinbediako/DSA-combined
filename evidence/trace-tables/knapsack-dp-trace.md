# Trace table — 0/1 Knapsack (Dynamic Programming)

Algorithm: `Knapsack.solve(int[] weights, int[] values, int capacity)`
Input: `weights = [1, 3, 4, 5]`, `values = [1, 4, 5, 7]`, `capacity = 7`
(same input as `KnapsackTest.classicExampleFindsOptimalValueAndReconstructsSelection`)

## Tabulation — `table[i][w]` = best value using only the first `i` items within capacity `w`

| i \ w | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 |
|---|---|---|---|---|---|---|---|---|
| 0 (no items) | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 1 (item0: w=1,v=1) | 0 | 1 | 1 | 1 | 1 | 1 | 1 | 1 |
| 2 (item1: w=3,v=4) | 0 | 1 | 1 | 4 | 5 | 5 | 5 | 5 |
| 3 (item2: w=4,v=5) | 0 | 1 | 1 | 4 | 5 | 6 | 6 | 9 |
| 4 (item3: w=5,v=7) | 0 | 1 | 1 | 4 | 5 | 7 | 8 | 9 |

Sample cell derivation — `table[3][7]`: exclude item2 → `table[2][7]` = 5;
include item2 (weight 4, value 5) → `table[2][7-4] + 5` = `table[2][3] + 5`
= 4 + 5 = 9. Since 9 > 5, `table[3][7] = 9`.

Answer: `table[4][7] = 9`.

## Reconstruction — walk backward from `table[n][capacity]`

| i | w | `table[i][w]` | `table[i-1][w]` | Equal? | Action |
|---|---|---|---|---|---|
| 4 | 7 | 9 | 9 | yes | item3 **not** included; w unchanged |
| 3 | 7 | 9 | 5 | no | item2 **included** (weight 4, value 5); w ← 7-4 = 3 |
| 2 | 3 | 4 | 1 | no | item1 **included** (weight 3, value 4); w ← 3-3 = 0 |
| 1 | 0 | 0 | 0 | yes | item0 **not** included |

Selected indices (ascending): **[1, 2]** — weight 3+4=7 (≤ capacity 7),
value 4+5=**9**, matching the tabulated answer exactly.

## Recurrence relation (brief section 7)

```
table[i][w] = table[i-1][w]                                    if weight[i-1] > w
            = max(table[i-1][w], table[i-1][w-weight[i-1]] + value[i-1])   otherwise
```

Base case: `table[0][w] = 0` for all `w` (no items available, no value
possible). Complexity: O(n × capacity) time and space to build the
table, O(n) additional for reconstruction.

## Why DP succeeds where Greedy fails

This is the direct counterpart to `GreedyAssignment`'s counterexample
(`evidence/trace-tables/` — see `GreedyAssignmentTest`): a greedy,
highest-value-first or lightest-first strategy on this exact input can
easily lock in a locally-attractive item early and miss the true
optimum, because it never reconsiders a decision. The DP table instead
implicitly considers *every* combination of included/excluded items
before committing, which is exactly why `table[i][w]` is guaranteed to
hold the true optimal value for that (items-so-far, capacity) pair.
