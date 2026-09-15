package com.placementtracker.resume;

import com.placementtracker.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ResumeRepository {

    public void add(Resume resume) {
        String sql = "INSERT INTO resumes (id, label, version, filename, date_added, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, resume.getId());
            stmt.setString(2, resume.getLabel());
            stmt.setString(3, resume.getVersion());
            stmt.setString(4, resume.getFilename());
            stmt.setDate(5, java.sql.Date.valueOf(resume.getDateAdded()));
            stmt.setTimestamp(6, java.sql.Timestamp.valueOf(resume.getCreatedAt()));

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save resume: " + e.getMessage(), e);
        }
    }

    public List<Resume> getAll() {
        List<Resume> resumes = new ArrayList<>();
        String sql = "SELECT * FROM resumes";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                resumes.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch resumes: " + e.getMessage(), e);
        }

        return resumes;
    }

    public Resume findById(String id) {
        String sql = "SELECT * FROM resumes WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch resume: " + e.getMessage(), e);
        }

        return null;
    }

    public boolean deleteById(String id) {
        String sql = "DELETE FROM resumes WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete resume: " + e.getMessage(), e);
        }
    }

    public boolean existsByLabelAndVersion(String label, String version) {
        String sql = "SELECT COUNT(*) FROM resumes WHERE LOWER(label) = LOWER(?) AND LOWER(version) = LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, label);
            stmt.setString(2, version);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to check for duplicate resume: " + e.getMessage(), e);
        }

        return false;
    }

        private Resume mapRow(ResultSet rs) throws SQLException {
        return new Resume(
                rs.getString("id"),
                rs.getString("label"),
                rs.getString("version"),
                rs.getString("filename"),
                rs.getDate("date_added").toLocalDate(),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}