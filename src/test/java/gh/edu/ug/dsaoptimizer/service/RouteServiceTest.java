package gh.edu.ug.dsaoptimizer.service;

import gh.edu.ug.dsaoptimizer.model.Location;
import gh.edu.ug.dsaoptimizer.model.Road;
import gh.edu.ug.dsaoptimizer.structures.DynamicArray;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RouteServiceTest {

    /** 4 locations (1..4), roads added both directions to simulate walkways. */
    private static RouteService buildFourLocationNetwork() {
        DynamicArray<Location> locations = new DynamicArray<>();
        locations.add(new Location(1, "Commonwealth Hall", "Legon Hill", "Hostel", 5.65, -0.19));
        locations.add(new Location(2, "Balme Library", "Central Campus", "Library", 5.65, -0.19));
        locations.add(new Location(3, "JQB", "Central Campus", "Lecture Hall", 5.65, -0.19));
        locations.add(new Location(4, "Main Gate Shuttle Stop", "South Campus", "Shuttle Stop", 5.63, -0.19));

        DynamicArray<Road> roads = new DynamicArray<>();
        int roadId = 1;
        roads.add(new Road(roadId++, 1, 2, 100, 2, 1.0));
        roads.add(new Road(roadId++, 2, 1, 100, 2, 1.0));
        roads.add(new Road(roadId++, 2, 3, 150, 3, 1.0));
        roads.add(new Road(roadId++, 3, 2, 150, 3, 1.0));
        roads.add(new Road(roadId++, 3, 4, 400, 6, 1.0));
        roads.add(new Road(roadId, 4, 3, 400, 6, 1.0));

        return new RouteService(locations, roads);
    }

    @Test
    void shortestPathFollowsTheOnlyAvailableRoute() {
        RouteService service = buildFourLocationNetwork();
        Object[] path = service.shortestPath(1, 4);
        assertArrayEquals(new Integer[]{1, 2, 3, 4}, path);
        assertEquals(650.0, service.shortestDistance(1, 4));
    }

    @Test
    void reachableFromReturnsAllConnectedLocations() {
        RouteService service = buildFourLocationNetwork();
        Object[] reachable = service.reachableFrom(1);
        assertEquals(4, reachable.length);
    }

    @Test
    void unreachableLocationHasInfiniteDistance() {
        DynamicArray<Location> locations = new DynamicArray<>();
        locations.add(new Location(1, "A", "Area", "Type", null, null));
        locations.add(new Location(2, "B", "Area", "Type", null, null));
        // no roads at all -- 2 is isolated from 1
        RouteService service = new RouteService(locations, new DynamicArray<>());

        assertEquals(Double.POSITIVE_INFINITY, service.shortestDistance(1, 2));
        assertNull(service.shortestPath(1, 2));
        assertEquals(1, service.reachableFrom(1).length); // only itself
    }
}
