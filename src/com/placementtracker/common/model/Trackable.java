package com.placementtracker.common.model;

import java.time.LocalDateTime;

public interface Trackable {

    String getId();

    LocalDateTime getCreatedAt();

    boolean isComplete();

    String summary();
}