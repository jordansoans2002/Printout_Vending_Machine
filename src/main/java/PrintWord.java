import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.IOException;

public class PrintWord {
    public static void main(String[] args){
        new PrintWord().printWordFile();
    }

    void printWordFile() {
        try {
            XWPFDocument adf = new XWPFDocument(OPCPackage.open("src/main/resources/com/example/printout_vending_software/test files/LOGBOOK.docx"));
        }catch(InvalidFormatException | IOException e){
            System.out.println(e.getMessage());
        }
    }
}
