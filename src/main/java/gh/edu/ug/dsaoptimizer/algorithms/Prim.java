package gh.edu.ug.dsaoptimizer.algorithms;

import gh.edu.ug.dsaoptimizer.structures.DynamicArray;
import gh.edu.ug.dsaoptimizer.structures.Graph;
import gh.edu.ug.dsaoptimizer.structures.HashSetWrapper;
import gh.edu.ug.dsaoptimizer.structures.PriorityQueueHeap;

import java.util.Comparator;

/**
 * Prim's minimum spanning tree algorithm, grown outward from a start
 * node using this project's own PriorityQueueHeap as the "cheapest
 * crossing edge" frontier (no java.util.PriorityQueue).
 *
 * <p>Complexity: O(E log E) -- every edge can be offered to the heap
 * once, and each offer/poll is O(log E).
 *
 * <p>Edge case: if the graph is disconnected, Prim only spans the
 * connected component reachable from {@code start} -- the frontier
 * heap empties before every node is included, and the loop stops
 * there rather than producing a spanning forest (unlike Kruskal,
 * which naturally produces a minimum spanning forest across all
 * components since it doesn't grow from a single root).
 */
public final class Prim {

    private Prim() {
        // utility class -- no instances
    }

    public static <T> MSTResult<T> mst(Graph<T> graph, T start) {
        if (graph == null) throw new IllegalArgumentException("graph must not be null");
        if (start == null) throw new IllegalArgumentException("start must not be null");
        if (!graph.hasNode(start)) throw new IllegalArgumentException("start not in graph: " + start);

        HashSetWrapper<T> inMst = new HashSetWrapper<>();
        PriorityQueueHeap<CandidateEdge<T>> frontier =
                new PriorityQueueHeap<>(Comparator.comparingDouble(e -> e.weight));

        inMst.add(start);
        offerEdgesFrom(graph, start, inMst, frontier);

        DynamicArray<WeightedEdge<T>> mstEdges = new DynamicArray<>();
        double totalCost = 0.0;
        int totalNodes = graph.nodeCount();

        while (!frontier.isEmpty() && inMst.size() < totalNodes) {
            CandidateEdge<T> candidate = frontier.poll();
            if (inMst.contains(candidate.to)) {
                continue; // stale entry -- endpoint already joined via a cheaper edge
            }
            inMst.add(candidate.to);
            mstEdges.add(new WeightedEdge<>(candidate.from, candidate.to, candidate.weight));
            totalCost += candidate.weight;
            offerEdgesFrom(graph, candidate.to, inMst, frontier);
        }

        return new MSTResult<>(toArray(mstEdges), totalCost);
    }

    private static <T> void offerEdgesFrom(Graph<T> graph, T node, HashSetWrapper<T> inMst,
                                            PriorityQueueHeap<CandidateEdge<T>> frontier) {
        graph.neighbors(node).forEach((neighbor, weight) -> {
            if (!inMst.contains(neighbor)) {
                frontier.offer(new CandidateEdge<>(node, neighbor, weight));
            }
        });
    }

    @SuppressWarnings("unchecked")
    private static <T> WeightedEdge<T>[] toArray(DynamicArray<WeightedEdge<T>> list) {
        WeightedEdge<T>[] result = new WeightedEdge[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

    private static final class CandidateEdge<T> {
        final T from;
        final T to;
        final double weight;

        CandidateEdge(T from, T to, double weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }
}
