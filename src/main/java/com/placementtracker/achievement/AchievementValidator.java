package com.placementtracker.achievement;

import com.placementtracker.common.util.InputValidator;

public final class AchievementValidator {

    private AchievementValidator() {
        // utility class, no instances
    }

    public static boolean isValidName(String name) {
        return InputValidator.isNonEmpty(name);
    }

    public static boolean isValidOrganizer(String organizer) {
        return InputValidator.isNonEmpty(organizer);
    }

    public static boolean isValidResult(String result) {
        return InputValidator.isNonEmpty(result);
    }

    public static boolean isValidIssuingOrg(String issuingOrg) {
        return InputValidator.isNonEmpty(issuingOrg);
    }

    public static boolean isValidRank(String rank) {
        return InputValidator.isNonEmpty(rank);
    }

    public static boolean isValidCompetitionName(String competitionName) {
        return InputValidator.isNonEmpty(competitionName);
    }
}