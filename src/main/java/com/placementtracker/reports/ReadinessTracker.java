package com.placementtracker.reports;

import com.placementtracker.achievement.AchievementTracker;
import com.placementtracker.application.ApplicationTracker;
import com.placementtracker.dsa.DSATracker;
import com.placementtracker.project.ProjectTracker;
import com.placementtracker.study.StudyTracker;

import java.util.Map;

@org.springframework.stereotype.Service
public class ReadinessTracker {

    private final ReadinessScoreService service;

    public ReadinessTracker(DSATracker dsaTracker, ProjectTracker projectTracker,
                             StudyTracker studyTracker, AchievementTracker achievementTracker,
                             ApplicationTracker applicationTracker) {
        this.service = new ReadinessScoreService(
                dsaTracker, projectTracker, studyTracker, achievementTracker, applicationTracker
        );
    }

    public double overallReadiness() {
        return service.getOverallReadiness();
    }

    public Map<String, Double> breakdown() {
        return service.getBreakdown();
    }
}