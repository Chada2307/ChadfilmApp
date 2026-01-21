package org.example.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class MainFrame extends JFrame {
    private JMenuItem itemEksportPdf; // pdf
    private JMenuItem itemDrukujPdf; // pdf
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
        menuPlik.setMnemonic(KeyEvent.VK_P); // alt + p otwiera menu

        itemDodaj = new JMenuItem("Dodaj film", KeyEvent.VK_D);
        // ctrl + n nowy film
        itemDodaj.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));


        itemUsun= new JMenuItem("Usuń zaznaczony", KeyEvent.VK_U);
        itemUsun.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));

        itemEksportPdf = new JMenuItem("Eksportuj do PDF");
        itemEksportPdf.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));

        itemDrukujPdf = new JMenuItem("Drukuj");
        itemDrukujPdf.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));

        itemWyjscie= new JMenuItem("Wyjście", KeyEvent.VK_W);
        itemWyjscie.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));

        menuPlik.add(itemDodaj);
        menuPlik.add(itemUsun);
        menuPlik.addSeparator();
        menuPlik.add(itemEksportPdf);
        menuPlik.add(itemDrukujPdf);
        menuPlik.addSeparator();
        menuPlik.add(itemWyjscie);

        JMenu menuKonto = new JMenu("Konto");
        menuKonto.setMnemonic(KeyEvent.VK_K);

        itemZaloguj = new JMenuItem("Zaloguj", KeyEvent.VK_Z);
        itemZaloguj.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK));

        itemRejestracja = new JMenuItem("Rejestracja", KeyEvent.VK_R);
        itemRejestracja.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));

        menuKonto.add(itemZaloguj);
        menuKonto.add(itemRejestracja);

        menuBar.add(menuPlik);
        menuBar.add(menuKonto);
        setJMenuBar(menuBar);
    }
    public JMenuItem getItemEksportPdf(){ return itemEksportPdf; }// getter zeby kontroler widzial o co chodzi
    public JMenuItem getItemDrukujPdf(){ return itemDrukujPdf; }

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
