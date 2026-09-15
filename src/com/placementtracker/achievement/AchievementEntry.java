package com.placementtracker.achievement;

import com.placementtracker.common.model.BaseEntry;

import java.time.LocalDateTime;

public class AchievementEntry extends BaseEntry {

    private final Achievement achievement;

    // Used when creating a brand-new achievement entry — generates a fresh UUID via BaseEntry.
    public AchievementEntry(Achievement achievement) {
        super("ACH");
        this.achievement = achievement;
        this.complete = true;
    }

    // Used when reconstructing an AchievementEntry from a database row.
    public AchievementEntry(String id, Achievement achievement, LocalDateTime createdAt) {
        super(id, true);
        this.achievement = achievement;
        this.complete = true;
    }

    public Achievement getAchievement() {
        return achievement;
    }

    @Override
    public String summary() {
        String detail = switch (achievement) {
            case Hackathon(var name, var organizer, var date, var result) ->
                    "Hackathon: " + name + " by " + organizer + " (" + date + ") — Result: " + result;
            case Certification(var name, var issuingOrg, var date) ->
                    "Certification: " + name + " from " + issuingOrg + " (" + date + ")";
            case CompetitionAward(var competitionName, var rank, var date) ->
                    "Award: " + rank + " place at " + competitionName + " (" + date + ")";
        };
        return "[" + getId() + "] " + detail;
    }
}