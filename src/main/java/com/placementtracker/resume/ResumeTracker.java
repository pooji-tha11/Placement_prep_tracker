package com.placementtracker.resume;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

@Component
public class ResumeTracker {
    private final ResumeService service = new ResumeService();

    public List<Resume> viewAll() { return service.listAll(); }
    public Resume viewById(String id) { return service.findById(id); }

    public Resume addResume(String label, String version, LocalDate dateAdded, MultipartFile file) throws Exception {
        return service.addResume(label, version, dateAdded, file);
    }

    
    public Resume addResume(String label, String version, String filename, LocalDate dateAdded) throws Exception {
        return service.addResume(label, version, filename, dateAdded);
    }

    public boolean resumeExists(String id) {
        return service.exists(id);
    }

    public void exportToCSV(String filePath) throws java.io.IOException {
        service.exportToCSV(filePath);
    }

    public List<String> importFromCSV(String filePath) throws java.io.IOException {
        return service.importFromCSV(filePath);
    }
    public boolean removeResume(String id) { return service.deleteResume(id); }

    public Resume updateResume(String id, String label, String version, LocalDate dateAdded, MultipartFile file) throws Exception {
        return service.updateResume(id, label, version, dateAdded, file);
    }
    
    public ResponseEntity<Resource> downloadResumeFile(String id) throws Exception {
        Resume resume = service.findById(id);
        if (resume == null || resume.getStoredFileName() == null) {
            return ResponseEntity.notFound().build();
        }
        Path path = service.getFilePath(resume.getStoredFileName());
        Resource resource = new UrlResource(path.toUri());
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(resume.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resume.getOriginalFileName() + "\"")
                .body(resource);
    }
}
