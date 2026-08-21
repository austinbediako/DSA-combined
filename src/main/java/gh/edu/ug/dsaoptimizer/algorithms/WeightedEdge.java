package gh.edu.ug.dsaoptimizer.algorithms;

/** A simple (from, to, weight) tuple used by Prim/Kruskal MST results. */
public final class WeightedEdge<T> {

    public final T from;
    public final T to;
    public final double weight;

    public WeightedEdge(T from, T to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return from + " -- " + to + " (" + weight + ")";
    }
}
