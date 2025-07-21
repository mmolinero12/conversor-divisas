package org.exchangerateinvestment;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

// Muy probablemente NO se reguiera este código. El objetivo de existir es solamente
// para pruebas.

public class StandardEndpointMenuApplication extends Application {
    @Override
    public void start(Stage multipleCurrenciesMenuStage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(StandardEndpointMenuApplication.class.getResource("standardendpointmenu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        multipleCurrenciesMenuStage.setTitle("Exchange Rate Investment App!");
        // Ruta relativa desde la carpeta src/main/resources
        Image icon = new Image("euro-coin1.png");
        multipleCurrenciesMenuStage.getIcons().add(icon);
        multipleCurrenciesMenuStage.setScene(scene);
        multipleCurrenciesMenuStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}