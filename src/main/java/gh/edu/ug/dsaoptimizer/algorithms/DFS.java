package gh.edu.ug.dsaoptimizer.algorithms;

import gh.edu.ug.dsaoptimizer.structures.Graph;
import gh.edu.ug.dsaoptimizer.structures.HashSetWrapper;
import gh.edu.ug.dsaoptimizer.structures.Stack;

/**
 * Depth-first search over a {@link Graph}, iterative using this
 * project's own Stack and HashSetWrapper (no java.util recursion-stack
 * shortcuts or java.util.HashSet).
 *
 * <p>Complexity: O(V + E), same as BFS -- every node and edge is
 * examined at most once. The traversal order differs from BFS because
 * a stack (LIFO) explores one branch fully before backtracking, while
 * a queue (FIFO) explores level by level.
 */
public final class DFS {

    private DFS() {
        // utility class -- no instances
    }

    /** Returns nodes in the order DFS visits them, starting from source. */
    public static <T> Object[] traverse(Graph<T> graph, T source) {
        if (graph == null) throw new IllegalArgumentException("graph must not be null");
        if (source == null) throw new IllegalArgumentException("source must not be null");
        if (!graph.hasNode(source)) throw new IllegalArgumentException("source not in graph: " + source);

        HashSetWrapper<T> visited = new HashSetWrapper<>();
        Stack<T> stack = new Stack<>();
        Object[] order = new Object[graph.nodeCount()];
        int count = 0;

        stack.push(source);

        while (!stack.isEmpty()) {
            T current = stack.pop();
            if (visited.contains(current)) {
                continue; // already visited via a different path
            }
            visited.add(current);
            order[count++] = current;

            graph.neighbors(current).forEach((neighbor, weight) -> {
                if (!visited.contains(neighbor)) {
                    stack.push(neighbor);
                }
            });
        }

        Object[] result = new Object[count];
        System.arraycopy(order, 0, result, 0, count);
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
