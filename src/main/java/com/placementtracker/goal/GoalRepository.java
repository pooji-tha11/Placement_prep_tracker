package com.placementtracker.goal;

import com.placementtracker.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GoalRepository {

    public void add(Goal<?> goal) {
        String sql = "INSERT INTO goals (id, description, target_count, current_count, deadline) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, goal.getId());
            stmt.setString(2, goal.getDescription());
            stmt.setInt(3, goal.getTargetCount());
            stmt.setInt(4, goal.getCurrentCount());
            stmt.setDate(5, java.sql.Date.valueOf(goal.getDeadline()));

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save goal: " + e.getMessage(), e);
        }
    }

    public List<Goal<?>> getAll() {
        List<Goal<?>> goals = new ArrayList<>();
        String sql = "SELECT * FROM goals";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                goals.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch goals: " + e.getMessage(), e);
        }

        return goals;
    }

    public Goal<?> findById(String id) {
        String sql = "SELECT * FROM goals WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch goal: " + e.getMessage(), e);
        }

        return null;
    }

    public boolean deleteById(String id) {
        String sql = "DELETE FROM goals WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete goal: " + e.getMessage(), e);
        }
    }

    // New: persists updated progress. Same reasoning as Project.updateStarForm() —
    // mutating an in-memory Goal returned by findById() no longer has any lasting effect.
    public boolean updateProgress(String id, int currentCount) {
        String sql = "UPDATE goals SET current_count = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, currentCount);
            stmt.setString(2, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update goal progress: " + e.getMessage(), e);
        }
    }

    private Goal<?> mapRow(ResultSet rs) throws SQLException {
        return new Goal<>(
                rs.getString("id"),
                rs.getString("description"),
                rs.getInt("target_count"),
                rs.getInt("current_count"),
                rs.getDate("deadline").toLocalDate()
        );
    }
}