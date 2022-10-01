module com.example.printout_vending_software {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.apache.pdfbox;
    requires org.apache.poi.ooxml;

    opens com.example.printout_vending_software to javafx.fxml;
    exports com.example.printout_vending_software;
}