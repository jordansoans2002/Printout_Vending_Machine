import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.*;
import java.io.*;

public class PrintImage {
    public static void main(String[] args) throws IOException {
        new PrintImage().printImage();
    }

    public void printImage() throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            String pngPath="src/main/resources/com/example/printout_vending_software/test files/feature board.PNG";
            File png = new File(pngPath);
            InputStream pngFile = new FileInputStream(pngPath);
            DocFlavor docFlavorPng = DocFlavor.INPUT_STREAM.PNG;
            Doc docPNG = new SimpleDoc(pngFile, docFlavorPng, null);

            InputStream jpgFile = new FileInputStream("src/main/resources/com/example/printout_vending_software/test files/description.JPG");
            DocFlavor docFlavorJpg = DocFlavor.INPUT_STREAM.JPEG;
            Doc docJPG = new SimpleDoc(jpgFile, docFlavorJpg, null);

            PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
            aset.add(MediaSizeName.ISO_A10);
            aset.add(Chromaticity.MONOCHROME);
            aset.add(OrientationRequested.LANDSCAPE);
            aset.add(new Copies(2));
            aset.add(new JobPriority(1));

            PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
            for (int i = 0; i < services.length; i++) {
                System.out.println((i + 1) + ". " + services[i].getName());
            }
            //System.out.println("Enter the printer to be used");
            //int select=Integer.parseInt(in.readLine());
            DocPrintJob job = ServiceUI.printDialog(null,0,0,services,services[2],docFlavorJpg,aset).createPrintJob();
            try {
                job.print(docPNG, aset);
                //job.print(docJPG,aset);
                //job.print(docPDF,aset);
                //job.print(docWord,aset);
            } catch (PrintException e) {
                System.out.println("PrintException: " + e.getMessage());
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}

