package com.placementtracker.app;
import com.placementtracker.dsa.DSAMenu;
import com.placementtracker.goal.GoalMenu;
import com.placementtracker.project.ProjectMenu;
import com.placementtracker.resume.ResumeMenu;
import com.placementtracker.resume.ResumeTracker;
import com.placementtracker.studystreak.StudyMenu;
import com.placementtracker.achievement.AchievementMenu;
import com.placementtracker.application.ApplicationMenu;
import com.placementtracker.common.util.ConsoleUtil;

import java.util.Scanner;

public class MenuRouter {

    private final Scanner scanner = new Scanner(System.in);
    private final ResumeTracker resumeTracker = new ResumeTracker();

    public void start() {
        boolean running = true;

        while (running) {
            ConsoleUtil.printDivider();
            System.out.println("PLACEMENT PREPARATION TRACKER");
            ConsoleUtil.printDivider();
            System.out.println("1. Competitive Programming Tracker (DSA)");
            System.out.println("2. Project Tracker");
            System.out.println("3. Resume Manager");
            System.out.println("4. Job & Internship Applications");
            System.out.println("5. Study Streak Tracker");
            System.out.println("6. Hackathon & Certification Tracker");
            System.out.println("7. Goal Setting");
            System.out.println("0. Exit");

            int choice = ConsoleUtil.readIntInRange(scanner, "Enter your choice: ", 0, 7);

            switch (choice) {
                case 1 -> routeToDSA();
                case 2 -> routeToProject();
                case 3 -> routeToResume();
                case 4 -> routeToApplication();
                case 5 -> routeToStudy();
                case 6 -> routeToAchievement();
                case 7 -> routeToGoal();
                case 0 -> running = false;
            }
        }

        System.out.println("Exiting Placement Preparation Tracker. Goodbye!");
        scanner.close();
    }

    private void routeToDSA() {
    new DSAMenu(scanner).show();
}

    private void routeToProject() {
    new ProjectMenu(scanner).show();
}
private void routeToResume() {
    new ResumeMenu(scanner, resumeTracker).show();
}
private void routeToApplication() {
    new ApplicationMenu(scanner, resumeTracker).show();
}
private void routeToStudy() {
    new StudyMenu(scanner).show();
}
private void routeToAchievement() {
    new AchievementMenu(scanner).show();
}
private void routeToGoal() {
    new GoalMenu(scanner).show();
}
}       