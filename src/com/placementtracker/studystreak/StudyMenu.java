package com.placementtracker.studystreak;

import com.placementtracker.common.util.ConsoleUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class StudyMenu {

    private final Scanner scanner;
    private final StudyTracker tracker = new StudyTracker();

    public StudyMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public void show() {
        boolean back = false;

        while (!back) {
            ConsoleUtil.printDivider();
            System.out.println("STUDY STREAK TRACKER");
            ConsoleUtil.printDivider();
            System.out.println("1. Log Study Session");
            System.out.println("2. View All Sessions");
            System.out.println("3. View Current Streak");
            System.out.println("4. View Longest Streak");
            System.out.println("5. View Weekly Study Summary");
            System.out.println("0. Back to Main Menu");

            int choice = ConsoleUtil.readIntInRange(scanner, "Enter your choice: ", 0, 5);

            switch (choice) {
                case 1 -> logSession();
                case 2 -> viewAll();
                case 3 -> viewCurrentStreak();
                case 4 -> viewLongestStreak();
                case 5 -> viewWeeklySummary();
                case 0 -> back = true;
            }
        }
    }

    private void logSession() {
        LocalDate date = null;
        while (date == null) {
            System.out.print("Date (YYYY-MM-DD, press Enter for today): ");
            String raw = scanner.nextLine().trim();
            if (raw.isEmpty()) {
                date = LocalDate.now();
            } else {
                date = com.placementtracker.common.util.InputValidator.parseDateOrNull(raw);
                if (date == null) {
                    System.out.println("Invalid date format. Please use YYYY-MM-DD.");
                }
            }
        }

        int duration = ConsoleUtil.readIntInRange(scanner, "Duration (minutes): ", 1, 1440);
        String topic = ConsoleUtil.readNonEmptyLine(scanner, "Topic: ");

        try {
            StudySession session = tracker.logSession(date, duration, topic);
            System.out.println("Session logged successfully:");
            System.out.println(session.summary());
        } catch (IllegalArgumentException e) {
            System.out.println("Could not log session: " + e.getMessage());
        }
    }

    private void viewAll() {
        List<StudySession> sessions = tracker.viewAll();
        if (sessions.isEmpty()) {
            System.out.println("No sessions logged.");
            return;
        }
        for (StudySession s : sessions) {
            System.out.println(s.summary());
        }
    }

    private void viewCurrentStreak() {
        System.out.println("Current streak: " + tracker.currentStreak() + " day(s)");
    }

    private void viewLongestStreak() {
        System.out.println("Longest streak: " + tracker.longestStreak() + " day(s)");
    }

    private void viewWeeklySummary() {
        System.out.println("Total study time this week: " + tracker.weeklyMinutes() + " minutes");
    }
}