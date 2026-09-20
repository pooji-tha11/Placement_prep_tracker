package com.placementtracker.reports;

import com.placementtracker.application.ApplicationStatus;
import com.placementtracker.dsa.Difficulty;
import com.placementtracker.project.ProjectStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportTracker tracker;
    private final ReadinessTracker readinessTracker;

    public ReportController(ReportTracker tracker, ReadinessTracker readinessTracker) {
        this.tracker = tracker;
        this.readinessTracker = readinessTracker;
    }

    @GetMapping("/dsa/difficulty")
    public Map<Difficulty, Long> difficultyDistribution() {
        return tracker.difficultyDistribution();
    }

    @GetMapping("/dsa/topics")
    public Map<String, Long> topicDistribution() {
        return tracker.topicDistribution();
    }

    @GetMapping("/dsa/weak-topics")
    public List<String> weakTopics(@RequestParam(defaultValue = "3") int threshold) {
        return tracker.weakTopics(threshold);
    }

    @GetMapping("/projects/status")
    public Map<ProjectStatus, Long> projectStatusBreakdown() {
        return tracker.projectStatusBreakdown();
    }

    @GetMapping("/applications/status")
    public Map<ApplicationStatus, Long> applicationStatusBreakdown() {
        return tracker.applicationStatusBreakdown();
    }

    @GetMapping("/applications/conversion-rate")
    public double interviewConversionRate() {
        return tracker.interviewConversionRate();
    }

    @GetMapping("/study/summary")
    public Map<String, Object> studySummary() {
        return Map.of(
                "weeklyMinutes", tracker.weeklyStudyMinutes(),
                "currentStreak", tracker.currentStreak(),
                "longestStreak", tracker.longestStreak()
        );
    }

    @GetMapping("/readiness")
    public Map<String, Object> readiness() {
        return Map.of(
                "overall", readinessTracker.overallReadiness(),
                "breakdown", readinessTracker.breakdown()
        );
    }
}
