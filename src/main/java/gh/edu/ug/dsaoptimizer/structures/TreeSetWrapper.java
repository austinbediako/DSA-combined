package gh.edu.ug.dsaoptimizer.structures;

/**
 * TreeSet wrapper backed by RedBlackTreeMap (values unused).
 */
public class TreeSetWrapper<K> {
    private final RedBlackTreeMap<K, Object> tree;
    private static final Object PRESENT = new Object();
    public TreeSetWrapper() { tree = new RedBlackTreeMap<>(); }
    public TreeSetWrapper(java.util.Comparator<? super K> cmp) { tree = new RedBlackTreeMap<>(cmp); }
    public boolean add(K k) { if (k==null) throw new NullPointerException(); return tree.put(k,PRESENT)==null; }
    public boolean contains(K k) { return tree.containsKey(k); }
    public int size() { return tree.size(); }
}
