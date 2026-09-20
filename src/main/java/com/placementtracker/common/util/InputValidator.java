package com.placementtracker.common.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public final class InputValidator {

    private InputValidator() {
        // utility class, no instances
    }

    public static boolean isNonEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isValidRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    public static LocalDate parseDateOrNull(String value) {
        try {
            return LocalDate.parse(value.trim());
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}