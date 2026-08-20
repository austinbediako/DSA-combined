package gh.edu.ug.dsaoptimizer.structures;

/**
 * Unbalanced binary search tree over a natural-ordering key type.
 * - Disallows null keys.
 * - Duplicate inserts overwrite (no duplicate nodes), consistent with
 *   HashTable.put's semantics elsewhere in this project.
 *
 * Complexity (n = number of nodes, h = tree height): insert/search
 * O(h), worst case O(n) for a degenerate (already-sorted-input) tree
 * -- this worst case is exactly why RedBlackTree exists as the
 * balanced alternative for the BST-vs-balanced-tree experiment in
 * docs/PERFORMANCE_EXPERIMENTS.md.
 */
public class BST<T extends Comparable<T>> {

    private static final class Node<T> {
        T value;
        Node<T> left;
        Node<T> right;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<T> root;
    private int size;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void insert(T value) {
        if (value == null) throw new NullPointerException("null values not allowed");
        root = insert(root, value);
    }

    private Node<T> insert(Node<T> node, T value) {
        if (node == null) {
            size++;
            return new Node<>(value);
        }
        int cmp = value.compareTo(node.value);
        if (cmp < 0) {
            node.left = insert(node.left, value);
        } else if (cmp > 0) {
            node.right = insert(node.right, value);
        } else {
            node.value = value; // overwrite, no duplicate node
        }
        return node;
    }

    public boolean contains(T value) {
        if (value == null) throw new NullPointerException("null values not allowed");
        Node<T> cur = root;
        while (cur != null) {
            int cmp = value.compareTo(cur.value);
            if (cmp == 0) return true;
            cur = cmp < 0 ? cur.left : cur.right;
        }
        return false;
    }

    /** In-order traversal: returns all values in ascending order. */
    public Object[] inorder() {
        Object[] result = new Object[size];
        inorder(root, result, new int[]{0});
        return result;
    }

    private void inorder(Node<T> node, Object[] result, int[] idx) {
        if (node == null) return;
        inorder(node.left, result, idx);
        result[idx[0]++] = node.value;
        inorder(node.right, result, idx);
    }

    /** Height of the tree; an empty tree has height -1, a single node has height 0. */
    public int height() {
        return height(root);
    }

    private int height(Node<T> node) {
        if (node == null) return -1;
        return 1 + Math.max(height(node.left), height(node.right));
    }
}
