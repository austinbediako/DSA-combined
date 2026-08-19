package gh.edu.ug.dsaoptimizer.persistence;

import gh.edu.ug.dsaoptimizer.model.AlgorithmRun;
import gh.edu.ug.dsaoptimizer.model.AuditEvent;
import gh.edu.ug.dsaoptimizer.model.Location;
import gh.edu.ug.dsaoptimizer.model.Resource;
import gh.edu.ug.dsaoptimizer.model.ServiceRequest;
import gh.edu.ug.dsaoptimizer.structures.DynamicArray;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Loads the real schema and real seed CSVs end to end -- this is the
 * "database is part of the running system" evidence required by the
 * brief (section 4), not just a unit test of one method.
 */
class PersistenceIntegrationTest {

    private Database db;

    @BeforeEach
    void setUp() throws Exception {
        db = Database.openInMemory();
        db.applySchema(Path.of("database/schema.sql"));
    }

    @AfterEach
    void tearDown() throws Exception {
        db.close();
    }

    @Test
    void loadsAllSeedDataAndRoundTripsThroughTheDatabase() throws Exception {
        LocationRepository locationRepo = new LocationRepository(db.getConnection());
        RoadRepository roadRepo = new RoadRepository(db.getConnection());
        ResourceRepository resourceRepo = new ResourceRepository(db.getConnection());
        ServiceRequestRepository requestRepo = new ServiceRequestRepository(db.getConnection());

        DynamicArray<Location> locations = locationRepo.loadFromCsv(Path.of("data/processed/locations.csv"));
        DynamicArray<gh.edu.ug.dsaoptimizer.model.Road> roads =
                roadRepo.loadFromCsv(Path.of("data/processed/roads.csv"));
        DynamicArray<Resource> resources = resourceRepo.loadFromCsv(Path.of("data/processed/resources.csv"));
        DynamicArray<ServiceRequest> requests =
                requestRepo.loadFromCsv(Path.of("data/processed/service_requests.csv"));

        assertEquals(50, locations.size());
        assertEquals(101, roads.size());
        assertEquals(30, resources.size());
        assertEquals(300, requests.size());

        // Round trip: what we just inserted should be exactly what findAll() returns.
        assertEquals(50, locationRepo.findAll().size());
        assertEquals(101, roadRepo.findAll().size());
        assertEquals(30, resourceRepo.findAll().size());
        assertEquals(300, requestRepo.findAll().size());

        Location first = locationRepo.findAll().get(0);
        assertEquals(1, first.getLocationId());
    }

    @Test
    void updatesResourceAvailabilityAndRequestAssignment() throws Exception {
        LocationRepository locationRepo = new LocationRepository(db.getConnection());
        ResourceRepository resourceRepo = new ResourceRepository(db.getConnection());
        ServiceRequestRepository requestRepo = new ServiceRequestRepository(db.getConnection());

        locationRepo.loadFromCsv(Path.of("data/processed/locations.csv"));
        resourceRepo.loadFromCsv(Path.of("data/processed/resources.csv"));
        requestRepo.loadFromCsv(Path.of("data/processed/service_requests.csv"));

        resourceRepo.updateAvailability("RES-001", Resource.AvailabilityStatus.BUSY);
        Resource updated = findByIdOrThrow(resourceRepo.findAll(), "RES-001");
        assertEquals(Resource.AvailabilityStatus.BUSY, updated.getAvailabilityStatus());

        requestRepo.updateStatusAndAssignment("REQ-001", ServiceRequest.Status.ASSIGNED, "RES-001");
        ServiceRequest updatedRequest = findRequestOrThrow(requestRepo.findAll(), "REQ-001");
        assertEquals(ServiceRequest.Status.ASSIGNED, updatedRequest.getStatus());
        assertEquals("RES-001", updatedRequest.getAssignedResourceId());
    }

    @Test
    void insertsAndReadsBackAlgorithmRunsAndAuditEvents() throws Exception {
        AlgorithmRunRepository runRepo = new AlgorithmRunRepository(db.getConnection());
        AuditEventRepository eventRepo = new AuditEventRepository(db.getConnection());

        int runId = runRepo.insert(new AlgorithmRun(null, "quicksort", 1000, 45000L, 12L, Instant.now()));
        assertTrue(runId > 0);
        assertEquals(1, runRepo.findAll().size());
        assertEquals("quicksort", runRepo.findAll().get(0).getAlgorithmName());

        // related_request_id is nullable and FK-checked -- null here since this
        // test doesn't load service_requests seed data.
        int eventId = eventRepo.insert(new AuditEvent(null, "ASSIGN", null,
                "Assigned RES-001 to REQ-001", Instant.now()));
        assertTrue(eventId > 0);
        assertEquals(1, eventRepo.findAll().size());
        assertEquals("ASSIGN", eventRepo.findAll().get(0).getEventType());
    }

    private static Resource findByIdOrThrow(DynamicArray<Resource> resources, String id) {
        for (int i = 0; i < resources.size(); i++) {
            if (resources.get(i).getResourceId().equals(id)) return resources.get(i);
        }
        throw new AssertionError("resource not found: " + id);
    }

    private static ServiceRequest findRequestOrThrow(DynamicArray<ServiceRequest> requests, String id) {
        for (int i = 0; i < requests.size(); i++) {
            if (requests.get(i).getRequestId().equals(id)) return requests.get(i);
        }
        throw new AssertionError("request not found: " + id);
    }
}
