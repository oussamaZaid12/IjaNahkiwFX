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

    public Connection getConnection() {
        return connection;
    }
}
