package com.alnehal.chiniotestimator.print;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.printservice.PrintJob;
import android.printservice.PrintService;
import android.util.Log;
import android.widget.Toast;

import com.alnehal.chiniotestimator.R;
import com.hp.mss.hpprint.model.PDFPrintItem;
import com.hp.mss.hpprint.model.PrintItem;
import com.hp.mss.hpprint.model.PrintJobData;
import com.hp.mss.hpprint.model.asset.PDFAsset;
import com.hp.mss.hpprint.util.PrintUtil;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfEncodings;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.interfaces.PdfRunDirection;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import model.CartModel;
import model.ExtraDetailCartModel;


/**
 * Created by Engr Shahbaz Idrees on 8/15/2016.
 */



public  class PrintForm {

    ////  Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 25, Font.BOLD);
    private static String FILE= Environment.getExternalStorageDirectory()+"/CP.pdf";

   Context context;

    float totalAmount=0;

    String totalTax="0";
    String totalExtraPh="0";

    String  totalPH="0";
    String totalTPH="0";
    String totalGrandTotal="0";

    float totalQyantity=0;
    String previousBalance, thisInvoice, currentBalnce;
    public ArrayList<CartModel> list = new ArrayList<>();
    Calendar cal;
    int day;
    int month;
    int year;

    String dateString;
    String pdfFileName;


    ArrayList<ExtraDetailCartModel> listExtraItems;
    String date;
    String oderDate;
    String packageName;
    String vrnoa;
    String noOfPersons;
    String netAmountValue;
    String foodTypeName;
    String comments;
    String taxPercent;
    public  PrintForm(Context context, ArrayList<CartModel> list, String ph, String ExtraPH, String Tax,String TPH, String pdfFileName,   ArrayList<ExtraDetailCartModel> listExtraItems,
                      String date,String oderDate,String packageName,String vrnoa,String noOfPersons, String netAmountValue,String comments, String foodTypeName, String taxPercent)
    {
        this.context=context;
        this.list=list;

        this.comments=comments;
        this.foodTypeName=foodTypeName;
        this.taxPercent=taxPercent;


        this.previousBalance=previousBalance;
        this.thisInvoice=thisInvoice;
        this.totalPH=ph;
        this.totalExtraPh=ExtraPH;
        this.totalTax=Tax;
        this.totalTPH=TPH;


       this. pdfFileName=pdfFileName;

//        cal = Calendar.getInstance();
//        day = cal.get(Calendar.DAY_OF_MONTH);
//        month = cal.get(Calendar.MONTH);
//        year = cal.get(Calendar.YEAR);
//        dateString =year + "-" + month + "-" + day;

        this.listExtraItems=listExtraItems;
        this.date=date;
        this.oderDate=oderDate;
        this.packageName=packageName;
        this.vrnoa=vrnoa;
        this.noOfPersons=noOfPersons;
        this.netAmountValue=netAmountValue;

        print();
    }



    public     void print()
    {



        File f=new File(FILE);
        if(!(f.exists()))
        {
            try {
                f.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
               // Log.e("FileCreated", "file");



            }
        }


        try
        {
            com.itextpdf.text.Document  document = new  com.itextpdf.text.Document();


            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();

            addTitlePage(document);


            document.close();

            //Toast.makeText(context, "Pdf Created",Toast.LENGTH_LONG).show();
           // pdfOpner(f);


            PDFAsset pdfAsset4x6 = new PDFAsset(FILE, false);
            PrintItem printItemDefault = new PDFPrintItem(PrintItem.ScaleType.CENTER, pdfAsset4x6);
            PrintJobData printJobData = new PrintJobData(context, printItemDefault);
            printJobData.setJobName("Document");
            PrintUtil.setPrintJobData(printJobData);
            PrintUtil.print((Activity) context);


//            //Please refer the below code :
//        PrintDocumentAdapter printAdapter = webView.CreatePrintDocumentAdapter();
//            PrintManager printManager = (PrintManager)this
//                    .GetSystemService(Context.PrintService);
//            string jobName = "example.pdf";
//            printManager.Print(jobName, printAdapter,
//                    null);
        }
        catch (Exception e)
        {
            Log.e("Errorrr", e.getMessage());
            Toast.makeText(context, "Error---in Pdf Created", Toast.LENGTH_LONG).show();
        }



    }



