package com.placementtracker.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {

    private DatabaseConnection() {
        // utility class, no instances
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DatabaseConfig.URL, DatabaseConfig.USERNAME, DatabaseConfig.PASSWORD
        );
    }
}