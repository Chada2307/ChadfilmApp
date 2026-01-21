package org.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    private Connection conn;

    public UserDAO(Connection conn){
        this.conn = conn;
    }

    public boolean login(String user, String pass) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user);
            stmt.setString(2, pass);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void register(String user, String pass) throws SQLException {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user);
            stmt.setString(2, pass);
            stmt.executeUpdate();
        }
    }

    public boolean ifExists(String user) {
        String query = "SELECT username FROM users WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            return false;
        }
    }
}
