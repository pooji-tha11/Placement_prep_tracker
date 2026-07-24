package com.placementtracker.common.util;

import java.util.Scanner;

public final class ConsoleUtil {

    private ConsoleUtil() {
        // utility class, no instances
    }

    public static void printDivider() {
        System.out.println("-".repeat(50));
    }

    public static String readNonEmptyLine(Scanner scanner, String prompt) {
        String value;
        do {
            System.out.print(prompt);
            value = scanner.nextLine();
            if (!InputValidator.isNonEmpty(value)) {
                System.out.println("Input cannot be empty. Please try again.");
            }
        } while (!InputValidator.isNonEmpty(value));
        return value.trim();
    }

    public static int readIntInRange(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String raw = scanner.nextLine();
            try {
                int value = Integer.parseInt(raw.trim());
                if (InputValidator.isValidRange(value, min, max)) {
                    return value;
                }
                System.out.println("Please enter a value between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }
}