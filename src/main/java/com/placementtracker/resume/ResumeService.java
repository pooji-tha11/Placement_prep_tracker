package com.placementtracker.resume;

import com.placementtracker.common.exception.DuplicateResumeException;
import com.placementtracker.common.exception.InvalidFileDataException;
import com.placementtracker.common.util.FileUtil;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ResumeService {
    private final ResumeRepository repository = new ResumeRepository();
    private final Path uploadDir = Paths.get("uploads/resumes");

    public ResumeService() {
        try {
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    public Resume addResume(String label, String version, LocalDate dateAdded, MultipartFile file)
            throws Exception {
        if (!ResumeValidator.isValidLabel(label)) throw new IllegalArgumentException("Resume label cannot be empty.");
        if (!ResumeValidator.isValidVersion(version)) throw new IllegalArgumentException("Resume version cannot be empty.");
        
        if (repository.existsByLabelAndVersion(label, version)) {
            throw new DuplicateResumeException("A resume with label \"" + label + "\" and version \"" + version + "\" already exists.");
        }

        String storedFileName = null;
        String originalFileName = null;
        String contentType = null;
        Long size = null;
        String metadataFilename = "N/A";

        if (file != null && !file.isEmpty()) {
            validateFile(file);
            storedFileName = UUID.randomUUID().toString() + getExtension(file.getOriginalFilename());
            originalFileName = file.getOriginalFilename();
            contentType = file.getContentType();
            size = file.getSize();
            metadataFilename = originalFileName;
            
            Files.copy(file.getInputStream(), uploadDir.resolve(storedFileName));
        }

        Resume resume = new Resume(label, version, metadataFilename, dateAdded, storedFileName, originalFileName, contentType, size);
        repository.add(resume);
        return resume;
    }
    
    private void validateFile(MultipartFile file) {
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("File size exceeds 5MB limit.");
        }
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("application/pdf") && 
            !contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {
            throw new IllegalArgumentException("Only PDF and DOCX files are allowed.");
        }
    }
    
    private String getExtension(String filename) {
        if (filename == null) return "";
        int lastDot = filename.lastIndexOf('.');
        return (lastDot == -1) ? "" : filename.substring(lastDot);
    }

    public List<Resume> listAll() { return repository.getAll(); }
    public Resume findById(String id) { return repository.findById(id); }

    public boolean deleteResume(String id) {
        Resume r = repository.findById(id);
        if (r != null && r.getStoredFileName() != null) {
            try {
                Files.deleteIfExists(uploadDir.resolve(r.getStoredFileName()));
            } catch (IOException e) {
                // log error, continue to delete from DB
            }
        }
        return repository.deleteById(id);
    }

    public Resume updateResume(String id, String label, String version, LocalDate dateAdded, MultipartFile file) throws Exception {
        Resume r = repository.findById(id);
        if (r == null) throw new IllegalArgumentException("Resume not found");
        
        String storedFileName = r.getStoredFileName();
        String originalFileName = r.getOriginalFileName();
        String contentType = r.getContentType();
        Long size = r.getFileSizeBytes();
        String metadataFilename = r.getFilename();
        
        if (file != null && !file.isEmpty()) {
            validateFile(file);
            
            // Delete old file
            if (storedFileName != null) {
                try {
                    Files.deleteIfExists(uploadDir.resolve(storedFileName));
                } catch (IOException e) { }
            }
            
            storedFileName = UUID.randomUUID().toString() + getExtension(file.getOriginalFilename());
            originalFileName = file.getOriginalFilename();
            contentType = file.getContentType();
            size = file.getSize();
            metadataFilename = originalFileName;
            
            Files.copy(file.getInputStream(), uploadDir.resolve(storedFileName));
        }

        Resume updated = new Resume(id, label, version, metadataFilename, dateAdded, r.getCreatedAt(), 
                storedFileName, originalFileName, contentType, size);
        repository.update(updated);
        return updated;
    }
    
    
    public Resume addResume(String label, String version, String filename, LocalDate dateAdded)
            throws Exception {
        if (!ResumeValidator.isValidLabel(label)) throw new IllegalArgumentException("Resume label cannot be empty.");
        if (!ResumeValidator.isValidVersion(version)) throw new IllegalArgumentException("Resume version cannot be empty.");
        if (repository.existsByLabelAndVersion(label, version)) {
            throw new DuplicateResumeException("A resume with label \"" + label + "\" and version \"" + version + "\" already exists.");
        }
        Resume resume = new Resume(label, version, filename, dateAdded, null, null, null, null);
        repository.add(resume);
        return resume;
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
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            try {
                Resume resume = parseAndAddRow(line);
                report.add("Row " + (i + 1) + ": Added \"" + resume.getLabel() + "\"");
            } catch (Exception e) {
                report.add("Row " + (i + 1) + ": Skipped — " + e.getMessage());
            }
        }
        return report;
    }

    private Resume parseAndAddRow(String line) throws Exception {
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
    public Path getFilePath(String storedFileName) {
        return uploadDir.resolve(storedFileName);
    }
}
