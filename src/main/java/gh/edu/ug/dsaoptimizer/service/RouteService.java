package gh.edu.ug.dsaoptimizer.service;

import gh.edu.ug.dsaoptimizer.algorithms.BFS;
import gh.edu.ug.dsaoptimizer.model.Location;
import gh.edu.ug.dsaoptimizer.model.Road;
import gh.edu.ug.dsaoptimizer.structures.DynamicArray;
import gh.edu.ug.dsaoptimizer.structures.Graph;

/**
 * Builds a {@link Graph} of location IDs from Location/Road model data
 * loaded via the persistence layer, and answers the route/reachability
 * questions the brief asks for (section 3.2, 3.3): fastest route
 * between two locations, and which locations are reachable from a
 * given dispatch point.
 */
public class RouteService {

    private final Graph<Integer> graph = new Graph<>();

    public RouteService(DynamicArray<Location> locations, DynamicArray<Road> roads) {
        for (int i = 0; i < locations.size(); i++) {
            graph.addNode(locations.get(i).getLocationId());
        }
        for (int i = 0; i < roads.size(); i++) {
            Road road = roads.get(i);
            // Each row in roads.csv represents one physical, bidirectional
            // walkway (its distance/travel_time/condition weight describe
            // the path itself, not a one-way direction), but the seed data
            // only stores it once per pair. Add both directions here --
            // otherwise the graph is silently directed, which is wrong for
            // BFS reachability and breaks Prim's MST (it can only expand
            // via outgoing edges from nodes already in the tree, so a
            // missing reverse edge forces costlier detours even though
            // Kruskal, which just harvests every node's outgoing edges
            // into one flat list, happens to still find the correct
            // answer regardless of direction).
            graph.addEdge(road.getFromLocationId(), road.getToLocationId(), road.getDistance());
            graph.addEdge(road.getToLocationId(), road.getFromLocationId(), road.getDistance());
        }
    }

    public Graph<Integer> getGraph() {
        return graph;
    }

    /** Shortest path (by road distance) as an ordered array of location IDs, or null if unreachable. */
    public Object[] shortestPath(int fromLocationId, int toLocationId) {
        return graph.shortestPath(fromLocationId, toLocationId);
    }

    /** Shortest distance, or {@code Double.POSITIVE_INFINITY} if toLocationId is unreachable. */
    public double shortestDistance(int fromLocationId, int toLocationId) {
        Double distance = graph.dijkstra(fromLocationId).get(toLocationId);
        return distance == null ? Double.POSITIVE_INFINITY : distance;
    }

    /** All locations reachable from the given dispatch point (including itself). */
    public Object[] reachableFrom(int locationId) {
        return BFS.traverse(graph, locationId);
    }
}
