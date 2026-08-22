# Trace table — Kruskal's Algorithm (MST + connectivity trace)

Algorithm: `Kruskal.mst(Graph<T> graph)`, backed by `DisjointSet`
(brief section 6: "Kruskal connectivity trace"). Same graph as the
Dijkstra/Prim evidence, treated as undirected (each edge usable in
either direction, matching how `RouteService` builds the real road
network):

```
A-B: 1   A-C: 4   B-C: 2   B-D: 5   C-D: 1
```

## Step 1 — sort edges by weight (reusing MergeSort)

| Order | Edge | Weight |
|---|---|---|
| 1 | A-B | 1 |
| 2 | C-D | 1 |
| 3 | B-C | 2 |
| 4 | A-C | 4 |
| 5 | B-D | 5 |

## Step 2 — process edges in order, using DisjointSet to detect cycles

Initial state: `makeSet` on A, B, C, D — four singleton sets, each its
own root.

| Edge | `connected(from, to)`? | Action | Sets after this step |
|---|---|---|---|
| A-B (1) | false | union(A,B) — add to MST | {A,B}, {C}, {D} |
| C-D (1) | false | union(C,D) — add to MST | {A,B}, {C,D} |
| B-C (2) | false | union(B,C) — add to MST | {A,B,C,D} |
| A-C (4) | **true** | already connected — **skip** (would close a cycle) | {A,B,C,D} |
| B-D (5) | **true** | already connected — **skip** (would close a cycle) | {A,B,C,D} |

Result: MST edges = `{A-B(1), C-D(1), B-C(2)}`, total cost = **4**.
This matches `KruskalTest.classicFourNodeExampleProducesMinimumSpanningTree`
and `DisjointSetTest.kruskalConnectivityTraceFourNodesThreeEdges`.

## Disconnected-graph edge case

If a fifth node `Z` exists with no edges to A/B/C/D, Kruskal simply
never encounters an edge that could union `{Z}` into the main
component — it produces a **minimum spanning forest**: the 3-edge MST
above plus the singleton `{Z}`, rather than erroring. See
`KruskalTest.disconnectedGraphProducesSpanningForestNotSingleTree`.

## Correctness note (cycle property)

Every time Kruskal skips an edge because `connected(from, to)` is
already true, that edge's two endpoints are already joined by some
path using only smaller-or-equal-weight edges (since edges are
processed in ascending order) — so including it would only ever create
a cycle, never improve connectivity. This is the cut/cycle property
that guarantees Kruskal's greedy choice is safe at every step, unlike
`GreedyAssignment`'s locally-optimal-but-globally-wrong strategy.
