package gh.edu.ug.dsaoptimizer.structures;

/**
 * Disjoint-set (union-find) over a generic element type, backed by
 * this project's own HashTable rather than java.util.HashMap.
 *
 * Uses union by rank plus path compression, giving find/union an
 * amortised near-O(1) (inverse-Ackermann) time complexity. Used by
 * Kruskal's algorithm to detect cycles when building an MST.
 */
public class DisjointSet<T> {

    private final HashTable<T, T> parent = new HashTable<>();
    private final HashTable<T, Integer> rank = new HashTable<>();
    private int setCount;

    public void makeSet(T element) {
        if (element == null) throw new NullPointerException("null elements not allowed");
        if (parent.containsKey(element)) return; // already a member
        parent.put(element, element);
        rank.put(element, 0);
        setCount++;
    }

    public boolean contains(T element) {
        if (element == null) throw new NullPointerException("null elements not allowed");
        return parent.containsKey(element);
    }

    /** Finds the representative of element's set, with path compression. */
    public T find(T element) {
        if (element == null) throw new NullPointerException("null elements not allowed");
        if (!parent.containsKey(element)) {
            throw new IllegalArgumentException("element is not in any set: " + element);
        }
        T p = parent.get(element);
        if (!p.equals(element)) {
            T root = find(p);
            parent.put(element, root); // path compression
            return root;
        }
        return element;
    }

    /**
     * Unions the sets containing a and b, using union by rank.
     * Returns true if a and b were in different sets (i.e. a union
     * actually happened), false if they were already in the same set
     * -- Kruskal uses this return value to detect cycle-forming edges.
     */
    public boolean union(T a, T b) {
        T rootA = find(a);
        T rootB = find(b);
        if (rootA.equals(rootB)) return false;

        int rankA = rank.get(rootA);
        int rankB = rank.get(rootB);
        if (rankA < rankB) {
            parent.put(rootA, rootB);
        } else if (rankA > rankB) {
            parent.put(rootB, rootA);
        } else {
            parent.put(rootB, rootA);
            rank.put(rootA, rankA + 1);
        }
        setCount--;
        return true;
    }

    /** Number of distinct sets currently tracked. */
    public int setCount() {
        return setCount;
    }
}
