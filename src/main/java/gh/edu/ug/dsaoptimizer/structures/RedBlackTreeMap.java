package gh.edu.ug.dsaoptimizer.structures;

import java.util.Comparator;

/**
 * Basic Red-Black Tree map supporting put/get/containsKey. Removal not implemented.
 * Comparator supported; falls back to Comparable.
 */
public class RedBlackTreeMap<K, V> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private final Comparator<? super K> cmp;
    private Node root;
    private int size = 0;

    private class Node {
        K key; V val; Node left, right, parent; boolean color = RED;
        Node(K k, V v, Node p) { key = k; val = v; parent = p; }
    }

    public RedBlackTreeMap() { this.cmp = null; }
    public RedBlackTreeMap(Comparator<? super K> cmp) { this.cmp = cmp; }

    @SuppressWarnings("unchecked")
    private int compare(K a, K b) {
        if (cmp != null) return cmp.compare(a, b);
        return ((Comparable<? super K>) a).compareTo(b);
    }

    public V get(K key) {
        Node n = root;
        while (n != null) {
            int c = compare(key, n.key);
            if (c == 0) return n.val;
            n = c < 0 ? n.left : n.right;
        }
        return null;
    }

    public boolean containsKey(K key) { return get(key) != null; }

    public V put(K key, V value) {
        if (key == null || value == null) throw new NullPointerException("null");
        if (root == null) { root = new Node(key, value, null); root.color = BLACK; size++; return null; }
        Node p = null; Node n = root; int cmpres = 0;
        while (n != null) { p = n; cmpres = compare(key, n.key); if (cmpres == 0) { V old = n.val; n.val = value; return old; } n = cmpres < 0 ? n.left : n.right; }
        Node x = new Node(key, value, p);
        if (cmpres < 0) p.left = x; else p.right = x;
        fixAfterInsertion(x);
        size++;
        return null;
    }

    private void rotateLeft(Node p) {
        if (p == null) return;
        Node r = p.right; p.right = r.left; if (r.left != null) r.left.parent = p; r.parent = p.parent;
        if (p.parent == null) root = r; else if (p.parent.left == p) p.parent.left = r; else p.parent.right = r;
        r.left = p; p.parent = r;
    }

    private void rotateRight(Node p) {
        if (p == null) return;
        Node l = p.left; p.left = l.right; if (l.right != null) l.right.parent = p; l.parent = p.parent;
        if (p.parent == null) root = l; else if (p.parent.right == p) p.parent.right = l; else p.parent.left = l;
        l.right = p; p.parent = l;
    }

    private void fixAfterInsertion(Node x) {
        x.color = RED;
        while (x != null && x != root && x.parent.color == RED) {
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
                Node y = rightOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) { setColor(parentOf(x), BLACK); setColor(y, BLACK); setColor(parentOf(parentOf(x)), RED); x = parentOf(parentOf(x)); }
                else {
                    if (x == rightOf(parentOf(x))) { x = parentOf(x); rotateLeft(x); }
                    setColor(parentOf(x), BLACK); setColor(parentOf(parentOf(x)), RED); rotateRight(parentOf(parentOf(x)));
                }
            } else {
                Node y = leftOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) { setColor(parentOf(x), BLACK); setColor(y, BLACK); setColor(parentOf(parentOf(x)), RED); x = parentOf(parentOf(x)); }
                else {
                    if (x == leftOf(parentOf(x))) { x = parentOf(x); rotateRight(x); }
                    setColor(parentOf(x), BLACK); setColor(parentOf(parentOf(x)), RED); rotateLeft(parentOf(parentOf(x)));
                }
            }
        }
        root.color = BLACK;
    }

    private static <K,V> Node parentOf(Node n) { return n == null ? null : n.parent; }
    private static <K,V> Node leftOf(Node n) { return n == null ? null : n.left; }
    private static <K,V> Node rightOf(Node n) { return n == null ? null : n.right; }
    private static <K,V> boolean colorOf(Node n) { return n == null ? BLACK : n.color; }
    private static <K,V> void setColor(Node n, boolean c) { if (n != null) n.color = c; }

    public int size() { return size; }
}
