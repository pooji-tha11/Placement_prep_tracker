package com.placementtracker.resume;

import com.placementtracker.common.util.InputValidator;

public final class ResumeValidator {

    private ResumeValidator() {
        // utility class, no instances
    }

    public static boolean isValidLabel(String label) {
        return InputValidator.isNonEmpty(label);
    }

    public static boolean isValidVersion(String version) {
        return InputValidator.isNonEmpty(version);
    }

    public static boolean isValidFilename(String filename) {
        return InputValidator.isNonEmpty(filename);
    }
}