package com.placementtracker.reports;

import com.placementtracker.achievement.AchievementTracker;
import com.placementtracker.application.ApplicationTracker;
import com.placementtracker.dsa.DSATracker;
import com.placementtracker.project.Project;
import com.placementtracker.project.ProjectStatus;
import com.placementtracker.project.ProjectTracker;
import com.placementtracker.study.StudyTracker;

import java.util.LinkedHashMap;
import java.util.Map;

public class ReadinessScoreService {

    // Known simplification: fixed targets for now. A future improvement is to pull
    // personalized targets from an active Goal for each category (via GoalTracker),
    // falling back to these defaults when no goal exists for that category.
    private static final int DSA_TARGET = 100;
    private static final int PROJECT_TARGET = 2;
    private static final int STUDY_STREAK_TARGET = 30;
    private static final int ACHIEVEMENT_TARGET = 5;
    private static final int APPLICATION_TARGET = 20;

    // Category weights must sum to 1.0
    private static final double DSA_WEIGHT = 0.30;
    private static final double PROJECT_WEIGHT = 0.20;
    private static final double APPLICATION_WEIGHT = 0.20;
    private static final double STUDY_WEIGHT = 0.15;
    private static final double ACHIEVEMENT_WEIGHT = 0.15;

    private final DSATracker dsaTracker;
    private final ProjectTracker projectTracker;
    private final StudyTracker studyTracker;
    private final AchievementTracker achievementTracker;
    private final ApplicationTracker applicationTracker;

    public ReadinessScoreService(DSATracker dsaTracker, ProjectTracker projectTracker,
                                  StudyTracker studyTracker, AchievementTracker achievementTracker,
                                  ApplicationTracker applicationTracker) {
        this.dsaTracker = dsaTracker;
        this.projectTracker = projectTracker;
        this.studyTracker = studyTracker;
        this.achievementTracker = achievementTracker;
        this.applicationTracker = applicationTracker;
    }

    public double getDsaScore() {
        int solved = dsaTracker.viewAll().size();
        return cappedPercentage(solved, DSA_TARGET);
    }

    public double getProjectScore() {
        long completed = projectTracker.viewAll().stream()
                .filter(p -> p.getStatus() == ProjectStatus.COMPLETED)
                .count();
        return cappedPercentage((int) completed, PROJECT_TARGET);
    }

    public double getStudyScore() {
        int longestStreak = studyTracker.longestStreak();
        return cappedPercentage(longestStreak, STUDY_STREAK_TARGET);
    }

    public double getAchievementScore() {
        int count = achievementTracker.viewAll().size();
        return cappedPercentage(count, ACHIEVEMENT_TARGET);
    }

    public double getApplicationScore() {
        int count = applicationTracker.viewAll().size();
        return cappedPercentage(count, APPLICATION_TARGET);
    }

    public double getOverallReadiness() {
        return (getDsaScore() * DSA_WEIGHT)
                + (getProjectScore() * PROJECT_WEIGHT)
                + (getApplicationScore() * APPLICATION_WEIGHT)
                + (getStudyScore() * STUDY_WEIGHT)
                + (getAchievementScore() * ACHIEVEMENT_WEIGHT);
    }

    public Map<String, Double> getBreakdown() {
        Map<String, Double> breakdown = new LinkedHashMap<>();
        breakdown.put("DSA", getDsaScore());
        breakdown.put("Projects", getProjectScore());
        breakdown.put("Applications", getApplicationScore());
        breakdown.put("Study", getStudyScore());
        breakdown.put("Achievements", getAchievementScore());
        return breakdown;
    }

    private double cappedPercentage(int actual, int target) {
        if (target <= 0) {
            return 0.0;
        }
        return Math.min(100.0, (actual * 100.0) / target);
    }
}