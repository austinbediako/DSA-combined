package gh.edu.ug.dsaoptimizer.structures;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BSTTest {

    @Test
    void insertAndContainsFindsInsertedValues() {
        BST<Integer> bst = new BST<>();
        bst.insert(50);
        bst.insert(30);
        bst.insert(70);
        bst.insert(20);
        bst.insert(40);

        assertTrue(bst.contains(50));
        assertTrue(bst.contains(20));
        assertTrue(bst.contains(40));
        assertFalse(bst.contains(99));
        assertEquals(5, bst.size());
    }

    @Test
    void inorderTraversalReturnsSortedOrder() {
        BST<Integer> bst = new BST<>();
        int[] values = {50, 30, 70, 20, 40, 60, 80};
        for (int v : values) bst.insert(v);

        Object[] sorted = bst.inorder();
        assertArrayEquals(new Integer[]{20, 30, 40, 50, 60, 70, 80}, sorted);
    }

    @Test
    void emptyTreeBoundary() {
        BST<Integer> bst = new BST<>();
        assertTrue(bst.isEmpty());
        assertEquals(0, bst.size());
        assertEquals(-1, bst.height());
        assertFalse(bst.contains(1));
        assertEquals(0, bst.inorder().length);
    }

    @Test
    void singleNodeBoundary() {
        BST<Integer> bst = new BST<>();
        bst.insert(10);
        assertEquals(1, bst.size());
        assertEquals(0, bst.height());
        assertTrue(bst.contains(10));
    }

    @Test
    void duplicateInsertOverwritesRatherThanDuplicatingNode() {
        BST<Integer> bst = new BST<>();
        bst.insert(10);
        bst.insert(10);
        bst.insert(10);
        assertEquals(1, bst.size());
    }

    @Test
    void degenerateSortedInsertProducesLinearHeight() {
        BST<Integer> bst = new BST<>();
        for (int i = 1; i <= 5; i++) bst.insert(i);
        // sorted-ascending insert order degenerates to a right-leaning chain
        assertEquals(4, bst.height());
    }

    @Test
    void nullInputThrows() {
        BST<Integer> bst = new BST<>();
        assertThrows(NullPointerException.class, () -> bst.insert(null));
        assertThrows(NullPointerException.class, () -> bst.contains(null));
    }
}
