import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.util.Map;

public class PrintPdf {
    public static void printPdf(byte[] byteArrayOutput, Map<String,String> properties) {
        try {
            PDDocument pdfDoc=PDDocument.load(byteArrayOutput);
            PrintService[] services= PrintServiceLookup.lookupPrintServices(null,null);
            //DocPrintJob job= ServiceUI.printDialog(null,0,0,services,services[2],null,null).createPrintJob();

            for(int i=0;i<services.length;i++){
                System.out.println(services[i].getName());
            }

            int copies=1;
            if(properties!=null) {
                copies = Integer.parseInt(properties.get("copies"));
                if (copies < 1) copies = 1;
            }
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPageable(new PDFPageable(pdfDoc));
            job.setPrintService(services[2]);
            job.setCopies(copies);
            job.print();
            pdfDoc.close();
            System.out.println("printed");
        } catch (PrinterException | IOException e) {
            e.printStackTrace();
        }

    }
}