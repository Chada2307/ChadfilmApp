package org.example.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainFrame extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;

    private JMenuItem itemDodaj;
    private JMenuItem itemUsun;
    private JMenuItem itemZaloguj;
    private JMenuItem itemRejestracja;
    private JMenuItem itemWyjscie;

    public MainFrame(){
        super("Baza Filmow");
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initMenu();
        initTable();
        initStatusLabel();
    }

    private void initTable(){
        String[] columns = {"Tytuł", "Opis", "Gatunek", "Ocena"};

        tableModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex){

                if (columnIndex == 3){
                    return Integer.class;
                }
                return String.class;
            }
        };
        table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void initMenu(){
        JMenuBar menuBar = new JMenuBar();
        JMenu menuPlik = new JMenu("Plik");
        itemDodaj = new JMenuItem("Dodaj film");
        itemUsun= new JMenuItem("Usuń zaznaczony");
        itemWyjscie= new JMenuItem("Wyjście");

        menuPlik.add(itemDodaj);
        menuPlik.add(itemUsun);
        menuPlik.addSeparator();
        menuPlik.add(itemWyjscie);

        JMenu menuKonto = new JMenu("Konto");
        itemZaloguj = new JMenuItem("Zaloguj");
        itemRejestracja = new JMenuItem("Rejestracja");
        menuKonto.add(itemZaloguj);
        menuKonto.add(itemRejestracja);

        menuBar.add(menuPlik);
        menuBar.add(menuKonto);
        setJMenuBar(menuBar);
    }

    private void initStatusLabel(){
        statusLabel = new JLabel(" Gotowy ");
        statusLabel.setBorder(BorderFactory.createEtchedBorder());
        statusLabel.setPreferredSize(new Dimension(getWidth(),25));
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void setTableData(Object[][] dane){
        tableModel.setRowCount(0);
        for (Object[] row : dane){
            tableModel.addRow(row);
        }
    }

    public void setStatus(String text){
        statusLabel.setText(" " + text);
    }

    public void enableGuestMode(){
        itemDodaj.setEnabled(false);
        itemUsun.setEnabled(false);
        itemZaloguj.setText("Zaloguj");
        itemRejestracja.setEnabled(true);
        setStatus("Tryb gościa. ");
    }

    public void enableLoggedMode(String user){
        itemDodaj.setEnabled(true);
        itemUsun.setEnabled(true);
        itemZaloguj.setText("Wyloguj");
        itemRejestracja.setEnabled(false);
        setStatus("Zalogowano jako: " + user);
    }

    public JTable getTable() {return table;}
    public JMenuItem getItemDodaj() {return itemDodaj;}
    public JMenuItem getItemUsun() {return itemUsun;}
    public JMenuItem getItemZaloguj() {return itemZaloguj;}
    public JMenuItem getItemRejestracja() {return itemRejestracja;}
    public JMenuItem getItemWyjscie() {return itemWyjscie;}
}
