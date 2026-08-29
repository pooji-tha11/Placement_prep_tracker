package com.placementtracker.achievement;

import java.time.LocalDate;
import java.util.List;

public class AchievementTracker {

    private final AchievementService service = new AchievementService();

    public AchievementEntry addHackathon(String name, String organizer, LocalDate date, String result) {
        return service.addHackathon(name, organizer, date, result);
    }

    public AchievementEntry addCertification(String name, String issuingOrg, LocalDate date) {
        return service.addCertification(name, issuingOrg, date);
    }

    public AchievementEntry addCompetitionAward(String competitionName, String rank, LocalDate date) {
        return service.addCompetitionAward(competitionName, rank, date);
    }

    public List<AchievementEntry> viewAll() {
        return service.listAll();
    }

    public AchievementEntry viewById(String id) {
        return service.findById(id);
    }

    public boolean removeAchievement(String id) {
        return service.deleteAchievement(id);
    }
}