    public  void pdfOpner(File path)
    {

        Intent target=new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(path),"application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        Intent intent= Intent.createChooser(target,"Open File");
        try{

            context.startActivity(intent);
        }
        catch (ActivityNotFoundException e)
        {

            Toast.makeText(context,"PDF VIWER IS NOT FOUND", Toast.LENGTH_LONG).show();
        }

    }

//    private static PrintService findPrintService(String printerName) {
//
//        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
//        for (PrintService printService : printServices) {
//            if (printService.getName().trim().equals(printerName)) {
//                return printService;
//            }
//        }
//        return null;
//    }





    private  void addTitlePage(com.itextpdf.text.Document document)
            throws DocumentException, IOException {



        Font customFont=itextFontWithBold(25);


        Paragraph Header=new Paragraph(pdfFileName,customFont);
        Header.setAlignment(Element.ALIGN_CENTER);
        document.add(Header);

        Paragraph p3=new Paragraph();
                addEmptyLine(p3, 1);

        document.add(p3);

// customer table
        smallTable(document);
        // options table
        table2(document);
        // Food Type table
        orderDateTabel(document);
        // no of persons table
        noOfPersonsTabel(document);

        // remarks/commnets table
        remarksCommentTabel(document);


        // now give space from  main Table
        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 1);
        document.add(preface);


