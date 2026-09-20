package com.placementtracker.achievement;

import java.time.LocalDate;
import java.util.List;

public class AchievementService {

    private final AchievementRepository repository = new AchievementRepository();

    public AchievementEntry addHackathon(String name, String organizer, LocalDate date, String result) {
        if (!AchievementValidator.isValidName(name)) {
            throw new IllegalArgumentException("Hackathon name cannot be empty.");
        }
        if (!AchievementValidator.isValidOrganizer(organizer)) {
            throw new IllegalArgumentException("Organizer cannot be empty.");
        }
        if (!AchievementValidator.isValidResult(result)) {
            throw new IllegalArgumentException("Result cannot be empty.");
        }

        Hackathon hackathon = new Hackathon(name, organizer, date, result);
        AchievementEntry entry = new AchievementEntry(hackathon);
        repository.add(entry);
        return entry;
    }

    public AchievementEntry addCertification(String name, String issuingOrg, LocalDate date) {
        if (!AchievementValidator.isValidName(name)) {
            throw new IllegalArgumentException("Certification name cannot be empty.");
        }
        if (!AchievementValidator.isValidIssuingOrg(issuingOrg)) {
            throw new IllegalArgumentException("Issuing organization cannot be empty.");
        }

        Certification certification = new Certification(name, issuingOrg, date);
        AchievementEntry entry = new AchievementEntry(certification);
        repository.add(entry);
        return entry;
    }

    public AchievementEntry addCompetitionAward(String competitionName, String rank, LocalDate date) {
        if (!AchievementValidator.isValidCompetitionName(competitionName)) {
            throw new IllegalArgumentException("Competition name cannot be empty.");
        }
        if (!AchievementValidator.isValidRank(rank)) {
            throw new IllegalArgumentException("Rank cannot be empty.");
        }

        CompetitionAward award = new CompetitionAward(competitionName, rank, date);
        AchievementEntry entry = new AchievementEntry(award);
        repository.add(entry);
        return entry;
    }

    public List<AchievementEntry> listAll() {
        return repository.getAll();
    }

    public AchievementEntry findById(String id) {
        return repository.findById(id);
    }

    public boolean deleteAchievement(String id) {
        return repository.deleteById(id);
    }
}