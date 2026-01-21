package org.example.controller;

import org.example.dao.FilmDAO;
import org.example.dao.UserDAO;
import org.example.model.Film;
import org.example.utils.PdfManager;

import org.example.view.MainFrame;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class AppController {
    //instancja logera dla tej klasy
    private static final Logger logger = LogManager.getLogger(AppController.class);

    private MainFrame view;
    private FilmDAO filmDAO;
    private UserDAO userDAO;

    private boolean isLoggedIn = false;

    public AppController(MainFrame view, FilmDAO filmDAO, UserDAO userDAO){
        this.view = view;
        this.filmDAO = filmDAO;
        this.userDAO = userDAO;

        initLogic();
    }

    private void initLogic(){
        view.getItemDodaj().addActionListener(e -> handleAdding());
        view.getItemUsun().addActionListener(e -> handleDeletion());

        view.getItemEksportPdf().addActionListener(e -> handlePDF()); // listener do pdfa
        view.getItemDrukujPdf().addActionListener(e ->  handlePrintPDF()); // listener do pdfa

        view.getItemZaloguj().addActionListener(e -> handleLogin());
        view.getItemRejestracja().addActionListener(e -> handleRegister());
        view.getItemWyjscie().addActionListener(e -> System.exit(0));

        view.enableGuestMode();
        refreshTable();
        view.setVisible(true);
    }

    private void refreshTable(){
        try{
            List<Film> filmy = filmDAO.fetchAll();
            Object[][] data = new Object[filmy.size()][];
            for (int i = 0; i < filmy.size(); i++){
                data[i] = filmy.get(i).toRow();
            }
            view.setTableData(data);
        } catch (SQLException e ){
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Błąd podczas pobierania danych: " + e.getMessage());
        }
    }

    private void handleAdding(){
        JTextField titleField = new JTextField(20);
        JTextField descField = new JTextField(20);
        String[] gatunki = {"Akcja", "Komedia", "Dramat", "Horror"};
        JComboBox<String> typeBox = new JComboBox<>(gatunki);

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
        ButtonGroup group = new ButtonGroup();
        List<JRadioButton> rbs = new ArrayList<>();

        for(int i = 1; i <= 5; i++){
            JRadioButton rb = new JRadioButton(String.valueOf(i));
            rb.setActionCommand(String.valueOf(i));
            group.add(rb);
            radioPanel.add(rb);
            rbs.add(rb);
        }
        rbs.get(2).setSelected(true);

        JPanel panel = new JPanel(new GridLayout(0,2,10,10));
        panel.add(new JLabel("Tytuł")); panel.add(titleField);
        panel.add(new JLabel("Opis: ")); panel.add(descField);
        panel.add(new JLabel("Gatunek: ")); panel.add(typeBox);
        panel.add(new JLabel("Ocena: ")); panel.add(radioPanel);

        int result = JOptionPane.showConfirmDialog(view, panel, "Nowy film", JOptionPane.OK_CANCEL_OPTION);

        if(result == JOptionPane.OK_OPTION){
            String title = titleField.getText().trim();
            String desc  = descField.getText().trim();
            if (title.isEmpty()) return;

            String type = (String) typeBox.getSelectedItem();
            int rating = Integer.parseInt(group.getSelection().getActionCommand());

            Film nowyFilm = new Film(title, desc, type, rating);

            try{
                filmDAO.addMovie(nowyFilm);
                view.setStatus("Dodano film: " + title);
                refreshTable();
                logger.info("dodano nowy film: {} ({})", title, type);
            }catch(SQLException e ){
                logger.error("Błąd podczas dodawania filmu", e);
                JOptionPane.showMessageDialog(view, "Błąd bazy: " + e.getMessage());
            }
        }
    }

    private void handleDeletion() {
        int row = view.getTable().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Zaznacz wiersz!");
            return;
        }

        String title = (String) view.getTable().getValueAt(row, 0); // Kolumna 0 to Tytuł

        int confirm = JOptionPane.showConfirmDialog(view, "Usunąć film: " + title + "?", "Potwierdź", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                filmDAO.deleteMovie(title);
                view.setStatus("Usunięto: " + title);
                refreshTable();
                logger.info("Usunięto film o tytule: {}", title);
            } catch (SQLException e) {
                logger.error("Nie udało się usunąć filmu", e);
                e.printStackTrace();
            }
        }
    }

    private void handlePDF() {
        JFileChooser ch = new JFileChooser();
        ch.setSelectedFile(new File("filmy_raport.pdf"));

        if (ch.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            File file = ch.getSelectedFile();
            // Sprawdzenie czy user dodał .pdf, jak nie to dodajemy
            if (!file.getName().toLowerCase().endsWith(".pdf")) {
                file = new File(file.getParentFile(), file.getName() + ".pdf");
            }

            try {
                PdfManager.eksportujDoPDF(view.getTable(), file);

                JOptionPane.showMessageDialog(view, "Pomyślnie wyeksportowano do PDF!");
                logger.info("Wyeksportowano dane do pliku: {}", file.getAbsolutePath());

            } catch (Exception e) {
                logger.error("Błąd eksportu PDF", e);
                JOptionPane.showMessageDialog(view, "Błąd: " + e.getMessage());
            }
        }
    }
    private void handlePrintPDF(){
        JFileChooser file = new JFileChooser();
        file.setDialogTitle("Wybierz plik pdf do wydruku");
        file.setFileFilter(new FileNameExtensionFilter("Pliki PDF","pdf"));
        if(file.showOpenDialog(view) == JFileChooser.APPROVE_OPTION){
            File pdfile = file.getSelectedFile();
            try{
                PdfManager.drukujPlikPDF(pdfile);
                logger.info("Wyslano plik do drukarki:{}", pdfile.getName());
            }catch (Exception e){
                logger.error("Blad drukowania pliku pdf", e);
                JOptionPane.showMessageDialog(view, "nie udalo sie wydrukowac "+ e.getMessage());
            }
        }
    }

    private void handleLogin() {
        if (isLoggedIn) {
            isLoggedIn = false;
            view.enableGuestMode();
            return;
        }
        JTextField loginF = new JTextField();
        JPasswordField passF = new JPasswordField();
        Object[] msg = {"Login:", loginF, "Hasło:", passF};

        if (JOptionPane.showConfirmDialog(view, msg, "Logowanie", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            if (userDAO.login(loginF.getText(), new String(passF.getPassword()))) {
                isLoggedIn = true;
                view.enableLoggedMode(loginF.getText());
            } else {
                JOptionPane.showMessageDialog(view, "Błędne dane!", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleRegister() {
        JTextField loginF = new JTextField();
        JPasswordField passF = new JPasswordField();
        JCheckBox regBox = new JCheckBox("Akceptuję regulamin");
        Object[] msg = {"Login:", loginF, "Hasło:", passF, " ", regBox};

        if (JOptionPane.showConfirmDialog(view, msg, "Rejestracja", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            if (!regBox.isSelected()) {
                JOptionPane.showMessageDialog(view, "Zaakceptuj regulamin.");
                return;
            }
            String login = loginF.getText().trim();
            if (login.isEmpty()) return;

            if (userDAO.ifExists(login)) {
                JOptionPane.showMessageDialog(view, "Użytkownik istnieje.");
                return;
            }

            try {
                userDAO.register(login, new String(passF.getPassword()));
                JOptionPane.showMessageDialog(view, "Konto utworzone!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
