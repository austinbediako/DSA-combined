package gh.edu.ug.dsaoptimizer.structures;

/**
 * Lightweight HashSet wrapper using HashTable.
 */
public class HashSetWrapper<K> {
    private static final Object PRESENT = new Object();
    private final HashTable<K, Object> ht = new HashTable<>();

    public boolean add(K k) { if (k==null) throw new NullPointerException(); return ht.put(k, PRESENT) == null; }
    public boolean contains(K k) { if (k==null) throw new NullPointerException(); return ht.containsKey(k); }
    public boolean remove(K k) { if (k==null) throw new NullPointerException(); return ht.remove(k) != null; }
    public int size() { return ht.size(); }
    public void clear() { ht.clear(); }
}
