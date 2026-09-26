package com.placementtracker.project;

import com.placementtracker.common.exception.IncompleteStarFormException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectTracker tracker;
    private final StarAssistantService starAssistantService;

    public ProjectController(ProjectTracker tracker, StarAssistantService starAssistantService) {
        this.tracker = tracker;
        this.starAssistantService = starAssistantService;
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
    
    public record ImproveFieldRequest(String field, String text) {}

    @PostMapping("/{id}/star/improve")
    public ResponseEntity<?> improveStarField(@PathVariable String id, @RequestBody ImproveFieldRequest req) {
        try {
            StarAssistantService.ImproveResponse res = starAssistantService.improveField(req.field(), req.text());
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Could not generate suggestion: " + e.getMessage()));
        }
    }

    @PostMapping("/{id}/star/score")
    public ResponseEntity<?> scoreStar(@PathVariable String id, @RequestBody StarForm form) {
        try {
            StarAssistantService.ScoreResponse res = starAssistantService.scoreStar(form);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Could not score STAR: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public Project update(@PathVariable String id, @RequestBody CreateProjectRequest req) {
        return tracker.updateProject(id, req.title(), req.domain(), req.techStack(), req.repoLink(), req.status());
    }

}
