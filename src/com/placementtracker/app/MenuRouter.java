package com.placementtracker.app;
import com.placementtracker.dsa.DSAMenu;
import com.placementtracker.project.ProjectMenu;
import com.placementtracker.common.util.ConsoleUtil;

import java.util.Scanner;

public class MenuRouter {

    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        boolean running = true;

        while (running) {
            ConsoleUtil.printDivider();
            System.out.println("PLACEMENT PREPARATION TRACKER");
            ConsoleUtil.printDivider();
            System.out.println("1. Competitive Programming Tracker (DSA)");
            System.out.println("2. Project Tracker");
            System.out.println("0. Exit");

            int choice = ConsoleUtil.readIntInRange(scanner, "Enter your choice: ", 0, 2);

            switch (choice) {
                case 1 -> routeToDSA();
                case 2 -> routeToProject();
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
}       