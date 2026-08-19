package gh.edu.ug.dsaoptimizer.persistence;

import gh.edu.ug.dsaoptimizer.model.Location;
import gh.edu.ug.dsaoptimizer.structures.DynamicArray;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationRepository {

    private final Connection connection;

    public LocationRepository(Connection connection) {
        this.connection = connection;
    }

    public void insert(Location location) throws SQLException {
        String sql = "INSERT INTO locations (location_id, name, area, type, latitude, longitude) "
                + "VALUES (?, ?, ?, ?, ?, ?) "
                + "ON CONFLICT(location_id) DO UPDATE SET name=excluded.name, area=excluded.area, "
                + "type=excluded.type, latitude=excluded.latitude, longitude=excluded.longitude";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, location.getLocationId());
            ps.setString(2, location.getName());
            ps.setString(3, location.getArea());
            ps.setString(4, location.getType());
            if (location.getLatitude() == null) ps.setNull(5, java.sql.Types.REAL);
            else ps.setDouble(5, location.getLatitude());
            if (location.getLongitude() == null) ps.setNull(6, java.sql.Types.REAL);
            else ps.setDouble(6, location.getLongitude());
            ps.executeUpdate();
        }
    }

    public DynamicArray<Location> findAll() throws SQLException {
        DynamicArray<Location> result = new DynamicArray<>();
        String sql = "SELECT location_id, name, area, type, latitude, longitude FROM locations "
                + "ORDER BY location_id";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Double lat = rs.getObject("latitude") == null ? null : rs.getDouble("latitude");
                Double lon = rs.getObject("longitude") == null ? null : rs.getDouble("longitude");
                result.add(new Location(
                        rs.getInt("location_id"),
                        rs.getString("name"),
                        rs.getString("area"),
                        rs.getString("type"),
                        lat,
                        lon
                ));
            }
        }
        return result;
    }

    /** Parses the seed CSV and inserts every row, returning the loaded locations. */
    public DynamicArray<Location> loadFromCsv(Path csvPath) throws IOException, SQLException {
        DynamicArray<Location> loaded = new DynamicArray<>();
        for (String[] row : CsvUtil.readRows(csvPath)) {
            // location_id,name,area,type,latitude,longitude
            Double lat = CsvUtil.blankToNull(row[4]) == null ? null : Double.parseDouble(row[4]);
            Double lon = CsvUtil.blankToNull(row[5]) == null ? null : Double.parseDouble(row[5]);
            Location location = new Location(
                    Integer.parseInt(row[0]), row[1], row[2], row[3], lat, lon
            );
            insert(location);
            loaded.add(location);
        }
        return loaded;
    }
}
