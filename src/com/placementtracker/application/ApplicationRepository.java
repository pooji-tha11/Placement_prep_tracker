package com.placementtracker.application;

import java.util.ArrayList;
import java.util.List;

public class ApplicationRepository {

    private final List<JobApplication> applications = new ArrayList<>();

    public void add(JobApplication application) {
        applications.add(application);
    }

    public List<JobApplication> getAll() {
        return new ArrayList<>(applications);
    }

    public JobApplication findById(String id) {
        for (JobApplication a : applications) {
            if (a.getId().equals(id)) {
                return a;
            }
        }
        return null;
    }

    public boolean deleteById(String id) {
        return applications.removeIf(a -> a.getId().equals(id));
    }

    public boolean existsByCompanyAndRole(String company, String role) {
        for (JobApplication a : applications) {
            if (a.getCompany().equalsIgnoreCase(company) && a.getRole().equalsIgnoreCase(role)) {
                return true;
            }
        }
        return false;
    }

    public boolean updateStatus(String id, ApplicationStatus newStatus) {
        JobApplication application = findById(id);
        if (application == null) {
            return false;
        }
        application.setStatus(newStatus);
        return true;
    }
}