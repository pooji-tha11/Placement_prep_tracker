package com.placementtracker.timer;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/focus-sessions")
public class FocusController {

    private final FocusTracker tracker;

    public FocusController(FocusTracker tracker) {
        this.tracker = tracker;
    }

    @GetMapping
    public List<FocusSession> getAll() {
        return tracker.viewAll();
    }

    @PostMapping
    public FocusSession start(@RequestBody StartSessionRequest req) {
        return tracker.startSession(req.topic(), req.durationMinutes());
    }

    public record StartSessionRequest(String topic, int durationMinutes) {}
}
