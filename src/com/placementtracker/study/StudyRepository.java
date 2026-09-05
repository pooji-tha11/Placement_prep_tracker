package com.placementtracker.study;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class StudyRepository {

    // Keyed and sorted by date; each date can hold multiple sessions
    private final TreeMap<LocalDate, List<StudySession>> sessionsByDate = new TreeMap<>();

    public void add(StudySession session) {
        sessionsByDate
                .computeIfAbsent(session.getDate(), d -> new ArrayList<>())
                .add(session);
    }

    public List<StudySession> getAll() {
        List<StudySession> all = new ArrayList<>();
        for (List<StudySession> sessionsOnDate : sessionsByDate.values()) {
            all.addAll(sessionsOnDate);
        }
        return all;
    }

    public List<StudySession> getSessionsForDate(LocalDate date) {
        return sessionsByDate.getOrDefault(date, new ArrayList<>());
    }

    public boolean hasSessionOn(LocalDate date) {
        return sessionsByDate.containsKey(date);
    }

    // TreeMap-specific: ordered iteration for streak calculation
    public java.util.NavigableSet<LocalDate> getDatesAscending() {
        return sessionsByDate.navigableKeySet();
    }

    // TreeMap-specific: efficient range query for a weekly window
    public Map<LocalDate, List<StudySession>> getSessionsBetween(LocalDate from, LocalDate to) {
        return sessionsByDate.subMap(from, true, to, true);
    }
}