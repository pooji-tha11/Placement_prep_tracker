package com.placementtracker.common.model;

import java.time.LocalDateTime;

public abstract class BaseEntry implements Trackable {


    private final String id;
    private final LocalDateTime createdAt;
    protected boolean complete;

    protected BaseEntry(String idPrefix) {
        this.id = idPrefix + "-" + java.util.UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
        this.complete = false;
    }
        protected BaseEntry(String id, boolean isExistingId) {
        this.id = id;
        this.createdAt = java.time.LocalDateTime.now(); // overwritten by caller when reconstructing from DB
        this.complete = false;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    public void markComplete() {
        this.complete = true;
    }

    @Override
    public abstract String summary();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntry other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}