package gh.edu.ug.dsaoptimizer.persistence;

import gh.edu.ug.dsaoptimizer.model.Road;
import gh.edu.ug.dsaoptimizer.structures.DynamicArray;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoadRepository {

    private final Connection connection;

    public RoadRepository(Connection connection) {
        this.connection = connection;
    }

    public void insert(Road road) throws SQLException {
        String sql = "INSERT INTO roads (road_id, from_location_id, to_location_id, distance, "
                + "travel_time, road_condition_weight) VALUES (?, ?, ?, ?, ?, ?) "
                + "ON CONFLICT(road_id) DO UPDATE SET from_location_id=excluded.from_location_id, "
                + "to_location_id=excluded.to_location_id, distance=excluded.distance, "
                + "travel_time=excluded.travel_time, road_condition_weight=excluded.road_condition_weight";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, road.getRoadId());
            ps.setInt(2, road.getFromLocationId());
            ps.setInt(3, road.getToLocationId());
            ps.setDouble(4, road.getDistance());
            ps.setDouble(5, road.getTravelTime());
            ps.setDouble(6, road.getRoadConditionWeight());
            ps.executeUpdate();
        }
    }

    public DynamicArray<Road> findAll() throws SQLException {
        DynamicArray<Road> result = new DynamicArray<>();
        String sql = "SELECT road_id, from_location_id, to_location_id, distance, travel_time, "
                + "road_condition_weight FROM roads ORDER BY road_id";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(new Road(
                        rs.getInt("road_id"),
                        rs.getInt("from_location_id"),
                        rs.getInt("to_location_id"),
                        rs.getDouble("distance"),
                        rs.getDouble("travel_time"),
                        rs.getDouble("road_condition_weight")
                ));
            }
        }
        return result;
    }

    /** Parses the seed CSV and inserts every row, returning the loaded roads. */
    public DynamicArray<Road> loadFromCsv(Path csvPath) throws IOException, SQLException {
        DynamicArray<Road> loaded = new DynamicArray<>();
        for (String[] row : CsvUtil.readRows(csvPath)) {
            // road_id,from_location_id,to_location_id,distance,travel_time,road_condition_weight
            Road road = new Road(
                    Integer.parseInt(row[0]), Integer.parseInt(row[1]), Integer.parseInt(row[2]),
                    Double.parseDouble(row[3]), Double.parseDouble(row[4]), Double.parseDouble(row[5])
            );
            insert(road);
            loaded.add(road);
        }
        return loaded;
    }
}
