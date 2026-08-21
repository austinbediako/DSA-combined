package gh.edu.ug.dsaoptimizer.algorithms;

import gh.edu.ug.dsaoptimizer.structures.Graph;
import gh.edu.ug.dsaoptimizer.structures.HashSetWrapper;
import gh.edu.ug.dsaoptimizer.structures.Queue;

/**
 * Breadth-first search over a {@link Graph}, using this project's own
 * Queue and HashSetWrapper (no java.util.ArrayDeque/HashSet).
 *
 * <p>Complexity: O(V + E) -- every node is enqueued/visited once, and
 * every edge is examined once when expanding its source node.
 *
 * <p>Answers "which locations are reachable from the current dispatch
 * point" (brief section 3.3) via {@link #reachableFrom}, and gives the
 * level-by-level traversal order used for the BFS trace table.
 */
public final class BFS {

    private BFS() {
        // utility class -- no instances
    }

    /** Returns nodes in the order BFS visits them, starting from source. */
    public static <T> Object[] traverse(Graph<T> graph, T source) {
        if (graph == null) throw new IllegalArgumentException("graph must not be null");
        if (source == null) throw new IllegalArgumentException("source must not be null");
        if (!graph.hasNode(source)) throw new IllegalArgumentException("source not in graph: " + source);

        HashSetWrapper<T> visited = new HashSetWrapper<>();
        Queue<T> queue = new Queue<>();
        Object[] order = new Object[graph.nodeCount()];
        int[] count = {0};

        visited.add(source);
        queue.enqueue(source);

        while (!queue.isEmpty()) {
            T current = queue.dequeue();
            order[count[0]++] = current;

            graph.neighbors(current).forEach((neighbor, weight) -> {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.enqueue(neighbor);
                }
            });
        }

        Object[] result = new Object[count[0]];
        System.arraycopy(order, 0, result, 0, count[0]);
        return result;
    }

    /** Returns whether target is reachable from source (including source == target). */
    public static <T> boolean isReachable(Graph<T> graph, T source, T target) {
        if (target == null) throw new IllegalArgumentException("target must not be null");
        for (Object node : traverse(graph, source)) {
            if (node.equals(target)) return true;
        }
        return false;
    }
}
