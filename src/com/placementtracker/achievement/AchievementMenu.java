package com.placementtracker.achievement;

import com.placementtracker.common.util.ConsoleUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class AchievementMenu {

    private final Scanner scanner;
    private final AchievementTracker tracker = new AchievementTracker();

    public AchievementMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public void show() {
        boolean back = false;

        while (!back) {
            ConsoleUtil.printDivider();
            System.out.println("HACKATHON & CERTIFICATION TRACKER");
            ConsoleUtil.printDivider();
            System.out.println("1. Add Hackathon");
            System.out.println("2. Add Certification");
            System.out.println("3. Add Competition Award");
            System.out.println("4. View All Achievements");
            System.out.println("5. Delete Achievement");
            System.out.println("0. Back to Main Menu");

            int choice = ConsoleUtil.readIntInRange(scanner, "Enter your choice: ", 0, 5);

            switch (choice) {
                case 1 -> addHackathon();
                case 2 -> addCertification();
                case 3 -> addCompetitionAward();
                case 4 -> viewAll();
                case 5 -> deleteAchievement();
                case 0 -> back = true;
            }
        }
    }

    private void addHackathon() {
        String name = ConsoleUtil.readNonEmptyLine(scanner, "Hackathon Name: ");
        String organizer = ConsoleUtil.readNonEmptyLine(scanner, "Organizer: ");
        LocalDate date = readDate();
        String result = ConsoleUtil.readNonEmptyLine(scanner, "Result (e.g. Winner, Finalist): ");

        try {
            AchievementEntry entry = tracker.addHackathon(name, organizer, date, result);
            System.out.println("Hackathon added successfully:");
            System.out.println(entry.summary());
        } catch (IllegalArgumentException e) {
            System.out.println("Could not add hackathon: " + e.getMessage());
        }
    }

    private void addCertification() {
        String name = ConsoleUtil.readNonEmptyLine(scanner, "Certification Name: ");
        String issuingOrg = ConsoleUtil.readNonEmptyLine(scanner, "Issuing Organization: ");
        LocalDate date = readDate();

        try {
            AchievementEntry entry = tracker.addCertification(name, issuingOrg, date);
            System.out.println("Certification added successfully:");
            System.out.println(entry.summary());
        } catch (IllegalArgumentException e) {
            System.out.println("Could not add certification: " + e.getMessage());
        }
    }

    private void addCompetitionAward() {
        String competitionName = ConsoleUtil.readNonEmptyLine(scanner, "Competition Name: ");
        String rank = ConsoleUtil.readNonEmptyLine(scanner, "Rank (e.g. 1st, 2nd): ");
        LocalDate date = readDate();

        try {
            AchievementEntry entry = tracker.addCompetitionAward(competitionName, rank, date);
            System.out.println("Competition award added successfully:");
            System.out.println(entry.summary());
        } catch (IllegalArgumentException e) {
            System.out.println("Could not add competition award: " + e.getMessage());
        }
    }

    private void viewAll() {
        List<AchievementEntry> entries = tracker.viewAll();
        if (entries.isEmpty()) {
            System.out.println("No achievements found.");
            return;
        }
        for (AchievementEntry e : entries) {
            System.out.println(e.summary());
        }
    }

    private void deleteAchievement() {
        String id = ConsoleUtil.readNonEmptyLine(scanner, "Enter Achievement ID to delete: ");
        boolean removed = tracker.removeAchievement(id);
        System.out.println(removed ? "Achievement deleted." : "No achievement found with that ID.");
    }

    private LocalDate readDate() {
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
        return date;
    }
}