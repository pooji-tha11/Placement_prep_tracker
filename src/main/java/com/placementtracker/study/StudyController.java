package com.placementtracker.study;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/study")
public class StudyController {

    private final StudyTracker tracker;

    public StudyController(StudyTracker tracker) {
        this.tracker = tracker;
    }

    @GetMapping
    public List<StudySession> getAll() {
        return tracker.viewAll();
    }

    @GetMapping("/streak/current")
    public int currentStreak() {
        return tracker.currentStreak();
    }

    @GetMapping("/streak/longest")
    public int longestStreak() {
        return tracker.longestStreak();
    }

    @GetMapping("/weekly-minutes")
    public int weeklyMinutes() {
        return tracker.weeklyMinutes();
    }

    @PostMapping
    public StudySession log(@RequestBody LogSessionRequest req) {
        return tracker.logSession(req.date(), req.durationMinutes(), req.topic());
    }

    public record LogSessionRequest(LocalDate date, int durationMinutes, String topic) {}
}
