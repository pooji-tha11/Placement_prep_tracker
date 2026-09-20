package com.placementtracker.project;

import com.placementtracker.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectRepository {

    public void add(Project project) {
        String sql = "INSERT INTO projects "
                + "(id, title, domain, tech_stack, repo_link, status, "
                + "star_situation, star_task, star_action, star_result, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, project.getId());
            stmt.setString(2, project.getTitle());
            stmt.setString(3, project.getDomain());
            stmt.setString(4, String.join(",", project.getTechStack()));
            stmt.setString(5, project.getRepoLink());
            stmt.setString(6, project.getStatus().toString());

            StarForm form = project.getStarForm();
            stmt.setString(7, form != null ? form.getSituation() : null);
            stmt.setString(8, form != null ? form.getTask() : null);
            stmt.setString(9, form != null ? form.getAction() : null);
            stmt.setString(10, form != null ? form.getResult() : null);

            stmt.setTimestamp(11, java.sql.Timestamp.valueOf(project.getCreatedAt()));

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save project: " + e.getMessage(), e);
        }
    }

    public List<Project> getAll() {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM projects";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                projects.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch projects: " + e.getMessage(), e);
        }

        return projects;
    }

    public Project findById(String id) {
        String sql = "SELECT * FROM projects WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch project: " + e.getMessage(), e);
        }

        return null;
    }

    public boolean deleteById(String id) {
        String sql = "DELETE FROM projects WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete project: " + e.getMessage(), e);
        }
    }

    // New: persists a completed STAR form to an existing project row.
    // Required because, unlike the old ArrayList version, mutating an in-memory
    // Project object no longer has any effect on what's actually stored.
    public boolean updateStarForm(String projectId, StarForm form) {
        String sql = "UPDATE projects SET star_situation = ?, star_task = ?, "
                + "star_action = ?, star_result = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, form.getSituation());
            stmt.setString(2, form.getTask());
            stmt.setString(3, form.getAction());
            stmt.setString(4, form.getResult());
            stmt.setString(5, projectId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save STAR form: " + e.getMessage(), e);
        }
    }

    public boolean updateStatus(String id, ProjectStatus status) {
        String sql = "UPDATE projects SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            stmt.setString(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update status: " + e.getMessage(), e);
        }
    }

    private Project mapRow(ResultSet rs) throws SQLException {
        List<String> techStack = List.of(rs.getString("tech_stack").split(","));

        StarForm starForm = null;
        String situation = rs.getString("star_situation");
        if (situation != null) {
            starForm = new StarForm(
                    situation,
                    rs.getString("star_task"),
                    rs.getString("star_action"),
                    rs.getString("star_result")
            );
        }

        return new Project(
                rs.getString("id"),
                rs.getString("title"),
                rs.getString("domain"),
                techStack,
                rs.getString("repo_link"),
                ProjectStatus.valueOf(rs.getString("status")),
                starForm
        );
    }
}