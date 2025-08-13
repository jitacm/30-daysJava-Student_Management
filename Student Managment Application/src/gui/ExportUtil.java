package gui;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.opencsv.CSVWriter;
import model.Student;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.stream.Stream;

public class ExportUtil {

    // --- CSV Export Logic ---
    public static void exportToCsv(List<Student> students, File file) throws Exception {
        try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {
            // Write header
            String[] header = {"Roll No", "Name", "College", "City", "Percentage"};
            writer.writeNext(header);
            
            // Write data rows
            for (Student s : students) {
                String[] row = {
                    String.valueOf(s.getRollNum()),
                    s.getName(),
                    s.getClgName(),
                    s.getCity(),
                    String.format("%.2f", s.getPercentage())
                };
                writer.writeNext(row);
            }
        }
    }

    // --- PDF Report Generation Logic ---
    public static void generatePdfReport(List<Student> students, File file) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new java.io.FileOutputStream(file));
        document.open();

        // Add a title to the document
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, BaseColor.DARK_GRAY);
        Paragraph title = new Paragraph("Student Management System Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20f);
        document.add(title);

        // Create a table with 5 columns
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);
        
        // Add table headers
        addTableHeader(table);

        // Add student data to the table
        for (Student s : students) {
            table.addCell(String.valueOf(s.getRollNum()));
            table.addCell(s.getName());
            table.addCell(s.getClgName());
            table.addCell(s.getCity());
            table.addCell(String.format("%.2f", s.getPercentage()));
        }
        
        document.add(table);
        
        // Add summary statistics
        if (!students.isEmpty()) {
            double totalPercentage = students.stream().mapToDouble(Student::getPercentage).sum();
            double averagePercentage = totalPercentage / students.size();
            document.add(new Paragraph("\nSummary Statistics:", new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD)));
            document.add(new Paragraph("Total Students: " + students.size()));
            document.add(new Paragraph("Average Percentage: " + String.format("%.2f", averagePercentage)));
        }

        document.close();
    }
    
    private static void addTableHeader(PdfPTable table) {
        Stream.of("Roll No", "Name", "College", "City", "Percentage")
            .forEach(columnTitle -> {
                PdfPCell header = new PdfPCell();
                header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                header.setBorderWidth(1);
                header.setPhrase(new Phrase(columnTitle));
                header.setHorizontalAlignment(Element.ALIGN_CENTER);
                header.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(header);
            });
    }
}