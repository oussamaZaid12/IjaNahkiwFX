package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDB {

    private final String USER = "root";
    private final String PWD = "";
    private final String URL = "jdbc:mysql://localhost:3306/ijanahkiw2";
    private Connection connection;
    public static MyDB instance;

    private MyDB() {
        try {
            connection = DriverManager.getConnection(URL, USER, PWD);
            connection.setAutoCommit(true);  // Activation de l'auto-commit
            System.out.println("Connected to DB with auto-commit enabled.");
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }

    public static MyDB getInstance() {
        if (instance == null) {
            instance = new MyDB();
        }
        return instance;
    }

    public Connection getConnectionX() {
        return connection;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                reconnect();
            }
        } catch (SQLException e) {
            System.out.println("Failed to check connection status: " + e.getMessage());
            reconnect();
        }
        return connection;
    }

    private void reconnect() {
        try {
            connection = DriverManager.getConnection(URL, USER, PWD);
            connection.setAutoCommit(true);
            System.out.println("Reconnected to the database.");
        } catch (SQLException e) {
            System.out.println("Failed to reconnect to the database: " + e.getMessage());
            throw new RuntimeException("Database connection failed", e);
        }
    }

}
