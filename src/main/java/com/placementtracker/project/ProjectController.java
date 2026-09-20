package com.placementtracker.project;

import com.placementtracker.common.exception.IncompleteStarFormException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectTracker tracker;

    public ProjectController(ProjectTracker tracker) {
        this.tracker = tracker;
    }

    @GetMapping
    public List<Project> getAll() {
        return tracker.viewAll();
    }

    @GetMapping("/{id}")
    public Project getById(@PathVariable String id) {
        Project project = tracker.viewById(id);
        if (project == null) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND);
        }
        return project;
    }

    @GetMapping("/search")
    public List<Project> search(@RequestParam(required = false) String domain,
                                 @RequestParam(required = false) String technology) {
        return tracker.advancedSearch(domain, technology);
    }

    @PostMapping
    public Project create(@RequestBody CreateProjectRequest req) {
        return tracker.addProject(req.title(), req.domain(), req.techStack(),
                req.repoLink(), req.status());
    }

    @PostMapping("/{id}/star")
    public void submitStarForm(@PathVariable String id, @RequestBody StarForm form)
            throws IncompleteStarFormException {
        tracker.submitStarForm(id, form);
    }

    @PatchMapping("/{id}/status")
    public void updateStatus(@PathVariable String id, @RequestBody UpdateStatusRequest req) {
        tracker.updateStatus(id, req.status());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        tracker.removeProject(id);
    }

    public record CreateProjectRequest(String title, String domain, List<String> techStack,
                                        String repoLink, ProjectStatus status) {}

    public record UpdateStatusRequest(ProjectStatus status) {}
}
