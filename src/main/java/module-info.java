module org.exchangerateinvestment {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;

    // Donde se localizan las Clases para crear los objetos
    opens org.modelo to javafx.fxml;
    exports org.modelo;

    // donde se localizar los Applications y Controllers JavaFX
    opens org.exchangerateinvestment to javafx.fxml;
    exports org.exchangerateinvestment;

}