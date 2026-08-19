package gh.edu.ug.dsaoptimizer.model;

import java.util.Objects;

public class Resource {

    public enum AvailabilityStatus {
        AVAILABLE, BUSY, OFFLINE
    }

    private final String resourceId;
    private final String type;
    private final int homeLocationId;
    private final int capacity;
    private final AvailabilityStatus availabilityStatus;

    public Resource(String resourceId, String type, int homeLocationId,
                     int capacity, AvailabilityStatus availabilityStatus) {
        this.resourceId = Objects.requireNonNull(resourceId, "resourceId");
        this.type = Objects.requireNonNull(type, "type");
        this.homeLocationId = homeLocationId;
        this.capacity = capacity;
        this.availabilityStatus = Objects.requireNonNull(availabilityStatus, "availabilityStatus");
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getType() {
        return type;
    }

    public int getHomeLocationId() {
        return homeLocationId;
    }

    public int getCapacity() {
        return capacity;
    }

    public AvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Resource)) return false;
        Resource that = (Resource) o;
        return resourceId.equals(that.resourceId);
    }

    @Override
    public int hashCode() {
        return resourceId.hashCode();
    }

    @Override
    public String toString() {
        return "Resource{" +
                "resourceId='" + resourceId + '\'' +
                ", type='" + type + '\'' +
                ", availabilityStatus=" + availabilityStatus +
                '}';
    }
}
