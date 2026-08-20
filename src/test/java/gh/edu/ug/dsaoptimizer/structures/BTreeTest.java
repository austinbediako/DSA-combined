package gh.edu.ug.dsaoptimizer.structures;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BTreeTest {

    @Test
    void insertAndContainsFindsInsertedKeys() {
        BTree<Integer> tree = new BTree<>();
        int[] values = {10, 20, 5, 6, 12, 30, 7, 17};
        for (int v : values) tree.insert(v);

        for (int v : values) assertTrue(tree.contains(v));
        assertFalse(tree.contains(99));
        assertEquals(values.length, tree.size());
    }

    @Test
    void inorderTraversalReturnsSortedOrder() {
        BTree<Integer> tree = new BTree<>();
        int[] values = {10, 20, 5, 6, 12, 30, 7, 17};
        for (int v : values) tree.insert(v);

        assertArrayEquals(new Integer[]{5, 6, 7, 10, 12, 17, 20, 30}, tree.inorder());
    }

    @Test
    void forcesNodeSplitsWithMinDegreeTwo() {
        // minDegree 2 -> max 3 keys per node before a split is required.
        // Inserting 1..10 in order forces repeated splits and root growth.
        BTree<Integer> tree = new BTree<>(2);
        for (int i = 1; i <= 10; i++) tree.insert(i);

        assertEquals(10, tree.size());
        Integer[] expected = new Integer[10];
        for (int i = 0; i < 10; i++) expected[i] = i + 1;
        assertArrayEquals(expected, tree.inorder());

        // With max 3 keys/node and 10 keys, the tree must have split at
        // least once beyond a single leaf root.
        assertTrue(tree.height() >= 1);
    }

    @Test
    void emptyTreeBoundary() {
        BTree<Integer> tree = new BTree<>();
        assertTrue(tree.isEmpty());
        assertEquals(0, tree.size());
        assertEquals(0, tree.height());
        assertFalse(tree.contains(1));
        assertEquals(0, tree.inorder().length);
    }

    @Test
    void singleKeyBoundary() {
        BTree<Integer> tree = new BTree<>();
        tree.insert(42);
        assertEquals(1, tree.size());
        assertTrue(tree.contains(42));
        assertEquals(0, tree.height());
    }

    @Test
    void duplicateInsertIsIgnoredNoSizeChange() {
        BTree<Integer> tree = new BTree<>();
        tree.insert(10);
        tree.insert(10);
        tree.insert(10);
        assertEquals(1, tree.size());
    }

    @Test
    void invalidMinDegreeThrows() {
        assertThrows(IllegalArgumentException.class, () -> new BTree<Integer>(1));
    }

    @Test
    void nullInputThrows() {
        BTree<Integer> tree = new BTree<>();
        assertThrows(NullPointerException.class, () -> tree.insert(null));
        assertThrows(NullPointerException.class, () -> tree.contains(null));
    }
}
