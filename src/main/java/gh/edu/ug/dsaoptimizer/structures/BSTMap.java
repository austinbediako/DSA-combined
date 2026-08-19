package gh.edu.ug.dsaoptimizer.structures;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;

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

    public List<K> keysInOrder() {
        ArrayList<K> out = new ArrayList<>();
        inorder(root, out);
        return out;
    }

    private void inorder(Node n, List<K> out) {
        if (n == null) return;
        inorder(n.left, out);
        out.add(n.key);
        inorder(n.right, out);
    }
}
