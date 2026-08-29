package com.placementtracker.goal;

import java.time.LocalDate;
import java.util.List;

public class GoalTracker {

    private final GoalService service = new GoalService();

    public Goal<?> createGoal(String description, int targetCount, LocalDate deadline) {
        return service.createGoal(description, targetCount, deadline);
    }

    public boolean updateProgress(String goalId, int currentCount) {
        return service.updateProgress(goalId, currentCount);
    }

    public List<Goal<?>> viewAll() {
        return service.listAll();
    }

    public boolean removeGoal(String id) {
        return service.deleteGoal(id);
    }
}