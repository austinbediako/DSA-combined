package gh.edu.ug.dsaoptimizer.structures;

/**
 * Custom Set built on top of this project's own HashTable (brief
 * section 6: "Set, map | custom set/map on top of hash table or BST").
 * HashTable itself already satisfies the "map" half of that
 * requirement (put/get/remove/containsKey) -- Graph and DisjointSet
 * both depend on it directly for exactly that reason.
 */
public class Set<T> {

    private static final Object PRESENT = new Object();

    private final HashTable<T, Object> table = new HashTable<>();

    public boolean add(T element) {
        if (element == null) throw new NullPointerException("null elements not allowed");
        boolean isNew = !table.containsKey(element);
        table.put(element, PRESENT);
        return isNew;
    }

    public boolean contains(T element) {
        if (element == null) throw new NullPointerException("null elements not allowed");
        return table.containsKey(element);
    }

    public boolean remove(T element) {
        if (element == null) throw new NullPointerException("null elements not allowed");
        boolean existed = table.containsKey(element);
        table.remove(element);
        return existed;
    }

    public int size() {
        return table.size();
    }

    public boolean isEmpty() {
        return table.isEmpty();
    }

    /** Returns all members, in no particular order. */
    public Object[] toArray() {
        Object[] result = new Object[table.size()];
        int[] idx = {0};
        table.forEach((key, value) -> result[idx[0]++] = key);
        return result;
    }
}
