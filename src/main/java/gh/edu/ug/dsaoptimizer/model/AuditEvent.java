package gh.edu.ug.dsaoptimizer.model;

import java.time.Instant;
import java.util.Objects;

public class AuditEvent {

    private final Integer eventId; // nullable until persisted (autoincrement)
    private final String eventType;
    private final String relatedRequestId; // nullable
    private final String description;
    private final Instant eventTime;

    public AuditEvent(Integer eventId, String eventType, String relatedRequestId,
                       String description, Instant eventTime) {
        this.eventId = eventId;
        this.eventType = Objects.requireNonNull(eventType, "eventType");
        this.relatedRequestId = relatedRequestId;
        this.description = Objects.requireNonNull(description, "description");
        this.eventTime = Objects.requireNonNull(eventTime, "eventTime");
    }

    public Integer getEventId() {
        return eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public String getRelatedRequestId() {
        return relatedRequestId;
    }

    public String getDescription() {
        return description;
    }

    public Instant getEventTime() {
        return eventTime;
    }

    @Override
    public String toString() {
        return "AuditEvent{" +
                "eventId=" + eventId +
                ", eventType='" + eventType + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
