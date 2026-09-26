package com.placementtracker.resume;

import com.placementtracker.common.model.BaseEntry;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Resume extends BaseEntry {

    private final String label;
    private final String version;
    private final String filename;
    private final LocalDate dateAdded;
    
    private final String storedFileName;
    private final String originalFileName;
    private final String contentType;
    private final Long fileSizeBytes;

    public Resume(String label, String version, String filename, LocalDate dateAdded,
                  String storedFileName, String originalFileName, String contentType, Long fileSizeBytes) {
        super("RES");
        this.label = label;
        this.version = version;
        this.filename = filename;
        this.dateAdded = dateAdded;
        this.storedFileName = storedFileName;
        this.originalFileName = originalFileName;
        this.contentType = contentType;
        this.fileSizeBytes = fileSizeBytes;
        this.complete = true;
    }

    public Resume(String id, String label, String version, String filename,
                  LocalDate dateAdded, LocalDateTime createdAt,
                  String storedFileName, String originalFileName, String contentType, Long fileSizeBytes) {
        super(id, true);
        this.label = label;
        this.version = version;
        this.filename = filename;
        this.dateAdded = dateAdded;
        this.storedFileName = storedFileName;
        this.originalFileName = originalFileName;
        this.contentType = contentType;
        this.fileSizeBytes = fileSizeBytes;
        this.complete = true;
    }

    public String getLabel() { return label; }
    public String getVersion() { return version; }
    public String getFilename() { return filename; }
    public LocalDate getDateAdded() { return dateAdded; }
    
    public String getStoredFileName() { return storedFileName; }
    public String getOriginalFileName() { return originalFileName; }
    public String getContentType() { return contentType; }
    public Long getFileSizeBytes() { return fileSizeBytes; }

    @Override
    public String summary() {
        return "[" + getId() + "] " + label + " (" + version + ") — File: " + filename + ", Added: " + dateAdded;
    }
}
