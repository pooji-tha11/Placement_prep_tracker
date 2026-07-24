package com.placementtracker.dsa;

import com.placementtracker.common.model.BaseEntry;

import java.time.LocalDate;

public class Problem extends BaseEntry {

    private final String platform;
    private final String dsaTag;
    private final Difficulty difficulty;
    private final int confidenceLevel;
    private final LocalDate solvedDate;

    public Problem(String platform, String dsaTag, Difficulty difficulty,
                   int confidenceLevel, LocalDate solvedDate) {
        super("PROB");
        this.platform = platform;
        this.dsaTag = dsaTag;
        this.difficulty = difficulty;
        this.confidenceLevel = confidenceLevel;
        this.solvedDate = solvedDate;
        this.complete = true; // a logged problem is, by definition, solved
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