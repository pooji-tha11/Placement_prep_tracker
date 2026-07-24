package com.placementtracker.dsa;

import java.time.LocalDate;
import java.util.List;

public class DSAService {

    private final DSARepository repository = new DSARepository();

    public Problem addProblem(String platform, String dsaTag, Difficulty difficulty,
                               int confidenceLevel, LocalDate solvedDate) {
        if (!DSAValidator.isValidPlatform(platform)) {
            throw new IllegalArgumentException("Platform cannot be empty.");
        }
        if (!DSAValidator.isValidTag(dsaTag)) {
            throw new IllegalArgumentException("DSA tag cannot be empty.");
        }
        if (!DSAValidator.isValidConfidence(confidenceLevel)) {
            throw new IllegalArgumentException("Confidence level must be between 1 and 5.");
        }

        Problem problem = new Problem(platform, dsaTag, difficulty, confidenceLevel, solvedDate);
        repository.add(problem);
        return problem;
    }

    public List<Problem> listAll() {
        return repository.getAll();
    }

    public List<Problem> filterByTag(String tag) {
        return repository.filterByTag(tag);
    }

    public List<Problem> filterByDifficulty(Difficulty difficulty) {
        return repository.filterByDifficulty(difficulty);
    }

    public boolean deleteProblem(String id) {
        return repository.deleteById(id);
    }

    public double getAverageConfidence() {
        List<Problem> all = repository.getAll();
        if (all.isEmpty()) {
            return 0.0;
        }
        int total = 0;
        for (Problem p : all) {
            total += p.getConfidenceLevel();
        }
        return (double) total / all.size();
    }
}
