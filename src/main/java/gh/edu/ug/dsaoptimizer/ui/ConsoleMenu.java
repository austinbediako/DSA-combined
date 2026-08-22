package gh.edu.ug.dsaoptimizer.ui;

import gh.edu.ug.dsaoptimizer.algorithms.InsertionSort;
import gh.edu.ug.dsaoptimizer.algorithms.Kruskal;
import gh.edu.ug.dsaoptimizer.algorithms.LinearSearch;
import gh.edu.ug.dsaoptimizer.algorithms.BinarySearch;
import gh.edu.ug.dsaoptimizer.algorithms.MSTResult;
import gh.edu.ug.dsaoptimizer.algorithms.MergeSort;
import gh.edu.ug.dsaoptimizer.algorithms.Prim;
import gh.edu.ug.dsaoptimizer.algorithms.QuickSort;
import gh.edu.ug.dsaoptimizer.algorithms.SelectionSort;
import gh.edu.ug.dsaoptimizer.algorithms.WeightedEdge;
import gh.edu.ug.dsaoptimizer.model.AlgorithmRun;
import gh.edu.ug.dsaoptimizer.model.AuditEvent;
import gh.edu.ug.dsaoptimizer.model.Location;
import gh.edu.ug.dsaoptimizer.model.Resource;
import gh.edu.ug.dsaoptimizer.model.Road;
import gh.edu.ug.dsaoptimizer.model.ServiceRequest;
import gh.edu.ug.dsaoptimizer.persistence.AlgorithmRunRepository;
import gh.edu.ug.dsaoptimizer.persistence.AuditEventRepository;
import gh.edu.ug.dsaoptimizer.persistence.LocationRepository;
import gh.edu.ug.dsaoptimizer.persistence.ResourceRepository;
import gh.edu.ug.dsaoptimizer.persistence.RoadRepository;
import gh.edu.ug.dsaoptimizer.persistence.ServiceRequestRepository;
import gh.edu.ug.dsaoptimizer.service.ResourceAssignmentService;
import gh.edu.ug.dsaoptimizer.service.RouteService;
import gh.edu.ug.dsaoptimizer.service.SchedulingEngine;
import gh.edu.ug.dsaoptimizer.structures.DynamicArray;

import java.sql.SQLException;
import java.time.Instant;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;

/**
 * Console menu -- lets an examiner run every required demonstration
 * (brief section 8.4) without touching source code: load the
 * database, search records, run search/sort algorithms, compute
 * routes and MSTs, dispatch requests, and assign resources.
 */
public class ConsoleMenu {

    private final Scanner scanner;
    private final LocationRepository locationRepository;
    private final RoadRepository roadRepository;
    private final ResourceRepository resourceRepository;
    private final ServiceRequestRepository serviceRequestRepository;
    private final AlgorithmRunRepository algorithmRunRepository;
    private final AuditEventRepository auditEventRepository;

    private DynamicArray<Location> locations = new DynamicArray<>();
    private DynamicArray<Road> roads = new DynamicArray<>();
    private DynamicArray<Resource> resources = new DynamicArray<>();
    private DynamicArray<ServiceRequest> serviceRequests = new DynamicArray<>();
    private RouteService routeService;

    public ConsoleMenu(Scanner scanner,
                        LocationRepository locationRepository,
                        RoadRepository roadRepository,
                        ResourceRepository resourceRepository,
                        ServiceRequestRepository serviceRequestRepository,
                        AlgorithmRunRepository algorithmRunRepository,
                        AuditEventRepository auditEventRepository) {
        this.scanner = scanner;
        this.locationRepository = locationRepository;
        this.roadRepository = roadRepository;
        this.resourceRepository = resourceRepository;
        this.serviceRequestRepository = serviceRequestRepository;
        this.algorithmRunRepository = algorithmRunRepository;
        this.auditEventRepository = auditEventRepository;
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> reloadFromDatabase();
                    case "2" -> searchLocations();
                    case "3" -> listServiceRequestsByStatus();
                    case "4" -> dispatchDemo();
                    case "5" -> routeDemo();
                    case "6" -> mstDemo();
                    case "7" -> sortingDemo();
                    case "8" -> searchingDemo();
                    case "9" -> resourceAssignmentDemo();
                    case "10" -> viewAuditLog();
                    case "0" -> running = false;
                    default -> System.out.println("Unrecognised option: " + choice);
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        System.out.println("Goodbye.");
    }

