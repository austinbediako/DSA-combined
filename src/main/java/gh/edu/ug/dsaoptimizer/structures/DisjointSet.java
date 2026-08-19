package gh.edu.ug.dsaoptimizer.structures;

/**
 * Disjoint set (Union-Find) with path compression and union by rank.
 */
public class DisjointSet<T> {
    private final HashTable<T, T> parent = new HashTable<>();
    private final HashTable<T, Integer> rank = new HashTable<>();

    public void makeSet(T x) {
        if (x == null) throw new NullPointerException("null element");
        if (!parent.containsKey(x)) {
            parent.put(x, x);
            rank.put(x, 0);
        }
    }

    public T find(T x) {
        if (x == null) throw new NullPointerException("null element");
        T p = parent.get(x);
        if (p == null) throw new IllegalArgumentException("element not found: " + x);
        if (!p.equals(x)) {
            T root = find(p);
            parent.put(x, root);
            return root;
        }
        return p;
    }

    public void union(T a, T b) {
        if (a == null || b == null) throw new NullPointerException("null element");
        T ra = find(a);
        T rb = find(b);
        if (ra.equals(rb)) return;
        int rka = rank.get(ra);
        int rkb = rank.get(rb);
        if (rka < rkb) parent.put(ra, rb);
        else if (rka > rkb) parent.put(rb, ra);
        else {
            parent.put(rb, ra);
            rank.put(ra, rka + 1);
        }
    }

    public boolean connected(T a, T b) {
        if (a == null || b == null) throw new NullPointerException("null element");
        return find(a).equals(find(b));
    }
}
