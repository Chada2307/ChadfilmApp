package org.example;

import org.example.controller.AppController;
import org.example.dao.FilmDAO;
import org.example.dao.UserDAO;
import org.example.db.Dbconn;
import org.example.view.MainFrame;

import javax.swing.*;
import java.sql.Connection;

public class App {
    public static void main(String[] args) {

//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            try {
                // 1. Połączenie z bazą
                Dbconn db = new Dbconn();
                Connection conn = db.getConnection();

                // 2. Utworzenie warstwy danych (DAO)
                FilmDAO filmDAO = new FilmDAO(conn);
                UserDAO userDAO = new UserDAO(conn);

                // 3. Utworzenie widoku (View)
                MainFrame view = new MainFrame();

                // 4. Utworzenie kontrolera, który to wszystko łączy
                new AppController(view, filmDAO, userDAO);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Błąd krytyczny aplikacji: " + e.getMessage());
            }
        });
    }
}