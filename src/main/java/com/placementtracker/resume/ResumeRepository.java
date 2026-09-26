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
        String sql = "INSERT INTO resumes (id, label, version, filename, date_added, created_at, stored_file_name, original_file_name, content_type, file_size_bytes) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, resume.getId());
            stmt.setString(2, resume.getLabel());
            stmt.setString(3, resume.getVersion());
            stmt.setString(4, resume.getFilename());
            stmt.setDate(5, java.sql.Date.valueOf(resume.getDateAdded()));
            stmt.setTimestamp(6, java.sql.Timestamp.valueOf(resume.getCreatedAt()));
            stmt.setString(7, resume.getStoredFileName());
            stmt.setString(8, resume.getOriginalFileName());
            stmt.setString(9, resume.getContentType());
            if (resume.getFileSizeBytes() != null) stmt.setLong(10, resume.getFileSizeBytes());
            else stmt.setNull(10, java.sql.Types.BIGINT);

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
            while (rs.next()) resumes.add(mapRow(rs));
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
                if (rs.next()) return mapRow(rs);
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
            return stmt.executeUpdate() > 0;
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
                if (rs.next()) return rs.getInt(1) > 0;
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
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getString("stored_file_name"),
                rs.getString("original_file_name"),
                rs.getString("content_type"),
                rs.getObject("file_size_bytes") != null ? rs.getLong("file_size_bytes") : null
        );
    }

    public boolean update(Resume r) {
        String sql = "UPDATE resumes SET label = ?, version = ?, filename = ?, date_added = ?, stored_file_name = ?, original_file_name = ?, content_type = ?, file_size_bytes = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, r.getLabel());
            stmt.setString(2, r.getVersion());
            stmt.setString(3, r.getFilename());
            stmt.setDate(4, java.sql.Date.valueOf(r.getDateAdded()));
            stmt.setString(5, r.getStoredFileName());
            stmt.setString(6, r.getOriginalFileName());
            stmt.setString(7, r.getContentType());
            if (r.getFileSizeBytes() != null) stmt.setLong(8, r.getFileSizeBytes());
            else stmt.setNull(8, java.sql.Types.BIGINT);
            stmt.setString(9, r.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update resume: " + e.getMessage(), e);
        }
    }
}
