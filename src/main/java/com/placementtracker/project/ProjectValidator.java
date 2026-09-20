package com.placementtracker.project;

import com.placementtracker.common.util.InputValidator;

import java.util.List;

public final class ProjectValidator {

    private ProjectValidator() {
        // utility class, no instances
    }

    public static boolean isValidTitle(String title) {
        return InputValidator.isNonEmpty(title);
    }

    public static boolean isValidDomain(String domain) {
        return InputValidator.isNonEmpty(domain);
    }

    public static boolean isValidTechStack(List<String> techStack) {
        return techStack != null && !techStack.isEmpty();
    }

    public static boolean isValidRepoLink(String repoLink) {
        return InputValidator.isNonEmpty(repoLink);
    }

    public static boolean isStarFormComplete(StarForm form) {
        return form != null && form.isComplete();
    }
}