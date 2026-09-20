package com.placementtracker.achievement;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/achievements")
public class AchievementController {

    private final AchievementTracker tracker;

    public AchievementController(AchievementTracker tracker) {
        this.tracker = tracker;
    }

    @GetMapping
    public List<AchievementResponse> getAll() {
        return tracker.viewAll().stream()
                .map(this::toResponse)
                .toList();
    }

    private AchievementResponse toResponse(AchievementEntry entry) {
        return switch (entry.getAchievement()) {
            case Hackathon(var name, var organizer, var date, var result) ->
                    new AchievementResponse(entry.getId(), "HACKATHON", name, organizer, result, null, null, date);
            case Certification(var name, var issuingOrg, var date) ->
                    new AchievementResponse(entry.getId(), "CERTIFICATION", name, null, null, issuingOrg, null, date);
            case CompetitionAward(var competitionName, var rank, var date) ->
                    new AchievementResponse(entry.getId(), "AWARD", competitionName, null, null, null, rank, date);
        };
    }

    public record AchievementResponse(String id, String type, String name, String organizer,
                                       String result, String issuingOrg, String rank,
                                       java.time.LocalDate date) {}

    @PostMapping("/hackathon")
    public AchievementEntry addHackathon(@RequestBody HackathonRequest req) {
        return tracker.addHackathon(req.name(), req.organizer(), req.date(), req.result());
    }

    @PostMapping("/certification")
    public AchievementEntry addCertification(@RequestBody CertificationRequest req) {
        return tracker.addCertification(req.name(), req.issuingOrg(), req.date());
    }

    @PostMapping("/award")
    public AchievementEntry addAward(@RequestBody AwardRequest req) {
        return tracker.addCompetitionAward(req.competitionName(), req.rank(), req.date());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        tracker.removeAchievement(id);
    }

    public record HackathonRequest(String name, String organizer, LocalDate date, String result) {}
    public record CertificationRequest(String name, String issuingOrg, LocalDate date) {}
    public record AwardRequest(String competitionName, String rank, LocalDate date) {}
}
