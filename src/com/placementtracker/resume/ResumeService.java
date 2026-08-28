package com.placementtracker.resume;

import com.placementtracker.common.exception.DuplicateResumeException;

import java.time.LocalDate;
import java.util.List;

public class ResumeService {

    private final ResumeRepository repository = new ResumeRepository();

    public Resume addResume(String label, String version, String filename, LocalDate dateAdded)
            throws DuplicateResumeException {
        if (!ResumeValidator.isValidLabel(label)) {
            throw new IllegalArgumentException("Resume label cannot be empty.");
        }
        if (!ResumeValidator.isValidVersion(version)) {
            throw new IllegalArgumentException("Resume version cannot be empty.");
        }
        if (!ResumeValidator.isValidFilename(filename)) {
            throw new IllegalArgumentException("Filename cannot be empty.");
        }
        if (repository.existsByLabelAndVersion(label, version)) {
            throw new DuplicateResumeException(
                    "A resume with label \"" + label + "\" and version \"" + version + "\" already exists."
            );
        }

        Resume resume = new Resume(label, version, filename, dateAdded);
        repository.add(resume);
        return resume;
    }

    public List<Resume> listAll() {
        return repository.getAll();
    }

    public Resume findById(String id) {
        return repository.findById(id);
    }

    public boolean deleteResume(String id) {
        return repository.deleteById(id);
    }

    public boolean exists(String id) {
        return repository.findById(id) != null;
    }
}