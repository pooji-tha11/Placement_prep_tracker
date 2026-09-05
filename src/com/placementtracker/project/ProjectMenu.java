package com.placementtracker.project;

import com.placementtracker.common.exception.IncompleteStarFormException;
import com.placementtracker.common.util.ConsoleUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProjectMenu {

    private final Scanner scanner;
    private final ProjectTracker tracker;

    public ProjectMenu(Scanner scanner, ProjectTracker tracker) {
        this.scanner = scanner;
        this.tracker = tracker;
    }

    public void show() {
        boolean back = false;

        while (!back) {
            ConsoleUtil.printDivider();
            System.out.println("PROJECT TRACKER");
            ConsoleUtil.printDivider();
            System.out.println("1. Add Project");
            System.out.println("2. View All Projects");
            System.out.println("3. Delete Project");
            System.out.println("4. STAR Intake Wizard (Interview-Ready Portfolio)");
            System.out.println("5. Advanced Search");
            System.out.println("0. Back to Main Menu");

            int choice = ConsoleUtil.readIntInRange(scanner, "Enter your choice: ", 0, 5);

            switch (choice) {
                case 1 -> addProject();
                case 2 -> viewAll();
                case 3 -> deleteProject();
                case 4 -> runStarIntake();
                case 5 -> advancedSearch();
                case 0 -> back = true;
            }
        }
    }

    private void addProject() {
        String title = ConsoleUtil.readNonEmptyLine(scanner, "Project Title: ");
        String domain = ConsoleUtil.readNonEmptyLine(scanner, "Domain (e.g. Web, ML, Mobile): ");

        String techStackRaw = ConsoleUtil.readNonEmptyLine(scanner, "Tech Stack (comma-separated): ");
        List<String> techStack = new ArrayList<>();
        for (String tech : techStackRaw.split(",")) {
            if (!tech.trim().isEmpty()) {
                techStack.add(tech.trim());
            }
        }

        String repoLink = ConsoleUtil.readNonEmptyLine(scanner, "Repository Link: ");

        ProjectStatus status = null;
        while (status == null) {
            String raw = ConsoleUtil.readNonEmptyLine(scanner, "Status (PLANNED/IN_PROGRESS/COMPLETED): ");
            try {
                status = ProjectStatus.valueOf(raw.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid status. Please enter PLANNED, IN_PROGRESS, or COMPLETED.");
            }
        }

        try {
            Project project = tracker.addProject(title, domain, techStack, repoLink, status);
            System.out.println("Project added successfully:");
            System.out.println(project.summary());
        } catch (IllegalArgumentException e) {
            System.out.println("Could not add project: " + e.getMessage());
        }
    }

    private void viewAll() {
        List<Project> projects = tracker.viewAll();
        if (projects.isEmpty()) {
            System.out.println("No projects found.");
            return;
        }
        for (Project p : projects) {
            System.out.println(p.summary());
            ConsoleUtil.printDivider();
        }
    }

    private void deleteProject() {
        String id = ConsoleUtil.readNonEmptyLine(scanner, "Enter Project ID to delete: ");
        boolean removed = tracker.removeProject(id);
        System.out.println(removed ? "Project deleted." : "No project found with that ID.");
    }

    private void runStarIntake() {
        String id = ConsoleUtil.readNonEmptyLine(scanner, "Enter Project ID for STAR intake: ");
        Project project = tracker.viewById(id);
        if (project == null) {
            System.out.println("No project found with that ID.");
            return;
        }

        System.out.println("Starting STAR Intake Wizard for: " + project.getTitle());

        boolean submitted = false;
        while (!submitted) {
            String situation = ConsoleUtil.readNonEmptyLine(scanner, "Step 1/4 — Situation: ");
            String task = ConsoleUtil.readNonEmptyLine(scanner, "Step 2/4 — Task: ");
            String action = ConsoleUtil.readNonEmptyLine(scanner, "Step 3/4 — Action: ");
            String result = ConsoleUtil.readNonEmptyLine(scanner, "Step 4/4 — Result: ");

            StarForm form = new StarForm(situation, task, action, result);

            try {
                tracker.submitStarForm(id, form);
                System.out.println("STAR form submitted successfully. Project is now interview-ready.");
                submitted = true;
            } catch (IncompleteStarFormException e) {
                System.out.println("Submission failed: " + e.getMessage());
                System.out.println("Let's redo the wizard for this project.");
            }
        }
    }
        private void advancedSearch() {
        System.out.println("Leave any field blank to skip that filter.");

        System.out.print("Domain (optional): ");
        String domain = scanner.nextLine().trim();
        if (domain.isEmpty()) domain = null;

        System.out.print("Technology (optional): ");
        String technology = scanner.nextLine().trim();
        if (technology.isEmpty()) technology = null;

        List<Project> results = tracker.advancedSearch(domain, technology);
        if (results.isEmpty()) {
            System.out.println("No projects matched.");
            return;
        }
        for (Project p : results) {
            System.out.println(p.summary());
            ConsoleUtil.printDivider();
        }
    }
}