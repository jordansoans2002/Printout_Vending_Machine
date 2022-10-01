package com.example.printout_vending_software;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;

public class PDFDocs {
    public static void main(String[] args){
        new PDFDocs();
    }

    void loadPDF() throws IOException {
        File pdfFile=new File("src/main/resources/com/example/printout_vending_software/test files/Proposal.pdf");
        PDDocument pdfDoc=PDDocument.load(pdfFile);

    }
}
