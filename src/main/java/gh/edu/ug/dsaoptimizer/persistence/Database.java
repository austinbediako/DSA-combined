package gh.edu.ug.dsaoptimizer.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Owns a single SQLite JDBC connection and applies database/schema.sql
 * against it. This is the only class in the project allowed to talk
 * JDBC directly -- repositories depend on the Connection it exposes.
 */
public class Database implements AutoCloseable {

    private final Connection connection;

    private Database(Connection connection) {
        this.connection = connection;
    }

    /** Opens (creating if absent) a SQLite database file at dbPath. */
    public static Database open(Path dbPath) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        try (Statement st = conn.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON");
        } catch (SQLException e) {
            try {
                conn.close();
            } catch (SQLException ignored) {
                // best-effort cleanup, original exception is what matters
            }
            throw e;
        }
        return new Database(conn);
    }

    /** In-memory database, mainly for tests. */
    public static Database openInMemory() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite::memory:");
        try (Statement st = conn.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON");
        }
        return new Database(conn);
    }

    /** Executes every statement in the given schema.sql file. */
    public void applySchema(Path schemaSqlPath) throws SQLException, IOException {
        String sql = Files.readString(schemaSqlPath);
        applySchema(sql);
    }

    public void applySchema(String schemaSql) throws SQLException {
        String[] statements = schemaSql.split(";");
        try (Statement st = connection.createStatement()) {
            for (String raw : statements) {
                String trimmed = stripComments(raw).trim();
                if (trimmed.isEmpty()) continue;
                st.execute(trimmed);
            }
        }
    }

    private static String stripComments(String sql) {
        StringBuilder result = new StringBuilder();
        for (String line : sql.split("\n")) {
            String l = line.trim();
            if (l.startsWith("--")) continue;
            result.append(line).append('\n');
        }
        return result.toString();
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }
}
