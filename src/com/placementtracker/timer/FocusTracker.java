package com.placementtracker.timer;

import java.util.List;

public class FocusTracker {

    private final FocusService service = new FocusService();

    public FocusSession runSession(String topic, int durationMinutes) {
        return service.runSession(topic, durationMinutes);
    }

    public List<FocusSession> viewAll() {
        return service.listAll();
    }
}