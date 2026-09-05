package com.placementtracker.project;

import com.placementtracker.common.exception.IncompleteStarFormException;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.List;

public class ProjectService {

    private final ProjectRepository repository = new ProjectRepository();

    public Project addProject(String title, String domain, List<String> techStack,
                               String repoLink, ProjectStatus status) {
        if (!ProjectValidator.isValidTitle(title)) {
            throw new IllegalArgumentException("Title cannot be empty.");
        }
        if (!ProjectValidator.isValidDomain(domain)) {
            throw new IllegalArgumentException("Domain cannot be empty.");
        }
        if (!ProjectValidator.isValidTechStack(techStack)) {
            throw new IllegalArgumentException("Tech stack must contain at least one entry.");
        }
        if (!ProjectValidator.isValidRepoLink(repoLink)) {
            throw new IllegalArgumentException("Repo link cannot be empty.");
        }

        Project project = new Project(title, domain, techStack, repoLink, status);
        repository.add(project);
        return project;
    }

    public List<Project> listAll() {
        return repository.getAll();
    }

    public Project findById(String id) {
        return repository.findById(id);
    }

    public boolean deleteProject(String id) {
        return repository.deleteById(id);
    }

    public void attachStarForm(String projectId, StarForm form) throws IncompleteStarFormException {
        Project project = repository.findById(projectId);
        if (project == null) {
            throw new IllegalArgumentException("No project found with ID: " + projectId);
        }
        if (!ProjectValidator.isStarFormComplete(form)) {
            throw new IncompleteStarFormException(
                    "STAR form is incomplete — all four fields (Situation, Task, Action, Result) are required."
            );
        }
        project.setStarForm(form);
    }
        public List<Project> advancedSearch(String domain, String technology) {
        Predicate<Project> matchesDomain = (domain == null || domain.isBlank())
                ? p -> true
                : p -> p.getDomain().equalsIgnoreCase(domain);

        Predicate<Project> matchesTechnology = (technology == null || technology.isBlank())
                ? p -> true
                : p -> p.getTechStack().stream()
                        .anyMatch(tech -> tech.equalsIgnoreCase(technology));

        return repository.getAll().stream()
                .filter(matchesDomain.and(matchesTechnology))
                .collect(Collectors.toList());
    }
}