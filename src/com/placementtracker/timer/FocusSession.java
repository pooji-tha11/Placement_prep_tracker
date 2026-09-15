package com.placementtracker.timer;

import com.placementtracker.common.model.BaseEntry;

import java.time.LocalDateTime;

public class FocusSession extends BaseEntry {

    private final String topic;
    private final int durationMinutes;

    // Used when creating a brand-new focus session — generates a fresh UUID via BaseEntry.
    public FocusSession(String topic, int durationMinutes) {
        super("FOCUS");
        this.topic = topic;
        this.durationMinutes = durationMinutes;
        this.complete = true;
    }

    // Used when reconstructing a FocusSession from a database row.
    public FocusSession(String id, String topic, int durationMinutes, LocalDateTime createdAt) {
        super(id, true);
        this.topic = topic;
        this.durationMinutes = durationMinutes;
        this.complete = true;
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