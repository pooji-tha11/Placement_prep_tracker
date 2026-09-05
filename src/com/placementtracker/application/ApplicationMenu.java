package com.placementtracker.application;

import com.placementtracker.common.exception.DuplicateApplicationException;
import com.placementtracker.common.exception.InvalidApplicationDataException;
import com.placementtracker.common.util.ConsoleUtil;
import com.placementtracker.resume.Resume;
import com.placementtracker.resume.ResumeTracker;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ApplicationMenu {

    private final Scanner scanner;
    private final ApplicationTracker tracker;

    public ApplicationMenu(Scanner scanner, ResumeTracker resumeTracker) {
        this.scanner = scanner;
        this.tracker = new ApplicationTracker(resumeTracker);
    }

    public void show() {
        boolean back = false;

        while (!back) {
            ConsoleUtil.printDivider();
            System.out.println("JOB & INTERNSHIP APPLICATION TRACKER");
            ConsoleUtil.printDivider();
            System.out.println("1. Add Application");
            System.out.println("2. View All Applications");
            System.out.println("3. Update Application Status");
            System.out.println("4. View Resume Used for an Application");
            System.out.println("5. Delete Application");
            System.out.println("6. Export Applications to File");
            System.out.println("7. Import Applications from File");
            System.out.println("0. Back to Main Menu");

            int choice = ConsoleUtil.readIntInRange(scanner, "Enter your choice: ", 0, 7);

            switch (choice) {
                case 1 -> addApplication();
                case 2 -> viewAll();
                case 3 -> updateStatus();
                case 4 -> viewResumeForApplication();
                case 5 -> deleteApplication();
                case 6 -> exportToFile();
                case 7 -> importFromFile();
                case 0 -> back = true;
            }
        }
    }

    private void addApplication() {
        String company = ConsoleUtil.readNonEmptyLine(scanner, "Company: ");
        String role = ConsoleUtil.readNonEmptyLine(scanner, "Role: ");

        LocalDate dateApplied = null;
        while (dateApplied == null) {
            String raw = ConsoleUtil.readNonEmptyLine(scanner, "Date Applied (YYYY-MM-DD): ");
            dateApplied = com.placementtracker.common.util.InputValidator.parseDateOrNull(raw);
            if (dateApplied == null) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }
        }

        ApplicationStatus status = null;
        while (status == null) {
            String raw = ConsoleUtil.readNonEmptyLine(
                    scanner, "Status (SAVED/APPLIED/ASSESSMENT/INTERVIEW/REJECTED/SELECTED): ");
            try {
                status = ApplicationStatus.valueOf(raw.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid status. Please enter one of the listed options.");
            }
        }

        System.out.print("Job Link (optional, press Enter to skip): ");
        String jobLink = scanner.nextLine().trim();

        System.out.print("Job Description (optional, press Enter to skip): ");
        String jobDescription = scanner.nextLine().trim();

        System.out.print("Required Skills (comma-separated, optional): ");
        String skillsRaw = scanner.nextLine().trim();
        List<String> requiredSkills = new ArrayList<>();
        if (!skillsRaw.isEmpty()) {
            for (String skill : skillsRaw.split(",")) {
                if (!skill.trim().isEmpty()) {
                    requiredSkills.add(skill.trim());
                }
            }
        }

        System.out.print("Notes (optional, press Enter to skip): ");
        String notes = scanner.nextLine().trim();

        String resumeId = ConsoleUtil.readNonEmptyLine(scanner, "Resume ID used for this application: ");

        try {
            JobApplication application = tracker.addApplication(
                    company, role, dateApplied, status, jobLink, jobDescription, requiredSkills, notes, resumeId
            );
            System.out.println("Application added successfully:");
            System.out.println(application.summary());
        } catch (InvalidApplicationDataException | DuplicateApplicationException e) {
            System.out.println("Could not add application: " + e.getMessage());
        }
    }

    private void viewAll() {
        List<JobApplication> applications = tracker.viewAll();
        if (applications.isEmpty()) {
            System.out.println("No applications found.");
            return;
        }
        for (JobApplication a : applications) {
            System.out.println(a.summary());
            ConsoleUtil.printDivider();
        }
    }

    private void updateStatus() {
        String id = ConsoleUtil.readNonEmptyLine(scanner, "Enter Application ID: ");

        ApplicationStatus status = null;
        while (status == null) {
            String raw = ConsoleUtil.readNonEmptyLine(
                    scanner, "New Status (SAVED/APPLIED/ASSESSMENT/INTERVIEW/REJECTED/SELECTED): ");
            try {
                status = ApplicationStatus.valueOf(raw.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid status. Please enter one of the listed options.");
            }
        }

        boolean updated = tracker.updateStatus(id, status);
        System.out.println(updated ? "Status updated." : "No application found with that ID.");
    }

    private void viewResumeForApplication() {
        String id = ConsoleUtil.readNonEmptyLine(scanner, "Enter Application ID: ");
        JobApplication application = tracker.viewById(id);

        if (application == null) {
            System.out.println("No application found with that ID.");
            return;
        }

        Resume resume = tracker.getResumeForApplication(id);
        if (resume == null) {
            System.out.println("This application references a resume that no longer exists.");
            return;
        }

        System.out.println("Resume used for this application:");
        System.out.println(resume.summary());
    }

    private void deleteApplication() {
        String id = ConsoleUtil.readNonEmptyLine(scanner, "Enter Application ID to delete: ");
        boolean removed = tracker.removeApplication(id);
        System.out.println(removed ? "Application deleted." : "No application found with that ID.");
    }
        private void exportToFile() {
        String path = ConsoleUtil.readNonEmptyLine(scanner, "File path to export to (e.g. applications.csv): ");
        try {
            tracker.exportToCSV(path);
            System.out.println("Applications exported successfully to " + path);
        } catch (java.io.IOException e) {
            System.out.println("Export failed: " + e.getMessage());
        }
    }

    private void importFromFile() {
        String path = ConsoleUtil.readNonEmptyLine(scanner, "File path to import from (e.g. applications.csv): ");
        try {
            List<String> report = tracker.importFromCSV(path);
            if (report.isEmpty()) {
                System.out.println("No rows found to import.");
            } else {
                report.forEach(System.out::println);
            }
        } catch (java.io.IOException e) {
            System.out.println("Import failed: " + e.getMessage());
        }
    }
}