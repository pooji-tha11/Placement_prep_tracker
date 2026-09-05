package com.placementtracker.timer;

import java.util.ArrayList;
import java.util.List;

public class FocusRepository {

    private final List<FocusSession> sessions = new ArrayList<>();

    public void add(FocusSession session) {
        sessions.add(session);
    }

    public List<FocusSession> getAll() {
        return new ArrayList<>(sessions);
    }
}