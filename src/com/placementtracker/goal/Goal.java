package com.placementtracker.goal;

import com.placementtracker.common.model.Trackable;

import java.time.LocalDate;

public class Goal<T extends Trackable> {

    private final String id;
    private final String description;
    private final int targetCount;
    private int currentCount;
    private final LocalDate deadline;

    private static int counter = 1000;

    public Goal(String description, int targetCount, LocalDate deadline) {
        this.id = "GOAL-" + (counter++);
        this.description = description;
        this.targetCount = targetCount;
        this.currentCount = 0;
        this.deadline = deadline;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getTargetCount() {
        return targetCount;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void updateProgress(int currentCount) {
        this.currentCount = currentCount;
    }

    public double getProgressPercentage() {
        if (targetCount == 0) {
            return 0.0;
        }
        return Math.min(100.0, (currentCount * 100.0) / targetCount);
    }

    public boolean isAchieved() {
        return currentCount >= targetCount;
    }

    public boolean isOverdue() {
        return !isAchieved() && LocalDate.now().isAfter(deadline);
    }

    public String summary() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(id).append("] ").append(description);
        sb.append(" — ").append(currentCount).append("/").append(targetCount);
        sb.append(String.format(" (%.1f%%)", getProgressPercentage()));
        sb.append(", Deadline: ").append(deadline);
        if (isAchieved()) {
            sb.append(" — ACHIEVED");
        } else if (isOverdue()) {
            sb.append(" — OVERDUE");
        }
        return sb.toString();
    }
}