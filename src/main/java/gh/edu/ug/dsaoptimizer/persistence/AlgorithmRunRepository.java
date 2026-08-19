package gh.edu.ug.dsaoptimizer.persistence;

import gh.edu.ug.dsaoptimizer.model.AlgorithmRun;
import gh.edu.ug.dsaoptimizer.structures.DynamicArray;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;

public class AlgorithmRunRepository {

    private final Connection connection;

    public AlgorithmRunRepository(Connection connection) {
        this.connection = connection;
    }

    /** Inserts a run and returns its generated run_id. */
    public int insert(AlgorithmRun run) throws SQLException {
        String sql = "INSERT INTO algorithm_runs (algorithm_name, input_size, time_ns, memory_kb, "
                + "date_run) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, run.getAlgorithmName());
            ps.setInt(2, run.getInputSize());
            ps.setLong(3, run.getTimeNs());
            if (run.getMemoryKb() == null) ps.setNull(4, java.sql.Types.INTEGER);
            else ps.setLong(4, run.getMemoryKb());
            ps.setString(5, run.getDateRun().toString());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        }
    }

    public DynamicArray<AlgorithmRun> findAll() throws SQLException {
        DynamicArray<AlgorithmRun> result = new DynamicArray<>();
        String sql = "SELECT run_id, algorithm_name, input_size, time_ns, memory_kb, date_run "
                + "FROM algorithm_runs ORDER BY run_id";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Long memKb = rs.getObject("memory_kb") == null ? null : rs.getLong("memory_kb");
                result.add(new AlgorithmRun(
                        rs.getInt("run_id"),
                        rs.getString("algorithm_name"),
                        rs.getInt("input_size"),
                        rs.getLong("time_ns"),
                        memKb,
                        Instant.parse(rs.getString("date_run"))
                ));
            }
        }
        return result;
    }
}
