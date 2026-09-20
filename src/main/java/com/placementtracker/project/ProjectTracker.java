package com.placementtracker.project;

import com.placementtracker.common.exception.IncompleteStarFormException;

import java.util.List;

@org.springframework.stereotype.Service
public class ProjectTracker {

    private final ProjectService service = new ProjectService();

    public Project addProject(String title, String domain, List<String> techStack,
                               String repoLink, ProjectStatus status) {
        return service.addProject(title, domain, techStack, repoLink, status);
    }

    public List<Project> viewAll() {
        return service.listAll();
    }

    public Project viewById(String id) {
        return service.findById(id);
    }

    public boolean removeProject(String id) {
        return service.deleteProject(id);
    }

    public void submitStarForm(String projectId, StarForm form) throws IncompleteStarFormException {
        service.attachStarForm(projectId, form);
    }

    public void updateStatus(String id, ProjectStatus status) {
        service.updateStatus(id, status);
    }
        public List<Project> advancedSearch(String domain, String technology) {
        return service.advancedSearch(domain, technology);
    }
}