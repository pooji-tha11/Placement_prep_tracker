package com.placementtracker.timer;

import java.util.List;

@org.springframework.stereotype.Service
public class FocusTracker {

    private final FocusService service = new FocusService();

    public FocusSession runSession(String topic, int durationMinutes) {
        return service.runSession(topic, durationMinutes);
    }

    public List<FocusSession> viewAll() {
        return service.listAll();
    }

    public FocusSession startSession(String topic, int durationMinutes) {
        return service.startSession(topic, durationMinutes);
    }
}