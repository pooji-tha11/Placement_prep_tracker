package com.placementtracker.achievement;

import java.time.LocalDate;

public record CompetitionAward(String competitionName, String rank, LocalDate date) implements Achievement {
}