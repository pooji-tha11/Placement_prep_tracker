package com.placementtracker.timer;

import com.placementtracker.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FocusRepository {

    public void add(FocusSession session) {
        String sql = "INSERT INTO focus_sessions (id, topic, duration_minutes, created_at) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, session.getId());
            stmt.setString(2, session.getTopic());
            stmt.setInt(3, session.getDurationMinutes());
            stmt.setTimestamp(4, java.sql.Timestamp.valueOf(session.getCreatedAt()));

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save focus session: " + e.getMessage(), e);
        }
    }

    public List<FocusSession> getAll() {
        List<FocusSession> sessions = new ArrayList<>();
        String sql = "SELECT * FROM focus_sessions";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                sessions.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch focus sessions: " + e.getMessage(), e);
        }

        return sessions;
    }

    private FocusSession mapRow(ResultSet rs) throws SQLException {
        return new FocusSession(
                rs.getString("id"),
                rs.getString("topic"),
                rs.getInt("duration_minutes"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}