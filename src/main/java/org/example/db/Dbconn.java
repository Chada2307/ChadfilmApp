package org.example.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Dbconn {
    private Connection connection;

    public Connection getConnection() {
        if (connection == null) {
            try {
                Properties prop = new Properties();

                prop.load(new FileInputStream("config.properties"));

                String URL = prop.getProperty("URL");
                String USER = prop.getProperty("USER");
                String PASS = prop.getProperty("PASS");

                connection = DriverManager.getConnection(URL, USER, PASS);
            } catch (IOException | SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Błąd połączenia z bazą danych!");
            }
        }
        return connection;
    }
}