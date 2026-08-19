package gh.edu.ug.dsaoptimizer.persistence;

import gh.edu.ug.dsaoptimizer.model.AuditEvent;
import gh.edu.ug.dsaoptimizer.structures.DynamicArray;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;

public class AuditEventRepository {

    private final Connection connection;

    public AuditEventRepository(Connection connection) {
        this.connection = connection;
    }

    /** Inserts an event and returns its generated event_id. */
    public int insert(AuditEvent event) throws SQLException {
        String sql = "INSERT INTO audit_events (event_type, related_request_id, description, "
                + "event_time) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, event.getEventType());
            ps.setString(2, event.getRelatedRequestId());
            ps.setString(3, event.getDescription());
            ps.setString(4, event.getEventTime().toString());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        }
    }

    public DynamicArray<AuditEvent> findAll() throws SQLException {
        DynamicArray<AuditEvent> result = new DynamicArray<>();
        String sql = "SELECT event_id, event_type, related_request_id, description, event_time "
                + "FROM audit_events ORDER BY event_id";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(new AuditEvent(
                        rs.getInt("event_id"),
                        rs.getString("event_type"),
                        rs.getString("related_request_id"),
                        rs.getString("description"),
                        Instant.parse(rs.getString("event_time"))
                ));
            }
        }
        return result;
    }
}
