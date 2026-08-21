package gh.edu.ug.dsaoptimizer.algorithms;

import gh.edu.ug.dsaoptimizer.structures.DynamicArray;

/**
 * 0/1 knapsack via bottom-up tabulation: selects a subset of requests
 * (each with a "weight"/cost and a "value"/priority) that maximises
 * total value without exceeding a budget/capacity constraint.
 *
 * <p>Unlike {@link GreedyAssignment}'s locally-optimal, one-decision-
 * at-a-time strategy, this considers every combination implicitly by
 * building up {@code table[i][w]} = the best value achievable using
 * only the first {@code i} items within capacity {@code w} -- which is
 * exactly what guarantees the true optimum (this is the DP
 * counterpart to the greedy failure case documented there).
 *
 * <p>Complexity: O(n * capacity) time and space for the table, where
 * n = number of items. Reconstruction (finding which items were
 * actually chosen) is an additional O(n) backward pass over the table.
 */
public final class Knapsack {

    private Knapsack() {
        // utility class -- no instances
    }

    public static Result solve(int[] weights, int[] values, int capacity) {
        if (weights == null || values == null) {
            throw new IllegalArgumentException("weights and values must not be null");
        }
        if (weights.length != values.length) {
            throw new IllegalArgumentException("weights and values must have the same length");
        }
        if (capacity < 0) {
            throw new IllegalArgumentException("capacity must be non-negative");
        }

        int n = weights.length;
        int[][] table = new int[n + 1][capacity + 1];

        for (int i = 1; i <= n; i++) {
            int weight = weights[i - 1];
            int value = values[i - 1];
            for (int w = 0; w <= capacity; w++) {
                table[i][w] = table[i - 1][w]; // option: exclude item i-1
                if (weight <= w) {
                    int included = table[i - 1][w - weight] + value;
                    if (included > table[i][w]) {
                        table[i][w] = included; // option: include item i-1
                    }
                }
            }
        }

        int[] selected = reconstruct(table, weights, n, capacity);
        return new Result(table[n][capacity], selected, table);
    }

    /** Backtracks through the table to find which items were actually chosen. */
    private static int[] reconstruct(int[][] table, int[] weights, int n, int capacity) {
        DynamicArray<Integer> chosenReversed = new DynamicArray<>();
        int w = capacity;
        for (int i = n; i > 0; i--) {
            if (table[i][w] != table[i - 1][w]) {
                chosenReversed.add(i - 1);
                w -= weights[i - 1];
            }
        }
        int[] chosen = new int[chosenReversed.size()];
        for (int k = 0; k < chosen.length; k++) {
            chosen[k] = chosenReversed.get(chosenReversed.size() - 1 - k);
        }
        return chosen;
    }

    public static final class Result {
        public final int maxValue;
        /** Indices (into the original weights/values arrays) of the chosen items, ascending. */
        public final int[] selectedIndices;
        /** The full DP table, exposed for the DP trace table evidence. */
        public final int[][] table;

        Result(int maxValue, int[] selectedIndices, int[][] table) {
            this.maxValue = maxValue;
            this.selectedIndices = selectedIndices;
            this.table = table;
        }
    }
}
