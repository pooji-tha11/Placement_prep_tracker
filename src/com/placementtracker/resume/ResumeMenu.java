package com.placementtracker.resume;

import com.placementtracker.common.exception.DuplicateResumeException;
import com.placementtracker.common.util.ConsoleUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ResumeMenu {

    private final Scanner scanner;
    private final ResumeTracker tracker = new ResumeTracker();

    public ResumeMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public void show() {
        boolean back = false;

        while (!back) {
            ConsoleUtil.printDivider();
            System.out.println("RESUME MANAGER");
            ConsoleUtil.printDivider();
            System.out.println("1. Add Resume");
            System.out.println("2. View All Resumes");
            System.out.println("3. Delete Resume");
            System.out.println("0. Back to Main Menu");

            int choice = ConsoleUtil.readIntInRange(scanner, "Enter your choice: ", 0, 3);

            switch (choice) {
                case 1 -> addResume();
                case 2 -> viewAll();
                case 3 -> deleteResume();
                case 0 -> back = true;
            }
        }
    }

    private void addResume() {
        String label = ConsoleUtil.readNonEmptyLine(scanner, "Resume Label (e.g. Full Stack Resume): ");
        String version = ConsoleUtil.readNonEmptyLine(scanner, "Version (e.g. v2): ");
        String filename = ConsoleUtil.readNonEmptyLine(scanner, "Filename (e.g. SWE_Google_v2.pdf): ");

        try {
            Resume resume = tracker.addResume(label, version, filename, LocalDate.now());
            System.out.println("Resume added successfully:");
            System.out.println(resume.summary());
        } catch (IllegalArgumentException | DuplicateResumeException e) {
            System.out.println("Could not add resume: " + e.getMessage());
        }
    }

    private void viewAll() {
        List<Resume> resumes = tracker.viewAll();
        if (resumes.isEmpty()) {
            System.out.println("No resumes found.");
            return;
        }
        for (Resume r : resumes) {
            System.out.println(r.summary());
        }
    }

    private void deleteResume() {
        String id = ConsoleUtil.readNonEmptyLine(scanner, "Enter Resume ID to delete: ");
        boolean removed = tracker.removeResume(id);
        System.out.println(removed ? "Resume deleted." : "No resume found with that ID.");
    }
}