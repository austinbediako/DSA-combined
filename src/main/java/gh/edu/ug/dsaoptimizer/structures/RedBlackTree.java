package gh.edu.ug.dsaoptimizer.structures;

/**
 * Left-leaning red-black tree (Sedgewick's formulation) -- a
 * self-balancing BST guaranteeing O(log n) height by maintaining:
 * 1. No red node has a red child (no two consecutive red links).
 * 2. Every root-to-null-leaf path has the same number of black links.
 * 3. Red links lean left.
 *
 * Insert-only (deletion is not required by the brief). Balance is
 * restored on the way back up the recursion via left/right rotations
 * and colour flips -- see insert() for where each case is handled.
 */
public class RedBlackTree<T extends Comparable<T>> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static final class Node<T> {
        T value;
        Node<T> left;
        Node<T> right;
        boolean color;

        Node(T value, boolean color) {
            this.value = value;
            this.color = color;
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

    private boolean isRed(Node<T> node) {
        return node != null && node.color == RED;
    }

    public void insert(T value) {
        if (value == null) throw new NullPointerException("null values not allowed");
        root = insert(root, value);
        root.color = BLACK;
    }

    private Node<T> insert(Node<T> node, T value) {
        if (node == null) {
            size++;
            return new Node<>(value, RED);
        }

        int cmp = value.compareTo(node.value);
        if (cmp < 0) {
            node.left = insert(node.left, value);
        } else if (cmp > 0) {
            node.right = insert(node.right, value);
        } else {
            node.value = value; // overwrite, no duplicate node
        }

        // Restore left-leaning red-black invariants on the way up.
        if (isRed(node.right) && !isRed(node.left)) {
            node = rotateLeft(node);
        }
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }
        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
        }

        return node;
    }

    private Node<T> rotateLeft(Node<T> node) {
        Node<T> newRoot = node.right;
        node.right = newRoot.left;
        newRoot.left = node;
        newRoot.color = node.color;
        node.color = RED;
        return newRoot;
    }

    private Node<T> rotateRight(Node<T> node) {
        Node<T> newRoot = node.left;
        node.left = newRoot.right;
        newRoot.right = node;
        newRoot.color = node.color;
        node.color = RED;
        return newRoot;
    }

    private void flipColors(Node<T> node) {
        node.color = !node.color;
        node.left.color = !node.left.color;
        node.right.color = !node.right.color;
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

    public int height() {
        return height(root);
    }

    private int height(Node<T> node) {
        if (node == null) return -1;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    /**
     * Structural invariant check (used by tests, not part of normal
     * operation): true if no red node has a red child, and every
     * root-to-null-leaf path has the same black-link count.
     */
    boolean isValidRedBlackTree() {
        if (isRed(root)) return false; // root must be black
        return hasNoRedRedViolation(root) && blackHeight(root) != -1;
    }

    private boolean hasNoRedRedViolation(Node<T> node) {
        if (node == null) return true;
        if (isRed(node) && (isRed(node.left) || isRed(node.right))) return false;
        return hasNoRedRedViolation(node.left) && hasNoRedRedViolation(node.right);
    }

    /** Returns the black-height if consistent on both subtrees, else -1. */
    private int blackHeight(Node<T> node) {
        if (node == null) return 0;
        int left = blackHeight(node.left);
        int right = blackHeight(node.right);
        if (left == -1 || right == -1 || left != right) return -1;
        return left + (isRed(node) ? 0 : 1);
    }
}
