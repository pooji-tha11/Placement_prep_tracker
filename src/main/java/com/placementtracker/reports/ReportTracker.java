package com.placementtracker.reports;

import com.placementtracker.application.ApplicationStatus;
import com.placementtracker.application.ApplicationTracker;
import com.placementtracker.dsa.DSATracker;
import com.placementtracker.dsa.Difficulty;
import com.placementtracker.project.ProjectStatus;
import com.placementtracker.project.ProjectTracker;


import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Service
public class ReportTracker {

    private final AnalyticsService service;

    public ReportTracker(DSATracker dsaTracker, ProjectTracker projectTracker,
                          ApplicationTracker applicationTracker) {
        this.service = new AnalyticsService(dsaTracker, projectTracker, applicationTracker);
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


}