package com.placementtracker.dsa;

import java.util.ArrayList;
import java.util.List;

public class DSARepository {

    private final List<Problem> problems = new ArrayList<>();

    public void add(Problem problem) {
        problems.add(problem);
    }

    public List<Problem> getAll() {
        return new ArrayList<>(problems);
    }

    public Problem findById(String id) {
        for (Problem p : problems) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public boolean deleteById(String id) {
        return problems.removeIf(p -> p.getId().equals(id));
    }

    public List<Problem> filterByTag(String tag) {
        List<Problem> result = new ArrayList<>();
        for (Problem p : problems) {
            if (p.getDsaTag().equalsIgnoreCase(tag)) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Problem> filterByDifficulty(Difficulty difficulty) {
        List<Problem> result = new ArrayList<>();
        for (Problem p : problems) {
            if (p.getDifficulty() == difficulty) {
                result.add(p);
            }
        }
        return result;
    }
}