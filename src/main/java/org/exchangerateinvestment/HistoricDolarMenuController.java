package org.exchangerateinvestment;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.JsonNode;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.modelo.ExchangeRate;
import org.modelo.Json;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class HistoricDolarMenuController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    // Opciones del ChoiceBox, debe guardarse en un array.
    private final String[] listaMonedas = {"Peso Mexicano", "Dólar Estadounidense"};

    //  **********************  Declaración de controles historicdolarmenu-view.fxml

    // Menu Barra

    @FXML
    private Button menuPrincipalButton;

    @FXML
    private Button graciasButton ;

    // Mensaje del Sistema
    @FXML
    private Label messageText;

    // DatePickers

    @FXML
    private DatePicker dateFromPicker;

    @FXML
    private DatePicker dateToPicker;

    @FXML
    private Label fromLabel;

    @FXML
    private Label toLabel;

    // exchangeTable
    @FXML
    private TableView<ExchangeRate> exchangeTable;

    @FXML
    private TableColumn<ExchangeRate, String> dateCol;

    @FXML
    private TableColumn<ExchangeRate, Double> valueCol;

    // LineCart

    @FXML
    private LineChart<?, ?> dollarTimeChart;

    @FXML
    private NumberAxis pesosMexicanos;

    @FXML
    private CategoryAxis tiempo;

    // calculadora de conversión de monedas

    @FXML
    private HBox calculadoraHBox;

    @FXML
    private TextField valorAConvertirTextField;

    @FXML
    private ChoiceBox<String> monedaAConvertirChoiceBox;

    @FXML
    private Button convertirButton;

    @FXML
    private Label valorConvertidoLabel;

    @FXML
    private Label monedaConvertidaLabel;

    // parsingButton

    @FXML
    private Button parsingButton;

    // Initialize

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        // Inicialización de las columnas de la TableView
        dateCol.setCellValueFactory(new PropertyValueFactory<ExchangeRate, String>("date"));
        valueCol.setCellValueFactory(new PropertyValueFactory<ExchangeRate, Double>("value"));

        // Inicialización del ChoiceBox de monedas
        monedaAConvertirChoiceBox.getItems().addAll(listaMonedas);
    }

    // Métodos de Control

    @FXML
    protected void menuPrincipalGo(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HistoricDolarMenuController
                .class.getResource("mainmenu-view.fxml")); // Primera opción.
        // Tener cuidado en seleccionar la Clase y el archivo .fxml
        // root = FXMLLoader.load(getClass().getResource("scene2-view.fxml"));  // Segunda opción

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        scene = new Scene(fxmlLoader.load()); // Primera opción
        //scene = new Scene(root);   // Segunda opción

        stage.setTitle("Exchange Rate Investment App!");
        // Ruta relativa desde la carpeta src/main/resources
        Image icon = new Image("euro-coin1.png");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

    }

    @FXML
    protected void GraciasGo(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HistoricDolarMenuController
                .class.getResource("graciasmenu-view.fxml")); // Primera opción.
        // Tener cuidado en seleccionar la Clase y el archivo .fxml
        // root = FXMLLoader.load(getClass().getResource("scene2-view.fxml"));  // Segunda opción

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        scene = new Scene(fxmlLoader.load()); // Primera opción
        //scene = new Scene(root);   // Segunda opción

        stage.setTitle("Exchange Rate Investment App!");
        // Ruta relativa desde la carpeta src/main/resources
        Image icon = new Image("euro-coin1.png");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

    }

    /**
     * Método onParseButtonClick()
     * Se obtienen las fechas inicio y fin del intervalor de tiempo para consultar el tipo de cambio.
     * Si las fechas inicio y fin son correctas (la fecha fin debe ser posterior o igual a la fecha de inicio),
     * Se procede a llamar a la función jsonParsingGet() para solicitar la información JSON.
     */
    @FXML
    protected void onParseButtonClick() {

        messageText.setText("Espere un momento por favor.");
        if (dateChecking()) {
            try {
                LocalDate localFromDate = dateFromPicker.getValue();
                LocalDate localToDate = dateToPicker.getValue();
                jsonParsingGet(localFromDate, localToDate);
            } catch (Exception e) {
                messageText.setText("Error durante la deserialización JSON");
            }
        }
    } // END de onParseButtonClick()

    @FXML
    protected void datePickerFromSelected() {
        LocalDate localFromDate = dateFromPicker.getValue();
        String pattern = "EEEE dd MMMM, yyyy";
        String datePattern = localFromDate.format(DateTimeFormatter.ofPattern(pattern));
        fromLabel.setText("De: " + datePattern);
        dateChecking();
    }

    @FXML
    protected void dateToPickerSelected() {
        LocalDate localToDate = dateToPicker.getValue();
        String pattern = "EEEE dd MMMM, yyyy";
        String datePattern = localToDate.format(DateTimeFormatter.ofPattern(pattern));
        toLabel.setText("A: " + datePattern);
        dateChecking();
    } // END de dateToPickerSelected()

    @FXML
    protected void rowClicked(){
        calculadoraHBox.setVisible(true);
        messageText.setText("Paso 4. Introduzca un valor y seleccione la moneda base que desea convertir.");
    }

    public boolean dateChecking(){
        boolean flag = false;

        try{
            LocalDate localFromDate = dateFromPicker.getValue();
            LocalDate localToDate = dateToPicker.getValue();

            if ((localToDate.isAfter(localFromDate)) | (localToDate.isEqual(localFromDate))) {
                messageText.setText("Paso 2: Las Fechas son  Correctas. Oprima el botón <Buscar>");
                flag = true;
            } else {
                messageText.setText("Error en las Fechas. Intente de nuevo");
                flag = false;
            }
        }
        catch(Exception e) {
            messageText.setText("Error de Excepción en las Fechas. Intente de nuevo");
        }
        return flag;
    } // END de dateChecking()

    /**
     * Método jsonParsingGet el cual hará la petición HTTP al portal, recibirá un objeto JSON,
     * el cual se utilizará para crear la lista y las series que se emplearán en exchangeTable y
     * dollartimechart
     * @param localFromDate LocalDate - Fecha inicial del periodo a consultar el tipo de cambio
     * @param localToDate LocalDate - Fecha final del periodo a consultar el tipo de cambio
     * @throws IOException
     * @throws InterruptedException
     */
    public void jsonParsingGet(LocalDate localFromDate, LocalDate localToDate) throws IOException, InterruptedException {

        List<Object> exchangeList = new ArrayList<>();
        String pattern = "yyyy-MM-dd";
        String dateFromPattern = localFromDate.format(DateTimeFormatter.ofPattern(pattern));
        String dateToPattern = localToDate.format(DateTimeFormatter.ofPattern(pattern));
        SimpleDateFormat formatoOriginal = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatoNuevo = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;

        String link = "https://www.banxico.org.mx/SieAPIRest/service/v1/series/" +
                "SF63528/datos/"+ dateFromPattern + "/" + dateToPattern  +
                "?token=9930eacab926ac51e9edf4b101bb577fd255f27443ff0cdfab3f19e035163a04";


        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(link))
                .build();

        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());

        // System.out.println(response.body());

        try{
            JsonNode rootNode = Json.parse(response.body());
            // Navigate to the "datos" array
            JsonNode datosArray = rootNode.path("bmx").path("series").path(0).path("datos");

            if (datosArray.isArray()) {

                // Declaración y reinicialización de observableList
                ObservableList<ExchangeRate> observableList = FXCollections.observableArrayList();


                // Inicialización-reinicialización de la gráfica
                XYChart.Series serieChart = new XYChart.Series<>();
                dollarTimeChart.getData().clear();

                // Iterate through each object in the "datos" array

                for (JsonNode dataNode : datosArray) {
                    JsonNode fechaNode = dataNode.get("fecha");
                    JsonNode valorNode = dataNode.get("dato");
                    // Tabla
                    observableList.add(new ExchangeRate(fechaNode.asText(), valorNode.asDouble()));

                    // Gráfica
                    String fechaMexico = fechaNode.asText();
                    date = formatoOriginal.parse(fechaMexico);
                    String fechaInternacional = formatoNuevo.format(date);
                    serieChart.getData().add(new XYChart.Data<>(fechaInternacional, valorNode.asDouble()));

                }

                exchangeTable.setItems(observableList);
                exchangeTable.setVisible(true);
                dollarTimeChart.setVisible(true);
                dollarTimeChart.getData().addAll(serieChart);
                messageText.setText("Paso 3: Seleccione una fecha dentro de la tabla y realice su conversión entre monedas");

            } // END del if

        } catch (Exception e) {    // IOException | ParseException e
                e.printStackTrace();
        }

    } // END jsonParsingGet()


    @FXML
    protected void convertirButton(){

        try{
            // Extracción del valor en el renglón seleccionado de la tabla
            ExchangeRate clickedRow = exchangeTable.getSelectionModel().getSelectedItem();
            Double rowValue = clickedRow.getValue();

            // Extracción del valor a convertir y el tipo de moneda origen
            double valorAConvertir = Double.parseDouble(valorAConvertirTextField.getText());
            String monedaAConvertir = monedaAConvertirChoiceBox.getValue();

            // Declaración del valor convertido y la moneda convetida
            double valorConvertido;
            String monedaConvertida;

            // "Peso Mexicano", "Dólar Estadounidense"
            if(monedaAConvertir == "Peso Mexicano" ){
                valorConvertido = valorAConvertir / rowValue;
                valorConvertidoLabel.setText(String.format("%.2f", valorConvertido));
                monedaConvertidaLabel.setText("USD");

            } else if (monedaAConvertir == "Dólar Estadounidense") {
                valorConvertido = valorAConvertir * rowValue;
                valorConvertidoLabel.setText(String.format("%.2f", valorConvertido));
                monedaConvertidaLabel.setText("MXN");
            } else {
                messageText.setText("Error en la conversión. Revise el valor a convertir y la moneda");
            }

        } catch(Exception e) {

        }


    } // END de convertirButton(

}   // END de la Clase