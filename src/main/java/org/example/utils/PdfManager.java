package org.example.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

import javax.swing.*;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileOutputStream;


public class PdfManager {

    public static void eksportujDoPDF(JTable table, File plik) throws Exception{
        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream(plik));

        doc.open();
        Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Raport filmowy", fontHeader);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        doc.add(title);

        PdfPTable pdfTable = new PdfPTable(table.getColumnCount());

        for(int i = 0; i < table.getColumnCount(); i++){
            PdfPCell cell = new PdfPCell(new Phrase(table.getColumnName(i)));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            pdfTable.addCell(cell);
        }
        for (int row = 0; row < table.getRowCount(); row++){
            for(int col = 0; col < table.getColumnCount(); col++){
                Object val = table.getValueAt(row,col);
                pdfTable.addCell(val != null ? val.toString() : "");
            }
        }
        doc.add(pdfTable);
        doc.close();
    }

    public static void drukujPlikPDF(File plikPDF) throws Exception{
        try (PDDocument document = PDDocument.load(plikPDF)){
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setJobName(plikPDF.getName());
            job.setPageable(new PDFPageable(document));
            if(job.printDialog()){
                job.print();
            }
        }
    }

}
