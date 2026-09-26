package com.placementtracker.reports;

import com.placementtracker.achievement.AchievementTracker;
import com.placementtracker.application.ApplicationTracker;
import com.placementtracker.dsa.DSATracker;
import com.placementtracker.project.ProjectTracker;


import java.util.Map;

@org.springframework.stereotype.Service
public class ReadinessTracker {

    private final ReadinessScoreService service;

    public ReadinessTracker(DSATracker dsaTracker, ProjectTracker projectTracker,
                             AchievementTracker achievementTracker,
                             ApplicationTracker applicationTracker) {
        this.service = new ReadinessScoreService(
                dsaTracker, projectTracker, achievementTracker, applicationTracker
        );
    }

    public double overallReadiness() {
        return service.getOverallReadiness();
    }

    public Map<String, Double> breakdown() {
        return service.getBreakdown();
    }
}