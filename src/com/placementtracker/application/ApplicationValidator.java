package com.placementtracker.application;

import com.placementtracker.common.util.InputValidator;

public final class ApplicationValidator {

    private ApplicationValidator() {
        // utility class, no instances
    }

    public static boolean isValidCompany(String company) {
        return InputValidator.isNonEmpty(company);
    }

    public static boolean isValidRole(String role) {
        return InputValidator.isNonEmpty(role);
    }

    public static boolean isValidResumeId(String resumeId) {
        return InputValidator.isNonEmpty(resumeId);
    }
}