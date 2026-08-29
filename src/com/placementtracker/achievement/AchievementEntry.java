package com.placementtracker.achievement;

import com.placementtracker.common.model.BaseEntry;

public class AchievementEntry extends BaseEntry {

    private final Achievement achievement;

    public AchievementEntry(Achievement achievement) {
        super("ACH");
        this.achievement = achievement;
        this.complete = true; // a recorded achievement is complete by definition
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