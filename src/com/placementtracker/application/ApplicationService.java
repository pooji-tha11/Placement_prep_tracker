package com.placementtracker.application;

import com.placementtracker.common.exception.DuplicateApplicationException;
import com.placementtracker.common.exception.InvalidApplicationDataException;
import com.placementtracker.common.exception.InvalidFileDataException;
import com.placementtracker.common.util.FileUtil;
import com.placementtracker.resume.Resume;
import com.placementtracker.resume.ResumeTracker;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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
        public void exportToCSV(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("company,role,dateApplied,status,jobLink,jobDescription,requiredSkills,notes,resumeId");
        for (JobApplication a : repository.getAll()) {
            String skills = String.join(";", a.getRequiredSkills());
            lines.add(String.join(",",
                    escape(a.getCompany()), escape(a.getRole()), a.getDateApplied().toString(),
                    a.getStatus().toString(), escape(a.getJobLink()), escape(a.getJobDescription()),
                    escape(skills), escape(a.getNotes()), a.getResumeId()
            ));
        }
        FileUtil.writeLines(filePath, lines);
    }

    public List<String> importFromCSV(String filePath) throws IOException {
        List<String> report = new ArrayList<>();
        List<String> lines = FileUtil.readLines(filePath);

        for (int i = 1; i < lines.size(); i++) { // skip header row
            String line = lines.get(i);
            try {
                JobApplication application = parseAndAddRow(line);
                report.add("Row " + (i + 1) + ": Added \"" + application.getRole()
                        + " @ " + application.getCompany() + "\"");
            } catch (InvalidFileDataException | InvalidApplicationDataException
                     | DuplicateApplicationException | IllegalArgumentException e) {
                report.add("Row " + (i + 1) + ": Skipped — " + e.getMessage());
            }
        }
        return report;
    }

    private JobApplication parseAndAddRow(String line)
            throws InvalidFileDataException, InvalidApplicationDataException, DuplicateApplicationException {
        String[] fields = line.split(",", -1);
        if (fields.length != 9) {
            throw new InvalidFileDataException("Expected 9 fields, found " + fields.length + ".");
        }

        LocalDate dateApplied;
        ApplicationStatus status;
        try {
            dateApplied = LocalDate.parse(fields[2].trim());
        } catch (DateTimeParseException e) {
            throw new InvalidFileDataException("Invalid date format: " + fields[2]);
        }
        try {
            status = ApplicationStatus.valueOf(fields[3].trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidFileDataException("Invalid status: " + fields[3]);
        }

        List<String> skills = new ArrayList<>();
        if (!fields[6].trim().isEmpty()) {
            for (String s : fields[6].split(";")) {
                if (!s.trim().isEmpty()) {
                    skills.add(s.trim());
                }
            }
        }

        return addApplication(
                fields[0].trim(), fields[1].trim(), dateApplied, status,
                fields[4].trim(), fields[5].trim(), skills, fields[7].trim(), fields[8].trim()
        );
    }

    private String escape(String value) {
        return value == null ? "" : value.replace(",", ";");
    }
        public List<JobApplication> advancedSearch(String companyKeyword, ApplicationStatus status,
                                                String requiredSkill) {
        Predicate<JobApplication> matchesCompany = (companyKeyword == null || companyKeyword.isBlank())
                ? a -> true
                : a -> a.getCompany().toLowerCase().contains(companyKeyword.toLowerCase());

        Predicate<JobApplication> matchesStatus = (status == null)
                ? a -> true
                : a -> a.getStatus() == status;

        Predicate<JobApplication> matchesSkill = (requiredSkill == null || requiredSkill.isBlank())
                ? a -> true
                : a -> a.getRequiredSkills().stream()
                        .anyMatch(skill -> skill.equalsIgnoreCase(requiredSkill));

        return repository.getAll().stream()
                .filter(matchesCompany.and(matchesStatus).and(matchesSkill))
                .collect(Collectors.toList());
    }

    public Set<String> getAllRequiredSkills() {
        return repository.getAll().stream()
                .flatMap(a -> a.getRequiredSkills().stream())
                .collect(Collectors.toCollection(TreeSet::new));
    }
}