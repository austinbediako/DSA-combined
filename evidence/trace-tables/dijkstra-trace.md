# Trace table — Dijkstra's Algorithm

Algorithm: `Graph.dijkstra(T source)` / `Graph.shortestPath(T source, T target)`
Input graph (directed edges as shown; this is the same example used in
`GraphTest.testDijkstraShortestPath` and the Kruskal/Prim MST evidence):

```
A --1--> B --2--> C --1--> D
A --4--> C
B --5--> D
```

Source: `A`. Priority queue entries shown as `(node, distance)`.

| Step | Node polled | dist so far | Action | Distance table after step (A, B, C, D) |
|---|---|---|---|---|
| init | — | — | dist[A]=0, all others=∞; push (A,0) | 0, ∞, ∞, ∞ |
| 1 | A (0) | 0 | relax A→B: 0+1=1 < ∞ → update, push (B,1); relax A→C: 0+4=4 < ∞ → update, push (C,4) | 0, 1, 4, ∞ |
| 2 | B (1) | 1 | relax B→C: 1+2=3 < 4 → update, push (C,3); relax B→D: 1+5=6 < ∞ → update, push (D,6) | 0, 1, 3, 6 |
| 3 | C (3) | 3 | (this is the *updated* C entry; the earlier (C,4) is now stale) relax C→D: 3+1=4 < 6 → update, push (D,4) | 0, 1, 3, 4 |
| 4 | C (4) — stale | — | popped entry's distance (4) > current dist[C] (3) → skipped | 0, 1, 3, 4 |
| 5 | D (4) | 4 | no outgoing edges from D | 0, 1, 3, 4 |
| 6 | D (6) — stale | — | popped entry's distance (6) > current dist[D] (4) → skipped | 0, 1, 3, 4 |

Final distances: `A=0, B=1, C=3, D=4`.

Predecessor path reconstruction for `shortestPath(A, D)`: walk
predecessors backward from D — `D ← C ← B ← A`, then reverse:
**`[A, B, C, D]`**, matching `GraphTest.testDijkstraShortestPath`.

Loop invariant (proof sketch, brief section 10): whenever a node `u`
is popped from the priority queue with the *current* (non-stale)
distance, `dist[u]` already equals the true shortest distance from the
source. This holds because Dijkstra always extracts the minimum
remaining tentative distance, and edge weights are non-negative, so no
later relaxation through an unprocessed (necessarily farther) node
could ever produce a shorter path to `u`. The stale-entry check
(`if (d > dist.get(u)) continue`) is what makes it safe to leave old,
now-obsolete queue entries in place rather than removing them.
