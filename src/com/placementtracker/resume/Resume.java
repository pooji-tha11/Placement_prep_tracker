package com.placementtracker.resume;

import com.placementtracker.common.model.BaseEntry;

import java.time.LocalDate;

public class Resume extends BaseEntry {

    private final String label;
    private final String version;
    private final String filename;
    private final LocalDate dateAdded;

    public Resume(String label, String version, String filename, LocalDate dateAdded) {
        super("RES");
        this.label = label;
        this.version = version;
        this.filename = filename;
        this.dateAdded = dateAdded;
        this.complete = true; // a saved resume record is complete by definition
    }

    public String getLabel() {
        return label;
    }

    public String getVersion() {
        return version;
    }

    public String getFilename() {
        return filename;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    @Override
    public String summary() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(getId()).append("] ");
        sb.append(label).append(" (").append(version).append(")");
        sb.append(" — File: ").append(filename);
        sb.append(", Added: ").append(dateAdded);
        return sb.toString();
    }
}