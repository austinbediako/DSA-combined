package gh.edu.ug.dsaoptimizer.service;

import gh.edu.ug.dsaoptimizer.algorithms.GreedyAssignment;
import gh.edu.ug.dsaoptimizer.model.Resource;
import gh.edu.ug.dsaoptimizer.model.ServiceRequest;
import gh.edu.ug.dsaoptimizer.structures.DynamicArray;

/**
 * Assigns resources to pending service requests using
 * {@link GreedyAssignment}, costed by real road-network distance from
 * each resource's home location to each request's source location
 * (via {@link RouteService}). See {@code GreedyAssignmentTest} for
 * why this strategy is not guaranteed globally optimal.
 */
public class ResourceAssignmentService {

    private final RouteService routeService;

    public ResourceAssignmentService(RouteService routeService) {
        this.routeService = routeService;
    }

    /**
     * @return assignment.assignment[i] is the index into {@code resources}
     *         assigned to {@code requests[i]}.
     */
    public GreedyAssignment.AssignmentResult assign(DynamicArray<ServiceRequest> requests,
                                                      DynamicArray<Resource> resources) {
        int requestCount = requests.size();
        int resourceCount = resources.size();
        double[][] cost = new double[requestCount][resourceCount];

        for (int i = 0; i < requestCount; i++) {
            ServiceRequest request = requests.get(i);
            for (int j = 0; j < resourceCount; j++) {
                Resource resource = resources.get(j);
                cost[i][j] = routeService.shortestDistance(
                        resource.getHomeLocationId(), request.getSourceLocationId());
            }
        }

        return GreedyAssignment.assign(cost);
    }
}
