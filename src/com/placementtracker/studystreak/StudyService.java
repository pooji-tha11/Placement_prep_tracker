package com.placementtracker.studystreak;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class StudyService {

    private final StudyRepository repository = new StudyRepository();

    public StudySession logSession(LocalDate date, int durationMinutes, String topic) {
        if (!StudyValidator.isValidDuration(durationMinutes)) {
            throw new IllegalArgumentException("Duration must be a positive number of minutes.");
        }
        if (!StudyValidator.isValidTopic(topic)) {
            throw new IllegalArgumentException("Topic cannot be empty.");
        }

        StudySession session = new StudySession(date, durationMinutes, topic);
        repository.add(session);
        return session;
    }

    public List<StudySession> listAll() {
        return repository.getAll();
    }

    public int getCurrentStreak() {
        int streak = 0;
        LocalDate cursor = LocalDate.now();

        while (repository.hasSessionOn(cursor)) {
            streak++;
            cursor = cursor.minusDays(1);
        }
        return streak;
    }

    public int getLongestStreak() {
        int longest = 0;
        int current = 0;
        LocalDate previous = null;

        for (LocalDate date : repository.getDatesAscending()) {
            if (previous != null && date.equals(previous.plusDays(1))) {
                current++;
            } else {
                current = 1;
            }
            longest = Math.max(longest, current);
            previous = date;
        }
        return longest;
    }

    public int getDailyStudyMinutes(LocalDate date) {
        int total = 0;
        for (StudySession s : repository.getSessionsForDate(date)) {
            total += s.getDurationMinutes();
        }
        return total;
    }

    public int getWeeklyStudyMinutes() {
        LocalDate today = LocalDate.now();
        LocalDate weekAgo = today.minusDays(6); // inclusive 7-day window

        int total = 0;
        Map<LocalDate, List<StudySession>> weekSessions = repository.getSessionsBetween(weekAgo, today);
        for (List<StudySession> sessionsOnDate : weekSessions.values()) {
            for (StudySession s : sessionsOnDate) {
                total += s.getDurationMinutes();
            }
        }
        return total;
    }
}