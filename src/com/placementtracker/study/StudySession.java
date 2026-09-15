package com.placementtracker.study;

import com.placementtracker.common.model.BaseEntry;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class StudySession extends BaseEntry {

    private final LocalDate date;
    private final int durationMinutes;
    private final String topic;

    // Used when creating a brand-new session — generates a fresh UUID via BaseEntry.
    public StudySession(LocalDate date, int durationMinutes, String topic) {
        super("STUDY");
        this.date = date;
        this.durationMinutes = durationMinutes;
        this.topic = topic;
        this.complete = true;
    }

    // Used when reconstructing a StudySession from a database row.
    public StudySession(String id, LocalDate date, int durationMinutes, String topic,
                         LocalDateTime createdAt) {
        super(id, true);
        this.date = date;
        this.durationMinutes = durationMinutes;
        this.topic = topic;
        this.complete = true;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public String getTopic() {
        return topic;
    }

    @Override
    public String summary() {
        return "[" + getId() + "] " + date + " — " + topic + " (" + durationMinutes + " min)";
    }
}