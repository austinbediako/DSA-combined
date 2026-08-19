package gh.edu.ug.dsaoptimizer.structures;

/**
 * Lightweight HashMap wrapper over project HashTable.
 */
public class HashMapWrapper<K, V> {
    private final HashTable<K, V> ht = new HashTable<>();

    public V put(K k, V v) { if (k==null||v==null) throw new NullPointerException(); return ht.put(k,v); }
    public V get(K k) { if (k==null) throw new NullPointerException(); return ht.get(k); }
    public V remove(K k) { if (k==null) throw new NullPointerException(); return ht.remove(k); }
    public boolean containsKey(K k) { if (k==null) throw new NullPointerException(); return ht.containsKey(k); }
    public int size() { return ht.size(); }
    public void clear() { ht.clear(); }
}
