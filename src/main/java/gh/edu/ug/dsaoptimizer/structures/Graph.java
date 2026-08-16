package gh.edu.ug.dsaoptimizer.structures;

/**
 * Directed, weighted graph using adjacency lists backed by custom HashTable.
 * - Generic node type T (must implement equals/hashCode reasonably).
 * - Disallows null nodes and null edges.
 * - Edge weights must be non-negative (Dijkstra requirement).
 *
 * Provides Dijkstra shortest-path algorithm (non-negative weights).
 */
public class Graph<T> {
    private final HashTable<T, HashTable<T, Double>> adj = new HashTable<>();

    public void addNode(T node) {
        if (node == null) throw new NullPointerException("node cannot be null");
        if (!adj.containsKey(node)) adj.put(node, new HashTable<>());
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
        HashTable<T, Double> edges = adj.get(from);
        edges.put(to, weight);
    }

    public boolean hasEdge(T from, T to) {
        if (from == null || to == null) throw new NullPointerException("nodes cannot be null");
        HashTable<T, Double> edges = adj.get(from);
        return edges != null && edges.containsKey(to);
    }

    public HashTable<T, Double> neighbors(T node) {
        if (node == null) throw new NullPointerException("node cannot be null");
        HashTable<T, Double> map = adj.get(node);
        return map == null ? new HashTable<>() : map;
    }

    public void removeEdge(T from, T to) {
        if (from == null || to == null) throw new NullPointerException("nodes cannot be null");
        HashTable<T, Double> edges = adj.get(from);
        if (edges != null) edges.remove(to);
    }

    public void removeNode(T node) {
        if (node == null) throw new NullPointerException("node cannot be null");
        if (!adj.containsKey(node)) return;
        adj.remove(node);
        // remove incoming edges
        adj.forEach((k, edges) -> {
            if (edges != null) edges.remove(node);
        });
    }

    /**
     * Dijkstra's algorithm from source. Returns a map of distances (Double.POSITIVE_INFINITY if unreachable).
     */
    public HashTable<T, Double> dijkstra(T source) {
        if (source == null) throw new NullPointerException("source cannot be null");
        if (!adj.containsKey(source)) throw new IllegalArgumentException("source node not found in graph");

        final double INF = Double.POSITIVE_INFINITY;
        HashTable<T, Double> dist = new HashTable<>();
        HashTable<T, T> pred = new HashTable<>();

        // initialize distances
        adj.forEach((node, edges) -> dist.put(node, INF));
        dist.put(source, 0.0);

        // min-heap using PriorityQueueHeap with comparator on distances
        PriorityQueueHeap<QueueEntry<T>> pq = new PriorityQueueHeap<>( (a, b) -> Double.compare(a.distance, b.distance) );
        pq.offer(new QueueEntry<>(source, 0.0));

        while (!pq.isEmpty()) {
            QueueEntry<T> e = pq.poll();
            T u = e.node;
            double d = e.distance;
            Double curDist = dist.get(u);
            if (curDist == null) continue;
            if (d > curDist) continue; // stale

            HashTable<T, Double> neighbors = adj.get(u);
            if (neighbors == null) continue;
            neighbors.forEach((v, w) -> {
                double alt = d + w;
                Double dv = dist.get(v);
                if (dv == null || alt < dv) {
                    dist.put(v, alt);
                    pred.put(v, u);
                    pq.offer(new QueueEntry<>(v, alt));
                }
            });
        }

        return dist;
    }

    /**
     * Compute shortest path from source to target using Dijkstra. Returns
     * the path as an array from source..target, or null if target
     * unreachable. Throws if source/target not present.
     *
     * <p>Returns {@code Object[]} rather than {@code T[]} for the same
     * type-erasure reason as {@link DoublyLinkedList#toArray()}.
     */
    public Object[] shortestPath(T source, T target) {
        if (source == null || target == null) throw new NullPointerException("nodes cannot be null");
        if (!adj.containsKey(source) || !adj.containsKey(target)) throw new IllegalArgumentException("source/target not in graph");

        final double INF = Double.POSITIVE_INFINITY;
        HashTable<T, Double> dist = new HashTable<>();
        HashTable<T, T> pred = new HashTable<>();

        adj.forEach((node, edges) -> dist.put(node, INF));
        dist.put(source, 0.0);

        PriorityQueueHeap<QueueEntry<T>> pq = new PriorityQueueHeap<>((a, b) -> Double.compare(a.distance, b.distance));
        pq.offer(new QueueEntry<>(source, 0.0));

        while (!pq.isEmpty()) {
            QueueEntry<T> e = pq.poll();
            T u = e.node;
            double d = e.distance;
            Double curDist = dist.get(u);
            if (curDist == null || d > curDist) continue;
            if (u.equals(target)) break;

            HashTable<T, Double> neighbors = adj.get(u);
            if (neighbors == null) continue;
            neighbors.forEach((v, w) -> {
                double alt = d + w;
                Double dv = dist.get(v);
                if (dv == null || alt < dv) {
                    dist.put(v, alt);
                    pred.put(v, u);
                    pq.offer(new QueueEntry<>(v, alt));
                }
            });
        }

        Double tdist = dist.get(target);
        if (tdist == null || tdist.isInfinite()) return null;

        Deque<T> dq = new Deque<>();
        T cur = target;
        while (cur != null) {
            dq.addFirst(cur);
            cur = pred.get(cur);
        }

        Object[] path = dq.toArray();
        if (path.length == 0 || !path[0].equals(source)) return null;
        return path;
    }

    private static final class QueueEntry<T> {
        final T node;
        final double distance;

        QueueEntry(T node, double distance) {
            this.node = node;
            this.distance = distance;
        }
    }
}
