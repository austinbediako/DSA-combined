package gh.edu.ug.dsaoptimizer.structures;

import java.util.Comparator;

/**
 * Simple BST-backed map (no balancing). Supports comparator or Comparable keys.
 */
public class BSTMap<K, V> {
    private final Comparator<? super K> cmp;
    private Node root;
    private int size = 0;

    private class Node {
        K key;
        V val;
        Node left, right;
        Node(K k, V v) { key = k; val = v; }
    }

    public BSTMap() { this.cmp = null; }
    public BSTMap(Comparator<? super K> cmp) { this.cmp = cmp; }

    @SuppressWarnings("unchecked")
    private int compare(K a, K b) {
        if (cmp != null) return cmp.compare(a, b);
        return ((Comparable<? super K>) a).compareTo(b);
    }

    public V get(K key) {
        if (key == null) throw new NullPointerException("key");
        Node n = root;
        while (n != null) {
            int c = compare(key, n.key);
            if (c == 0) return n.val;
            if (c < 0) n = n.left; else n = n.right;
        }
        return null;
    }

    public boolean containsKey(K key) { return get(key) != null; }

    public V put(K key, V value) {
        if (key == null || value == null) throw new NullPointerException("null key/value");
        if (root == null) { root = new Node(key, value); size++; return null; }
        Node n = root;
        while (true) {
            int c = compare(key, n.key);
            if (c == 0) { V old = n.val; n.val = value; return old; }
            if (c < 0) {
                if (n.left == null) { n.left = new Node(key, value); size++; return null; }
                n = n.left;
            } else {
                if (n.right == null) { n.right = new Node(key, value); size++; return null; }
                n = n.right;
            }
        }
    }

    public int size() { return size; }

    /** Tree height; an empty tree has height -1, a single node has height 0. */
    public int height() {
        return height(root);
    }

    private int height(Node n) {
        if (n == null) return -1;
        return 1 + Math.max(height(n.left), height(n.right));
    }

    /**
     * Returns all keys in ascending order.
     *
     * <p>Returns {@code Object[]} rather than {@code K[]}: Java cannot
     * safely create a generic array at runtime without a {@code Class<K>}
     * token, so casting an {@code Object[]} to {@code K[]} here would
     * compile but throw {@code ClassCastException} at the caller's
     * first typed array assignment.
     */
    public Object[] keysInOrder() {
        Object[] result = new Object[size];
        inorder(root, result, new int[]{0});
        return result;
    }

    private void inorder(Node n, Object[] result, int[] idx) {
        if (n == null) return;
        inorder(n.left, result, idx);
        result[idx[0]++] = n.key;
        inorder(n.right, result, idx);
    }
}
