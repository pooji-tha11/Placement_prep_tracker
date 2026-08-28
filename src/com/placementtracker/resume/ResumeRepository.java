package com.placementtracker.resume;

import java.util.ArrayList;
import java.util.List;

public class ResumeRepository {

    private final List<Resume> resumes = new ArrayList<>();

    public void add(Resume resume) {
        resumes.add(resume);
    }

    public List<Resume> getAll() {
        return new ArrayList<>(resumes);
    }

    public Resume findById(String id) {
        for (Resume r : resumes) {
            if (r.getId().equals(id)) {
                return r;
            }
        }
        return null;
    }

    public boolean deleteById(String id) {
        return resumes.removeIf(r -> r.getId().equals(id));
    }

    public boolean existsByLabelAndVersion(String label, String version) {
        for (Resume r : resumes) {
            if (r.getLabel().equalsIgnoreCase(label) && r.getVersion().equalsIgnoreCase(version)) {
                return true;
            }
        }
        return false;
    }
}