package com.placementtracker.dsa;

import com.placementtracker.common.util.InputValidator;

public final class DSAValidator {

    private DSAValidator() {
        // utility class, no instances
    }

    public static boolean isValidConfidence(int confidence) {
        return InputValidator.isValidRange(confidence, 1, 5);
    }

    public static boolean isValidPlatform(String platform) {
        return InputValidator.isNonEmpty(platform);
    }

    public static boolean isValidTag(String tag) {
        return InputValidator.isNonEmpty(tag);
    }

    public static Difficulty parseDifficulty(String raw) {
        if (raw == null) {
            return null;
        }
        try {
            return Difficulty.valueOf(raw.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}