package com.placementtracker.application;

import com.placementtracker.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ApplicationRepository {

    public void add(JobApplication application) {
        String sql = "INSERT INTO applications "
                + "(id, company, role, date_applied, status, job_link, job_description, "
                + "required_skills, notes, resume_id, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, application.getId());
            stmt.setString(2, application.getCompany());
            stmt.setString(3, application.getRole());
            stmt.setDate(4, java.sql.Date.valueOf(application.getDateApplied()));
            stmt.setString(5, application.getStatus().toString());
            stmt.setString(6, application.getJobLink());
            stmt.setString(7, application.getJobDescription());
            stmt.setString(8, String.join(";", application.getRequiredSkills()));
            stmt.setString(9, application.getNotes());
            stmt.setString(10, application.getResumeId());
            stmt.setTimestamp(11, java.sql.Timestamp.valueOf(application.getCreatedAt()));

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save application: " + e.getMessage(), e);
        }
    }

    public List<JobApplication> getAll() {
        List<JobApplication> applications = new ArrayList<>();
        String sql = "SELECT * FROM applications";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                applications.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch applications: " + e.getMessage(), e);
        }

        return applications;
    }

    public JobApplication findById(String id) {
        String sql = "SELECT * FROM applications WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch application: " + e.getMessage(), e);
        }

        return null;
    }

    public boolean deleteById(String id) {
        String sql = "DELETE FROM applications WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete application: " + e.getMessage(), e);
        }
    }

    public boolean existsByCompanyAndRole(String company, String role) {
        String sql = "SELECT COUNT(*) FROM applications WHERE LOWER(company) = LOWER(?) AND LOWER(role) = LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, company);
            stmt.setString(2, role);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to check for duplicate application: " + e.getMessage(), e);
        }

        return false;
    }

    public boolean updateStatus(String id, ApplicationStatus newStatus) {
        String sql = "UPDATE applications SET status = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus.toString());
            stmt.setString(2, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update application status: " + e.getMessage(), e);
        }
    }

    private JobApplication mapRow(ResultSet rs) throws SQLException {
        List<String> skills = new ArrayList<>();
        String skillsRaw = rs.getString("required_skills");
        if (skillsRaw != null && !skillsRaw.isBlank()) {
            for (String skill : skillsRaw.split(";")) {
                skills.add(skill.trim());
            }
        }

        return new JobApplication(
                rs.getString("id"),
                rs.getString("company"),
                rs.getString("role"),
                rs.getDate("date_applied").toLocalDate(),
                ApplicationStatus.valueOf(rs.getString("status")),
                rs.getString("job_link"),
                rs.getString("job_description"),
                skills,
                rs.getString("notes"),
                rs.getString("resume_id"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}