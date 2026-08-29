package com.placementtracker.application;

import com.placementtracker.common.exception.DuplicateApplicationException;
import com.placementtracker.common.exception.InvalidApplicationDataException;
import com.placementtracker.resume.Resume;
import com.placementtracker.resume.ResumeTracker;

import java.time.LocalDate;
import java.util.List;

public class ApplicationService {

    private final ApplicationRepository repository = new ApplicationRepository();
    private final ResumeTracker resumeTracker;

    public ApplicationService(ResumeTracker resumeTracker) {
        this.resumeTracker = resumeTracker;
    }

    public JobApplication addApplication(String company, String role, LocalDate dateApplied,
                                          ApplicationStatus status, String jobLink, String jobDescription,
                                          List<String> requiredSkills, String notes, String resumeId)
            throws InvalidApplicationDataException, DuplicateApplicationException {

        if (!ApplicationValidator.isValidCompany(company)) {
            throw new InvalidApplicationDataException("Company name cannot be empty.");
        }
        if (!ApplicationValidator.isValidRole(role)) {
            throw new InvalidApplicationDataException("Role cannot be empty.");
        }
        if (!ApplicationValidator.isValidResumeId(resumeId)) {
            throw new InvalidApplicationDataException("A resume ID must be provided for this application.");
        }
        if (!resumeTracker.resumeExists(resumeId)) {
            throw new InvalidApplicationDataException(
                    "No resume found with ID \"" + resumeId + "\". Add it in Resume Manager first."
            );
        }
        if (repository.existsByCompanyAndRole(company, role)) {
            throw new DuplicateApplicationException(
                    "An application for \"" + role + "\" at \"" + company + "\" already exists."
            );
        }

        JobApplication application = new JobApplication(
                company, role, dateApplied, status, jobLink, jobDescription, requiredSkills, notes, resumeId
        );
        repository.add(application);
        return application;
    }

    public List<JobApplication> listAll() {
        return repository.getAll();
    }

    public JobApplication findById(String id) {
        return repository.findById(id);
    }

    public boolean deleteApplication(String id) {
        return repository.deleteById(id);
    }

    public boolean updateStatus(String id, ApplicationStatus newStatus) {
        return repository.updateStatus(id, newStatus);
    }

    public Resume getResumeForApplication(String applicationId) {
        JobApplication application = repository.findById(applicationId);
        if (application == null) {
            return null;
        }
        return resumeTracker.viewById(application.getResumeId());
    }
}