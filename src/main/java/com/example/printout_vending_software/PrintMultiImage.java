package com.example.printout_vending_software;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import java.io.*;

//print multiple images in the same document
public class PrintMultiImage {
    public static void main(String[] args) throws IOException {
        new PrintMultiImage().printImage();

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

            InputStream pngFile = new FileInputStream("TestPrintOptions/test files/table.png");
            DocFlavor docFlavorPng = DocFlavor.INPUT_STREAM.PNG;
            Doc docPNG = new SimpleDoc(pngFile, docFlavorPng, null);

            InputStream jpgFile = new FileInputStream("TestPrintOptions/test files/monkey.JPG");
            DocFlavor docFlavorJpg = DocFlavor.INPUT_STREAM.JPEG;
            Doc docJPG = new SimpleDoc(jpgFile, docFlavorJpg, null);

            PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
            aset.add(MediaSizeName.ISO_A10);
            //aset.add(Chromaticity.MONOCHROME);
            aset.add(OrientationRequested.REVERSE_PORTRAIT);
            aset.add(new Copies(2));
            //aset.add(new JobPriority(1));*/

            MultiDocPrintService[] multiDocPrintServices = PrintServiceLookup.lookupMultiDocPrintServices(
                    null,
                    null
            );
            for (int i = 0; i < multiDocPrintServices.length; i++) {
                System.out.println((i + 1) + ". " + multiDocPrintServices[i].getName());
            }
            System.out.println("Enter the printer to be used");
            int select=Integer.parseInt(in.readLine());
            MultiDocPrintJob multiJob =multiDocPrintServices[select-1].createMultiDocPrintJob();
            PrintService s = PrintServiceLookup.lookupPrintServices(null,null)[2];
            //MultiDocPrintJob multiJob = (MultiDocPrintJob) s.createPrintJob();
            //MultiDocPrintJob multiJob = multiDocPrintServices[0].createMultiDocPrintJob();
            HandleMultiDoc pg1 = new HandleMultiDoc(pngFile);
            HandleMultiDoc pg2 = new HandleMultiDoc(jpgFile);
            HandleMultiDoc pg3 = new HandleMultiDoc(pngFile);
            pg1.next = pg2;
            pg2.next = pg3;
            try {
                multiJob.print(pg1, aset);
            } catch (PrintException e) {
                System.out.println("PrintException: " + e.getMessage());
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }


    static class HandleMultiDoc implements MultiDoc {
        Doc doc = null;
        PrintMultiImage.HandleMultiDoc next = null;

        HandleMultiDoc(InputStream f) {
            doc = new SimpleDoc(f, DocFlavor.INPUT_STREAM.JPEG, null);
        }

        @Override
        public Doc getDoc() throws IOException {
            return doc;
        }

        @Override
        public MultiDoc next() throws IOException {
            return next;
        }
    }
}
