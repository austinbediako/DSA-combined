package gh.edu.ug.dsaoptimizer.service;

import gh.edu.ug.dsaoptimizer.model.ServiceRequest;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class SchedulingEngineTest {

    private static ServiceRequest request(String id, ServiceRequest.Urgency urgency, String isoTime) {
        return new ServiceRequest(id, 1, 2, "maintenance", urgency,
                Instant.parse(isoTime), null, ServiceRequest.Status.PENDING);
    }

    @Test
    void fifoDispatchesInSubmissionOrder() {
        SchedulingEngine engine = new SchedulingEngine(5);
        ServiceRequest r1 = request("REQ-001", ServiceRequest.Urgency.LOW, "2026-08-01T07:00:00Z");
        ServiceRequest r2 = request("REQ-002", ServiceRequest.Urgency.CRITICAL, "2026-08-01T07:01:00Z");
        engine.submitFifo(r1);
        engine.submitFifo(r2);

        assertEquals(r1, engine.dispatchNextFifo()); // FIFO ignores urgency entirely
        assertEquals(r2, engine.dispatchNextFifo());
        assertTrue(engine.isFifoEmpty());
    }

    @Test
    void priorityDispatchesMostUrgentFirstRegardlessOfSubmissionOrder() {
        SchedulingEngine engine = new SchedulingEngine(5);
        ServiceRequest low = request("REQ-001", ServiceRequest.Urgency.LOW, "2026-08-01T07:00:00Z");
        ServiceRequest critical = request("REQ-002", ServiceRequest.Urgency.CRITICAL, "2026-08-01T07:01:00Z");
        ServiceRequest medium = request("REQ-003", ServiceRequest.Urgency.MEDIUM, "2026-08-01T07:02:00Z");

        engine.submitPriority(low);
        engine.submitPriority(critical);
        engine.submitPriority(medium);

        assertEquals(critical, engine.dispatchNextPriority());
        assertEquals(medium, engine.dispatchNextPriority());
        assertEquals(low, engine.dispatchNextPriority());
        assertTrue(engine.isPriorityEmpty());
    }

    @Test
    void priorityTiesBrokenByEarlierSubmissionTime() {
        SchedulingEngine engine = new SchedulingEngine(5);
        ServiceRequest earlier = request("REQ-001", ServiceRequest.Urgency.HIGH, "2026-08-01T07:00:00Z");
        ServiceRequest later = request("REQ-002", ServiceRequest.Urgency.HIGH, "2026-08-01T07:05:00Z");

        engine.submitPriority(later);
        engine.submitPriority(earlier);

        assertEquals(earlier, engine.dispatchNextPriority());
        assertEquals(later, engine.dispatchNextPriority());
    }

    @Test
    void circularSlotsWrapAroundAsTheyFreeAndRefill() {
        SchedulingEngine engine = new SchedulingEngine(2);
        ServiceRequest r1 = request("REQ-001", ServiceRequest.Urgency.LOW, "2026-08-01T07:00:00Z");
        ServiceRequest r2 = request("REQ-002", ServiceRequest.Urgency.LOW, "2026-08-01T07:01:00Z");
        ServiceRequest r3 = request("REQ-003", ServiceRequest.Urgency.LOW, "2026-08-01T07:02:00Z");

        engine.occupySlot(r1);
        engine.occupySlot(r2);
        assertTrue(engine.areSlotsFull());

        assertEquals(r1, engine.freeSlot()); // front frees, rear stays put
        assertFalse(engine.areSlotsFull());
        engine.occupySlot(r3); // wraps around to reuse the freed slot
        assertTrue(engine.areSlotsFull());
        assertEquals(2, engine.occupiedSlotCount());

        assertEquals(r2, engine.freeSlot());
        assertEquals(r3, engine.freeSlot());
        assertTrue(engine.areSlotsEmpty());
    }

    @Test
    void dequeLetsUrgentRequestsJumpAheadOfAlreadyQueuedNormalOnes() {
        SchedulingEngine engine = new SchedulingEngine(5);
        ServiceRequest normal1 = request("REQ-001", ServiceRequest.Urgency.LOW, "2026-08-01T07:00:00Z");
        ServiceRequest normal2 = request("REQ-002", ServiceRequest.Urgency.LOW, "2026-08-01T07:01:00Z");
        ServiceRequest urgent = request("REQ-003", ServiceRequest.Urgency.CRITICAL, "2026-08-01T07:02:00Z");

        engine.submitNormal(normal1);
        engine.submitNormal(normal2);
        engine.submitUrgent(urgent); // jumps to the front despite arriving last

        assertEquals(urgent, engine.dispatchNextFromDeque());
        assertEquals(normal1, engine.dispatchNextFromDeque());
        assertEquals(normal2, engine.dispatchNextFromDeque());
        assertTrue(engine.isDequeEmpty());
    }

    @Test
    void dispatchingFromEmptyQueuesThrows() {
        SchedulingEngine engine = new SchedulingEngine(2);
        assertThrows(NoSuchElementException.class, engine::dispatchNextFifo);
        assertThrows(NoSuchElementException.class, engine::dispatchNextPriority);
        assertThrows(NoSuchElementException.class, engine::freeSlot);
        assertThrows(NoSuchElementException.class, engine::dispatchNextFromDeque);
    }

    @Test
    void occupyingBeyondCapacityThrows() {
        SchedulingEngine engine = new SchedulingEngine(1);
        engine.occupySlot(request("REQ-001", ServiceRequest.Urgency.LOW, "2026-08-01T07:00:00Z"));
        assertThrows(IllegalStateException.class, () ->
                engine.occupySlot(request("REQ-002", ServiceRequest.Urgency.LOW, "2026-08-01T07:01:00Z")));
    }
}
