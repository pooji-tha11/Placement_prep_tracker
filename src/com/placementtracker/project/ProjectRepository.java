package com.placementtracker.project;

import java.util.ArrayList;
import java.util.List;

public class ProjectRepository {

    private final List<Project> projects = new ArrayList<>();

    public void add(Project project) {
        projects.add(project);
    }

    public List<Project> getAll() {
        return new ArrayList<>(projects);
    }

    public Project findById(String id) {
        for (Project p : projects) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public boolean deleteById(String id) {
        return projects.removeIf(p -> p.getId().equals(id));
    }
}