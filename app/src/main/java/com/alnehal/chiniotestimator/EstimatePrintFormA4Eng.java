package com.alnehal.chiniotestimator;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.hp.mss.hpprint.model.PDFPrintItem;
import com.hp.mss.hpprint.model.PrintItem;
import com.hp.mss.hpprint.model.PrintJobData;
import com.hp.mss.hpprint.model.asset.PDFAsset;
import com.hp.mss.hpprint.util.PrintUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import model.ItemModel;


/**
 * Created by Engr Shahbaz Idrees on 8/15/2016.
 */


public class EstimatePrintFormA4Eng {

    ////  Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 25, Font.BOLD);
    private static String FILE = "";

    private Context context;
    private double totalAmount = 0;
    private double totalDiscount = 0;
    private int totalQyantity = 0;
    private int noOfPersons = 0;
    private String orderDate, partyName, orderAmount;
    private ArrayList<ItemModel> itemModelArrayList = new ArrayList<>();
    private Calendar cal;
    private int day;
    private int month;
    private int year;

    String dateString;
    String pdfFileName;

    public EstimatePrintFormA4Eng(Context context, ArrayList<ItemModel> itemModelArrayList, String orderDate,
                                  String pdfFileName, String partyName, String orderAmount, int noOfPersons) {
        this.context = context;
        this.itemModelArrayList = itemModelArrayList;

        this.orderDate = orderDate;
        this.pdfFileName = pdfFileName;
        this.partyName = partyName;
        this.orderAmount = orderAmount;
        this.noOfPersons = noOfPersons;

        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        dateString = year + "-" + month + "-" + day;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String currentTimeStamp = dateFormat.format(new Date());
        FILE = Environment.getExternalStorageDirectory() + "/" + currentTimeStamp + ".pdf";
        print();
    }


