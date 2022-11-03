import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;

public class PDFDocs {
    public static void main(String[] args){
        new PDFDocs().loadPDF();
    }

    void loadPDF() {
        try {
            File pdfFile=new File("src/main/resources/Test files/Proposal.pdf");
            PDDocument pdfDoc=PDDocument.load(pdfFile);
            PrintService[] services= PrintServiceLookup.lookupPrintServices(null,null);
            //DocPrintJob job= ServiceUI.printDialog(null,0,0,services,services[2],null,null).createPrintJob();

            for(int i=0;i<services.length;i++){
                System.out.println(services[i].getName());
            }
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPageable(new PDFPageable(pdfDoc));
            job.setPrintService(services[2]);
            job.print();
            System.out.println("printed");
        } catch (PrinterException | IOException e) {
            e.printStackTrace();
        }

    }
}
