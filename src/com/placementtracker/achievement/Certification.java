package com.placementtracker.achievement;

import java.time.LocalDate;

public record Certification(String name, String issuingOrg, LocalDate date) implements Achievement {
}