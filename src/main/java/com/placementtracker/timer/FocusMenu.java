package com.placementtracker.timer;

import com.placementtracker.common.util.ConsoleUtil;

import java.util.List;
import java.util.Scanner;

public class FocusMenu {

    private final Scanner scanner;
    private final FocusTracker tracker = new FocusTracker();

    public FocusMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public void show() {
        boolean back = false;

        while (!back) {
            ConsoleUtil.printDivider();
            System.out.println("FOCUS SESSION TIMER");
            ConsoleUtil.printDivider();
            System.out.println("1. Start Focus Session");
            System.out.println("2. View Past Sessions");
            System.out.println("0. Back to Main Menu");

            int choice = ConsoleUtil.readIntInRange(scanner, "Enter your choice: ", 0, 2);

            switch (choice) {
                case 1 -> startSession();
                case 2 -> viewAll();
                case 0 -> back = true;
            }
        }
    }

    private void startSession() {
        String topic = ConsoleUtil.readNonEmptyLine(scanner, "Topic (e.g. Dynamic Programming): ");
        int duration = ConsoleUtil.readIntInRange(scanner, "Duration in minutes (1-180): ", 1, 180);

        System.out.println("Starting focus session on \"" + topic + "\" for " + duration + " minute(s)...");
        System.out.println("(Note: this will block the console until the session completes.)");

        try {
            FocusSession session = tracker.runSession(topic, duration);
            System.out.println("Session recorded:");
            System.out.println(session.summary());
        } catch (IllegalArgumentException e) {
            System.out.println("Could not start session: " + e.getMessage());
        }
    }

    private void viewAll() {
        List<FocusSession> sessions = tracker.viewAll();
        if (sessions.isEmpty()) {
            System.out.println("No focus sessions recorded yet.");
            return;
        }
        for (FocusSession s : sessions) {
            System.out.println(s.summary());
        }
    }
}