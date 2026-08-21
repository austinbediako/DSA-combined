package gh.edu.ug.dsaoptimizer.algorithms;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KnapsackTest {

    @Test
    void classicExampleFindsOptimalValueAndReconstructsSelection() {
        // Standard textbook example: weights {1,3,4,5}, values {1,4,5,7}, capacity 7.
        // Optimal: items at indices 1 and 2 (weight 3+4=7, value 4+5=9).
        int[] weights = {1, 3, 4, 5};
        int[] values = {1, 4, 5, 7};
        Knapsack.Result result = Knapsack.solve(weights, values, 7);

        assertEquals(9, result.maxValue);
        assertArrayEquals(new int[]{1, 2}, result.selectedIndices);

        // Reconstruction must actually respect the capacity and match the claimed value.
        int totalWeight = 0;
        int totalValue = 0;
        for (int idx : result.selectedIndices) {
            totalWeight += weights[idx];
            totalValue += values[idx];
        }
        assertTrue(totalWeight <= 7);
        assertEquals(result.maxValue, totalValue);
    }

    @Test
    void zeroCapacityBoundarySelectsNothing() {
        int[] weights = {2, 3};
        int[] values = {10, 20};
        Knapsack.Result result = Knapsack.solve(weights, values, 0);
        assertEquals(0, result.maxValue);
        assertEquals(0, result.selectedIndices.length);
    }

    @Test
    void emptyItemsBoundary() {
        Knapsack.Result result = Knapsack.solve(new int[]{}, new int[]{}, 10);
        assertEquals(0, result.maxValue);
        assertEquals(0, result.selectedIndices.length);
    }

    @Test
    void singleItemThatFitsIsSelected() {
        Knapsack.Result result = Knapsack.solve(new int[]{5}, new int[]{100}, 10);
        assertEquals(100, result.maxValue);
        assertArrayEquals(new int[]{0}, result.selectedIndices);
    }

    @Test
    void singleItemThatDoesNotFitIsExcluded() {
        Knapsack.Result result = Knapsack.solve(new int[]{20}, new int[]{100}, 10);
        assertEquals(0, result.maxValue);
        assertEquals(0, result.selectedIndices.length);
    }

    @Test
    void mismatchedArrayLengthsThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> Knapsack.solve(new int[]{1, 2}, new int[]{1}, 5));
    }

    @Test
    void negativeCapacityThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> Knapsack.solve(new int[]{1}, new int[]{1}, -1));
    }

    @Test
    void nullInputThrows() {
        assertThrows(IllegalArgumentException.class, () -> Knapsack.solve(null, new int[]{1}, 5));
        assertThrows(IllegalArgumentException.class, () -> Knapsack.solve(new int[]{1}, null, 5));
    }
}
