package com.selectionarts.projectcensio.util.generator;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.selectionarts.projectcensio.apicontroller.viewmodel.AppViewModel;
import com.selectionarts.projectcensio.apicontroller.viewmodel.TaskViewModel;
import com.selectionarts.projectcensio.model.App;
import com.selectionarts.projectcensio.model.Types;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PDFGenerator {

    private static final Logger logger = LoggerFactory.getLogger(PDFGenerator.class);

    public static ByteArrayInputStream Report(AppViewModel app) {

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {


            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            Font h1 = new Font(Font.FontFamily.HELVETICA  , 25, Font.BOLD);
            Font h1_sub = new Font(Font.FontFamily.HELVETICA  , 18, Font.NORMAL);
            Font h2 = new Font(Font.FontFamily.HELVETICA  , 18, Font.BOLD);
            Font h3 = new Font(Font.FontFamily.HELVETICA    , 14, Font.NORMAL);
            Font h4 = new Font(Font.FontFamily.HELVETICA    , 12, Font.NORMAL);

            PdfWriter.getInstance(document, out);
            document.open();

            String pattern = "dd-MM-YYYY";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

            String date = simpleDateFormat.format(new Date());

            Paragraph title = new Paragraph("App Report",h1);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingBefore(20);
            document.add(title);

            title = new Paragraph(date,h1_sub);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);


            Paragraph SectionApp = new Paragraph(app.getTitle(),h2);
            SectionApp.setAlignment(Element.ALIGN_LEFT);
            document.add(SectionApp);

            SectionApp = new Paragraph(app.getDescription(),h3);
            SectionApp.setAlignment(Element.ALIGN_LEFT);
            document.add(SectionApp);

            if(app.getTaskAdditionalTypes().size() > 0)
            {
                SectionApp = new Paragraph("Additional Informations for " + (app.getTasklabeling().isEmpty()?"Requirement":app.getTasklabeling()),h3);
                SectionApp.setAlignment(Element.ALIGN_LEFT);
                document.add(SectionApp);

                List orderedList = new List(List.ORDERED);

                for(Types x : app.getTaskAdditionalTypes())
                {
                    orderedList.add(new ListItem(x.getTitle() + " (Type: " + x.getType()+")" ));
                }
                document.add(orderedList);
            }


            title = new Paragraph(app.getTasklabeling().isEmpty()?"Requirement":app.getTasklabeling(),h1_sub);
            title.setSpacingBefore(20);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(10);
            document.add(title);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(80);
            table.setWidths(new int[]{4, 2, 2,2,2});

            PdfPCell hcell;
            hcell = new PdfPCell(new Phrase("Title", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Thumbs Up", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Thumbs Down", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Comments", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Voters", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            for (TaskViewModel taskViewModel : app.getTaskViewModelSet() ) {

                PdfPCell cell;

                cell = new PdfPCell(new Phrase(taskViewModel.getTitle()));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(Long.toString(taskViewModel.getNrupvotes())));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(Long.toString(taskViewModel.getNrdownvotes())));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(Long.toString(taskViewModel.getNrcomments())));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(Long.toString(taskViewModel.getNrvotes())));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            document.add(table);

            document.close();

        } catch (DocumentException ex) {

            logger.error("Error occurred: {0}", ex);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}