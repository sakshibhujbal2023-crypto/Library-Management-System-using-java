
package org.example.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/library_management";

    private static final String USER = "root";

    private static final String PASSWORD = "12345678" +
            "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        try (Connection con = getConnection()) {
            System.out.println("MySQL Database Connected Successfully!");
        } catch (SQLException e) {
            System.out.println("Database Connection Failed!");
            e.printStackTrace();
        }
    }
}

