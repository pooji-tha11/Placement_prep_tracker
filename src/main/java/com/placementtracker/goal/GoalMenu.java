package com.placementtracker.goal;

import com.placementtracker.common.util.ConsoleUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class GoalMenu {

    private final Scanner scanner;
    private final GoalTracker tracker = new GoalTracker();

    public GoalMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public void show() {
        boolean back = false;

        while (!back) {
            ConsoleUtil.printDivider();
            System.out.println("GOAL SETTING");
            ConsoleUtil.printDivider();
            System.out.println("1. Create Goal");
            System.out.println("2. Update Progress");
            System.out.println("3. View All Goals");
            System.out.println("4. Delete Goal");
            System.out.println("0. Back to Main Menu");

            int choice = ConsoleUtil.readIntInRange(scanner, "Enter your choice: ", 0, 4);

            switch (choice) {
                case 1 -> createGoal();
                case 2 -> updateProgress();
                case 3 -> viewAll();
                case 4 -> deleteGoal();
                case 0 -> back = true;
            }
        }
    }

    private void createGoal() {
        String description = ConsoleUtil.readNonEmptyLine(
                scanner, "Goal Description (e.g. Solve 100 DSA problems): ");
        int targetCount = ConsoleUtil.readIntInRange(scanner, "Target Count: ", 1, 100000);

        LocalDate deadline = null;
        while (deadline == null) {
            String raw = ConsoleUtil.readNonEmptyLine(scanner, "Deadline (YYYY-MM-DD): ");
            deadline = com.placementtracker.common.util.InputValidator.parseDateOrNull(raw);
            if (deadline == null) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            } else if (deadline.isBefore(LocalDate.now())) {
                System.out.println("Deadline cannot be in the past.");
                deadline = null;
            }
        }

        try {
            Goal<?> goal = tracker.createGoal(description, targetCount, deadline);
            System.out.println("Goal created successfully:");
            System.out.println(goal.summary());
        } catch (IllegalArgumentException e) {
            System.out.println("Could not create goal: " + e.getMessage());
        }
    }

    private void updateProgress() {
        String id = ConsoleUtil.readNonEmptyLine(scanner, "Enter Goal ID: ");
        System.out.println("Tip: check the relevant tracker module for your current count first.");
        int currentCount = ConsoleUtil.readIntInRange(scanner, "Current Count: ", 0, 100000);

        try {
            boolean updated = tracker.updateProgress(id, currentCount);
            System.out.println(updated ? "Progress updated." : "No goal found with that ID.");
        } catch (IllegalArgumentException e) {
            System.out.println("Could not update progress: " + e.getMessage());
        }
    }

    private void viewAll() {
        List<Goal<?>> goals = tracker.viewAll();
        if (goals.isEmpty()) {
            System.out.println("No goals set.");
            return;
        }
        for (Goal<?> g : goals) {
            System.out.println(g.summary());
        }
    }

    private void deleteGoal() {
        String id = ConsoleUtil.readNonEmptyLine(scanner, "Enter Goal ID to delete: ");
        boolean removed = tracker.removeGoal(id);
        System.out.println(removed ? "Goal deleted." : "No goal found with that ID.");
    }
}