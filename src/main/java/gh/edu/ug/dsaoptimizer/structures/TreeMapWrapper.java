package gh.edu.ug.dsaoptimizer.structures;

/**
 * TreeMap wrapper backed by RedBlackTreeMap (no remove).
 */
public class TreeMapWrapper<K, V> {
    private final RedBlackTreeMap<K, V> tree;
    public TreeMapWrapper() { tree = new RedBlackTreeMap<>(); }
    public TreeMapWrapper(java.util.Comparator<? super K> cmp) { tree = new RedBlackTreeMap<>(cmp); }
    public V put(K k, V v) { return tree.put(k,v); }
    public V get(K k) { return tree.get(k); }
    public boolean containsKey(K k) { return tree.containsKey(k); }
    public int size() { return tree.size(); }
}
