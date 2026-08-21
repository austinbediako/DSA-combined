package gh.edu.ug.dsaoptimizer.algorithms;

import gh.edu.ug.dsaoptimizer.structures.DisjointSet;
import gh.edu.ug.dsaoptimizer.structures.DynamicArray;
import gh.edu.ug.dsaoptimizer.structures.Graph;

import java.util.Comparator;

/**
 * Kruskal's minimum spanning tree algorithm: sort all edges by weight
 * (reusing this project's own {@link MergeSort}), then greedily add
 * each edge that does not close a cycle, using {@link DisjointSet} to
 * detect cycles in near-constant time (this is the "Kruskal
 * connectivity trace" the brief asks for in section 6).
 *
 * <p>Complexity: O(E log E) for the sort, plus O(E * alpha(V)) for the
 * union-find operations (alpha = inverse Ackermann, effectively
 * constant) -- dominated by the sort.
 *
 * <p>Edge case: on a disconnected graph, Kruskal naturally produces a
 * minimum spanning *forest* -- it simply runs out of edges that both
 * connect components and don't close a cycle, leaving fewer than
 * V-1 edges in the result.
 */
public final class Kruskal {

    private Kruskal() {
        // utility class -- no instances
    }

    public static <T> MSTResult<T> mst(Graph<T> graph) {
        if (graph == null) throw new IllegalArgumentException("graph must not be null");

        Object[] nodesRaw = graph.nodes();
        if (nodesRaw.length == 0) {
            return new MSTResult<>(emptyEdgeArray(), 0.0);
        }

        DynamicArray<WeightedEdge<T>> candidateList = new DynamicArray<>();
        for (Object nodeObj : nodesRaw) {
            @SuppressWarnings("unchecked")
            T node = (T) nodeObj;
            graph.neighbors(node).forEach((neighbor, weight) ->
                    candidateList.add(new WeightedEdge<>(node, neighbor, weight)));
        }

        WeightedEdge<T>[] edges = toArray(candidateList);
        MergeSort.sort(edges, Comparator.comparingDouble((WeightedEdge<T> e) -> e.weight));

        DisjointSet<T> disjointSet = new DisjointSet<>();
        for (Object nodeObj : nodesRaw) {
            @SuppressWarnings("unchecked")
            T node = (T) nodeObj;
            disjointSet.makeSet(node);
        }

        DynamicArray<WeightedEdge<T>> mstEdges = new DynamicArray<>();
        double totalCost = 0.0;
        for (WeightedEdge<T> edge : edges) {
            if (!disjointSet.connected(edge.from, edge.to)) {
                disjointSet.union(edge.from, edge.to);
                mstEdges.add(edge);
                totalCost += edge.weight;
            }
        }

        return new MSTResult<>(toArray(mstEdges), totalCost);
    }

    @SuppressWarnings("unchecked")
    private static <T> WeightedEdge<T>[] toArray(DynamicArray<WeightedEdge<T>> list) {
        WeightedEdge<T>[] result = new WeightedEdge[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private static <T> WeightedEdge<T>[] emptyEdgeArray() {
        return new WeightedEdge[0];
    }
}
