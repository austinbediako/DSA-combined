package gh.edu.ug.dsaoptimizer.algorithms;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GreedyAssignmentTest {

    @Test
    void assignsEachRequestToItsCheapestAvailableResource() {
        // 2 requests, 2 resources, no conflict -- greedy happens to be optimal here.
        double[][] cost = {
                {1, 5},
                {5, 1},
        };
        GreedyAssignment.AssignmentResult result = GreedyAssignment.assign(cost);
        assertArrayEquals(new int[]{0, 1}, result.assignment);
        assertEquals(2.0, result.totalCost);
    }

    /**
     * Counterexample (brief section 10): a case where greedy fails to
     * find the minimum-cost assignment.
     *
     * <p>Request 1 (R1): cost 1 to resource X, cost 2 to resource Y.
     * Request 2 (R2): cost 2 to resource X, cost 100 to resource Y.
     *
     * <p>Greedy processes R1 first and grabs X (its cheapest option,
     * cost 1) since X is still available. R2 is then forced onto Y at
     * cost 100. Greedy total = 1 + 100 = 101.
     *
     * <p>The true optimal assignment is R1-&gt;Y (cost 2), R2-&gt;X
     * (cost 2), total = 4 -- dramatically better, but greedy can never
     * find it because it never reconsiders R1's earlier choice once
     * made. This is exactly the "one bad early decision, locked in
     * forever" failure mode that motivates dynamic programming
     * (which considers all combinations) over greedy for this class
     * of problem.
     */
    @Test
    void greedyFailsToFindTheOptimalAssignment() {
        double[][] cost = {
                {1, 2},   // R1: cheap to X, slightly more to Y
                {2, 100}, // R2: cheap to X too, very expensive to Y
        };

        GreedyAssignment.AssignmentResult greedyResult = GreedyAssignment.assign(cost);

        assertArrayEquals(new int[]{0, 1}, greedyResult.assignment); // R1->X, R2->Y
        assertEquals(101.0, greedyResult.totalCost);

        double trueOptimal = bruteForceOptimalTwoByTwo(cost);
        assertEquals(4.0, trueOptimal);
        assertTrue(greedyResult.totalCost > trueOptimal,
                "greedy (" + greedyResult.totalCost + ") should be strictly worse than optimal (" + trueOptimal + ")");
    }

    /** Brute force over both possible assignments for a 2x2 cost matrix, for the counterexample above. */
    private static double bruteForceOptimalTwoByTwo(double[][] cost) {
        double straight = cost[0][0] + cost[1][1];
        double swapped = cost[0][1] + cost[1][0];
        return Math.min(straight, swapped);
    }

    @Test
    void emptyRequestsBoundary() {
        double[][] cost = {};
        GreedyAssignment.AssignmentResult result = GreedyAssignment.assign(cost);
        assertEquals(0, result.assignment.length);
        assertEquals(0.0, result.totalCost);
    }

    @Test
    void moreRequestsThanResourcesThrows() {
        double[][] cost = {
                {1},
                {2},
        };
        assertThrows(IllegalStateException.class, () -> GreedyAssignment.assign(cost));
    }

    @Test
    void nullInputThrows() {
        assertThrows(IllegalArgumentException.class, () -> GreedyAssignment.assign(null));
    }
}
