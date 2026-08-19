package gh.edu.ug.dsaoptimizer.persistence;

import gh.edu.ug.dsaoptimizer.model.Resource;
import gh.edu.ug.dsaoptimizer.structures.DynamicArray;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResourceRepository {

    private final Connection connection;

    public ResourceRepository(Connection connection) {
        this.connection = connection;
    }

    public void insert(Resource resource) throws SQLException {
        String sql = "INSERT INTO resources (resource_id, type, home_location_id, capacity, "
                + "availability_status) VALUES (?, ?, ?, ?, ?) "
                + "ON CONFLICT(resource_id) DO UPDATE SET type=excluded.type, "
                + "home_location_id=excluded.home_location_id, capacity=excluded.capacity, "
                + "availability_status=excluded.availability_status";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, resource.getResourceId());
            ps.setString(2, resource.getType());
            ps.setInt(3, resource.getHomeLocationId());
            ps.setInt(4, resource.getCapacity());
            ps.setString(5, resource.getAvailabilityStatus().name());
            ps.executeUpdate();
        }
    }

    public void updateAvailability(String resourceId, Resource.AvailabilityStatus status) throws SQLException {
        String sql = "UPDATE resources SET availability_status = ? WHERE resource_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setString(2, resourceId);
            ps.executeUpdate();
        }
    }

    public DynamicArray<Resource> findAll() throws SQLException {
        DynamicArray<Resource> result = new DynamicArray<>();
        String sql = "SELECT resource_id, type, home_location_id, capacity, availability_status "
                + "FROM resources ORDER BY resource_id";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(new Resource(
                        rs.getString("resource_id"),
                        rs.getString("type"),
                        rs.getInt("home_location_id"),
                        rs.getInt("capacity"),
                        Resource.AvailabilityStatus.valueOf(rs.getString("availability_status"))
                ));
            }
        }
        return result;
    }

    /** Parses the seed CSV and inserts every row, returning the loaded resources. */
    public DynamicArray<Resource> loadFromCsv(Path csvPath) throws IOException, SQLException {
        DynamicArray<Resource> loaded = new DynamicArray<>();
        for (String[] row : CsvUtil.readRows(csvPath)) {
            // resource_id,type,home_location_id,capacity,availability_status
            Resource resource = new Resource(
                    row[0], row[1], Integer.parseInt(row[2]), Integer.parseInt(row[3]),
                    Resource.AvailabilityStatus.valueOf(row[4])
            );
            insert(resource);
            loaded.add(resource);
        }
        return loaded;
    }
}
