package com.placementtracker.dsa;

import com.placementtracker.common.model.BaseEntry;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Problem extends BaseEntry {

    private final String platform;
    private final String dsaTag;
    private final Difficulty difficulty;
    private final int confidenceLevel;
    private final LocalDate solvedDate;

    // Used when creating a brand-new problem entry — generates a fresh UUID via BaseEntry.
    public Problem(String platform, String dsaTag, Difficulty difficulty,
                   int confidenceLevel, LocalDate solvedDate) {
        super("PROB");
        this.platform = platform;
        this.dsaTag = dsaTag;
        this.difficulty = difficulty;
        this.confidenceLevel = confidenceLevel;
        this.solvedDate = solvedDate;
        this.complete = true;
    }

    // Used when reconstructing a Problem from a database row.
    public Problem(String id, String platform, String dsaTag, Difficulty difficulty,
                   int confidenceLevel, LocalDate solvedDate, LocalDateTime createdAt) {
        super(id, true);
        this.platform = platform;
        this.dsaTag = dsaTag;
        this.difficulty = difficulty;
        this.confidenceLevel = confidenceLevel;
        this.solvedDate = solvedDate;
        this.complete = true;
    }

    public String getPlatform() {
        return platform;
    }

    public String getDsaTag() {
        return dsaTag;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public int getConfidenceLevel() {
        return confidenceLevel;
    }

    public LocalDate getSolvedDate() {
        return solvedDate;
    }

    @Override
    public String summary() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(getId()).append("] ");
        sb.append(dsaTag).append(" (").append(difficulty).append(") ");
        sb.append("— Platform: ").append(platform);
        sb.append(", Confidence: ").append(confidenceLevel).append("/5");
        sb.append(", Solved: ").append(solvedDate);
        return sb.toString();
    }
}