package com.placementtracker.goal;

import java.util.ArrayList;
import java.util.List;

public class GoalRepository {

    // Wildcard: a list of goals, each targeting some Trackable type,
    // not necessarily the same one across elements (generics are erased at runtime).
    private final List<Goal<?>> goals = new ArrayList<>();

    public void add(Goal<?> goal) {
        goals.add(goal);
    }

    public List<Goal<?>> getAll() {
        return new ArrayList<>(goals);
    }

    public Goal<?> findById(String id) {
        for (Goal<?> g : goals) {
            if (g.getId().equals(id)) {
                return g;
            }
        }
        return null;
    }

    public boolean deleteById(String id) {
        return goals.removeIf(g -> g.getId().equals(id));
    }
}