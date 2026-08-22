package gh.edu.ug.dsaoptimizer.service;

import gh.edu.ug.dsaoptimizer.algorithms.GreedyAssignment;
import gh.edu.ug.dsaoptimizer.model.Location;
import gh.edu.ug.dsaoptimizer.model.Resource;
import gh.edu.ug.dsaoptimizer.model.Road;
import gh.edu.ug.dsaoptimizer.model.ServiceRequest;
import gh.edu.ug.dsaoptimizer.structures.DynamicArray;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ResourceAssignmentServiceTest {

    @Test
    void assignsRequestToTheNearestAvailableResource() {
        DynamicArray<Location> locations = new DynamicArray<>();
        locations.add(new Location(1, "Hostel", "Area", "Hostel", null, null));
        locations.add(new Location(2, "Shuttle Stop Near", "Area", "Shuttle Stop", null, null));
        locations.add(new Location(3, "Shuttle Stop Far", "Area", "Shuttle Stop", null, null));

        DynamicArray<Road> roads = new DynamicArray<>();
        roads.add(new Road(1, 1, 2, 100, 2, 1.0)); // hostel <-> near stop: short
        roads.add(new Road(2, 2, 1, 100, 2, 1.0));
        roads.add(new Road(3, 1, 3, 5000, 60, 1.0)); // hostel <-> far stop: long
        roads.add(new Road(4, 3, 1, 5000, 60, 1.0));

        RouteService routeService = new RouteService(locations, roads);
        ResourceAssignmentService assignmentService = new ResourceAssignmentService(routeService);

        DynamicArray<ServiceRequest> requests = new DynamicArray<>();
        requests.add(new ServiceRequest("REQ-001", 1, 1, "shuttle", ServiceRequest.Urgency.MEDIUM,
                Instant.parse("2026-08-01T07:00:00Z"), null, ServiceRequest.Status.PENDING));

        DynamicArray<Resource> resources = new DynamicArray<>();
        resources.add(new Resource("RES-FAR", "Shuttle-Bus", 3, 45, Resource.AvailabilityStatus.AVAILABLE));
        resources.add(new Resource("RES-NEAR", "Shuttle-Bus", 2, 45, Resource.AvailabilityStatus.AVAILABLE));

        GreedyAssignment.AssignmentResult result = assignmentService.assign(requests, resources);

        assertEquals(1, result.assignment[0]); // index 1 = RES-NEAR, the closer resource
        assertEquals(100.0, result.totalCost);
    }
}
