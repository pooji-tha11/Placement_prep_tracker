package com.placementtracker.timer;

import java.util.List;

public class FocusService {

    private final FocusRepository repository = new FocusRepository();

    public FocusSession runSession(String topic, int durationMinutes) {
        if (!FocusValidator.isValidTopic(topic)) {
            throw new IllegalArgumentException("Topic cannot be empty.");
        }
        if (!FocusValidator.isValidDuration(durationMinutes)) {
            throw new IllegalArgumentException("Duration must be between 1 and 180 minutes.");
        }

        Thread timerThread = new Thread(new FocusSessionRunner(durationMinutes));
        timerThread.start();

        try {
            timerThread.join(); // block the caller until the countdown finishes
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Main thread interrupted while waiting for focus session.");
        }

        FocusSession session = new FocusSession(topic, durationMinutes);
        repository.add(session);
        return session;
    }

    public List<FocusSession> listAll() {
        return repository.getAll();
    }
}