package gh.edu.ug.dsaoptimizer.algorithms;

/** Result of a minimum spanning tree computation: its edges and total cost. */
public final class MSTResult<T> {

    public final WeightedEdge<T>[] edges;
    public final double totalCost;

    public MSTResult(WeightedEdge<T>[] edges, double totalCost) {
        this.edges = edges;
        this.totalCost = totalCost;
    }
}
