package com.placementtracker.dsa;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/dsa")
public class DSAController {

    private final DSATracker tracker;

    public DSAController(DSATracker tracker) {
        this.tracker = tracker;
    }

    @GetMapping
    public List<Problem> getAll() {
        return tracker.viewAll();
    }

    @GetMapping("/tag/{tag}")
    public List<Problem> byTag(@PathVariable String tag) {
        return tracker.viewByTag(tag);
    }

    @GetMapping("/difficulty/{difficulty}")
    public List<Problem> byDifficulty(@PathVariable Difficulty difficulty) {
        return tracker.viewByDifficulty(difficulty);
    }

    @GetMapping("/search")
    public List<Problem> search(@RequestParam(required = false) String tag,
                                 @RequestParam(required = false) Difficulty difficulty,
                                 @RequestParam(required = false) Integer minConfidence) {
        return tracker.advancedSearch(tag, difficulty, minConfidence);
    }

    @GetMapping("/average-confidence")
    public double averageConfidence() {
        return tracker.averageConfidence();
    }

    @PostMapping
    public Problem create(@RequestBody CreateProblemRequest req) {
        return tracker.addProblem(req.platform(), req.dsaTag(), req.difficulty(),
                req.confidenceLevel(), req.solvedDate());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        tracker.removeProblem(id);
    }

    public record CreateProblemRequest(String platform, String dsaTag, Difficulty difficulty,
                                        int confidenceLevel, LocalDate solvedDate) {}
}
