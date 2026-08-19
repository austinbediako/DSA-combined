package gh.edu.ug.dsaoptimizer.model;

import java.util.Objects;

public class Location {

    private final int locationId;
    private final String name;
    private final String area;
    private final String type;
    private final Double latitude;  // nullable
    private final Double longitude; // nullable

    public Location(int locationId, String name, String area, String type,
                     Double latitude, Double longitude) {
        this.locationId = locationId;
        this.name = Objects.requireNonNull(name, "name");
        this.area = Objects.requireNonNull(area, "area");
        this.type = Objects.requireNonNull(type, "type");
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getLocationId() {
        return locationId;
    }

    public String getName() {
        return name;
    }

    public String getArea() {
        return area;
    }

    public String getType() {
        return type;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;
        Location that = (Location) o;
        return locationId == that.locationId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(locationId);
    }

    @Override
    public String toString() {
        return "Location{" +
                "locationId=" + locationId +
                ", name='" + name + '\'' +
                ", area='" + area + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
