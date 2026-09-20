package com.placementtracker.study;

import com.placementtracker.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudyRepository {

    public void add(StudySession session) {
        String sql = "INSERT INTO study_sessions (id, session_date, duration_minutes, topic, created_at) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, session.getId());
            stmt.setDate(2, java.sql.Date.valueOf(session.getDate()));
            stmt.setInt(3, session.getDurationMinutes());
            stmt.setString(4, session.getTopic());
            stmt.setTimestamp(5, java.sql.Timestamp.valueOf(session.getCreatedAt()));

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save study session: " + e.getMessage(), e);
        }
    }

    // Ordered by date ascending — this is what replaces TreeMap's automatic ordering.
    public List<StudySession> getAll() {
        List<StudySession> sessions = new ArrayList<>();
        String sql = "SELECT * FROM study_sessions ORDER BY session_date ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                sessions.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch study sessions: " + e.getMessage(), e);
        }

        return sessions;
    }

    public List<StudySession> getSessionsForDate(LocalDate date) {
        List<StudySession> sessions = new ArrayList<>();
        String sql = "SELECT * FROM study_sessions WHERE session_date = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, java.sql.Date.valueOf(date));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    sessions.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch sessions for date: " + e.getMessage(), e);
        }

        return sessions;
    }

    public boolean hasSessionOn(LocalDate date) {
        String sql = "SELECT COUNT(*) FROM study_sessions WHERE session_date = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, java.sql.Date.valueOf(date));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to check session existence: " + e.getMessage(), e);
        }

        return false;
    }

    public List<StudySession> getSessionsBetween(LocalDate from, LocalDate to) {
        List<StudySession> sessions = new ArrayList<>();
        String sql = "SELECT * FROM study_sessions WHERE session_date BETWEEN ? AND ? ORDER BY session_date ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, java.sql.Date.valueOf(from));
            stmt.setDate(2, java.sql.Date.valueOf(to));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    sessions.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch sessions in range: " + e.getMessage(), e);
        }

        return sessions;
    }

    // Distinct sorted dates that have at least one session — replaces TreeMap.navigableKeySet().
    public List<LocalDate> getDistinctDatesAscending() {
        List<LocalDate> dates = new ArrayList<>();
        String sql = "SELECT DISTINCT session_date FROM study_sessions ORDER BY session_date ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                dates.add(rs.getDate("session_date").toLocalDate());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch distinct dates: " + e.getMessage(), e);
        }

        return dates;
    }

    private StudySession mapRow(ResultSet rs) throws SQLException {
        return new StudySession(
                rs.getString("id"),
                rs.getDate("session_date").toLocalDate(),
                rs.getInt("duration_minutes"),
                rs.getString("topic"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}