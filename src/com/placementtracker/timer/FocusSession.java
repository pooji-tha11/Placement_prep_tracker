package com.placementtracker.timer;

import com.placementtracker.common.model.BaseEntry;

import java.time.LocalDateTime;

public class FocusSession extends BaseEntry {

    private final String topic;
    private final int durationMinutes;

    public FocusSession(String topic, int durationMinutes) {
        super("FOCUS");
        this.topic = topic;
        this.durationMinutes = durationMinutes;
        this.complete = true; // only ever recorded after the session finishes running
    }

    public String getTopic() {
        return topic;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    @Override
    public String summary() {
        return "[" + getId() + "] " + topic + " — " + durationMinutes + " min, completed " + getCreatedAt();
    }
}