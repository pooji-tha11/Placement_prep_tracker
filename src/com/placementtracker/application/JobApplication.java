package com.placementtracker.application;

import com.placementtracker.common.model.BaseEntry;

import java.time.LocalDate;
import java.util.List;

public class JobApplication extends BaseEntry {

    private final String company;
    private final String role;
    private final LocalDate dateApplied;
    private ApplicationStatus status;
    private final String jobLink;
    private final String jobDescription;
    private final List<String> requiredSkills;
    private final String notes;
    private final String resumeId;

    public JobApplication(String company, String role, LocalDate dateApplied, ApplicationStatus status,
                           String jobLink, String jobDescription, List<String> requiredSkills,
                           String notes, String resumeId) {
        super("APP");
        this.company = company;
        this.role = role;
        this.dateApplied = dateApplied;
        this.status = status;
        this.jobLink = jobLink;
        this.jobDescription = jobDescription;
        this.requiredSkills = requiredSkills;
        this.notes = notes;
        this.resumeId = resumeId;
        this.complete = (status == ApplicationStatus.SELECTED);
    }

    public String getCompany() {
        return company;
    }

    public String getRole() {
        return role;
    }

    public LocalDate getDateApplied() {
        return dateApplied;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
        this.complete = (status == ApplicationStatus.SELECTED);
    }

    public String getJobLink() {
        return jobLink;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public List<String> getRequiredSkills() {
        return requiredSkills;
    }

    public String getNotes() {
        return notes;
    }

    public String getResumeId() {
        return resumeId;
    }

    @Override
    public String summary() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(getId()).append("] ");
        sb.append(role).append(" @ ").append(company);
        sb.append(" — Status: ").append(status);
        sb.append("\n  Applied: ").append(dateApplied);
        sb.append("\n  Resume Used: ").append(resumeId);
        if (!requiredSkills.isEmpty()) {
            sb.append("\n  Required Skills: ").append(String.join(", ", requiredSkills));
        }
        if (jobLink != null && !jobLink.isEmpty()) {
            sb.append("\n  Job Link: ").append(jobLink);
        }
        if (notes != null && !notes.isEmpty()) {
            sb.append("\n  Notes: ").append(notes);
        }
        return sb.toString();
    }
}