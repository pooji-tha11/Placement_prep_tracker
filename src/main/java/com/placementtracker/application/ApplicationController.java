package com.placementtracker.application;

import com.placementtracker.common.exception.DuplicateApplicationException;
import com.placementtracker.common.exception.InvalidApplicationDataException;
import com.placementtracker.resume.Resume;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationTracker tracker;

    public ApplicationController(ApplicationTracker tracker) {
        this.tracker = tracker;
    }

    @GetMapping
    public List<JobApplication> getAll() {
        return tracker.viewAll();
    }

    @GetMapping("/{id}")
    public JobApplication getById(@PathVariable String id) {
        JobApplication application = tracker.viewById(id);
        if (application == null) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND);
        }
        return application;
    }

    @GetMapping("/{id}/resume")
    public Resume getResumeForApplication(@PathVariable String id) {
        return tracker.getResumeForApplication(id);
    }

    @GetMapping("/skills")
    public Set<String> getAllRequiredSkills() {
        return tracker.getAllRequiredSkills();
    }

    @GetMapping("/search")
    public List<JobApplication> search(@RequestParam(required = false) String company,
                                        @RequestParam(required = false) ApplicationStatus status,
                                        @RequestParam(required = false) String skill) {
        return tracker.advancedSearch(company, status, skill);
    }

    @PostMapping
    public JobApplication create(@RequestBody CreateApplicationRequest req)
            throws InvalidApplicationDataException, DuplicateApplicationException {
        return tracker.addApplication(req.company(), req.role(), req.dateApplied(), req.status(),
                req.jobLink(), req.jobDescription(), req.requiredSkills(), req.notes(), req.resumeId());
    }

    @PatchMapping("/{id}/status")
    public void updateStatus(@PathVariable String id, @RequestBody UpdateStatusRequest req) {
        tracker.updateStatus(id, req.status());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        tracker.removeApplication(id);
    }

    public record CreateApplicationRequest(String company, String role, LocalDate dateApplied,
                                            ApplicationStatus status, String jobLink, String jobDescription,
                                            List<String> requiredSkills, String notes, String resumeId) {}
    public record UpdateStatusRequest(ApplicationStatus status) {}
}
