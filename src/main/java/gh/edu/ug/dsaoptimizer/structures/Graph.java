package gh.edu.ug.dsaoptimizer.structures;

import java.util.*;

/**
 * Directed, weighted graph using adjacency lists.
 * - Generic node type T (must implement equals/hashCode reasonably).
 * - Disallows null nodes and null edges.
 * - Edge weights must be non-negative (Dijkstra requirement).
 *
 * Provides Dijkstra shortest-path algorithm (non-negative weights).
 */
public class Graph<T> {
    private final Map<T, Map<T, Double>> adj = new HashMap<>();

    public void addNode(T node) {
        if (node == null) throw new NullPointerException("node cannot be null");
        adj.putIfAbsent(node, new HashMap<>());
    }

    public boolean hasNode(T node) {
        if (node == null) throw new NullPointerException("node cannot be null");
        return adj.containsKey(node);
    }

    /**
     * Add a directed edge from -> to with given non-negative weight.
     */
    public void addEdge(T from, T to, double weight) {
        if (from == null || to == null) throw new NullPointerException("nodes cannot be null");
        if (weight < 0) throw new IllegalArgumentException("edge weight must be non-negative");
        addNode(from);
        addNode(to);
        adj.get(from).put(to, weight);
    }

    public boolean hasEdge(T from, T to) {
        if (from == null || to == null) throw new NullPointerException("nodes cannot be null");
        Map<T, Double> edges = adj.get(from);
        return edges != null && edges.containsKey(to);
    }

    public Map<T, Double> neighbors(T node) {
        if (node == null) throw new NullPointerException("node cannot be null");
        Map<T, Double> map = adj.get(node);
        return map == null ? Collections.emptyMap() : Collections.unmodifiableMap(map);
    }

    public void removeEdge(T from, T to) {
        if (from == null || to == null) throw new NullPointerException("nodes cannot be null");
        Map<T, Double> edges = adj.get(from);
        if (edges != null) edges.remove(to);
    }

    public void removeNode(T node) {
        if (node == null) throw new NullPointerException("node cannot be null");
        if (!adj.containsKey(node)) return;
        adj.remove(node);
        // remove incoming edges
        for (Map<T, Double> edges : adj.values()) {
            edges.remove(node);
        }
    }

    /**
     * Dijkstra's algorithm from source. Returns a map of distances (Double.POSITIVE_INFINITY if unreachable).
     * Also returns internally the predecessor map which can be used to reconstruct paths via shortestPath().
     */
    public Map<T, Double> dijkstra(T source) {
        if (source == null) throw new NullPointerException("source cannot be null");
        if (!adj.containsKey(source)) throw new IllegalArgumentException("source node not found in graph");

        // distances
        Map<T, Double> dist = new HashMap<>();
        Map<T, T> pred = new HashMap<>();
        for (T node : adj.keySet()) dist.put(node, Double.POSITIVE_INFINITY);
        dist.put(source, 0.0);

        // min-heap using PriorityQueueHeap with comparator on distances
        PriorityQueueHeap<Entry<T>> pq = new PriorityQueueHeap<>(Comparator.comparingDouble(e -> e.distance));
        pq.offer(new Entry<>(source, 0.0));

        while (!pq.isEmpty()) {
            Entry<T> e = pq.poll();
            T u = e.node;
            double d = e.distance;
            // stale entry check
            if (d > dist.get(u)) continue;

            Map<T, Double> neighbors = adj.get(u);
            if (neighbors == null) continue;
            for (Map.Entry<T, Double> nbr : neighbors.entrySet()) {
                T v = nbr.getKey();
                double w = nbr.getValue();
                double alt = d + w;
                if (alt < dist.get(v)) {
                    dist.put(v, alt);
                    pred.put(v, u);
                    pq.offer(new Entry<>(v, alt));
                }
            }
        }

        // store predecessor map by attaching to a special unmodifiable map in distances? Instead,
        // expose a path reconstruction method that calls dijkstra again to rebuild predecessors.
        // To avoid re-running dijkstra multiple times, provide a helper that returns both maps.
        // For simplicity, we return distances; shortestPath() will run a separate Dijkstra internally to get preds.

        return dist;
    }

    /**
     * Compute shortest path from source to target using Dijkstra. Returns the path as a list from source..target
     * or null if target unreachable. Throws if source/target not present.
     */
    public List<T> shortestPath(T source, T target) {
        if (source == null || target == null) throw new NullPointerException("nodes cannot be null");
        if (!adj.containsKey(source) || !adj.containsKey(target)) throw new IllegalArgumentException("source/target not in graph");

        // run Dijkstra but capture predecessors
        Map<T, Double> dist = new HashMap<>();
        Map<T, T> pred = new HashMap<>();
        for (T node : adj.keySet()) dist.put(node, Double.POSITIVE_INFINITY);
        dist.put(source, 0.0);

        PriorityQueueHeap<Entry<T>> pq = new PriorityQueueHeap<>(Comparator.comparingDouble(e -> e.distance));
        pq.offer(new Entry<>(source, 0.0));

        while (!pq.isEmpty()) {
            Entry<T> e = pq.poll();
            T u = e.node;
            double d = e.distance;
            if (d > dist.get(u)) continue;
            if (u.equals(target)) break; // early exit

            Map<T, Double> neighbors = adj.get(u);
            if (neighbors == null) continue;
            for (Map.Entry<T, Double> nbr : neighbors.entrySet()) {
                T v = nbr.getKey();
                double w = nbr.getValue();
                double alt = d + w;
                if (alt < dist.get(v)) {
                    dist.put(v, alt);
                    pred.put(v, u);
                    pq.offer(new Entry<>(v, alt));
                }
            }
        }

        if (dist.get(target).isInfinite()) return null;

        LinkedList<T> path = new LinkedList<>();
        T cur = target;
        while (cur != null) {
            path.addFirst(cur);
            cur = pred.get(cur);
        }
        // ensure path starts with source
        if (!path.isEmpty() && !path.getFirst().equals(source)) return null;
        return path;
    }

    private static final class Entry<T> {
        final T node;
        final double distance;

        Entry(T node, double distance) {
            this.node = node;
            this.distance = distance;
        }
    }
}
