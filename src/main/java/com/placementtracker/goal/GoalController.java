package com.placementtracker.goal;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    private final GoalTracker tracker;

    public GoalController(GoalTracker tracker) {
        this.tracker = tracker;
    }

    @GetMapping
    public List<Goal<?>> getAll() {
        return tracker.viewAll();
    }

    @PostMapping
    public Goal<?> create(@RequestBody CreateGoalRequest req) {
        return tracker.createGoal(req.description(), req.targetCount(), req.deadline());
    }

    @PatchMapping("/{id}/progress")
    public void updateProgress(@PathVariable String id, @RequestBody UpdateProgressRequest req) {
        tracker.updateProgress(id, req.currentCount());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        tracker.removeGoal(id);
    }

    public record CreateGoalRequest(String description, int targetCount, LocalDate deadline) {}
    public record UpdateProgressRequest(int currentCount) {}
}
