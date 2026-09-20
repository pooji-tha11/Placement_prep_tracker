package com.placementtracker.timer;

import com.placementtracker.common.util.InputValidator;

public final class FocusValidator {

    private static final int MAX_DURATION_MINUTES = 180;

    private FocusValidator() {
        // utility class, no instances
    }

    public static boolean isValidTopic(String topic) {
        return InputValidator.isNonEmpty(topic);
    }

    public static boolean isValidDuration(int durationMinutes) {
        return durationMinutes > 0 && durationMinutes <= MAX_DURATION_MINUTES;
    }
}