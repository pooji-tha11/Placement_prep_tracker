package com.placementtracker.achievement;

import java.util.ArrayList;
import java.util.List;

public class AchievementRepository {

    private final List<AchievementEntry> entries = new ArrayList<>();

    public void add(AchievementEntry entry) {
        entries.add(entry);
    }

    public List<AchievementEntry> getAll() {
        return new ArrayList<>(entries);
    }

    public AchievementEntry findById(String id) {
        for (AchievementEntry e : entries) {
            if (e.getId().equals(id)) {
                return e;
            }
        }
        return null;
    }

    public boolean deleteById(String id) {
        return entries.removeIf(e -> e.getId().equals(id));
    }
}