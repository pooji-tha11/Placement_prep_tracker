package com.placementtracker.achievement;

import com.placementtracker.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AchievementRepository {

    public void add(AchievementEntry entry) {
        String sql = "INSERT INTO achievements "
                + "(id, achievement_type, name, organizer, result, issuing_org, rank_label, "
                + "achievement_date, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, entry.getId());

            // Pattern-match the sealed type to pull out the right fields for each variant,
            // leaving the columns that don't apply to this type as NULL.
            switch (entry.getAchievement()) {
                case Hackathon(var name, var organizer, var date, var result) -> {
                    stmt.setString(2, "HACKATHON");
                    stmt.setString(3, name);
                    stmt.setString(4, organizer);
                    stmt.setString(5, result);
                    stmt.setString(6, null); // issuing_org — N/A for Hackathon
                    stmt.setString(7, null); // rank_label — N/A for Hackathon
                    stmt.setDate(8, java.sql.Date.valueOf(date));
                }
                case Certification(var name, var issuingOrg, var date) -> {
                    stmt.setString(2, "CERTIFICATION");
                    stmt.setString(3, name);
                    stmt.setString(4, null); // organizer — N/A for Certification
                    stmt.setString(5, null); // result — N/A for Certification
                    stmt.setString(6, issuingOrg);
                    stmt.setString(7, null); // rank_label — N/A for Certification
                    stmt.setDate(8, java.sql.Date.valueOf(date));
                }
                case CompetitionAward(var competitionName, var rank, var date) -> {
                    stmt.setString(2, "AWARD");
                    stmt.setString(3, competitionName);
                    stmt.setString(4, null); // organizer — N/A for Award
                    stmt.setString(5, null); // result — N/A for Award
                    stmt.setString(6, null); // issuing_org — N/A for Award
                    stmt.setString(7, rank);
                    stmt.setDate(8, java.sql.Date.valueOf(date));
                }
            }

            stmt.setTimestamp(9, java.sql.Timestamp.valueOf(entry.getCreatedAt()));

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save achievement: " + e.getMessage(), e);
        }
    }

    public List<AchievementEntry> getAll() {
        List<AchievementEntry> entries = new ArrayList<>();
        String sql = "SELECT * FROM achievements";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                entries.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch achievements: " + e.getMessage(), e);
        }

        return entries;
    }

    public AchievementEntry findById(String id) {
        String sql = "SELECT * FROM achievements WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch achievement: " + e.getMessage(), e);
        }

        return null;
    }

    public boolean deleteById(String id) {
        String sql = "DELETE FROM achievements WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete achievement: " + e.getMessage(), e);
        }
    }

    // Reconstructs the CORRECT sealed record type based on the discriminator column,
    // then wraps it in an AchievementEntry. This is the reverse direction of add() above.
    private AchievementEntry mapRow(ResultSet rs) throws SQLException {
        String type = rs.getString("achievement_type");
        LocalDate date = rs.getDate("achievement_date").toLocalDate();

        Achievement achievement = switch (type) {
            case "HACKATHON" -> new Hackathon(
                    rs.getString("name"),
                    rs.getString("organizer"),
                    date,
                    rs.getString("result")
            );
            case "CERTIFICATION" -> new Certification(
                    rs.getString("name"),
                    rs.getString("issuing_org"),
                    date
            );
            case "AWARD" -> new CompetitionAward(
                    rs.getString("name"),
                    rs.getString("rank_label"),
                    date
            );
            default -> throw new IllegalStateException("Unknown achievement_type in database: " + type);
        };

        return new AchievementEntry(
                rs.getString("id"),
                achievement,
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}