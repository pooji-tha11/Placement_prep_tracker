package com.placementtracker.study;

import com.placementtracker.common.model.BaseEntry;

import java.time.LocalDate;

public class StudySession extends BaseEntry {

    private final LocalDate date;
    private final int durationMinutes;
    private final String topic;

    public StudySession(LocalDate date, int durationMinutes, String topic) {
        super("STUDY");
        this.date = date;
        this.durationMinutes = durationMinutes;
        this.topic = topic;
        this.complete = true; // a logged session is complete by definition
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