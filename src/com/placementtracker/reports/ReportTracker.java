package com.placementtracker.reports;

import com.placementtracker.application.ApplicationStatus;
import com.placementtracker.application.ApplicationTracker;
import com.placementtracker.dsa.DSATracker;
import com.placementtracker.dsa.Difficulty;
import com.placementtracker.project.ProjectStatus;
import com.placementtracker.project.ProjectTracker;
import com.placementtracker.study.StudyTracker;

import java.util.List;
import java.util.Map;

public class ReportTracker {

    private final AnalyticsService service;

    public ReportTracker(DSATracker dsaTracker, ProjectTracker projectTracker,
                          ApplicationTracker applicationTracker, StudyTracker studyTracker) {
        this.service = new AnalyticsService(dsaTracker, projectTracker, applicationTracker, studyTracker);
    }

    public Map<Difficulty, Long> difficultyDistribution() {
        return service.getDifficultyDistribution();
    }

    public Map<String, Long> topicDistribution() {
        return service.getTopicDistribution();
    }

    public List<String> weakTopics(int confidenceThreshold) {
        return service.getWeakTopics(confidenceThreshold);
    }

    public double averageConfidence() {
        return service.getAverageConfidence();
    }

    public Map<ProjectStatus, Long> projectStatusBreakdown() {
        return service.getProjectStatusBreakdown();
    }

    public Map<ApplicationStatus, Long> applicationStatusBreakdown() {
        return service.getApplicationStatusBreakdown();
    }

    public double interviewConversionRate() {
        return service.getInterviewConversionRate();
    }

    public int weeklyStudyMinutes() {
        return service.getWeeklyStudyMinutes();
    }

    public int currentStreak() {
        return service.getCurrentStreak();
    }

    public int longestStreak() {
        return service.getLongestStreak();
    }
}