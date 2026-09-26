package com.placementtracker.resume;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Resume create(
            @RequestParam("label") String label,
            @RequestParam("version") String version,
            @RequestParam("dateAdded") LocalDate dateAdded,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) throws Exception {
        return tracker.addResume(label, version, dateAdded, file);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        try {
            boolean removed = tracker.removeResume(id);
            if (removed) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("foreign key")) {
                return ResponseEntity.badRequest().body("Cannot delete this resume because it is linked to one or more applications.");
            }
            return ResponseEntity.internalServerError().body("An error occurred while deleting the resume.");
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Resume update(
            @PathVariable String id,
            @RequestParam("label") String label,
            @RequestParam("version") String version,
            @RequestParam("dateAdded") LocalDate dateAdded,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) throws Exception {
        return tracker.updateResume(id, label, version, dateAdded, file);
    }
    
    @GetMapping("/{id}/file")
    public ResponseEntity<Resource> download(@PathVariable String id) throws Exception {
        return tracker.downloadResumeFile(id);
    }

}