        // Add a table
        createTableNew(document);


    }




   public   void createTableNew(com.itextpdf.text.Document  document)
           throws DocumentException, IOException {

       Font customFont=itextFontWithBold(16);

//       float[] columnwidth={1,5,2,2,2};
       float[] columnwidth={1,5};
        PdfPTable table1 = new PdfPTable(columnwidth);
        table1.setWidthPercentage(100);





      // Font font = new Font(BaseFont.createFont("assets/fonts/UrdTypeb.ttf", "UTF-8", BaseFont.EMBEDDED), 20);

     // BaseFont urName = BaseFont.createFont("assets/fonts/UrduKhushKhati.ttf",BaseFont.IDENTITY_H,16,Font.BOLD);

       Font urFontName= FontFactory.getFont("assets/fonts/Jameel Noori Nastaleeq Kasheeda.ttf",BaseFont.IDENTITY_H,26,Font.BOLD);

//       BaseFont baseFont = BaseFont.createFont("assets/fonts/UrdTypeb.ttf", BaseFont.IDENTITY_H,true);
//       Font font = new Font(baseFont);
//       //document.add(new Paragraph("Not English Text",font));

       //PdfPCell c1 = new PdfPCell(new Phrase("نمبر شمار",urFontName));

       PdfPCell c1 = new PdfPCell(new Phrase("Sr#",customFont));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBackgroundColor(BaseColor.GRAY);

       c1.setBorderWidthLeft(Rectangle.NO_BORDER);
       c1.setBorderWidthRight(Rectangle.NO_BORDER);

        table1.addCell(c1);


//        c1 = new PdfPCell(new Phrase("تفصیل",customFont));
       c1 = new PdfPCell(new Phrase("Description",customFont));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBackgroundColor(BaseColor.GRAY);
       c1.setBorderWidthLeft(Rectangle.NO_BORDER);
      // c1.setBorderWidthRight(Rectangle.NO_BORDER);
        table1.addCell(c1);

//        c1 = new PdfPCell(new Phrase("Qty",customFont));
//        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        c1.setBackgroundColor(BaseColor.GRAY);
//       c1.setBorderWidthLeft(Rectangle.NO_BORDER);
//       c1.setBorderWidthRight(Rectangle.NO_BORDER);
//        table.addCell(c1);
//        table.setHeaderRows(1);
//
//        c1 = new PdfPCell(new Phrase("Rate",customFont));
//        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        c1.setBackgroundColor(BaseColor.GRAY);
//       c1.setBorderWidthLeft(Rectangle.NO_BORDER);
//       c1.setBorderWidthRight(Rectangle.NO_BORDER);
//        table.addCell(c1);
//        table.setHeaderRows(1);
//
//
//        c1 = new PdfPCell(new Phrase("Amount",customFont));
//        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        c1.setBackgroundColor(BaseColor.GRAY);
//       c1.setBorderWidthLeft(Rectangle.NO_BORDER);
//
//        table.addCell(c1);
        table1.setHeaderRows(1);
       Font customFont1=itextFontWithoutBold(13);







       for (int i=0; i<list.size(); i++) {

            int k=i;
            k+=1;


            String description = list.get(i).getItemName();



            String rate = list.get(i).getMenuRate();




            //SrNo
                c1 = new PdfPCell(new Phrase(k+"",customFont1));
                c1.setHorizontalAlignment(Element.ALIGN_LEFT);
                c1.setBorderWidthRight(Rectangle.NO_BORDER);
                table1.addCell(c1);
            //description
                c1 = new PdfPCell(new Phrase(description,customFont1));
                c1.setHorizontalAlignment(Element.ALIGN_LEFT);
                c1.setBorderWidthLeft(Rectangle.NO_BORDER);
                table1.addCell(c1);



        }



       /// Extra iTems Table


       Font customFont2=itextFontWithBold(16);

//       float[] columnwidth={1,5,2,2,2};
       float[] columnwidth1={1,5,3};
       PdfPTable table2= new PdfPTable(columnwidth1);
       table2.setWidthPercentage(100);

       //PdfPCell c1 = new PdfPCell(new Phrase("نمبر شمار",customFont));
       PdfPCell c11 = new PdfPCell(new Phrase("Sr#",customFont2));
       c11.setHorizontalAlignment(Element.ALIGN_LEFT);
       c11.setBackgroundColor(BaseColor.GRAY);

       //  c1.setBorderWidthLeft(Rectangle.NO_BORDER);
      c11.setBorderWidthRight(Rectangle.NO_BORDER);

       table2.addCell(c11);

//        c1 = new PdfPCell(new Phrase("تفصیل",customFont));
       c11 = new PdfPCell(new Phrase("Extra Items",customFont));
       c11.setHorizontalAlignment(Element.ALIGN_LEFT);
       c11.setBackgroundColor(BaseColor.GRAY);
       c11.setBorderWidthLeft(Rectangle.NO_BORDER);
        c11.setBorderWidthRight(Rectangle.NO_BORDER);
       table2.addCell(c11);


       c11 = new PdfPCell(new Phrase("Price",customFont));
       c11.setHorizontalAlignment(Element.ALIGN_LEFT);
       c11.setBackgroundColor(BaseColor.GRAY);
       c11.setBorderWidthLeft(Rectangle.NO_BORDER);
      // c11.setBorderWidthRight(Rectangle.NO_BORDER);
       table2.addCell(c11);

       table2.setHeaderRows(1);




       for (int i=0; i<listExtraItems.size(); i++) {

           int k=i;
           k+=1;

           String description = listExtraItems.get(i).getItemName();
           String rate = listExtraItems.get(i).getItemRate();
           try {

               float rate1 = Float.parseFloat(rate);
           }
           catch (Exception e)
           {
               e.getMessage();
           }
           //SrNo
           c11 = new PdfPCell(new Phrase(k+"",customFont1));
           c11.setHorizontalAlignment(Element.ALIGN_LEFT);
           c11.setBorderWidthRight(Rectangle.NO_BORDER);
           table2.addCell(c11);
           //description
           c11 = new PdfPCell(new Phrase(description,customFont1));
           c11.setHorizontalAlignment(Element.ALIGN_LEFT);
           c11.setBorderWidthLeft(Rectangle.NO_BORDER);
           c11.setBorderWidthRight(Rectangle.NO_BORDER);
           table2.addCell(c11);

           //price
           c11 = new PdfPCell(new Phrase(rate,customFont1));
           c11.setHorizontalAlignment(Element.ALIGN_LEFT);
           c11.setBorderWidthLeft(Rectangle.NO_BORDER);
            table2.addCell(c11);



       }


       // Font customFont2=itextFontWithBold(15);
//       PdfPCell c2 = new PdfPCell();
//       c2 = new PdfPCell(new Phrase("SubTotal  "+"",customFont2));
//       c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
//       //c2.setBorderWidthRight(Rectangle.NO_BORDER);
//       c2.setColspan(2);
//       c2.setRowspan(1);


//       table.addCell(c2);
//       Float num1 = new Float( totalQyantity ) ;
//       int valueQuantity= (int) Math.floor( num1 ) ;

//       c2 = new PdfPCell(new Phrase(valueQuantity+"",customFont1));
//       c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
//      // c2.setBorderWidthLeft(Rectangle.NO_BORDER);
//       c2.setBorderWidthRight(Rectangle.NO_BORDER);

//       table.addCell(c2);
//
//       c2 = new PdfPCell(new Phrase(totalAmount+"",customFont1));
//       c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
//       c2.setBorderWidthLeft(Rectangle.NO_BORDER);
//       c1.setBorderWidthRight(Rectangle.NO_BORDER);
//        c2.setColspan(2);
//       table.addCell(c2);



       try {
            document.add(table1);

               Paragraph p3=new Paragraph();
               addEmptyLine(p3, 1);
               document.add(p3);

           smallTabelPackageAmount(document);

           Paragraph p4=new Paragraph();
           addEmptyLine(p4, 3);
           document.add(p4);


           document.add(table2);
           Paragraph p5=new Paragraph();
           addEmptyLine(p5, 1);
           document.add(p5);


// if extra items available then create extra table
           if (listExtraItems.size()>0) {
               smallTabelExtraAmount(document);
           }

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 1);


       NumberToWord object=new NumberToWord();




       Float num = new Float( totalAmount ) ;
       int dollars = (int) Math.floor( num ) ;
       int cent = (int) Math.floor( ( num - dollars ) * 100.0f ) ;
//       String s =  object.convert( dollars ) + " and "
//               + object.convert( cent ) + " cents" ;

       String wordsAmount=  object.convert( dollars ) ;


//       preface.add(new Paragraph("Amount in Words:     "+wordsAmount+"  Only",customFont2));

       float grossGPS=Float.parseFloat(totalPH)+Float.parseFloat(totalExtraPh);

        preface.add(new Paragraph("Per Head : "+grossGPS,customFont2));


       preface.add(new Paragraph("Tax  "+taxPercent+"%"+"                   :  "+totalTax,customFont2));



      Font customFont3= itextFontTotalAmountOrderDate(20);
       preface.add(new Paragraph("Total Amount : "+netAmountValue,customFont3));


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

//        //Second Line
//        LineSeparator Line2 = new LineSeparator(customFont2);
//        Line2.setLineWidth(2);
//        Line2.setPercentage(25);
//        Line2.setAlignment(Element.ALIGN_CENTER);
//        document.add(Line2);

//        //Third Line
//        LineSeparator Line3 = new LineSeparator(customFont2);
//        Line3.setLineWidth(2);
//        Line3.setPercentage(25);
//        Line3.setAlignment(Element.ALIGN_RIGHT);
//        document.add(Line3);


        //Space
        Paragraph Line_SPace=new Paragraph();
        addEmptyLine(Line_SPace,1);
        document.add(Line_SPace);

        Paragraph Approveby=new Paragraph();

//               Approveby.add( new Paragraph("           Approved By",customFont2));
//              Approveby.add( new Paragraph("                                      Accountant ",customFont2));
//       Approveby.add( new Paragraph("                                      Received By",customFont2));


        Approveby.add("           Approved By");
//        Approveby.add("                                      Accountant ");
//        Approveby.add("                                      Received By");
        document.add(Approveby);


    }



    public   void smallTable(com.itextpdf.text.Document document) throws DocumentException, IOException {


        Font customFont2=itextFontWithoutBold(15);

        float[] columnwidth = {3,2};
            PdfPTable table = new PdfPTable(columnwidth);
            table.setWidthPercentage(100);

            //Customer
            PdfPCell c1 = new PdfPCell(new Phrase("Customer : "+"Walking Customer",customFont2));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(Rectangle.NO_BORDER);

            table.addCell(c1);
            //Invoice
            PdfPCell c2 = new PdfPCell(new Phrase("Invoice #   "+vrnoa,customFont2));
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(c2);


        try {
            document.add(table);


        } catch (DocumentException e) {
            e.printStackTrace();
        }



    }

    public   void orderDateTabel(com.itextpdf.text.Document document) throws DocumentException, IOException {


        Font customFont2=itextFontWithoutBold(15);
        Font customFont3=itextFontWithBold(15);
        Font customFont4= itextFontTotalAmountOrderDate(20);

        float[] columnwidth = {3,2};
        PdfPTable table = new PdfPTable(columnwidth);
        table.setWidthPercentage(100);

        //Customer
        PdfPCell c1 = new PdfPCell(new Phrase("Food Type   : "+foodTypeName,customFont2));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(Rectangle.NO_BORDER);

        table.addCell(c1);
        //Invoice
        PdfPCell c2 = new PdfPCell(new Phrase("Order: "+oderDate,customFont4));
        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
        c2.setBackgroundColor(BaseColor.GRAY);
        table.addCell(c2);


        try {
            document.add(table);


        } catch (DocumentException e) {
            e.printStackTrace();
        }



    }

    public   void noOfPersonsTabel(com.itextpdf.text.Document document) throws DocumentException, IOException {


        Font customFont2=itextFontWithoutBold(15);
        Font customFont3=itextFontWithBold(15);

        float[] columnwidth = {3};
        PdfPTable table = new PdfPTable(columnwidth);
        table.setWidthPercentage(100);

        //Customer
        PdfPCell c1 = new PdfPCell(new Phrase("No Of Persons   : "+noOfPersons,customFont2));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(Rectangle.NO_BORDER);

        table.addCell(c1);
        //Invoice
//        PdfPCell c2 = new PdfPCell(new Phrase("check: "+oderDate,customFont3));
//        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
//        table.addCell(c2);


        try {
            document.add(table);


        } catch (DocumentException e) {
            e.printStackTrace();
        }



    }
    public   void remarksCommentTabel(com.itextpdf.text.Document document) throws DocumentException, IOException {


        Font customFont2=itextFontWithoutBold(15);
        Font customFont3=itextFontWithBold(15);

        float[] columnwidth = {3};
        PdfPTable table = new PdfPTable(columnwidth);
        table.setWidthPercentage(100);

        //Customer
        PdfPCell c1 = new PdfPCell(new Phrase("Remarks   : "+comments,customFont2));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(Rectangle.NO_BORDER);

        table.addCell(c1);
        //Invoice
//        PdfPCell c2 = new PdfPCell(new Phrase("check: "+oderDate,customFont3));
//        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
//        table.addCell(c2);


        try {
            document.add(table);


        } catch (DocumentException e) {
            e.printStackTrace();
        }



    }


    public  void table2(com.itextpdf.text.Document document) throws DocumentException, IOException {
        Font customFont2=itextFontWithoutBold(15);

        float[] columnwidth = {3,2};
        PdfPTable table = new PdfPTable(columnwidth);
        table.setWidthPercentage(100);

        //Invoice#
        PdfPCell c3 = new PdfPCell(new Phrase("Options    :  "+packageName,customFont2));
        c3.setHorizontalAlignment(Element.ALIGN_LEFT);
        c3.setBorder(Rectangle.NO_BORDER);
        table.addCell(c3);
        //Date
        PdfPCell c4 = new PdfPCell(new Phrase("Date:  "+date,customFont2));
        c4.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c4);


        try {
            document.add(table);


        } catch (DocumentException e) {
            e.printStackTrace();
        }


    }


    public  void smallTabelPackageAmount(com.itextpdf.text.Document document) throws DocumentException, IOException {
        Font customFont2=itextFontWithoutBold(15);

        float[] columnwidth = {1};
        PdfPTable table = new PdfPTable(columnwidth);
        table.setWidthPercentage(50);

        //Per Head
        PdfPCell c4 = new PdfPCell(new Phrase("Per Head:         "+totalPH,customFont2));
        c4.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c4);

        try {
            document.add(table);


        } catch (DocumentException e) {
            e.printStackTrace();
        }


    }

    public  void smallTabelExtraAmount(com.itextpdf.text.Document document) throws DocumentException, IOException {
        Font customFont2=itextFontWithoutBold(15);

        float[] columnwidth = {1};
        PdfPTable table = new PdfPTable(columnwidth);
        table.setWidthPercentage(50);

        //Extra
        PdfPCell c4 = new PdfPCell(new Phrase("Extra:         "+totalExtraPh,customFont2));
        c4.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c4);

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


    public Font itextFontTotalAmountOrderDate(int fontSize) throws DocumentException, IOException {

        BaseFont fontPath = null;

        fontPath = BaseFont.createFont("assets/fonts/Calibri.ttf", "UTF-8", BaseFont.EMBEDDED);


        Font font = new Font(fontPath, fontSize);

        font.isBold();

        return  font;

    }




    public Font itextFontWithBold(int fontSize) throws DocumentException, IOException {

        BaseFont fontPath = null;


               // fontPath = BaseFont.createFont("assets/fonts/Nafees Nastaleeq v1.02.ttf", "UTF-8", BaseFont.EMBEDDED);

        fontPath = BaseFont.createFont("assets/fonts/Calibri.ttf", "UTF-8", BaseFont.EMBEDDED);


        Font font = new Font(fontPath, fontSize);

        font.isBold();

        return  font;

    }


    public Font itextFontWithoutBold(int fontSize) throws DocumentException, IOException {

        BaseFont fontPath= null;

        fontPath = BaseFont.createFont("assets/fonts/Calibri.ttf", "UTF-8", BaseFont.EMBEDDED);


        Font font = new Font(fontPath, fontSize);
        return  font;

    }

}
