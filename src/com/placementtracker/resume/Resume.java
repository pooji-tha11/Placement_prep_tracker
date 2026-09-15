package com.placementtracker.resume;

import com.placementtracker.common.model.BaseEntry;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Resume extends BaseEntry {

    private final String label;
    private final String version;
    private final String filename;
    private final LocalDate dateAdded;

    // Used when creating a brand-new resume — generates a fresh UUID via BaseEntry.
    public Resume(String label, String version, String filename, LocalDate dateAdded) {
        super("RES");
        this.label = label;
        this.version = version;
        this.filename = filename;
        this.dateAdded = dateAdded;
        this.complete = true;
    }

    // Used when reconstructing a Resume from a database row, where the id and
    // createdAt already exist and must be preserved exactly, not regenerated.
    public Resume(String id, String label, String version, String filename,
                  LocalDate dateAdded, LocalDateTime createdAt) {
        super(id, true);
        this.label = label;
        this.version = version;
        this.filename = filename;
        this.dateAdded = dateAdded;
        this.complete = true;
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