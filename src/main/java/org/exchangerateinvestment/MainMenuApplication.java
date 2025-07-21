package org.exchangerateinvestment;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;

import java.io.IOException;

public class MainMenuApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(MainMenuApplication.class
                .getResource("mainmenu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Exchange Rate Investment App!");
        // Ruta relativa desde la carpeta src/main/resources
        Image icon = new Image("euro-coin1.png");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
