package com.wiem.pdfreadwrite;


import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PDFReader extends AppCompatActivity {


    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD); // Set of font family alrady present with itextPdf library.
    private static Font redFont;
    private static Font blackFont;
    private static Font blackFont_big;
    private static Font greenFont;

    static {
        redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                Font.BOLD, BaseColor.RED);
        blackFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                Font.NORMAL, BaseColor.BLACK);
        blackFont_big = new Font(Font.FontFamily.TIMES_ROMAN, 14,
                Font.NORMAL, BaseColor.BLACK);
        greenFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                Font.BOLD, BaseColor.GREEN);
    }

    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);

    public static final String DEST = "wiem.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void PrintDocument(String  dest) throws IOException, IOException {
        if(new File(dest).exists())
            new File(dest).delete();
        try {

//            Document document = new Document();
            Document document = new Document(PageSize.A4, 10f, 10f, 10f, 0f);//PENGUIN_SMALL_PAPERBACK used to set the paper size
            //PdfWriter.getInstance(document, new FileOutputStream(dest));
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
            System.out.println("dest file : "+dest);
            document.open();
            //addMetaData(document);
            addTitlePage(document , this , writer);
            //addContent(document);
            document.close();

            /*          File file = new File(dest);
             */
//            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+ DEST);
//            Uri path = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file);
//
//            Intent target = new Intent(Intent.ACTION_VIEW);
//            target.setDataAndType(Uri.fromFile(file),"application/pdf");
//            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//
//            Intent intent = Intent.createChooser(target, "Open File");

            //  File file = new File( dest);
            //   File pdfFile = new File(Environment.getExternalStorageDirectory() + "/Ap/" + "manual.pdf");  // -> filename = manual.pdf

            Uri excelPath;
            //  FileProvider.getUriForFile(getApplicationContext(), "com.example.asd", newFile);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                excelPath = FileProvider.getUriForFile(this,BuildConfig.APPLICATION_ID , new File(dest));
            } else {
                System.out.println("else... ");
                excelPath = Uri.fromFile(new File(dest));
            }
            System.out.println("excelPath : "+excelPath);
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(excelPath, "application/pdf");
            pdfIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pdfIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            try{
                startActivity(pdfIntent);
            }catch(ActivityNotFoundException e){
                System.out.println("error : "+e);
                Toast.makeText(this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error : "+e);
        }
    }

    private static void addMetaData(Document document) {
        document.addTitle("My first PDF");
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Lars Vogel");
        document.addCreator("Lars Vogel");
    }

    private static void addTitlePage(Document document , Context context , PdfWriter writer)
            throws DocumentException {

        //====================================================================//
        String numFacture = "FACTURE N FT09 \n Date d'émission : 20/04/2020";
        //Font f = new Font(Font.FontFamily.TIMES_ROMAN, 10.0f, Font.BOLD, BaseColor.BLACK);
       // Chunk c = new Chunk(numFacture, f);
        Paragraph paragraph_numFacture = new Paragraph(numFacture);
        paragraph_numFacture.setAlignment(Element.ALIGN_RIGHT);
        document.add((Element) paragraph_numFacture);

        //====================================================================//

        Resources res = context.getResources();
        Bitmap bm = BitmapFactory.decodeResource( res, R.drawable.iwassel);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 50, stream);
        Image img = null;
        byte[] byteArray = stream.toByteArray();
        try {
            img = Image.getInstance(byteArray);
            img.setAlignment(Element.ALIGN_LEFT);
            img.scalePercent(10);
            img.scaleAbsolute(100, 50);
            if (img != null) {
                document.add(img);
            }
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //====================================================================//
        String espace = "\n\n";

        Paragraph paragraph_espace = new Paragraph(espace);
        paragraph_espace.setAlignment(Element.ALIGN_LEFT);
        document.add((Element) paragraph_espace);
        //====================================================================//

        String table_info_entreprise_client = "iWASSEL SARL\n" +
                "Rue lac manitoba, residence PLATINUM\n" +
                "1053 tunis\n" +
                "Tunisie\n" +
                "Tél : 28348958\n" +
                "Email : YASMINE.DAHMOUL@GMAIL.COM\n" +
                "N°TVA : 1633272/A";

                String coordonneeClient = "Code client : FT\n" +
                        "FARM TRUST\n" +
                        "IMPASSE\n" +
                        "2091 MENZEH 5\n" +
                        "TUNISIE\n" +
                        "+21628061122\n" +
                        "N°TVA : 1634283/H/N/M/000";

        PdfPTable table_info_entreprise_client2 = new PdfPTable(new float[] { 45,10,45 });
        table_info_entreprise_client2.setWidthPercentage(100);
        createTwoParagrapheAtSameLine(table_info_entreprise_client2,table_info_entreprise_client,coordonneeClient);
        document.add(table_info_entreprise_client2);
        //====================================================================//
        String espace3 = "\n\n";

        Paragraph paragraph_espace3 = new Paragraph(espace3);
        paragraph_espace3.setAlignment(Element.ALIGN_RIGHT);
        document.add((Element) paragraph_espace3);
        //====================================================================//

        String desc = "Date d'exécution de la vente ou de la prestation : 15/04/2020";
        //Font f = new Font(Font.FontFamily.TIMES_ROMAN, 10.0f, Font.BOLD, BaseColor.BLACK);
        // Chunk c = new Chunk(numFacture, f);
        Paragraph paragraph_desc = new Paragraph(desc);
        paragraph_desc.setAlignment(Element.ALIGN_LEFT);
        document.add((Element) paragraph_desc);

        //====================================================================//

        String espace1 = "\n";

        Paragraph paragraph_espace1 = new Paragraph(espace1);
        paragraph_espace1.setAlignment(Element.ALIGN_RIGHT);
        document.add((Element) paragraph_espace1);

        //====================================================================//


        PdfPTable table = new PdfPTable(new float[] { 13, 35, 13, 13, 13, 13 });
        table.setWidthPercentage(100);
        List<String> titlesList = new ArrayList<>();

        titlesList.add("Réference");
        titlesList.add("Designation");
        titlesList.add("Quantité");
        titlesList.add("Prix HT");
        titlesList.add("Montant HT");
        titlesList.add("TVA");
        titlesList.add("");
        addHeaderRow(table, 6,titlesList);
        List<Invoices> listInvoices = new ArrayList<>();
        listInvoices.add(new Invoices("coucou la fifi " , 2.32 , 500.0 , 2.3 , 2.0 , 123654));
        listInvoices.add(new Invoices("coucou la fifi " , 2.32 , 500.0 , 2.3 , 2.0 , 123654));
        listInvoices.add(new Invoices("coucou la fifi " , 2.32 , 500.0 , 2.3 , 2.0 , 123654));
        listInvoices.add(new Invoices("coucou la fifi " , 2.32 , 500.0 , 2.3 , 2.0 , 123654));
        listInvoices.add(new Invoices("coucou la fifi " , 2.32 , 500.0 , 2.3 , 2.0 , 123654));
        listInvoices.add(new Invoices("coucou la fifi " , 2.32 , 500.0 , 2.3 , 2.0 , 123654));


        for (Invoices detailDevis : listInvoices){
            List<String> valuesList = new ArrayList<>();
            valuesList.add(detailDevis.getRéférence()+"");
            valuesList.add(detailDevis.getDésignation()+"");
            valuesList.add(detailDevis.getQuantite()+"");
            valuesList.add(detailDevis.getPrixUHT()+"");
            valuesList.add(detailDevis.getMontantHT()+"");
            valuesList.add(detailDevis.getmTVA()+"");
            valuesList.add("");
            addRow(table, 6,valuesList);
        }
        // Adds table to the doc
        document.add(table);


        //====================================================================//

        PdfPTable table_total = new PdfPTable(2);

        table_total.setWidthPercentage(40);
        table_total.setHorizontalAlignment(Element.ALIGN_RIGHT);
        addClassicRow(table_total,2,"Total HT","217.00"+"");
        addClassicRow(table_total,2,"Total TVA 7.00%","15.19");
        addClassicRow(table_total,2,"Total TTC","232.19");
        addClassicRow(table_total,2,"Net à payer (Dinar)","232.19");
        document.add(table_total);
         //====================================================================//
        String espace2 = "\n";

        Paragraph paragraph_espace2 = new Paragraph(espace2);
        paragraph_espace2.setAlignment(Element.ALIGN_RIGHT);
        document.add((Element) paragraph_espace2);

        //====================================================================//

        PdfPTable table1 = new PdfPTable(new float[] { 100 });
        table1.setWidthPercentage(100);
        List<String> titlesList1 = new ArrayList<>();

        titlesList1.add("Réglement");
        titlesList1.add("");
        addHeaderRowCenter(table1, 1,titlesList1);
        List<String> reglementList = new ArrayList<>();
        reglementList.add("- Date : 21/04/2020");



        for (String detailDevis : reglementList){
            List<String> valuesList1 = new ArrayList<>();
            valuesList1.add(reglementList.get(0)+"");
            valuesList1.add("");
            addRow(table1, 1,valuesList1);
        }
        // Adds table to the doc
        document.add(table1);


        //====================================================================//
        String espace4 = "\n";

        Paragraph paragraph_espace4 = new Paragraph(espace4);
        paragraph_espace4.setAlignment(Element.ALIGN_RIGHT);
        document.add((Element) paragraph_espace4);

        //====================================================================//
        String desc2 = "CHEQUE CLIENT DE 63 DINARS A DONNER CONTRE FACTURE";
        //Font f = new Font(Font.FontFamily.TIMES_ROMAN, 10.0f, Font.BOLD, BaseColor.BLACK);
        // Chunk c = new Chunk(numFacture, f);
        Paragraph paragraph_desc2 = new Paragraph(desc2);
        paragraph_desc2.setAlignment(Element.ALIGN_LEFT);
        document.add((Element) paragraph_desc2);

        //====================================================================//


        writer.setPageEvent(new MyFooter());
    }
    static class MyFooter extends PdfPageEventHelper {
        Font ffont = new Font(Font.FontFamily.UNDEFINED, 9, Font.ITALIC,BaseColor.LIGHT_GRAY);

        public void onEndPage(PdfWriter writer, Document document) {
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("iWASSEL SARL Rue lac manitoba, residence PLATINUM 1053 tunis Tél : 28348958"), 230, 30, 0);
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("page " + document.getPageNumber()), 550, 30, 0);
        }
    }

    public static void addClassicRow(PdfPTable table, int columns, String title, String title2) {
        // Creates another row that only have to columns.
        // The cell 5 and cell 6 width will span two columns
        // in width.
        BaseColor color = new BaseColor(246, 255, 246); // or red, green, blue, alpha
        Font boldFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);

        PdfPCell cell1 = new PdfPCell(new Phrase(title, boldFont));
        cell1.setColspan(1);
        cell1.setPadding(5);
        cell1.setBorder(Rectangle.NO_BORDER);
       // cell1.setBackgroundColor(color);//240
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell1);

        PdfPCell cell2 = new PdfPCell(new Phrase(title2, normalFont));
        cell1.setBackgroundColor(color);
        cell2.setPadding(5);
        table.addCell(cell2);

       /* PdfPCell cell3 = new PdfPCell(new Phrase(title3, normalFont));
        cell1.setBackgroundColor(color);
        cell3.setPadding(5);
        table.addCell(cell3);

        PdfPCell cell4 = new PdfPCell(new Phrase(title4, normalFont));
        cell1.setBackgroundColor(color);
        cell4.setPadding(5);
        table.addCell(cell4);

        PdfPCell cell5 = new PdfPCell(new Phrase(value, normalFont));
        cell1.setBackgroundColor(color);
        cell5.setPadding(5);
        table.addCell(cell5); */

        table.completeRow();
    }

    public static void addRow(PdfPTable table, int columns, List<String> values) {
        // Creates another row that only have to columns.
        // The cell 5 and cell 6 width will span two columns
        // in width.
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
        //table.resetColumnCount(columns);
        for (int i = 0; i < values.size()-1 ;i++){
            PdfPCell cell = new PdfPCell(new Phrase(values.get(i), normalFont));
            cell.setPadding(5);
            table.addCell(cell);
        }


        table.completeRow();
    }

    public static void addHeaderRow(PdfPTable table, int columns, List<String> titles) {
        // Creates another row that only have to columns.
        // The cell 5 and cell 6 width will span two columns
        // in width.
        BaseColor color = new BaseColor(246, 255, 246); // or red, green, blue, alpha
        Font boldFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);

        for (int i = 0; i < titles.size()-1 ;i++){
            PdfPCell cell = new PdfPCell(new Phrase(titles.get(i), boldFont));
            cell.setColspan(1);
            cell.setPadding(5);
            cell.setBackgroundColor(color);//240

            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
        }
        table.completeRow();
    }

    public static void addHeaderRowCenter(PdfPTable table, int columns, List<String> titles) {
        // Creates another row that only have to columns.
        // The cell 5 and cell 6 width will span two columns
        // in width.

        Font boldFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);

        for (int i = 0; i < titles.size()-1 ;i++){
            PdfPCell cell = new PdfPCell(new Phrase(titles.get(i), boldFont));
            cell.setColspan(1);
            cell.setPadding(5);

            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
        table.completeRow();
    }
    public static void createTwoParagrapheAtSameLine(PdfPTable table, String text,String text2){

        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(0);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        PdfPCell cell1 = new PdfPCell(new Phrase(""));
        cell1.setPadding(0);
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell1);

        PdfPCell cell2 = new PdfPCell(new Phrase(text2));
        cell2.setPadding(0);
        cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell2.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell2);


        table.completeRow();
    }
    private static void addContent(Document document) throws DocumentException {
        Anchor anchor = new Anchor("First Chapter", catFont);
        anchor.setName("First Chapter");

        // Second parameter is the number of the chapter
        Chapter catPart = new Chapter(new Paragraph(), 1);

        Paragraph subPara = new Paragraph("Subcategory 1", subFont);
        Section subCatPart = catPart.addSection(subPara);
        // subCatPart.add(new Paragraph("Hello"));

        // subPara = new Paragraph("Subcategory 2", subFont);
        // subCatPart = catPart.addSection(subPara);
        //subCatPart.add(new Paragraph("Paragraph 1"));
        // subCatPart.add(new Paragraph("Paragraph 2"));
        // subCatPart.add(new Paragraph("Paragraph 3"));

        // add a list
        // createList(subCatPart);
        Paragraph paragraph = new Paragraph();
        // addEmptyLine(paragraph, 5);
        //subCatPart.add(paragraph);

        // add a table
        createTable(subCatPart);

        // now add all this to the document
        document.add(catPart);

        // Next section
        anchor = new Anchor("Second Chapter", catFont);
        anchor.setName("Second Chapter");

        // Second parameter is the number of the chapter
        catPart = new Chapter(new Paragraph(anchor), 1);

        subPara = new Paragraph("Subcategory", subFont);
        subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("This is a very important message"));

        // now add all this to the document
        document.add(catPart);

    }

    private static void createTable(Section subCatPart)
            throws BadElementException {
        PdfPTable table = new PdfPTable(5);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(new Phrase("No"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Date"));
        c1.setNoWrap(false);
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Description"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Credit",greenFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Debit",redFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell(c1);

        table.setHeaderRows(1);

        table.addCell("1");
        table.addCell("20/8.2019");
        table.addCell("gave by vikram");
        table.addCell("2000");
        table.addCell("");


        table.addCell("2");
        table.addCell("21/8.2019");
        table.addCell("gave by vikram");
        table.addCell("4000");
        table.addCell("");

        table.addCell("2");
        table.addCell("21/8.2019");
        table.addCell("test by vikram");
        table.addCell("");
        table.addCell("4000");
        subCatPart.add(table);

    }
//    private static void createList(Section subCatPart) {
//        List list = new List(true, false, 10);
//        list.add(new ListItem("First point"));
//        list.add(new ListItem("Second point"));
//        list.add(new ListItem("Third point"));
//        subCatPart.add(list);
//    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
    public void createPdf(View view) {

        try {

            System.out.println("Common : " + Common.getAppPath(PDFReader.this)+DEST);
            PrintDocument(Common.getAppPath(PDFReader.this)+DEST);


        } catch (IOException e) {
            e.printStackTrace();
        }
  }


}