    public void print() {


        File f = new File(FILE);
        if (!(f.exists())) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                // Log.e("FileCreated", "file");


            }
        }


        try {
            Document document
                    = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();

            addTitlePage(document);

            document.close();

            //Toast.makeText(context, "Pdf Created",Toast.LENGTH_LONG).show();
//            pdfOpner(f);

/*Intent pluginIntent = new Intent(context, PrintPluginManagerActivity.class);
            context.startActivity(pluginIntent);*/

            PDFAsset pdfAsset4x6 = new PDFAsset(FILE, false);
            PrintItem printItemDefault = new PDFPrintItem(PrintItem.ScaleType.CENTER, pdfAsset4x6);
            PrintJobData printJobData = new PrintJobData(context, printItemDefault);
            printJobData.setJobName("Example");
            PrintUtil.setPrintJobData(printJobData);
            PrintUtil.print((Activity) context);
        } catch (Exception e) {
            Log.e("Errorrr", e.getMessage());
            Toast.makeText(context, "Error---in Pdf Created", Toast.LENGTH_LONG).show();
        }


    }


    public void pdfOpner(File path) {

        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(path), "application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        Intent intent = Intent.createChooser(target, "Open File");
        try {

            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {

            Toast.makeText(context, "PDF VIWER IS NOT FOUND", Toast.LENGTH_LONG).show();
        }

    }


    private void addTitlePage(com.itextpdf.text.Document document)
            throws DocumentException, IOException {


        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        Image myImg = null;
        try {
            myImg = Image.getInstance(stream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                - document.rightMargin() - 0) / myImg.getWidth()) * 100;

        myImg.scalePercent(scaler);
//        myImg.setAlignment(Image.MIDDLE);
        // set Rotation
       /* myImg.setAlignment(Image.MIDDLE);
        myImg.setAbsolutePosition(PageSize.A4.getWidth() / 2, PageSize.A4.getHeight() / 6);*/

        // add image to document
        //document.add(myImg);


        Font customFont = itextFontWithBold(12);


        Paragraph Header = new Paragraph(pdfFileName, customFont);
        Header.setAlignment(Element.ALIGN_CENTER);
        document.add(Header);

        Paragraph p3 = new Paragraph();
        addEmptyLine(p3, 1);

        document.add(p3);
        orderDetails(document);
//        table2(document);
        Paragraph preface = new Paragraph();


        addEmptyLine(preface, 1);


        document.add(preface);

        // Add a table
        createTableNew(document);


    }


    public void createTableNew(com.itextpdf.text.Document document)
            throws DocumentException, IOException {

        Font customFont = itextFontWithBold(10);

        float[] columnwidth = {1, 8, 2, 2, 3};
        PdfPTable table = new PdfPTable(columnwidth);
        table.setWidthPercentage(100);


        PdfPCell c1 = new PdfPCell(new Phrase("Sr#", customFont));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setPaddingTop(4);
        c1.setPaddingBottom(4);
        /*c1.setPaddingTop(5);
        c1.setPaddingBottom(5);
        c1.setPaddingLeft(3);
        c1.setPaddingRight(3);*/
        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);

        //  c1.setBorderWidthLeft(Rectangle.NO_BORDER);
        c1.setBorderWidthRight(Rectangle.NO_BORDER);

        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Item", customFont));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c1.setPaddingTop(4);
        c1.setPaddingBottom(4);/*c1.setPaddingTop(5);
        c1.setPaddingBottom(5);
        c1.setPaddingLeft(3);
        c1.setPaddingRight(3);*/
        c1.setBorderWidthLeft(Rectangle.NO_BORDER);
        c1.setBorderWidthRight(Rectangle.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("UOM", customFont));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c1.setPaddingTop(4);
        c1.setPaddingBottom(4);/*c1.setPaddingTop(5);
        c1.setPaddingBottom(5);
        c1.setPaddingLeft(3);
        c1.setPaddingRight(3);*/
        c1.setBorderWidthLeft(Rectangle.NO_BORDER);
        c1.setBorderWidthRight(Rectangle.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Qty", customFont));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c1.setPaddingTop(4);
        c1.setPaddingBottom(4);/* c1.setPaddingTop(5);
        c1.setPaddingBottom(5);
        c1.setPaddingLeft(3);
        c1.setPaddingRight(3);*/
        c1.setBorderWidthLeft(Rectangle.NO_BORDER);
        c1.setBorderWidthRight(Rectangle.NO_BORDER);
        table.addCell(c1);
        table.setHeaderRows(1);

        /*c1 = new PdfPCell(new Phrase("Rate", customFont));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c1.setPaddingTop(4);
        c1.setPaddingBottom(4);*//*c1.setPaddingTop(5);
        c1.setPaddingBottom(5);
        c1.setPaddingLeft(3);
        c1.setPaddingRight(3);*//*
        c1.setBorderWidthLeft(Rectangle.NO_BORDER);
        c1.setBorderWidthRight(Rectangle.NO_BORDER);
        table.addCell(c1);
        table.setHeaderRows(1);
*/
        c1 = new PdfPCell(new Phrase("Amount", customFont));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c1.setPaddingTop(4);
        c1.setPaddingBottom(4); /*c1.setPaddingTop(5);
        c1.setPaddingBottom(5);
        c1.setPaddingLeft(3);
        c1.setPaddingRight(3);*/
        c1.setBorderWidthLeft(Rectangle.NO_BORDER);
        table.addCell(c1);
        table.setHeaderRows(1);

       /* c1 = new PdfPCell(new Phrase("Discount", customFont));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c1.setBorderWidthLeft(Rectangle.NO_BORDER);
        c1.setPaddingTop(4);
        c1.setPaddingBottom(4);*//* c1.setPaddingTop(5);
        c1.setPaddingBottom(5);
        c1.setPaddingLeft(3);
        c1.setPaddingRight(3);*//*
        c1.setBorderWidthRight(Rectangle.NO_BORDER);
        table.addCell(c1);
        table.setHeaderRows(1);

        c1 = new PdfPCell(new Phrase("Net Amount", customFont));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.setPaddingTop(4);
        c1.setPaddingBottom(4);*//*c1.setPaddingTop(5);
        c1.setPaddingBottom(5);
        c1.setPaddingLeft(3);
        c1.setPaddingRight(3);*//*
        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c1.setBorderWidthLeft(Rectangle.NO_BORDER);
        table.addCell(c1);
        table.setHeaderRows(1);*/

        Font customFont1 = itextFontWithoutBold(10);


        ItemModel itemModel = null;

        for (int i = 0; i < itemModelArrayList.size(); i++) {

            int k = i;
            k += 1;
            itemModel = itemModelArrayList.get(i);

            String itemName = itemModel.getName();
            String uom = itemModel.getUom();

            int qty = (int) itemModel.getItemCartQty();
            double amount = itemModel.getAmountTotal();

            totalAmount += amount;
            totalQyantity += qty;

            //SrNo
            c1 = new PdfPCell(new Phrase((i + 1) + "", customFont1));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setPaddingTop(4);
            c1.setPaddingBottom(4);  /*c1.setPaddingTop(5);
            c1.setPaddingBottom(5);
            c1.setPaddingLeft(3);
            c1.setPaddingRight(3);*/
            c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c1.setBorderWidthRight(Rectangle.NO_BORDER);
            table.addCell(c1);
            //itemName
            c1 = new PdfPCell(new Phrase(itemName, customFont1));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setPaddingTop(4);
            c1.setPaddingBottom(4); /* c1.setPaddingTop(5);
            c1.setPaddingBottom(5);
            c1.setPaddingLeft(3);
            c1.setPaddingRight(3);*/
            c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c1.setBorderWidthLeft(Rectangle.NO_BORDER);
            c1.setBorderWidthRight(Rectangle.NO_BORDER);
            table.addCell(c1);

            //uom
            c1 = new PdfPCell(new Phrase(uom, customFont1));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setPaddingTop(4);
            c1.setPaddingBottom(4); /* c1.setPaddingTop(5);
            c1.setPaddingBottom(5);
            c1.setPaddingLeft(3);
            c1.setPaddingRight(3);*/
            c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c1.setBorderWidthLeft(Rectangle.NO_BORDER);
            c1.setBorderWidthRight(Rectangle.NO_BORDER);
            table.addCell(c1);

            //qty
            c1 = new PdfPCell(new Phrase(qty + "", customFont1));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c1.setPaddingTop(4);
            c1.setPaddingBottom(4);/* c1.setPaddingTop(5);
            c1.setPaddingBottom(5);
            c1.setPaddingLeft(3);
            c1.setPaddingRight(3);*/
            c1.setBorderWidthLeft(Rectangle.NO_BORDER);
            c1.setBorderWidthRight(Rectangle.NO_BORDER);
            table.addCell(c1);
           /* //Rate
            c1 = new PdfPCell(new Phrase("999999999.9", customFont1));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c1.setPaddingTop(4);
            c1.setPaddingBottom(4);*//*  c1.setPaddingTop(5);
            c1.setPaddingBottom(5);
            c1.setPaddingLeft(3);
            c1.setPaddingRight(3);*//*
            c1.setBorderWidthLeft(Rectangle.NO_BORDER);
            c1.setBorderWidthRight(Rectangle.NO_BORDER);
            table.addCell(c1);*/
            //Amount
            c1 = new PdfPCell(new Phrase(amount + "", customFont1));
            c1.setPaddingTop(4);
            c1.setPaddingBottom(4); /* c1.setPaddingTop(5);
            c1.setPaddingBottom(5);
            c1.setPaddingLeft(3);
            c1.setPaddingRight(3);*/
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c1.setBorderWidthLeft(Rectangle.NO_BORDER);
            table.addCell(c1);
            /*//Discount
            c1 = new PdfPCell(new Phrase("999999999.9", customFont1));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            c1.setPaddingTop(4);
            c1.setPaddingBottom(4); *//*  c1.setPaddingTop(5);
            c1.setPaddingBottom(5);
            c1.setPaddingLeft(3);
            c1.setPaddingRight(3);*//*
            c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c1.setBorderWidthLeft(Rectangle.NO_BORDER);
            c1.setBorderWidthRight(Rectangle.NO_BORDER);
            table.addCell(c1);
            //NetAmount
            c1 = new PdfPCell(new Phrase(("999999999.9") + "", customFont1));
            c1.setPaddingTop(4);
            c1.setPaddingBottom(4);  *//*c1.setPaddingTop(5);
            c1.setPaddingBottom(5);
            c1.setPaddingLeft(3);
            c1.setPaddingRight(3);*//*
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c1.setBorderWidthLeft(Rectangle.NO_BORDER);
            table.addCell(c1);*/


        }
        Font customFont2 = itextFontWithBold(10);
        PdfPCell c2 = new PdfPCell();
        c2 = new PdfPCell(new Phrase("SubTotal  " + "", customFont2));
        c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.setPaddingTop(4);
        c1.setPaddingBottom(4);/*    c2.setPaddingTop(5);
        c2.setPaddingBottom(5);
        c2.setPaddingLeft(3);
        c2.setPaddingRight(3);*/
        c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c2.setColspan(3);
        c2.setRowspan(1);


        table.addCell(c2);
        Float num1 = new Float(totalQyantity);
        int valueQuantity = (int) Math.floor(num1);

        c2 = new PdfPCell(new Phrase(totalQyantity + "", customFont2));
        c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.setPaddingTop(4);
        c1.setPaddingBottom(4); /* c2.setPaddingTop(5);
        c2.setPaddingBottom(5);
        c2.setPaddingLeft(3);
        c2.setPaddingRight(3);*/
        c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c2.setBorderWidthLeft(Rectangle.NO_BORDER);
        c2.setBorderWidthRight(Rectangle.NO_BORDER);
        table.addCell(c2);

        c2 = new PdfPCell(new Phrase(totalAmount + "", customFont2));
        c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.setPaddingTop(4);
        c1.setPaddingBottom(4);/*  c2.setPaddingTop(5);
        c2.setPaddingBottom(5);
        c2.setPaddingLeft(3);
        c2.setPaddingRight(3);*/
        c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c2.setBorderWidthLeft(Rectangle.NO_BORDER);
        table.addCell(c2);

      /*  c2 = new PdfPCell(new Phrase("999999999.9", customFont2));
        c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.setPaddingTop(4);
        c1.setPaddingBottom(4);*//* c2.setPaddingTop(5);
        c2.setPaddingBottom(5);
        c2.setPaddingLeft(3);
        c2.setPaddingRight(3);*//*
        c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c2.setBorderWidthLeft(Rectangle.NO_BORDER);
        c2.setBorderWidthRight(Rectangle.NO_BORDER);
        table.addCell(c2);

        c2 = new PdfPCell(new Phrase("999999999.9", customFont2));
        c1.setPaddingTop(4);
        c1.setPaddingBottom(4);   *//*     c2.setPaddingTop(5);
        c2.setPaddingBottom(5);
        c2.setPaddingLeft(3);
        c2.setPaddingRight(3);*//*
        c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c2.setBorderWidthLeft(Rectangle.NO_BORDER);
        table.addCell(c2);*/


        try {
            document.add(table);

        } catch (DocumentException e) {
            e.printStackTrace();
        }

      /*  Paragraph preface = new Paragraph();
        addEmptyLine(preface, 1);


        NumberToWord object = new NumberToWord();


        Float num = new Float(totalAmount - totalDiscount);
        int dollars = (int) Math.floor(num);
        int cent = (int) Math.floor((num - dollars) * 100.0f);
//       String s =  object.convert( dollars ) + " and "
//               + object.convert( cent ) + " cents" ;

        String wordsAmount = object.convert(dollars);


        preface.add(new Paragraph("Amount in Words:     " + wordsAmount + "  Only", customFont2));

        preface.add(new Paragraph("Previous Balance :     " + "2300000", customFont2));

        preface.add(new Paragraph("This Invoice:                " + totalAmount + "", customFont2));

        preface.add(new Paragraph("Current Balance  :      " + "2307644", customFont2));


        addEmptyLine(preface, 3);

        try {
            document.add(preface);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        addEmptyLine(preface, 1);

        //First Line
        LineSeparator Line1 = new LineSeparator(customFont2);

        Line1.setLineWidth(2);
        Line1.setPercentage(25);
        Line1.setAlignment(Element.ALIGN_LEFT);
        document.add(Line1);

        //Second Line
        LineSeparator Line2 = new LineSeparator(customFont2);
        Line2.setLineWidth(2);
        Line2.setPercentage(25);
        Line2.setAlignment(Element.ALIGN_CENTER);
        document.add(Line2);

        //Third Line
        LineSeparator Line3 = new LineSeparator(customFont2);
        Line3.setLineWidth(2);
        Line3.setPercentage(25);
        Line3.setAlignment(Element.ALIGN_RIGHT);
        document.add(Line3);


        //Space
        Paragraph Line_SPace = new Paragraph();
        addEmptyLine(Line_SPace, 1);
        document.add(Line_SPace);

        Paragraph Approveby = new Paragraph();

//               Approveby.add( new Paragraph("           Approved By",customFont2));
//              Approveby.add( new Paragraph("                                      Accountant ",customFont2));
//       Approveby.add( new Paragraph("                                      Received By",customFont2));


        Approveby.add("           Approved By");
        Approveby.add("                                      Accountant ");
        Approveby.add("                                      Received By");
        document.add(Approveby);
*/

    }


    public void orderDetails(com.itextpdf.text.Document document) throws DocumentException, IOException {


        Font customFont2 = itextFontWithoutBold(12);

        float[] columnwidth = {2, 2, 3};
        PdfPTable table = new PdfPTable(columnwidth);
        table.setWidthPercentage(100);

        //Customer
        PdfPCell c1 = new PdfPCell(new Phrase("Customer:", customFont2));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(Rectangle.NO_BORDER);
        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(partyName, customFont2));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.setBorder(Rectangle.NO_BORDER);
        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(c1);

        //Invoice
        c1 = new PdfPCell(new Phrase("Date: " + orderDate, customFont2));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c1.setBorder(Rectangle.NO_BORDER);
        table.addCell(c1);

        float[] columnwidth2 = {1, 1};
        PdfPTable table2 = new PdfPTable(columnwidth2);

        c1 = new PdfPCell(new Phrase("Persons:", customFont2));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c1.setBorder(Rectangle.NO_BORDER);
        table2.addCell(c1);

        c1 = new PdfPCell(new Phrase("" + noOfPersons, customFont2));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c1.setBorder(Rectangle.NO_BORDER);
        table2.addCell(c1);

        /*c1 = new PdfPCell(new Phrase("Order Discount :", customFont2));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c1.setBorder(Rectangle.NO_BORDER);
        table2.addCell(c1);

        c1 = new PdfPCell(new Phrase(String.format("%.1f", Double.parseDouble(orderDiscount)), customFont2));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c1.setBorder(Rectangle.NO_BORDER);
        table2.addCell(c1);
*/
        /*c1 = new PdfPCell(new Phrase("Final Order Amount :", customFont2));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c1.setBorder(Rectangle.NO_BORDER);
        table2.addCell(c1);

        c1 = new PdfPCell(new Phrase(String.format("%.2f", totalAmount, customFont2)));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c1.setBorder(Rectangle.NO_BORDER);
        table2.addCell(c1);
*/
        c1 = new PdfPCell(table2);
        c1.setBorderWidth(Rectangle.NO_BORDER);
        c1.setColspan(2);
        table.addCell(c1);

        c1 = new PdfPCell();
        c1.setBorderWidth(Rectangle.NO_BORDER);
        table.addCell(c1);

        try {
            document.add(table);


        } catch (DocumentException e) {
            e.printStackTrace();
        }


    }

    public void table2(com.itextpdf.text.Document document) throws DocumentException, IOException {
        Font customFont2 = itextFontWithoutBold(12);

        float[] columnwidth = {1, 1};
        PdfPTable table = new PdfPTable(columnwidth);
        table.setWidthPercentage(100);

        //Amount#
        PdfPCell c3 = new PdfPCell(new Phrase("Amount :  " + orderAmount, customFont2));
        c3.setHorizontalAlignment(Element.ALIGN_LEFT);
        c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c3.setBorder(Rectangle.NO_BORDER);
        table.addCell(c3);

        //Amount#
        c3 = new PdfPCell(new Phrase("Discount :  " + orderAmount, customFont2));
        c3.setHorizontalAlignment(Element.ALIGN_LEFT);
        c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c3.setBorder(Rectangle.NO_BORDER);
        table.addCell(c3);

        try {
            document.add(table);


        } catch (DocumentException e) {
            e.printStackTrace();
        }


    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }


    }


    public Font itextFontWithBold(int fontSize) throws DocumentException, IOException {

        BaseFont fontPath = null;

        fontPath = BaseFont.createFont("assets/fonts/Roboto-Bold.ttf", "UTF-8", BaseFont.EMBEDDED);


        Font font = new Font(fontPath, fontSize);

        font.isBold();

        return font;

    }


    public Font itextFontWithoutBold(int fontSize) throws DocumentException, IOException {

        BaseFont fontPath = null;

        fontPath = BaseFont.createFont("assets/fonts/Roboto-Light.ttf", "UTF-8", BaseFont.EMBEDDED);


        Font font = new Font(fontPath, fontSize);
        return font;

    }

}