    private void printMenu() {
        System.out.println();
        System.out.println("=== Ghana Smart Campus Service Operations Optimizer ===");
        System.out.println("1. Reload data from database");
        System.out.println("2. Search locations by name");
        System.out.println("3. List service requests by status");
        System.out.println("4. Dispatch demo (FIFO vs priority)");
        System.out.println("5. Route demo (shortest path + reachability)");
        System.out.println("6. Minimum spanning tree demo (Prim vs Kruskal)");
        System.out.println("7. Sorting comparison demo");
        System.out.println("8. Searching comparison demo");
        System.out.println("9. Resource assignment demo (greedy)");
        System.out.println("10. View audit log");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    private void reloadFromDatabase() throws Exception {
        locations = locationRepository.findAll();
        roads = roadRepository.findAll();
        resources = resourceRepository.findAll();
        serviceRequests = serviceRequestRepository.findAll();
        routeService = new RouteService(locations, roads);
        System.out.printf("Loaded %d locations, %d roads, %d resources, %d service requests.%n",
                locations.size(), roads.size(), resources.size(), serviceRequests.size());
    }

    private void searchLocations() {
        ensureLoaded();
        System.out.print("Enter a name substring to search for: ");
        String query = scanner.nextLine().trim().toLowerCase();
        int found = 0;
        for (int i = 0; i < locations.size(); i++) {
            Location loc = locations.get(i);
            if (loc.getName().toLowerCase().contains(query)) {
                System.out.println("  " + loc.getLocationId() + ": " + loc.getName()
                        + " (" + loc.getArea() + ", " + loc.getType() + ")");
                found++;
            }
        }
        System.out.println(found + " match(es).");
    }

    private void listServiceRequestsByStatus() {
        ensureLoaded();
        System.out.print("Enter status (PENDING/ASSIGNED/IN_PROGRESS/DONE/CANCELLED): ");
        String statusInput = scanner.nextLine().trim().toUpperCase();
        ServiceRequest.Status status = ServiceRequest.Status.valueOf(statusInput);
        int found = 0;
        for (int i = 0; i < serviceRequests.size(); i++) {
            ServiceRequest req = serviceRequests.get(i);
            if (req.getStatus() == status) {
                System.out.println("  " + req.getRequestId() + " [" + req.getUrgency() + "] " + req.getCategory());
                found++;
                if (found >= 20) {
                    System.out.println("  ... (showing first 20)");
                    break;
                }
            }
        }
        System.out.println(found + " match(es) shown.");
    }

    private void dispatchDemo() {
        ensureLoaded();
        SchedulingEngine engine = new SchedulingEngine(5);
        int sampleSize = Math.min(5, serviceRequests.size());
        for (int i = 0; i < sampleSize; i++) {
            engine.submitFifo(serviceRequests.get(i));
            engine.submitPriority(serviceRequests.get(i));
        }

        System.out.println("FIFO dispatch order:");
        while (!engine.isFifoEmpty()) {
            System.out.println("  " + engine.dispatchNextFifo().getRequestId());
        }

        System.out.println("Priority dispatch order (most urgent first):");
        while (!engine.isPriorityEmpty()) {
            ServiceRequest r = engine.dispatchNextPriority();
            System.out.println("  " + r.getRequestId() + " [" + r.getUrgency() + "]");
        }
    }

    private void routeDemo() {
        ensureLoaded();
        System.out.print("From location id: ");
        int from = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("To location id: ");
        int to = Integer.parseInt(scanner.nextLine().trim());

        Object[] path = routeService.shortestPath(from, to);
        if (path == null) {
            System.out.println("No path found from " + from + " to " + to + ".");
        } else {
            System.out.println("Shortest path: " + java.util.Arrays.toString(path));
            System.out.println("Distance: " + routeService.shortestDistance(from, to));
        }

        Object[] reachable = routeService.reachableFrom(from);
        System.out.println(reachable.length + " location(s) reachable from " + from + ".");
    }

    private void mstDemo() {
        ensureLoaded();
        Object[] nodes = routeService.getGraph().nodes();
        if (nodes.length == 0) {
            System.out.println("No locations loaded.");
            return;
        }
        Integer start = (Integer) nodes[0];

        MSTResult<Integer> primResult = Prim.mst(routeService.getGraph(), start);
        System.out.println("Prim MST from " + start + ": " + primResult.edges.length
                + " edges, total cost " + primResult.totalCost);

        MSTResult<Integer> kruskalResult = Kruskal.mst(routeService.getGraph());
        System.out.println("Kruskal MST: " + kruskalResult.edges.length
                + " edges, total cost " + kruskalResult.totalCost);
        for (WeightedEdge<Integer> edge : kruskalResult.edges) {
            System.out.println("  " + edge);
        }
    }

    private void sortingDemo() throws SQLException {
        int n = 2000;
        int[] base = randomArray(n);

        long selectionTime = timeSort(base.clone(), SelectionSort::sort);
        long insertionTime = timeSort(base.clone(), InsertionSort::sort);
        long mergeTime = timeSort(base.clone(), MergeSort::sort);
        long quickTime = timeSort(base.clone(), QuickSort::sort);

        System.out.println("Sorting " + n + " random ints:");
        System.out.println("  Selection: " + selectionTime + " ns");
        System.out.println("  Insertion: " + insertionTime + " ns");
        System.out.println("  Merge:     " + mergeTime + " ns");
        System.out.println("  Quick:     " + quickTime + " ns");

        algorithmRunRepository.insert(new AlgorithmRun(null, "selection_sort", n, selectionTime, null, Instant.now()));
        algorithmRunRepository.insert(new AlgorithmRun(null, "insertion_sort", n, insertionTime, null, Instant.now()));
        algorithmRunRepository.insert(new AlgorithmRun(null, "merge_sort", n, mergeTime, null, Instant.now()));
        algorithmRunRepository.insert(new AlgorithmRun(null, "quick_sort", n, quickTime, null, Instant.now()));
        System.out.println("Timings recorded in algorithm_runs.");
    }

    private void searchingDemo() throws SQLException {
        int n = 10000;
        int[] sorted = randomArray(n);
        java.util.Arrays.sort(sorted); // built-in sort here is just fixture setup, not assessed logic
        int target = sorted[n / 2];

        long start1 = System.nanoTime();
        LinearSearch.search(sorted, target);
        long linearTime = System.nanoTime() - start1;

        long start2 = System.nanoTime();
        BinarySearch.search(sorted, target);
        long binaryTime = System.nanoTime() - start2;

        System.out.println("Searching for a value among " + n + " sorted ints:");
        System.out.println("  Linear: " + linearTime + " ns");
        System.out.println("  Binary: " + binaryTime + " ns");

        algorithmRunRepository.insert(new AlgorithmRun(null, "linear_search", n, linearTime, null, Instant.now()));
        algorithmRunRepository.insert(new AlgorithmRun(null, "binary_search", n, binaryTime, null, Instant.now()));
        System.out.println("Timings recorded in algorithm_runs.");
    }

    private void resourceAssignmentDemo() throws SQLException {
        ensureLoaded();
        DynamicArray<ServiceRequest> pending = new DynamicArray<>();
        for (int i = 0; i < serviceRequests.size() && pending.size() < 5; i++) {
            if (serviceRequests.get(i).getStatus() == ServiceRequest.Status.PENDING) {
                pending.add(serviceRequests.get(i));
            }
        }
        DynamicArray<Resource> available = new DynamicArray<>();
        for (int i = 0; i < resources.size() && available.size() < 5; i++) {
            if (resources.get(i).getAvailabilityStatus() == Resource.AvailabilityStatus.AVAILABLE) {
                available.add(resources.get(i));
            }
        }

        if (pending.isEmpty() || available.isEmpty()) {
            System.out.println("Not enough pending requests / available resources to demonstrate.");
            return;
        }

        ResourceAssignmentService assignmentService = new ResourceAssignmentService(routeService);
        var result = assignmentService.assign(pending, available);

        System.out.println("Greedy resource assignment (total cost " + result.totalCost + "):");
        for (int i = 0; i < result.assignment.length; i++) {
            ServiceRequest req = pending.get(i);
            Resource res = available.get(result.assignment[i]);
            System.out.println("  " + req.getRequestId() + " -> " + res.getResourceId());
            auditEventRepository.insert(new AuditEvent(null, "ASSIGN", req.getRequestId(),
                    "Assigned " + res.getResourceId() + " to " + req.getRequestId(), Instant.now()));
        }
    }

    private void viewAuditLog() throws SQLException {
        DynamicArray<AuditEvent> events = auditEventRepository.findAll();
        if (events.isEmpty()) {
            System.out.println("No audit events recorded yet.");
            return;
        }
        for (int i = 0; i < events.size(); i++) {
            AuditEvent e = events.get(i);
            System.out.println("  [" + e.getEventTime() + "] " + e.getEventType() + ": " + e.getDescription());
        }
    }

    private void ensureLoaded() {
        if (routeService == null) {
            throw new IllegalStateException("Load the database first (option 1).");
        }
    }

    private static int[] randomArray(int n) {
        Random random = new Random(42); // fixed seed for reproducible demo runs
        int[] array = new int[n];
        for (int i = 0; i < n; i++) {
            array[i] = random.nextInt(1_000_000);
        }
        return array;
    }

    @FunctionalInterface
    private interface SortFn {
        void sort(int[] array);
    }

    private static long timeSort(int[] array, SortFn sortFn) {
        long start = System.nanoTime();
        sortFn.sort(array);
        return System.nanoTime() - start;
    }
}
