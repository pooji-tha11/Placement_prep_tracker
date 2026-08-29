package com.placementtracker.studystreak;

import com.placementtracker.common.util.InputValidator;

public final class StudyValidator {

    private StudyValidator() {
        // utility class, no instances
    }

    public static boolean isValidDuration(int durationMinutes) {
        return durationMinutes > 0;
    }

    public static boolean isValidTopic(String topic) {
        return InputValidator.isNonEmpty(topic);
    }
}