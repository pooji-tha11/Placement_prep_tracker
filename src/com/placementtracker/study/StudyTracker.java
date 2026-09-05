package com.placementtracker.study;

import java.time.LocalDate;
import java.util.List;

public class StudyTracker {

    private final StudyService service = new StudyService();

    public StudySession logSession(LocalDate date, int durationMinutes, String topic) {
        return service.logSession(date, durationMinutes, topic);
    }

    public List<StudySession> viewAll() {
        return service.listAll();
    }

    public int currentStreak() {
        return service.getCurrentStreak();
    }

    public int longestStreak() {
        return service.getLongestStreak();
    }

    public int dailyMinutes(LocalDate date) {
        return service.getDailyStudyMinutes(date);
    }

    public int weeklyMinutes() {
        return service.getWeeklyStudyMinutes();
    }
}