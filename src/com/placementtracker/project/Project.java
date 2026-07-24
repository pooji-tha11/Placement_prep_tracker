package com.placementtracker.project;

import com.placementtracker.common.model.BaseEntry;

import java.util.List;

public class Project extends BaseEntry {

    private final String title;
    private final String domain;
    private final List<String> techStack;
    private final String repoLink;
    private final ProjectStatus status;
    private StarForm starForm;

    public Project(String title, String domain, List<String> techStack,
                    String repoLink, ProjectStatus status) {
        super("PROJ");
        this.title = title;
        this.domain = domain;
        this.techStack = techStack;
        this.repoLink = repoLink;
        this.status = status;
        this.starForm = null;
        this.complete = (status == ProjectStatus.COMPLETED);
    }

    public String getTitle() {
        return title;
    }

    public String getDomain() {
        return domain;
    }

    public List<String> getTechStack() {
        return techStack;
    }

    public String getRepoLink() {
        return repoLink;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public StarForm getStarForm() {
        return starForm;
    }

    public void setStarForm(StarForm starForm) {
        this.starForm = starForm;
    }

    public boolean hasStarForm() {
        return starForm != null;
    }

    @Override
    public String summary() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(getId()).append("] ");
        sb.append(title).append(" (").append(domain).append(") — Status: ").append(status);
        sb.append("\n  Tech Stack: ").append(String.join(", ", techStack));
        sb.append("\n  Repo: ").append(repoLink);
        if (hasStarForm()) {
            sb.append("\n  Interview-Ready: YES\n").append(starForm.toFormattedString());
        } else {
            sb.append("\n  Interview-Ready: NO (STAR intake pending)");
        }
        return sb.toString();
    }
}