package org.example.model;

public class Film {
    private int id;
    private String tytul;
    private String opis;
    private String gatunek;
    private int ocena;

    public Film(String tytul, String opis, String gatunek, int ocena) {
        this.tytul = tytul;
        this.opis = opis;
        this.gatunek = gatunek;
        this.ocena = ocena;
    }

    public String getTytul() { return tytul; }
    public String getOpis() { return opis; }
    public String getGatunek() { return gatunek; }
    public int getOcena() { return ocena; }

    public Object[] toRow() {
        return new Object[]{tytul, opis, gatunek, ocena};
    }
}