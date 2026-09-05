package com.placementtracker.reports;

import com.placementtracker.application.ApplicationStatus;
import com.placementtracker.application.ApplicationTracker;
import com.placementtracker.application.JobApplication;
import com.placementtracker.dsa.DSATracker;
import com.placementtracker.dsa.Difficulty;
import com.placementtracker.dsa.Problem;
import com.placementtracker.project.Project;
import com.placementtracker.project.ProjectStatus;
import com.placementtracker.project.ProjectTracker;
import com.placementtracker.study.StudyTracker;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnalyticsService {

    private final DSATracker dsaTracker;
    private final ProjectTracker projectTracker;
    private final ApplicationTracker applicationTracker;
    private final StudyTracker studyTracker;

    public AnalyticsService(DSATracker dsaTracker, ProjectTracker projectTracker,
                             ApplicationTracker applicationTracker, StudyTracker studyTracker) {
        this.dsaTracker = dsaTracker;
        this.projectTracker = projectTracker;
        this.applicationTracker = applicationTracker;
        this.studyTracker = studyTracker;
    }

    // ---------- DSA analytics ----------

    public Map<Difficulty, Long> getDifficultyDistribution() {
        return dsaTracker.viewAll().stream()
                .collect(Collectors.groupingBy(Problem::getDifficulty, Collectors.counting()));
    }

    public Map<String, Long> getTopicDistribution() {
        return dsaTracker.viewAll().stream()
                .collect(Collectors.groupingBy(Problem::getDsaTag, Collectors.counting()));
    }

    public List<String> getWeakTopics(int confidenceThreshold) {
        Map<String, Double> averageConfidenceByTag = dsaTracker.viewAll().stream()
                .collect(Collectors.groupingBy(Problem::getDsaTag,
                        Collectors.averagingInt(Problem::getConfidenceLevel)));

        return averageConfidenceByTag.entrySet().stream()
                .filter(entry -> entry.getValue() < confidenceThreshold)
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public double getAverageConfidence() {
        return dsaTracker.averageConfidence();
    }

    // ---------- Project analytics ----------

    public Map<ProjectStatus, Long> getProjectStatusBreakdown() {
        return projectTracker.viewAll().stream()
                .collect(Collectors.groupingBy(Project::getStatus, Collectors.counting()));
    }

    // ---------- Application analytics ----------

    public Map<ApplicationStatus, Long> getApplicationStatusBreakdown() {
        return applicationTracker.viewAll().stream()
                .collect(Collectors.groupingBy(JobApplication::getStatus, Collectors.counting()));
    }

    public double getInterviewConversionRate() {
        List<JobApplication> all = applicationTracker.viewAll();
        if (all.isEmpty()) {
            return 0.0;
        }
        long reachedInterviewOrBeyond = all.stream()
                .filter(a -> a.getStatus() == ApplicationStatus.INTERVIEW
                        || a.getStatus() == ApplicationStatus.SELECTED)
                .count();
        return (reachedInterviewOrBeyond * 100.0) / all.size();
    }

    // ---------- Study analytics (pass-through, no need to recompute) ----------

    public int getWeeklyStudyMinutes() {
        return studyTracker.weeklyMinutes();
    }

    public int getCurrentStreak() {
        return studyTracker.currentStreak();
    }

    public int getLongestStreak() {
        return studyTracker.longestStreak();
    }
}