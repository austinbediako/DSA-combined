package gh.edu.ug.dsaoptimizer.algorithms;

/**
 * Greedy resource assignment: processes service requests in order and
 * assigns each one to whichever *currently available* resource is
 * cheapest for it (e.g. distance/travel-time cost), then marks that
 * resource unavailable for later requests.
 *
 * <p>Complexity: O(requests * resources) -- a linear scan over
 * available resources for each request.
 *
 * <p><b>Counterexample (brief section 7/10):</b> this greedy strategy
 * is NOT guaranteed to find the minimum-cost overall assignment. A
 * request processed early can grab a resource that would have been
 * far more valuable to a later request, while leaving that later
 * request stuck with an expensive option. See
 * {@code GreedyAssignmentTest.greedyFailsToFindTheOptimalAssignment}
 * for a concrete worked example (cost 101 chosen by greedy vs. the
 * true optimum of 4) -- this is exactly the kind of case the brief
 * asks for: local, request-by-request optimality does not imply
 * global optimality. The dynamic-programming solution in
 * {@link Knapsack} demonstrates the alternative: consider the whole
 * problem at once rather than committing greedily one step at a time.
 */
public final class GreedyAssignment {

    private GreedyAssignment() {
        // utility class -- no instances
    }

    /**
     * @param cost cost[i][j] = cost of assigning request i to resource j.
     *             Every row must have the same length.
     */
    public static AssignmentResult assign(double[][] cost) {
        if (cost == null) {
            throw new IllegalArgumentException("cost must not be null");
        }
        int numRequests = cost.length;
        int numResources = numRequests == 0 ? 0 : cost[0].length;

        boolean[] used = new boolean[numResources];
        int[] assignment = new int[numRequests];
        double totalCost = 0.0;

        for (int i = 0; i < numRequests; i++) {
            int best = -1;
            double bestCost = Double.POSITIVE_INFINITY;
            for (int j = 0; j < numResources; j++) {
                if (!used[j] && cost[i][j] < bestCost) {
                    bestCost = cost[i][j];
                    best = j;
                }
            }
            if (best == -1) {
                throw new IllegalStateException(
                        "no available resource left for request " + i);
            }
            used[best] = true;
            assignment[i] = best;
            totalCost += bestCost;
        }

        return new AssignmentResult(assignment, totalCost);
    }

    public static final class AssignmentResult {
        /** assignment[i] = index of the resource assigned to request i. */
        public final int[] assignment;
        public final double totalCost;

        AssignmentResult(int[] assignment, double totalCost) {
            this.assignment = assignment;
            this.totalCost = totalCost;
        }
    }
}
