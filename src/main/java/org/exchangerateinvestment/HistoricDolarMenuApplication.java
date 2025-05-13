package org.exchangerateinvestment;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;

import java.io.IOException;

public class HistoricDolarMenuApplication extends Application {
    @Override
    public void start(Stage historicDolarMenuStage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HistoricDolarMenuApplication.class.getResource("historicdolarmenu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        historicDolarMenuStage.setTitle("Exchange Rate Investment App!");
        // Ruta relativa desde la carpeta src/main/resources
        Image icon = new Image("euro-coin1.png");
        historicDolarMenuStage.getIcons().add(icon);
        historicDolarMenuStage.setScene(scene);
        historicDolarMenuStage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}