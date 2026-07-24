package com.placementtracker.dsa;

import com.placementtracker.common.util.ConsoleUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class DSAMenu {

    private final Scanner scanner;
    private final DSATracker tracker = new DSATracker();

    public DSAMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public void show() {
        boolean back = false;

        while (!back) {
            ConsoleUtil.printDivider();
            System.out.println("COMPETITIVE PROGRAMMING TRACKER");
            ConsoleUtil.printDivider();
            System.out.println("1. Add Problem");
            System.out.println("2. View All Problems");
            System.out.println("3. Filter by Tag");
            System.out.println("4. Filter by Difficulty");
            System.out.println("5. Delete Problem");
            System.out.println("6. View Average Confidence");
            System.out.println("0. Back to Main Menu");

            int choice = ConsoleUtil.readIntInRange(scanner, "Enter your choice: ", 0, 6);

            switch (choice) {
                case 1 -> addProblem();
                case 2 -> viewAll();
                case 3 -> filterByTag();
                case 4 -> filterByDifficulty();
                case 5 -> deleteProblem();
                case 6 -> viewAverageConfidence();
                case 0 -> back = true;
            }
        }
    }

    private void addProblem() {
        String platform = ConsoleUtil.readNonEmptyLine(scanner, "Platform (e.g. LeetCode): ");
        String tag = ConsoleUtil.readNonEmptyLine(scanner, "DSA Tag (e.g. Dynamic Programming): ");

        Difficulty difficulty = null;
        while (difficulty == null) {
            String raw = ConsoleUtil.readNonEmptyLine(scanner, "Difficulty (EASY/MEDIUM/HARD): ");
            difficulty = DSAValidator.parseDifficulty(raw);
            if (difficulty == null) {
                System.out.println("Invalid difficulty. Please enter EASY, MEDIUM, or HARD.");
            }
        }

        int confidence = ConsoleUtil.readIntInRange(scanner, "Confidence Level (1-5): ", 1, 5);

        try {
            Problem problem = tracker.addProblem(platform, tag, difficulty, confidence, LocalDate.now());
            System.out.println("Problem added successfully:");
            System.out.println(problem.summary());
        } catch (IllegalArgumentException e) {
            System.out.println("Could not add problem: " + e.getMessage());
        }
    }

    private void viewAll() {
        List<Problem> problems = tracker.viewAll();
        printList(problems);
    }

    private void filterByTag() {
        String tag = ConsoleUtil.readNonEmptyLine(scanner, "Enter tag to filter by: ");
        printList(tracker.viewByTag(tag));
    }

    private void filterByDifficulty() {
        Difficulty difficulty = null;
        while (difficulty == null) {
            String raw = ConsoleUtil.readNonEmptyLine(scanner, "Difficulty (EASY/MEDIUM/HARD): ");
            difficulty = DSAValidator.parseDifficulty(raw);
            if (difficulty == null) {
                System.out.println("Invalid difficulty. Please enter EASY, MEDIUM, or HARD.");
            }
        }
        printList(tracker.viewByDifficulty(difficulty));
    }

    private void deleteProblem() {
        String id = ConsoleUtil.readNonEmptyLine(scanner, "Enter Problem ID to delete: ");
        boolean removed = tracker.removeProblem(id);
        System.out.println(removed ? "Problem deleted." : "No problem found with that ID.");
    }

    private void viewAverageConfidence() {
        System.out.printf("Average confidence across all problems: %.2f / 5%n", tracker.averageConfidence());
    }

    private void printList(List<Problem> problems) {
        if (problems.isEmpty()) {
            System.out.println("No problems found.");
            return;
        }
        for (Problem p : problems) {
            System.out.println(p.summary());
        }
    }
}