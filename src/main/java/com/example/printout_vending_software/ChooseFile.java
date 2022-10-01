package com.example.printout_vending_software;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ChooseFile {
    public static void main(String []args){
        new ChooseFile().printSelectedFile();
    }

    void printSelectedFile(){
        try {
            String fileDirectory = "C:\\Users\\Hanniel\\Desktop\\Jordan\\Printout vending software\\src\\main\\resources\\com\\example\\printout_vending_software\\test files";
            File startingDirectory = new File(fileDirectory);
            JFileChooser selectFile = new JFileChooser(fileDirectory);//"H:\\");
            FileNameExtensionFilter filterSupported = new FileNameExtensionFilter("supported types", "jpg", "png", "pdf", "doc", "docx");
            FileNameExtensionFilter filterImages = new FileNameExtensionFilter("images", "jpg", "png", "jfif");
            FileNameExtensionFilter filterDocs = new FileNameExtensionFilter("documents", "pdf", "doc", "docx");
            String supported = "jpg png pdf doc docx JPG PNG PDF DOC DOCX";
            InputStream selectedFile = null;

            while (true) {
                selectFile.setCurrentDirectory(startingDirectory);
                selectFile.setFileFilter(filterSupported);
                selectFile.addChoosableFileFilter(filterImages);
                selectFile.addChoosableFileFilter(filterDocs);
                int selection = selectFile.showOpenDialog(null);
                if (selection == JFileChooser.APPROVE_OPTION) {
                    File selected = selectFile.getSelectedFile();
                    String path = selected.getAbsolutePath();
                    String ext = path.substring(path.lastIndexOf(".")+1);
                    if (path.startsWith(fileDirectory) && supported.contains(ext)) {
                        selectedFile = new FileInputStream(path);
                        break;
                    }
                } else if (selection == JFileChooser.CANCEL_OPTION)
                    break;
            }
            DocFlavor docFlavorSelected = DocFlavor.INPUT_STREAM.JPEG;
            Doc docSelected = new SimpleDoc(selectedFile, docFlavorSelected, null);

            PrintRequestAttributeSet aset=new HashPrintRequestAttributeSet();
            aset.add(MediaSizeName.ISO_A10);
            aset.add(Chromaticity.MONOCHROME);
            aset.add(OrientationRequested.LANDSCAPE);
            aset.add(new Copies(2));
            aset.add(new JobPriority(5));

            PrintService[] services= PrintServiceLookup.lookupPrintServices(null,null);
            DocPrintJob job= ServiceUI.printDialog(null,0,0,services,services[2],null,aset).createPrintJob();

            try{
                job.print(docSelected,aset);
            }catch (PrintException e){
                System.out.println("print exception "+ e.getMessage());
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }
}
