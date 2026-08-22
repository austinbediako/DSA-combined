package gh.edu.ug.dsaoptimizer.service;

import gh.edu.ug.dsaoptimizer.model.ServiceRequest;
import gh.edu.ug.dsaoptimizer.structures.CircularQueue;
import gh.edu.ug.dsaoptimizer.structures.Deque;
import gh.edu.ug.dsaoptimizer.structures.PriorityQueueHeap;
import gh.edu.ug.dsaoptimizer.structures.Queue;

/**
 * Service dispatch engine modelling four different dispatch rules
 * (brief M5), each backed by the matching custom structure:
 * <ul>
 *   <li>FIFO -- {@link Queue}: first submitted, first dispatched.</li>
 *   <li>Priority -- {@link PriorityQueueHeap}: most urgent first,
 *       tie-broken by earlier submission time.</li>
 *   <li>Circular -- {@link CircularQueue}: a fixed number of
 *       concurrent dispatch slots; demonstrates front/rear
 *       wrap-around as slots free up and refill.</li>
 *   <li>Deque -- {@link Deque}: an urgent request can jump straight
 *       to the front via {@code submitUrgent}, while normal requests
 *       join the back via {@code submitNormal}.</li>
 * </ul>
 */
public class SchedulingEngine {

    private final Queue<ServiceRequest> fifoQueue = new Queue<>();
    private final PriorityQueueHeap<ServiceRequest> priorityQueue;
    private final CircularQueue<ServiceRequest> dispatchSlots;
    private final Deque<ServiceRequest> urgentDeque = new Deque<>();

    public SchedulingEngine(int dispatchSlotCapacity) {
        this.priorityQueue = new PriorityQueueHeap<>(SchedulingEngine::compareByUrgencyThenTime);
        this.dispatchSlots = new CircularQueue<>(dispatchSlotCapacity);
    }

    /** Most urgent first (CRITICAL before LOW); ties broken by earlier submission time. */
    private static int compareByUrgencyThenTime(ServiceRequest a, ServiceRequest b) {
        int urgencyCompare = b.getUrgency().ordinal() - a.getUrgency().ordinal();
        if (urgencyCompare != 0) return urgencyCompare;
        return a.getTimeSubmitted().compareTo(b.getTimeSubmitted());
    }

    // ---- FIFO dispatch ----

    public void submitFifo(ServiceRequest request) {
        fifoQueue.enqueue(request);
    }

    public ServiceRequest dispatchNextFifo() {
        return fifoQueue.dequeue();
    }

    public boolean isFifoEmpty() {
        return fifoQueue.isEmpty();
    }

    // ---- Priority dispatch ----

    public void submitPriority(ServiceRequest request) {
        priorityQueue.offer(request);
    }

    public ServiceRequest dispatchNextPriority() {
        return priorityQueue.poll();
    }

    public boolean isPriorityEmpty() {
        return priorityQueue.isEmpty();
    }

    // ---- Circular (bounded concurrent dispatch slots) ----

    public void occupySlot(ServiceRequest request) {
        dispatchSlots.enqueue(request);
    }

    public ServiceRequest freeSlot() {
        return dispatchSlots.dequeue();
    }

    public boolean areSlotsFull() {
        return dispatchSlots.isFull();
    }

    public boolean areSlotsEmpty() {
        return dispatchSlots.isEmpty();
    }

    public int occupiedSlotCount() {
        return dispatchSlots.size();
    }

    // ---- Deque (urgent-jumps-the-queue) ----

    public void submitUrgent(ServiceRequest request) {
        urgentDeque.addFirst(request);
    }

    public void submitNormal(ServiceRequest request) {
        urgentDeque.addLast(request);
    }

    public ServiceRequest dispatchNextFromDeque() {
        return urgentDeque.removeFirst();
    }

    public boolean isDequeEmpty() {
        return urgentDeque.isEmpty();
    }
}
