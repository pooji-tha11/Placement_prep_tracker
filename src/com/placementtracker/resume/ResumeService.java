package com.placementtracker.resume;

import com.placementtracker.common.exception.DuplicateResumeException;
import com.placementtracker.common.exception.InvalidFileDataException;
import com.placementtracker.common.util.FileUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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
        public void exportToCSV(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("label,version,filename,dateAdded");
        for (Resume r : repository.getAll()) {
            lines.add(r.getLabel() + "," + r.getVersion() + "," + r.getFilename() + "," + r.getDateAdded());
        }
        FileUtil.writeLines(filePath, lines);
    }

    public List<String> importFromCSV(String filePath) throws IOException {
        List<String> report = new ArrayList<>();
        List<String> lines = FileUtil.readLines(filePath);

        for (int i = 1; i < lines.size(); i++) { // skip header row
            String line = lines.get(i);
            try {
                Resume resume = parseAndAddRow(line);
                report.add("Row " + (i + 1) + ": Added \"" + resume.getLabel() + "\"");
            } catch (InvalidFileDataException | DuplicateResumeException | IllegalArgumentException e) {
                report.add("Row " + (i + 1) + ": Skipped — " + e.getMessage());
            }
        }
        return report;
    }

    private Resume parseAndAddRow(String line) throws InvalidFileDataException, DuplicateResumeException {
        String[] fields = line.split(",", -1);
        if (fields.length != 4) {
            throw new InvalidFileDataException("Expected 4 fields, found " + fields.length + ".");
        }

        LocalDate dateAdded;
        try {
            dateAdded = LocalDate.parse(fields[3].trim());
        } catch (DateTimeParseException e) {
            throw new InvalidFileDataException("Invalid date format: " + fields[3]);
        }

        return addResume(fields[0].trim(), fields[1].trim(), fields[2].trim(), dateAdded);
    }
}