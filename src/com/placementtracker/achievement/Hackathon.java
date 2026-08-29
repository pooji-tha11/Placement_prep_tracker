package com.placementtracker.achievement;

import java.time.LocalDate;

public record Hackathon(String name, String organizer, LocalDate date, String result) implements Achievement {
}