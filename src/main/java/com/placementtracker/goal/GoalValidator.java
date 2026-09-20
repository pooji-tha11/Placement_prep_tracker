package com.placementtracker.goal;

import com.placementtracker.common.util.InputValidator;

import java.time.LocalDate;

public final class GoalValidator {

    private GoalValidator() {
        // utility class, no instances
    }

    public static boolean isValidDescription(String description) {
        return InputValidator.isNonEmpty(description);
    }

    public static boolean isValidTargetCount(int targetCount) {
        return targetCount > 0;
    }

    public static boolean isValidDeadline(LocalDate deadline) {
        return deadline != null && !deadline.isBefore(LocalDate.now());
    }
}