package com.placementtracker.application;

import com.placementtracker.common.exception.DuplicateApplicationException;
import com.placementtracker.common.exception.InvalidApplicationDataException;
import com.placementtracker.resume.Resume;
import com.placementtracker.resume.ResumeTracker;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ApplicationTracker {

    private final ApplicationService service;

    public ApplicationTracker(ResumeTracker resumeTracker) {
        this.service = new ApplicationService(resumeTracker);
    }

    public JobApplication addApplication(String company, String role, LocalDate dateApplied,
                                          ApplicationStatus status, String jobLink, String jobDescription,
                                          List<String> requiredSkills, String notes, String resumeId)
            throws InvalidApplicationDataException, DuplicateApplicationException {
        return service.addApplication(company, role, dateApplied, status, jobLink,
                jobDescription, requiredSkills, notes, resumeId);
    }

    public List<JobApplication> viewAll() {
        return service.listAll();
    }

    public JobApplication viewById(String id) {
        return service.findById(id);
    }

    public boolean removeApplication(String id) {
        return service.deleteApplication(id);
    }

    public boolean updateStatus(String id, ApplicationStatus newStatus) {
        return service.updateStatus(id, newStatus);
    }

    public Resume getResumeForApplication(String applicationId) {
        return service.getResumeForApplication(applicationId);
    }
        public void exportToCSV(String filePath) throws IOException {
        service.exportToCSV(filePath);
    }

    public List<String> importFromCSV(String filePath) throws IOException {
        return service.importFromCSV(filePath);
    }
}