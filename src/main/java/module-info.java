module com.example.printout_vending_software {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.printout_vending_software to javafx.fxml;
    exports com.example.printout_vending_software;
}