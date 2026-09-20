package com.placementtracker.resume;

import com.placementtracker.common.exception.DuplicateResumeException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    private final ResumeTracker tracker;

    public ResumeController(ResumeTracker tracker) {
        this.tracker = tracker;
    }

    @GetMapping
    public List<Resume> getAll() {
        return tracker.viewAll();
    }

    @GetMapping("/{id}")
    public Resume getById(@PathVariable String id) {
        Resume resume = tracker.viewById(id);
        if (resume == null) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND);
        }
        return resume;
    }

    @PostMapping
    public Resume create(@RequestBody CreateResumeRequest req) throws DuplicateResumeException {
        return tracker.addResume(req.label(), req.version(), req.filename(), req.dateAdded());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        tracker.removeResume(id);
    }

    public record CreateResumeRequest(String label, String version, String filename, LocalDate dateAdded) {}
}
