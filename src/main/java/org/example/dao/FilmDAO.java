package org.example.dao;

import org.example.model.Film;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FilmDAO {
    private Connection conn;

    public FilmDAO(Connection conn){
        this.conn = conn;
    }

    public List<Film> fetchAll() throws SQLException{
        List<Film> filmy = new ArrayList<>();
        String query = "SELECT * FROM movies";

        try(Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)){

            while (rs.next()) {
                filmy.add(new Film(
                    rs.getString("Title"),
                    rs.getString("Description"),
                    rs.getString("Type"),
                    rs.getInt("Reviews")
                ));
            }
        }
        return filmy;
    }

    public void addMovie(Film film) throws SQLException{
        String query = "INSERT INTO movies(Title, Description, Type, Reviews) VALUES (?,?,?,?)";
        try(PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, film.getTytul());
            stmt.setString(2, film.getOpis());
            stmt.setString(3, film.getGatunek());
            stmt.setInt(4, film.getOcena());
            stmt.executeUpdate();
        }
    }

    public void deleteMovie(String title) throws SQLException {
        String query = "DELETE FROM movies WHERE Title = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, title);
            stmt.executeUpdate();
        }
    }

}
