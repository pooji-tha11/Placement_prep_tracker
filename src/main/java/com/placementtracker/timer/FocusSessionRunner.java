package com.placementtracker.timer;

public class FocusSessionRunner implements Runnable {

    private static final long TICK_INTERVAL_MS = 60_000; // one real minute per tick

    private final int durationMinutes;

    public FocusSessionRunner(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    @Override
    public void run() {
        for (int remaining = durationMinutes; remaining > 0; remaining--) {
            System.out.println(remaining + " minute(s) remaining...");
            try {
                Thread.sleep(TICK_INTERVAL_MS);
            } catch (InterruptedException e) {
                // Re-set the interrupt flag so any calling code checking it still sees
                // this thread was interrupted, then stop the countdown immediately.
                Thread.currentThread().interrupt();
                System.out.println("Focus session interrupted before completion.");
                return;
            }
        }
        System.out.println("Time's up! Focus session complete.");
    }
}