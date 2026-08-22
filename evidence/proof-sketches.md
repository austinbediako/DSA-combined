# Proof Sketches

Required by the brief (section 10): at least 3 — a loop invariant for
search/sort, an induction/recursion proof, and a greedy or DP
correctness idea.

## 1. Loop invariant — Binary Search

Algorithm: `BinarySearch.search(int[] array, int target)`
(see `evidence/trace-tables/binary-search-trace.md` for a worked example)

**Precondition:** `array` is sorted in ascending order.

**Invariant:** at the start of every iteration of the `while (low <= high)`
loop, *if* `target` occurs anywhere in `array`, it occurs within the
index range `array[low..high]`.

- **Initialization:** before the first iteration, `low = 0` and
  `high = array.length - 1`, i.e. the full array — trivially, if
  `target` is present at all, it's within `array[low..high]`.
- **Maintenance:** each iteration computes `mid` and compares
  `array[mid]` to `target`.
  - If equal, the target is found and the loop returns immediately.
  - If `array[mid] < target`, then because the array is sorted,
    `target` (if present) cannot lie in `array[low..mid]` — so setting
    `low = mid + 1` preserves the invariant.
  - If `array[mid] > target`, symmetrically `target` cannot lie in
    `array[mid..high]`, so setting `high = mid - 1` preserves it.
- **Termination:** the loop ends when `low > high`, meaning the range
  `array[low..high]` is empty. By the invariant, `target` cannot be
  present anywhere in the array in this case, so returning `-1` is
  correct.

Each iteration strictly shrinks `high - low`, so the loop terminates
in at most `⌈log2(n+1)⌉` iterations.

**Why the precondition matters:** the maintenance step's correctness
depends entirely on the array being sorted — that's what licenses
"discard the half that can't contain the target." Violate it and the
invariant no longer holds after the first comparison; see
`BinarySearchTest.unsortedInputViolatesPreconditionAndGivesWrongAnswer`
and its trace in `evidence/trace-tables/binary-search-trace.md` for the
concrete counterexample.

## 2. Induction/recursion proof — Merge Sort

Algorithm: `MergeSort.sort` / the private `mergeSort`/`merge` helpers
(see `evidence/trace-tables/merge-sort-trace.md` for a worked example)

**Claim:** for any array `array[left..right]`, after
`mergeSort(array, left, right, comparator)` returns, `array[left..right]`
is sorted in ascending order according to `comparator`.

**Proof by strong induction on the range size `n = right - left + 1`.**

- **Base case (n ≤ 1):** `mergeSort` only recurses when `left < right`;
  a range of size 0 or 1 is trivially sorted (there's nothing to compare).
- **Inductive step:** assume the claim holds for every range strictly
  smaller than `n` (strong induction hypothesis). For a range of size
  `n > 1`, `mergeSort` computes `mid` and recursively sorts
  `array[left..mid]` and `array[mid+1..right]` — both strictly smaller
  than `n`, so by the hypothesis both halves are correctly sorted
  after their recursive calls return.
- It remains to show `merge` correctly combines two sorted halves into
  one sorted whole. `merge`'s own loop invariant: at the start of each
  iteration of its main `while (i < n1 && j < n2)` loop,
  `array[left..k-1]` contains the `k - left` smallest elements of the
  two halves combined, in sorted order. Each iteration compares the
  current heads of the two (sorted) temporary arrays `L`/`R` and
  appends whichever is smaller (or `L`'s on a tie, preserving order of
  first appearance) — extending the invariant by one element. When one
  side is exhausted, the remaining elements of the other side are
  already the largest remaining and already sorted relative to each
  other, so appending them in order preserves the invariant through to
  `k = right + 1`.
- Therefore `array[left..right]` is fully sorted after `merge` returns,
  completing the inductive step.

By strong induction, the claim holds for all `n`, including the
top-level call `mergeSort(array, 0, array.length - 1, comparator)`.

## 3. Greedy correctness — Kruskal's Algorithm (MST)

(See `evidence/trace-tables/kruskal-trace.md` for a worked example.
Contrast with `GreedyAssignmentTest.greedyFailsToFindTheOptimalAssignment`,
which shows a *different* greedy strategy that is **not** safe — the
point of including both is that greedy is only correct when the
problem has the right structure, and part of this project's
correctness evidence is knowing which is which.)

**Claim:** processing edges in ascending weight order and adding an
edge whenever it does not close a cycle (checked via `DisjointSet`)
produces a minimum spanning tree.

**Proof sketch (cut property / exchange argument):**

- **Cut property:** for any partition of the graph's vertices into two
  non-empty sets, the minimum-weight edge crossing that partition
  belongs to *some* minimum spanning tree. (Sketch: if an MST `T`
  didn't contain that minimum crossing edge `e`, then `T` must contain
  some other, more expensive edge `f` crossing the same partition
  (since `T` is a spanning tree, at least one of its edges must cross
  every such partition). Swapping `f` out for `e` keeps `T` a spanning
  tree — the graph stays connected because `e` reconnects exactly what
  `f` connected across that cut — and cannot increase total weight
  since `weight(e) ≤ weight(f)` by assumption. So an MST containing
  `e` exists.)
- Kruskal, at every step, considers the globally cheapest remaining
  edge. If that edge connects two different components (i.e.
  `!connected(from, to)`), those two components — each a connected
  subgraph — define a partition of the vertex set, and the edge being
  considered is the minimum-weight edge crossing it (every cheaper
  edge was already processed and, since it didn't create this
  situation, must lie strictly inside one component or the other).
  By the cut property, including it is always safe — some MST contains
  it.
- If instead the edge connects two vertices already in the same
  component (`connected(from, to)` is true), including it would create
  a cycle. A spanning tree by definition has no cycles, so it is
  correctly rejected.
- Repeating this for every edge in ascending order, using
  `DisjointSet.union`/`connected` to track components in near-constant
  time, yields a maximal cycle-free edge set connecting every
  reachable vertex — i.e. an MST (or minimum spanning forest, if the
  graph is disconnected — see `KruskalTest.disconnectedGraphProducesSpanningForestNotSingleTree`).

**Why this differs from `GreedyAssignment`'s failure:** the MST problem
has *matroid structure* — the "no cycle" constraint is closed under
taking subsets, and the cut property guarantees every greedy choice is
part of *some* optimal solution. The assignment problem in
`GreedyAssignment` has no such guarantee: committing request 1 to its
locally cheapest resource can make every remaining option for request
2 worse, with no exchange argument available to fix it after the fact
(see the worked counterexample: greedy cost 101 vs. true optimum 4).
