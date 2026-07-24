package com.placementtracker.dsa;

import java.time.LocalDate;
import java.util.List;

public class DSATracker {

    private final DSAService service = new DSAService();

    public Problem addProblem(String platform, String dsaTag, Difficulty difficulty,
                               int confidenceLevel, LocalDate solvedDate) {
        return service.addProblem(platform, dsaTag, difficulty, confidenceLevel, solvedDate);
    }

    public List<Problem> viewAll() {
        return service.listAll();
    }

    public List<Problem> viewByTag(String tag) {
        return service.filterByTag(tag);
    }

    public List<Problem> viewByDifficulty(Difficulty difficulty) {
        return service.filterByDifficulty(difficulty);
    }

    public boolean removeProblem(String id) {
        return service.deleteProblem(id);
    }

    public double averageConfidence() {
        return service.getAverageConfidence();
    }
}