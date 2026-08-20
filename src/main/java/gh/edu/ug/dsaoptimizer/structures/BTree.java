package gh.edu.ug.dsaoptimizer.structures;

/**
 * B-tree of minimum degree t: every node (except the root) has
 * between t-1 and 2t-1 keys, and between t and 2t children. Insertion
 * uses the classic top-down approach -- any full node is split
 * *before* descending into it, so a single pass from root to leaf is
 * always enough (no separate bottom-up fix-up pass needed).
 *
 * Models a database index page structure: each node here is
 * comparable to one disk/index page holding up to 2t-1 keys.
 */
public class BTree<T extends Comparable<T>> {

    private static final int DEFAULT_MIN_DEGREE = 3; // t=3: up to 5 keys/node, 6 children

    @SuppressWarnings("unchecked")
    private static final class Node<T> {
        final Object[] keys;
        final Node<T>[] children;
        int keyCount;
        boolean leaf;

        Node(int minDegree, boolean leaf) {
            this.keys = new Object[2 * minDegree - 1];
            this.children = new Node[2 * minDegree];
            this.keyCount = 0;
            this.leaf = leaf;
        }

        @SuppressWarnings("unchecked")
        T key(int i) {
            return (T) keys[i];
        }
    }

    private final int minDegree;
    private Node<T> root;
    private int size;

    public BTree() {
        this(DEFAULT_MIN_DEGREE);
    }

    public BTree(int minDegree) {
        if (minDegree < 2) throw new IllegalArgumentException("minDegree must be >= 2");
        this.minDegree = minDegree;
        this.root = new Node<>(minDegree, true);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void insert(T key) {
        if (key == null) throw new NullPointerException("null keys not allowed");
        if (contains(key)) return; // no duplicates

        Node<T> r = root;
        if (r.keyCount == 2 * minDegree - 1) {
            Node<T> newRoot = new Node<>(minDegree, false);
            newRoot.children[0] = r;
            splitChild(newRoot, 0);
            root = newRoot;
            insertNonFull(newRoot, key);
        } else {
            insertNonFull(r, key);
        }
        size++;
    }

    /** Splits the full child at parent.children[index] around its median key. */
    private void splitChild(Node<T> parent, int index) {
        Node<T> fullChild = parent.children[index];
        int t = minDegree;
        Node<T> newRight = new Node<>(minDegree, fullChild.leaf);

        // Right half of fullChild's keys (indices t..2t-2) move to newRight.
        newRight.keyCount = t - 1;
        for (int j = 0; j < t - 1; j++) {
            newRight.keys[j] = fullChild.keys[j + t];
        }
        if (!fullChild.leaf) {
            for (int j = 0; j < t; j++) {
                newRight.children[j] = fullChild.children[j + t];
            }
        }
        fullChild.keyCount = t - 1;

        // Shift parent's children right to make room for newRight, then insert it.
        for (int j = parent.keyCount; j >= index + 1; j--) {
            parent.children[j + 1] = parent.children[j];
        }
        parent.children[index + 1] = newRight;

        // Shift parent's keys right, then pull up fullChild's median key.
        for (int j = parent.keyCount - 1; j >= index; j--) {
            parent.keys[j + 1] = parent.keys[j];
        }
        parent.keys[index] = fullChild.keys[t - 1];
        parent.keyCount++;
    }

    private void insertNonFull(Node<T> node, T key) {
        int i = node.keyCount - 1;
        if (node.leaf) {
            while (i >= 0 && key.compareTo(node.key(i)) < 0) {
                node.keys[i + 1] = node.keys[i];
                i--;
            }
            node.keys[i + 1] = key;
            node.keyCount++;
        } else {
            while (i >= 0 && key.compareTo(node.key(i)) < 0) {
                i--;
            }
            i++;
            if (node.children[i].keyCount == 2 * minDegree - 1) {
                splitChild(node, i);
                if (key.compareTo(node.key(i)) > 0) {
                    i++;
                }
            }
            insertNonFull(node.children[i], key);
        }
    }

    public boolean contains(T key) {
        if (key == null) throw new NullPointerException("null keys not allowed");
        return search(root, key);
    }

    private boolean search(Node<T> node, T key) {
        int i = 0;
        while (i < node.keyCount && key.compareTo(node.key(i)) > 0) {
            i++;
        }
        if (i < node.keyCount && key.compareTo(node.key(i)) == 0) {
            return true;
        }
        if (node.leaf) {
            return false;
        }
        return search(node.children[i], key);
    }

    /** In-order traversal: returns all keys in ascending order. */
    public Object[] inorder() {
        Object[] result = new Object[size];
        inorder(root, result, new int[]{0});
        return result;
    }

    private void inorder(Node<T> node, Object[] result, int[] idx) {
        int i;
        for (i = 0; i < node.keyCount; i++) {
            if (!node.leaf) {
                inorder(node.children[i], result, idx);
            }
            result[idx[0]++] = node.keys[i];
        }
        if (!node.leaf) {
            inorder(node.children[i], result, idx);
        }
    }

    /** Height in levels; a single-node (leaf root) tree has height 0. */
    public int height() {
        int h = 0;
        Node<T> cur = root;
        while (!cur.leaf) {
            h++;
            cur = cur.children[0];
        }
        return h;
    }
}
