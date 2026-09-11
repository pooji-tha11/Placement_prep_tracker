package com.placementtracker.database;

public final class DatabaseConfig {

    private DatabaseConfig() {
        // constants only, no instances
    }

    public static final String URL = "jdbc:mysql://localhost:3306/placement_tracker";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "REPLACE_ME_LOCALLY"; // set during mysql_secure_installation
}