package org.example.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.stream.Stream;

public class PdfService {

    public static void generujPDF(JTable table, String filesource) throws Exception{
        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream(filesource));

        doc.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Raport: Lista Filmów", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        doc.add(title);

        PdfPTable pdfTable = new PdfPTable(table.getColumnCount());

        for (int i = 0; i < table.getColumnCount(); i++ ){
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setPhrase(new Phrase(table.getColumnName(i)));
            pdfTable.addCell(header);
        }

        for (int rows = 0; rows < table.getRowCount(); rows++){
            for(int cols = 0; cols < table.getColumnCount(); cols++){
                String value = table.getValueAt(rows, cols).toString();
                pdfTable.addCell(value);
            }
        }
        doc.add(pdfTable);
        doc.close();
    }
}
