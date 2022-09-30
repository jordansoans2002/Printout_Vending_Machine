package com.example.printout_vending_software;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import java.io.*;

public class PrintImage {
    public static void main(String[] args) throws IOException {
        new PrintImage().printImage();

    }

    public void printImage() throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            /*JFileChooser selectFile= new JFileChooser();//"H:\\");
            FileNameExtensionFilter filter=new FileNameExtensionFilter("images","jpg","png");
            selectFile.addChoosableFileFilter(filter);
            InputStream selectedFile=null;
            if(selectFile.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
                File selected=selectFile.getSelectedFile();
                selectedFile=new FileInputStream(selected.getAbsolutePath());
            }
            DocFlavor docFlavorSelected = DocFlavor.INPUT_STREAM.JPEG;
            Doc docSelected= new SimpleDoc(selectedFile,docFlavorSelected,null);*/

            File png = new File("TestPrintOptions/test files/table.png");
            InputStream pngFile = new FileInputStream("TestPrintOptions/test files/table.png");
            DocFlavor docFlavorPng = DocFlavor.INPUT_STREAM.PNG;
            Doc docPNG = new SimpleDoc(pngFile, docFlavorPng, null);

            InputStream jpgFile = new FileInputStream("TestPrintOptions/test files/monkey.JPG");
            DocFlavor docFlavorJpg = DocFlavor.INPUT_STREAM.JPEG;
            Doc docJPG = new SimpleDoc(jpgFile, docFlavorJpg, null);

            InputStream pdfFile = new FileInputStream("TestPrintOptions/outputs/testPrint.pdf");
            DocFlavor pdfFlavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            Doc docPDF = new SimpleDoc(pdfFile, pdfFlavor, null);

            InputStream wordFile = new FileInputStream("TestPrintOptions/test files/HelloWorld.doc");
            DocFlavor wordFlavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            Doc docWord = new SimpleDoc(wordFile, wordFlavor, null);

            PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
            aset.add(MediaSizeName.ISO_A10);
            //aset.add(Chromaticity.MONOCHROME);
            aset.add(OrientationRequested.REVERSE_PORTRAIT);
            aset.add(new Copies(2));
            //aset.add(new JobPriority(1));*/

            PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
            /*for (int i = 0; i < services.length; i++) {
                System.out.println((i + 1) + ". " + services[i].getName());
            }*/
            //System.out.println("Enter the printer to be used");
            //int select=Integer.parseInt(in.readLine());
            DocPrintJob job = ServiceUI.printDialog(null,0,0,services,services[2],docFlavorJpg,aset).createPrintJob();

            try {
                //job.print(docSelected, aset);
                //job.print(docPNG, aset);
                job.print(docJPG,aset);
                //job.print(docPDF,aset);
                //job.print(docWord,aset);
                //job.print(multiFiles,aset);
            } catch (PrintException e) {
                System.out.println("PrintException: " + e.getMessage());
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}

