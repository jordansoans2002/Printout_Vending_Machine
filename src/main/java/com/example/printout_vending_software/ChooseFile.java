package com.example.printout_vending_software;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;

public class ChooseFile {
    public static void main(String []args){
        new ChooseFile().printSelectedFile();
    }

    void printSelectedFile() {
        try {
            String fileDirectory = "C:\\Users\\chris\\Downloads\\Printout_Vending_Machine-learn-printing\\Printout_Vending_Machine-learn-printing\\src\\main\\resources\\com\\example\\printout_vending_software\\test files";
            File startingDirectory = new File(fileDirectory);
            JFileChooser selectFile = new JFileChooser(fileDirectory);//"H:\\");
            FileNameExtensionFilter filterSupported = new FileNameExtensionFilter("supported types", "jpg", "png", "pdf", "doc", "docx");
            FileNameExtensionFilter filterImages = new FileNameExtensionFilter("images", "jpg", "png", "jfif");
            FileNameExtensionFilter filterDocs = new FileNameExtensionFilter("documents", "pdf", "doc", "docx");
            String supported = "jpg png pdf doc docx JPG PNG PDF DOC DOCX";
            String ext="";
            InputStream selectedFile = null;
            PDDocument pdfDoc = null;
            while (true) {
                selectFile.setCurrentDirectory(startingDirectory);
                selectFile.setFileFilter(filterSupported);
                selectFile.addChoosableFileFilter(filterImages);
                selectFile.addChoosableFileFilter(filterDocs);
                int selection = selectFile.showOpenDialog(null);
                if (selection == JFileChooser.APPROVE_OPTION) {
                    File selected = selectFile.getSelectedFile();
                    String path = selected.getAbsolutePath();
                    ext = path.substring(path.lastIndexOf(".")+1);
                    if (path.startsWith(fileDirectory) && supported.contains(ext)) {
                        selectedFile = new FileInputStream(path);
                        if(ext.equalsIgnoreCase("pdf"))
                            pdfDoc=PDDocument.load(selectedFile);
                        break;
                    }
                } else if (selection == JFileChooser.CANCEL_OPTION)
                    break;
            }

            PrintRequestAttributeSet aset=new HashPrintRequestAttributeSet();
            aset.add(MediaSizeName.ISO_A10);
            aset.add(Chromaticity.MONOCHROME);
            //aset.add(OrientationRequested.LANDSCAPE);
            aset.add(new Copies(1));
            //aset.add(new JobPriority(5));

            PrintService[] services= PrintServiceLookup.lookupPrintServices(null,null);
            DocPrintJob job= ServiceUI.printDialog(null,0,0,services,services[3],null,aset).createPrintJob();

            if(!ext.equalsIgnoreCase("pdf")){
                DocFlavor docFlavorSelected = DocFlavor.INPUT_STREAM.JPEG;
                Doc docSelected = new SimpleDoc(selectedFile, docFlavorSelected, null);
                try{
                    job.print(docSelected,aset);
                }catch (PrintException e){
                    System.out.println("print exception "+ e.getMessage());
                }
            }
            else{
                PrinterJob pdfPrintJob = PrinterJob.getPrinterJob();
                pdfPrintJob.setPageable(new PDFPageable(pdfDoc));
                try {
                    pdfPrintJob.setPrintService(job.getPrintService());
                    pdfPrintJob.print();
                } catch (PrinterException e) {
                    e.printStackTrace();
                }
            }

        } catch(IOException  e){
            e.printStackTrace();
        }
    }
}
