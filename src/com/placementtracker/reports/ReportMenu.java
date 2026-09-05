package com.placementtracker.reports;

import com.placementtracker.application.ApplicationStatus;
import com.placementtracker.application.ApplicationTracker;
import com.placementtracker.common.util.ConsoleUtil;
import com.placementtracker.dsa.DSATracker;
import com.placementtracker.dsa.Difficulty;
import com.placementtracker.project.ProjectStatus;
import com.placementtracker.project.ProjectTracker;
import com.placementtracker.study.StudyTracker;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ReportMenu {

    private final Scanner scanner;
    private final ReportTracker tracker;

    public ReportMenu(Scanner scanner, DSATracker dsaTracker, ProjectTracker projectTracker,
                       ApplicationTracker applicationTracker, StudyTracker studyTracker) {
        this.scanner = scanner;
        this.tracker = new ReportTracker(dsaTracker, projectTracker, applicationTracker, studyTracker);
    }

    public void show() {
        boolean back = false;

        while (!back) {
            ConsoleUtil.printDivider();
            System.out.println("REPORTS & ANALYTICS");
            ConsoleUtil.printDivider();
            System.out.println("1. DSA Analytics");
            System.out.println("2. Project Analytics");
            System.out.println("3. Application Analytics");
            System.out.println("4. Study Analytics");
            System.out.println("0. Back to Main Menu");

            int choice = ConsoleUtil.readIntInRange(scanner, "Enter your choice: ", 0, 4);

            switch (choice) {
                case 1 -> showDsaAnalytics();
                case 2 -> showProjectAnalytics();
                case 3 -> showApplicationAnalytics();
                case 4 -> showStudyAnalytics();
                case 0 -> back = true;
            }
        }
    }

    private void showDsaAnalytics() {
        ConsoleUtil.printDivider();
        System.out.println("DSA Analytics");
        ConsoleUtil.printDivider();

        Map<Difficulty, Long> difficultyDist = tracker.difficultyDistribution();
        long maxDifficultyCount = difficultyDist.values().stream().mapToLong(Long::longValue).max().orElse(1);
        System.out.println("Difficulty Distribution:");
        for (Difficulty d : Difficulty.values()) {
            printBar(d.toString(), difficultyDist.getOrDefault(d, 0L), maxDifficultyCount);
        }

        Map<String, Long> topicDist = tracker.topicDistribution();
        long maxTopicCount = topicDist.values().stream().mapToLong(Long::longValue).max().orElse(1);
        System.out.println("\nTopic Distribution:");
        for (Map.Entry<String, Long> entry : topicDist.entrySet()) {
            printBar(entry.getKey(), entry.getValue(), maxTopicCount);
        }

        System.out.printf("%nAverage Confidence: %.2f / 5%n", tracker.averageConfidence());

        List<String> weakTopics = tracker.weakTopics(3);
        System.out.println("\nWeak Topics (average confidence below 3):");
        if (weakTopics.isEmpty()) {
            System.out.println("  None — no weak areas detected.");
        } else {
            weakTopics.forEach(topic -> System.out.println("  - " + topic));
        }
    }

    private void showProjectAnalytics() {
        ConsoleUtil.printDivider();
        System.out.println("Project Analytics");
        ConsoleUtil.printDivider();

        Map<ProjectStatus, Long> statusDist = tracker.projectStatusBreakdown();
        long max = statusDist.values().stream().mapToLong(Long::longValue).max().orElse(1);
        for (ProjectStatus s : ProjectStatus.values()) {
            printBar(s.toString(), statusDist.getOrDefault(s, 0L), max);
        }
    }

    private void showApplicationAnalytics() {
        ConsoleUtil.printDivider();
        System.out.println("Application Analytics");
        ConsoleUtil.printDivider();

        Map<ApplicationStatus, Long> statusDist = tracker.applicationStatusBreakdown();
        long max = statusDist.values().stream().mapToLong(Long::longValue).max().orElse(1);
        for (ApplicationStatus s : ApplicationStatus.values()) {
            printBar(s.toString(), statusDist.getOrDefault(s, 0L), max);
        }

        System.out.printf("%nInterview Conversion Rate: %.1f%%%n", tracker.interviewConversionRate());
    }

    private void showStudyAnalytics() {
        ConsoleUtil.printDivider();
        System.out.println("Study Analytics");
        ConsoleUtil.printDivider();
        System.out.println("Weekly Study Time: " + tracker.weeklyStudyMinutes() + " minutes");
        System.out.println("Current Streak: " + tracker.currentStreak() + " day(s)");
        System.out.println("Longest Streak: " + tracker.longestStreak() + " day(s)");
    }

    private void printBar(String label, long count, long maxCount) {
        int barLength = maxCount == 0 ? 0 : (int) ((count * 20.0) / maxCount);
        String bar = "#".repeat(Math.max(barLength, count > 0 ? 1 : 0));
        System.out.printf("  %-20s %s (%d)%n", label, bar, count);
    }
}