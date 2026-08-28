package com.placementtracker.resume;

import com.placementtracker.common.exception.DuplicateResumeException;

import java.time.LocalDate;
import java.util.List;

public class ResumeTracker {

    private final ResumeService service = new ResumeService();

    public Resume addResume(String label, String version, String filename, LocalDate dateAdded)
            throws DuplicateResumeException {
        return service.addResume(label, version, filename, dateAdded);
    }

    public List<Resume> viewAll() {
        return service.listAll();
    }

    public Resume viewById(String id) {
        return service.findById(id);
    }

    public boolean removeResume(String id) {
        return service.deleteResume(id);
    }

    public boolean resumeExists(String id) {
        return service.exists(id);
    }
}