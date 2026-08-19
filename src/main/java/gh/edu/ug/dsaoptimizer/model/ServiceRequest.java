package gh.edu.ug.dsaoptimizer.model;

import java.time.Instant;
import java.util.Objects;

public class ServiceRequest {

    public enum Urgency {
        LOW, MEDIUM, HIGH, CRITICAL
    }

    public enum Status {
        PENDING, ASSIGNED, IN_PROGRESS, DONE, CANCELLED
    }

    private final String requestId;
    private final int sourceLocationId;
    private final int destinationLocationId;
    private final String category;
    private final Urgency urgency;
    private final Instant timeSubmitted;
    private final Instant deadline; // nullable
    private final Status status;
    private final String assignedResourceId; // nullable

    public ServiceRequest(String requestId,
                           int sourceLocationId,
                           int destinationLocationId,
                           String category,
                           Urgency urgency,
                           Instant timeSubmitted,
                           Instant deadline,
                           Status status) {
        this(requestId, sourceLocationId, destinationLocationId, category,
                urgency, timeSubmitted, deadline, status, null);
    }

    public ServiceRequest(String requestId,
                           int sourceLocationId,
                           int destinationLocationId,
                           String category,
                           Urgency urgency,
                           Instant timeSubmitted,
                           Instant deadline,
                           Status status,
                           String assignedResourceId) {
        this.requestId = Objects.requireNonNull(requestId, "requestId");
        this.sourceLocationId = sourceLocationId;
        this.destinationLocationId = destinationLocationId;
        this.category = Objects.requireNonNull(category, "category");
        this.urgency = Objects.requireNonNull(urgency, "urgency");
        this.timeSubmitted = Objects.requireNonNull(timeSubmitted, "timeSubmitted");
        this.deadline = deadline; // may legitimately be null
        this.status = Objects.requireNonNull(status, "status");
        this.assignedResourceId = assignedResourceId; // may legitimately be null
    }

    public String getRequestId() {
        return requestId;
    }

    public int getSourceLocationId() {
        return sourceLocationId;
    }

    public int getDestinationLocationId() {
        return destinationLocationId;
    }

    public String getCategory() {
        return category;
    }

    public Urgency getUrgency() {
        return urgency;
    }

    public Instant getTimeSubmitted() {
        return timeSubmitted;
    }

    public Instant getDeadline() {
        return deadline;
    }

    public Status getStatus() {
        return status;
    }

    public String getAssignedResourceId() {
        return assignedResourceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceRequest)) return false;
        ServiceRequest that = (ServiceRequest) o;
        return requestId.equals(that.requestId);
    }

    @Override
    public int hashCode() {
        return requestId.hashCode();
    }

    @Override
    public String toString() {
        return "ServiceRequest{" +
                "requestId='" + requestId + '\'' +
                ", category='" + category + '\'' +
                ", urgency=" + urgency +
                ", status=" + status +
                '}';
    }
}