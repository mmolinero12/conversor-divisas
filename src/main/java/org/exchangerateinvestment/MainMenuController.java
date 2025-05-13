package org.exchangerateinvestment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private RadioButton opcion1RadioButton;

    @FXML
    private RadioButton opcion2RadioButton;

    @Override       // Debe colocarse aun cuando NO tenga código adentro.
    public void initialize(URL url, ResourceBundle resourceBundle){
    }


    @FXML
    protected void onOpcion1RadioButtonClick(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(MainMenuController
                .class.getResource("historicdolarmenu-view.fxml")); // Primera opción
        // Tener cuidado en seleccionar la Clase del Controlador Actual y el archivo .fxml
        // root = FXMLLoader.load(getClass().getResource("scene2-view.fxml"));  // Segunda opción

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        scene = new Scene(fxmlLoader.load()); // Primera opción
        //scene = new Scene(root);   // Segunda opción

        // scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());|
        stage.setTitle("Exchange Rate Investment App!");
        // Ruta relativa desde la carpeta src/main/resources
        Image icon = new Image("euro-coin1.png");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

    }


    @FXML
    protected void onOpcion2RadioButtonClick(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(MainMenuController
                .class.getResource("standardendpointmenu-view.fxml")); // Primera opción
        // Tener cuidado en seleccionar la Clase del Controlador Actual y el archivo .fxml
        // root = FXMLLoader.load(getClass().getResource("scene2-view.fxml"));  // Segunda opción

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        scene = new Scene(fxmlLoader.load()); // Primera opción
        //scene = new Scene(root);   // Segunda opción

        // scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());|
        stage.setTitle("Exchange Rate Investment App!");
        // Ruta relativa desde la carpeta src/main/resources
        Image icon = new Image("euro-coin1.png");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

    }

}