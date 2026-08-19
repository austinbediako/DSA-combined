package gh.edu.ug.dsaoptimizer.model;

public class Road {

    private final int roadId;
    private final int fromLocationId;
    private final int toLocationId;
    private final double distance;
    private final double travelTime;
    private final double roadConditionWeight;

    public Road(int roadId, int fromLocationId, int toLocationId,
                double distance, double travelTime, double roadConditionWeight) {
        this.roadId = roadId;
        this.fromLocationId = fromLocationId;
        this.toLocationId = toLocationId;
        this.distance = distance;
        this.travelTime = travelTime;
        this.roadConditionWeight = roadConditionWeight;
    }

    public int getRoadId() {
        return roadId;
    }

    public int getFromLocationId() {
        return fromLocationId;
    }

    public int getToLocationId() {
        return toLocationId;
    }

    public double getDistance() {
        return distance;
    }

    public double getTravelTime() {
        return travelTime;
    }

    public double getRoadConditionWeight() {
        return roadConditionWeight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Road)) return false;
        Road that = (Road) o;
        return roadId == that.roadId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(roadId);
    }

    @Override
    public String toString() {
        return "Road{" +
                "roadId=" + roadId +
                ", fromLocationId=" + fromLocationId +
                ", toLocationId=" + toLocationId +
                ", distance=" + distance +
                '}';
    }
}
