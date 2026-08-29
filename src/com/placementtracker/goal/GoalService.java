package com.placementtracker.goal;

import java.time.LocalDate;
import java.util.List;

public class GoalService {

    private final GoalRepository repository = new GoalRepository();

    public Goal<?> createGoal(String description, int targetCount, LocalDate deadline) {
        if (!GoalValidator.isValidDescription(description)) {
            throw new IllegalArgumentException("Description cannot be empty.");
        }
        if (!GoalValidator.isValidTargetCount(targetCount)) {
            throw new IllegalArgumentException("Target count must be a positive number.");
        }
        if (!GoalValidator.isValidDeadline(deadline)) {
            throw new IllegalArgumentException("Deadline cannot be in the past.");
        }

        Goal<?> goal = new Goal<>(description, targetCount, deadline);
        repository.add(goal);
        return goal;
    }

    public boolean updateProgress(String goalId, int currentCount) {
        Goal<?> goal = repository.findById(goalId);
        if (goal == null) {
            return false;
        }
        if (currentCount < 0) {
            throw new IllegalArgumentException("Current count cannot be negative.");
        }
        goal.updateProgress(currentCount);
        return true;
    }

    public List<Goal<?>> listAll() {
        return repository.getAll();
    }

    public boolean deleteGoal(String id) {
        return repository.deleteById(id);
    }
}