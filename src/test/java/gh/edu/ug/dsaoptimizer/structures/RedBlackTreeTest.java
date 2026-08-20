package gh.edu.ug.dsaoptimizer.structures;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RedBlackTreeTest {

    @Test
    void insertAndContainsFindsInsertedValues() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        for (int v : new int[]{50, 30, 70, 20, 40, 60, 80}) tree.insert(v);

        assertTrue(tree.contains(50));
        assertTrue(tree.contains(20));
        assertFalse(tree.contains(99));
        assertEquals(7, tree.size());
    }

    @Test
    void inorderTraversalReturnsSortedOrder() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        int[] values = {50, 30, 70, 20, 40, 60, 80};
        for (int v : values) tree.insert(v);

        assertArrayEquals(new Integer[]{20, 30, 40, 50, 60, 70, 80}, tree.inorder());
    }

    @Test
    void remainsBalancedOnSortedAscendingInsertUnlikePlainBST() {
        // This exact input degenerates a plain BST to height n-1 (see
        // BSTTest.degenerateSortedInsertProducesLinearHeight). A red-black
        // tree must stay within the 2*log2(n+1) balanced-height bound --
        // this is the evidence for the BST-vs-balanced-tree comparison
        // in docs/PERFORMANCE_EXPERIMENTS.md.
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        int n = 100;
        for (int i = 1; i <= n; i++) tree.insert(i);

        int maxBalancedHeight = (int) (2 * (Math.log(n + 1) / Math.log(2)));
        assertTrue(tree.height() <= maxBalancedHeight,
                "height " + tree.height() + " exceeds balanced bound " + maxBalancedHeight);
        assertEquals(n, tree.size());
    }

    @Test
    void maintainsRedBlackInvariantsAfterManyInsertsInVariousOrders() {
        RedBlackTree<Integer> ascending = new RedBlackTree<>();
        for (int i = 1; i <= 50; i++) ascending.insert(i);
        assertTrue(ascending.isValidRedBlackTree());

        RedBlackTree<Integer> descending = new RedBlackTree<>();
        for (int i = 50; i >= 1; i--) descending.insert(i);
        assertTrue(descending.isValidRedBlackTree());

        RedBlackTree<Integer> shuffled = new RedBlackTree<>();
        int[] order = {27, 3, 41, 15, 9, 33, 47, 1, 22, 38, 5, 19, 44, 11, 29};
        for (int v : order) shuffled.insert(v);
        assertTrue(shuffled.isValidRedBlackTree());
    }

    @Test
    void emptyTreeBoundary() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        assertTrue(tree.isEmpty());
        assertEquals(0, tree.size());
        assertEquals(-1, tree.height());
        assertFalse(tree.contains(1));
        assertEquals(0, tree.inorder().length);
    }

    @Test
    void singleNodeBoundaryRootIsBlack() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.insert(10);
        assertEquals(1, tree.size());
        assertEquals(0, tree.height());
        assertTrue(tree.isValidRedBlackTree());
    }

    @Test
    void duplicateInsertOverwritesRatherThanDuplicatingNode() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.insert(10);
        tree.insert(10);
        tree.insert(10);
        assertEquals(1, tree.size());
    }

    @Test
    void nullInputThrows() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        assertThrows(NullPointerException.class, () -> tree.insert(null));
        assertThrows(NullPointerException.class, () -> tree.contains(null));
    }
}
