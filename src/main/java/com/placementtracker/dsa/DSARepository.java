package com.placementtracker.dsa;

import com.placementtracker.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DSARepository {

    public void add(Problem problem) {
        String sql = "INSERT INTO problems "
                + "(id, platform, dsa_tag, difficulty, confidence_level, solved_date, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, problem.getId());
            stmt.setString(2, problem.getPlatform());
            stmt.setString(3, problem.getDsaTag());
            stmt.setString(4, problem.getDifficulty().toString());
            stmt.setInt(5, problem.getConfidenceLevel());
            stmt.setDate(6, java.sql.Date.valueOf(problem.getSolvedDate()));
            stmt.setTimestamp(7, java.sql.Timestamp.valueOf(problem.getCreatedAt()));

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save problem: " + e.getMessage(), e);
        }
    }

    public List<Problem> getAll() {
        List<Problem> problems = new ArrayList<>();
        String sql = "SELECT * FROM problems";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                problems.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch problems: " + e.getMessage(), e);
        }

        return problems;
    }

    public Problem findById(String id) {
        String sql = "SELECT * FROM problems WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch problem: " + e.getMessage(), e);
        }

        return null;
    }

    public boolean deleteById(String id) {
        String sql = "DELETE FROM problems WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete problem: " + e.getMessage(), e);
        }
    }

    public List<Problem> filterByTag(String tag) {
        List<Problem> results = new ArrayList<>();
        String sql = "SELECT * FROM problems WHERE LOWER(dsa_tag) = LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tag);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to filter problems by tag: " + e.getMessage(), e);
        }

        return results;
    }

    public List<Problem> filterByDifficulty(Difficulty difficulty) {
        List<Problem> results = new ArrayList<>();
        String sql = "SELECT * FROM problems WHERE difficulty = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, difficulty.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to filter problems by difficulty: " + e.getMessage(), e);
        }

        return results;
    }

    private Problem mapRow(ResultSet rs) throws SQLException {
        return new Problem(
                rs.getString("id"),
                rs.getString("platform"),
                rs.getString("dsa_tag"),
                Difficulty.valueOf(rs.getString("difficulty")),
                rs.getInt("confidence_level"),
                rs.getDate("solved_date").toLocalDate(),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}