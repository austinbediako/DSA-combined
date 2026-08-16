package gh.edu.ug.dsaoptimizer.structures;

/**
 * Adjacency-matrix based directed weighted graph implementation.
 * Uses custom HashTable to map nodes to integer indices and a double[][] matrix for weights.
 */
public class GraphMatrix<T> {
    private static final double INF = Double.POSITIVE_INFINITY;

    private HashTable<T, Integer> indexMap = new HashTable<>();
    private Object[] nodes; // store nodes by index
    private double[][] matrix;
    private int capacity = 16;
    private int size = 0;

    public GraphMatrix() {
        nodes = new Object[capacity];
        matrix = new double[capacity][capacity];
        for (int i = 0; i < capacity; i++) {
            for (int j = 0; j < capacity; j++) matrix[i][j] = INF;
        }
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity <= capacity) return;
        int newCap = capacity * 2;
        while (newCap < minCapacity) newCap *= 2;
        Object[] newNodes = new Object[newCap];
        double[][] newMatrix = new double[newCap][newCap];
        for (int i = 0; i < newCap; i++) for (int j = 0; j < newCap; j++) newMatrix[i][j] = INF;
        for (int i = 0; i < size; i++) newNodes[i] = nodes[i];
        for (int i = 0; i < size; i++) for (int j = 0; j < size; j++) newMatrix[i][j] = matrix[i][j];
        nodes = newNodes;
        matrix = newMatrix;
        capacity = newCap;
    }

    public void addNode(T node) {
        if (node == null) throw new NullPointerException("node cannot be null");
        if (indexMap.containsKey(node)) return;
        ensureCapacity(size + 1);
        nodes[size] = node;
        indexMap.put(node, size);
        size++;
    }

    public boolean hasNode(T node) {
        if (node == null) throw new NullPointerException("node cannot be null");
        return indexMap.containsKey(node);
    }

    public void addEdge(T from, T to, double weight) {
        if (from == null || to == null) throw new NullPointerException("nodes cannot be null");
        if (weight < 0) throw new IllegalArgumentException("edge weight must be non-negative");
        addNode(from);
        addNode(to);
        int i = indexMap.get(from);
        int j = indexMap.get(to);
        matrix[i][j] = weight;
    }

    public boolean hasEdge(T from, T to) {
        if (from == null || to == null) throw new NullPointerException("nodes cannot be null");
        if (!indexMap.containsKey(from) || !indexMap.containsKey(to)) return false;
        int i = indexMap.get(from);
        int j = indexMap.get(to);
        return matrix[i][j] != INF;
    }

    public void removeEdge(T from, T to) {
        if (from == null || to == null) throw new NullPointerException("nodes cannot be null");
        if (!indexMap.containsKey(from) || !indexMap.containsKey(to)) return;
        int i = indexMap.get(from);
        int j = indexMap.get(to);
        matrix[i][j] = INF;
    }

    public void removeNode(T node) {
        if (node == null) throw new NullPointerException("node cannot be null");
        if (!indexMap.containsKey(node)) return;
        int idx = indexMap.get(node);
        // remove mapping
        indexMap.remove(node);
        nodes[idx] = null;
        // clear row and column
        for (int j = 0; j < size; j++) matrix[idx][j] = INF;
        for (int i = 0; i < size; i++) matrix[i][idx] = INF;
        // Note: we don't compact indices to keep implementation simple
    }

    public HashTable<T, Double> dijkstra(T source) {
        if (source == null) throw new NullPointerException("source cannot be null");
        if (!indexMap.containsKey(source)) throw new IllegalArgumentException("source node not found in graph");

        HashTable<T, Double> dist = new HashTable<>();
        HashTable<T, T> pred = new HashTable<>();

        // initialize
        indexMap.forEach((node, idx) -> dist.put(node, INF));
        dist.put(source, 0.0);

        PriorityQueueHeap<MatrixEntry<T>> pq = new PriorityQueueHeap<>((a, b) -> Double.compare(a.distance, b.distance));
        pq.offer(new MatrixEntry<>(source, 0.0));

        while (!pq.isEmpty()) {
            MatrixEntry<T> me = pq.poll();
            T u = me.node;
            double d = me.distance;
            Double ud = dist.get(u);
            if (ud == null || d > ud) continue;
            int ui = indexMap.get(u);
            // iterate all possible v
            indexMap.forEach((v, vi) -> {
                double w = matrix[ui][vi];
                if (w == INF) return;
                double alt = d + w;
                Double dv = dist.get(v);
                if (dv == null || alt < dv) {
                    dist.put(v, alt);
                    pred.put(v, u);
                    pq.offer(new MatrixEntry<>(v, alt));
                }
            });
        }

        return dist;
    }

    public java.util.List<T> shortestPath(T source, T target) {
        if (source == null || target == null) throw new NullPointerException("nodes cannot be null");
        if (!indexMap.containsKey(source) || !indexMap.containsKey(target)) throw new IllegalArgumentException("source/target not in graph");

        final double INF = Double.POSITIVE_INFINITY;
        HashTable<T, Double> dist = new HashTable<>();
        HashTable<T, T> pred = new HashTable<>();

        indexMap.forEach((node, idx) -> dist.put(node, INF));
        dist.put(source, 0.0);

        PriorityQueueHeap<MatrixEntry<T>> pq = new PriorityQueueHeap<>((a, b) -> Double.compare(a.distance, b.distance));
        pq.offer(new MatrixEntry<>(source, 0.0));

        while (!pq.isEmpty()) {
            MatrixEntry<T> me = pq.poll();
            T u = me.node;
            double d = me.distance;
            Double ud = dist.get(u);
            if (ud == null || d > ud) continue;
            if (u.equals(target)) break;
            int ui = indexMap.get(u);
            indexMap.forEach((v, vi) -> {
                double w = matrix[ui][vi];
                if (w == INF) return;
                double alt = d + w;
                Double dv = dist.get(v);
                if (dv == null || alt < dv) {
                    dist.put(v, alt);
                    pred.put(v, u);
                    pq.offer(new MatrixEntry<>(v, alt));
                }
            });
        }

        Double td = dist.get(target);
        if (td == null || td.isInfinite()) return null;

        Deque<T> dq = new Deque<>();
        T cur = target;
        while (cur != null) {
            dq.addFirst(cur);
            cur = pred.get(cur);
        }

        java.util.List<T> path = new java.util.ArrayList<>();
        while (!dq.isEmpty()) path.add(dq.removeFirst());
        if (path.isEmpty() || !path.get(0).equals(source)) return null;
        return path;
    }

    private static final class MatrixEntry<T> {
        final T node;
        final double distance;

        MatrixEntry(T node, double distance) {
            this.node = node;
            this.distance = distance;
        }
    }
}
