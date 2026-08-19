package gh.edu.ug.dsaoptimizer.persistence;

import gh.edu.ug.dsaoptimizer.model.ServiceRequest;
import gh.edu.ug.dsaoptimizer.structures.DynamicArray;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

public class ServiceRequestRepository {

    private final Connection connection;

    public ServiceRequestRepository(Connection connection) {
        this.connection = connection;
    }

    public void insert(ServiceRequest request) throws SQLException {
        String sql = "INSERT INTO service_requests (request_id, source_location_id, "
                + "destination_location_id, category, urgency, time_submitted, deadline, status, "
                + "assigned_resource_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) "
                + "ON CONFLICT(request_id) DO UPDATE SET status=excluded.status, "
                + "assigned_resource_id=excluded.assigned_resource_id";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, request.getRequestId());
            ps.setInt(2, request.getSourceLocationId());
            ps.setInt(3, request.getDestinationLocationId());
            ps.setString(4, request.getCategory());
            ps.setString(5, request.getUrgency().name());
            ps.setString(6, request.getTimeSubmitted().toString());
            if (request.getDeadline() == null) ps.setNull(7, java.sql.Types.VARCHAR);
            else ps.setString(7, request.getDeadline().toString());
            ps.setString(8, request.getStatus().name());
            ps.setString(9, request.getAssignedResourceId());
            ps.executeUpdate();
        }
    }

    public void updateStatusAndAssignment(String requestId, ServiceRequest.Status status,
                                           String assignedResourceId) throws SQLException {
        String sql = "UPDATE service_requests SET status = ?, assigned_resource_id = ? "
                + "WHERE request_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setString(2, assignedResourceId);
            ps.setString(3, requestId);
            ps.executeUpdate();
        }
    }

    public DynamicArray<ServiceRequest> findAll() throws SQLException {
        DynamicArray<ServiceRequest> result = new DynamicArray<>();
        String sql = "SELECT request_id, source_location_id, destination_location_id, category, "
                + "urgency, time_submitted, deadline, status, assigned_resource_id "
                + "FROM service_requests ORDER BY time_submitted";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(fromRow(rs));
            }
        }
        return result;
    }

    private ServiceRequest fromRow(ResultSet rs) throws SQLException {
        String deadlineStr = rs.getString("deadline");
        return new ServiceRequest(
                rs.getString("request_id"),
                rs.getInt("source_location_id"),
                rs.getInt("destination_location_id"),
                rs.getString("category"),
                ServiceRequest.Urgency.valueOf(rs.getString("urgency")),
                Instant.parse(rs.getString("time_submitted")),
                deadlineStr == null ? null : Instant.parse(deadlineStr),
                ServiceRequest.Status.valueOf(rs.getString("status")),
                rs.getString("assigned_resource_id")
        );
    }

    /** Parses the seed CSV and inserts every row, returning the loaded requests. */
    public DynamicArray<ServiceRequest> loadFromCsv(Path csvPath) throws IOException, SQLException {
        DynamicArray<ServiceRequest> loaded = new DynamicArray<>();
        for (String[] row : CsvUtil.readRows(csvPath)) {
            // request_id,source_location_id,destination_location_id,category,urgency,
            // time_submitted,deadline,status
            String deadlineField = CsvUtil.blankToNull(row[6]);
            ServiceRequest request = new ServiceRequest(
                    row[0],
                    Integer.parseInt(row[1]),
                    Integer.parseInt(row[2]),
                    row[3],
                    ServiceRequest.Urgency.valueOf(row[4]),
                    Instant.parse(row[5]),
                    deadlineField == null ? null : Instant.parse(deadlineField),
                    ServiceRequest.Status.valueOf(row[7])
            );
            insert(request);
            loaded.add(request);
        }
        return loaded;
    }
}
