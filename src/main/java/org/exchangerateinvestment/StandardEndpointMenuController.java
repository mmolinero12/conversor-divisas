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
import javafx.scene.control.*;

import java.net.URL;

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
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class StandardEndpointMenuController implements  Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private String monedaReferencia;

    // Opciones del ChoiceBox, debe guardarse en un array.
    ArrayList<String> listaMonedasSeleccionadas = new ArrayList<>();
    private final String[] listaCheckBoxIds = {"cadCheckBox", "usdCheckBox", "eurCheckBox", "gbpCheckBox", "mxnCheckBox",
                                            "brlCheckBox", "cnyCheckBox"};

    // Un mapa para asociar los fx:id's (String) con las instancias de CheckBox
    private Map<String, CheckBox> checkBoxMap = new HashMap<>();

    //  **********************  Declaración de controles standardendpointmenu-view.fxml

    // Menu Barra

    @FXML
    private Button menuPrincipalButton;

    @FXML
    private Button graciasButton ;

    // Mensaje del Sistema
    @FXML
    private Label messageText;

    // Los RadioButton de Moneda Base - Toggle Group: monedaBase

    @FXML
    private ToggleGroup monedaBase;

    @FXML
    private RadioButton cadRadioButton;

    @FXML
    private RadioButton usdRadioButton;

    @FXML
    private RadioButton eurRadioButton;

    @FXML
    private RadioButton gbpRadioButton;

    @FXML
    private RadioButton mxnRadioButton;

    @FXML
    private RadioButton brlRadioButton;

    @FXML
    private RadioButton cnyRadioButton;

    // Los CheckBox de monedas

    @FXML
    private CheckBox cadCheckBox;

    @FXML
    private CheckBox usdCheckBox;

    @FXML
    private CheckBox eurCheckBox;

    @FXML
    private CheckBox gbpCheckBox;

    @FXML
    private CheckBox mxnCheckBox;

    @FXML
    private CheckBox brlCheckBox;

    @FXML
    private CheckBox cnyCheckBox;

    // exchangeTable
    @FXML
    private TableView<ExchangeRate> exchangeTable;

    @FXML
    private TableColumn<ExchangeRate, String> fromCol;

    @FXML
    private TableColumn<ExchangeRate, Double> toValueCol;

    @FXML
    private TableColumn<ExchangeRate, String> toMonedaCol;

    // calculadora de conversión de monedas

    @FXML
    private HBox calculadoraHBox;

    @FXML
    private TextField valorAConvertirTextField;

    @FXML
    private Label monedaAConvertirLabel;

    @FXML
    private Button convertirButton;

    @FXML
    private Label valorConvertidoLabel;

    @FXML
    private ChoiceBox<String> monedaAConvertirChoiceBox;

    // parsingButton

    @FXML
    private Button parsingButton;

    // Initialize

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        // En el método initialize, poblamos el mapa con las instancias inyectadas
        // Esto nos permitirá acceder a los CheckBox por su fx:id (String)
        checkBoxMap.put("cadCheckBox", cadCheckBox);
        checkBoxMap.put("usdCheckBox", usdCheckBox);
        checkBoxMap.put("eurCheckBox", eurCheckBox);
        checkBoxMap.put("gbpCheckBox", gbpCheckBox);
        checkBoxMap.put("mxnCheckBox", mxnCheckBox);
        checkBoxMap.put("brlCheckBox", brlCheckBox);
        checkBoxMap.put("cnyCheckBox", cnyCheckBox);

        // Opcional: Si quieres un debug inicial, puedes imprimir todos los checkboxes
        // for (Map.Entry<String, CheckBox> entry : checkBoxMap.entrySet()) {
        //     System.out.println("Inyectado: " + entry.getKey() + " -> " + entry.getValue().getText());
        // }


        // Inicialización de las columnas de la TableView
        fromCol.setCellValueFactory(new PropertyValueFactory<ExchangeRate, String>("from"));
        toValueCol.setCellValueFactory(new PropertyValueFactory<ExchangeRate, Double>("toValue"));
        toMonedaCol.setCellValueFactory(new PropertyValueFactory<ExchangeRate, String>("toCurrency"));

    }


    // Métodos de Control

    @FXML
    protected void menuPrincipalGo(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StandardEndpointMenuController
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
        FXMLLoader fxmlLoader = new FXMLLoader(StandardEndpointMenuController
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

    @FXML
    protected void rowClicked(){
        calculadoraHBox.setVisible(true);
        monedaAConvertirLabel.setText(monedaReferencia);

        // **** Cargar el ChoiceBox con el renglón seleccionado de la tabla
        // Extracción de la moneda a convertir de la tabla
        ExchangeRate clickedRow = exchangeTable.getSelectionModel().getSelectedItem();
        String rowMoneda = toMonedaCol.getCellData(clickedRow);
        // Actualización del ChoiceBox
        monedaAConvertirChoiceBox.setValue(rowMoneda);

        messageText.setText("Paso 3. Introduzca un valor y presione el botón <Convertir>.");

    }

    /**
     * Método onParseButtonClick()
     * Si currenciesChecking está OK, se procede a llamar a la función jsonParsingGet() para solicitar la información JSON.
     */
    @FXML
    protected void onParseButtonClick() {
        messageText.setText("Espere un momento por favor.");
        if (currenciesChecking()) {
            try {
                jsonParsingGet();
            } catch (Exception e) {
                messageText.setText("Error durante la deserialización JSON.");
            }
        } else {
            messageText.setText("Error. Debe seleccionar una Moneda Base y al menos una Moneda a Convertir");
        }
    } // END de onParseButtonClick()

    public boolean currenciesChecking(){
        boolean selectedToggleFlag = false;
        boolean selectedMonedaDestinoFlag = false;

        // Validación de los Radio Buttons de la Moneda Base - Toggle Group monedaBase
        Toggle selectedToggle = monedaBase.getSelectedToggle();
        if(selectedToggle != null){selectedToggleFlag = true;}

        if(cadCheckBox.isSelected()){ selectedMonedaDestinoFlag = true; }
        if(usdCheckBox.isSelected()){ selectedMonedaDestinoFlag = true; }
        if(eurCheckBox.isSelected()){ selectedMonedaDestinoFlag = true; }
        if(gbpCheckBox.isSelected()){ selectedMonedaDestinoFlag = true; }
        if(mxnCheckBox.isSelected()){ selectedMonedaDestinoFlag = true; }
        if(brlCheckBox.isSelected()){ selectedMonedaDestinoFlag = true; }
        if(cnyCheckBox.isSelected()){ selectedMonedaDestinoFlag = true; }

        return (selectedToggleFlag & selectedMonedaDestinoFlag);

    } // END de currenciesChecking()

    /**
     * Método jsonParsingGet el cual hará la petición HTTP al portal, recibirá un objeto JSON,
     * el cual se utilizará para crear la lista y las series que se emplearán en exchangeTable y
     * dollartimechart
     *
     */
    public void jsonParsingGet() throws IOException, InterruptedException  {

        List<Object> exchangeList = new ArrayList<>();

        RadioButton selectedRadioButton = (RadioButton) monedaBase.getSelectedToggle();
        String nombreRadioButton = selectedRadioButton.getText();
        Matcher matcher = Pattern.compile("\\((.*?)\\)").matcher(nombreRadioButton);
        matcher.find();     // Realiza la búsqueda del patrón en el texto.
        monedaReferencia = matcher.group(1);    // El primer resultado hallado se guarda en la variable resultado

        // Creación del ArrayList listaMonedasSeleccionadas

        for (String checkBoxId : listaCheckBoxIds) {
            CheckBox currentCheckBox = checkBoxMap.get(checkBoxId);
            if (currentCheckBox.isSelected()) {
                Matcher matcher2 = Pattern.compile("\\((.*?)\\)").matcher(currentCheckBox.getText());
                matcher2.find();     // Realiza la búsqueda del patrón en el texto.
                String monedaReferencia2 = matcher2.group(1);    // El primer resultado hallado se guarda en la variable resultado
                listaMonedasSeleccionadas.add(monedaReferencia2);
            }
        }

        // Inicialización del ChoiceBox de monedas
        monedaAConvertirChoiceBox.getItems().addAll(listaMonedasSeleccionadas);

        // https://v6.exchangerate-api.com/v6/YOUR-API-KEY/latest/USD

        String link = "https://v6.exchangerate-api.com/v6/ec9468fdbee96c3a80b5f590/latest/" + monedaReferencia;

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(link))
                .build();

        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());

        try{
            JsonNode rootNode = Json.parse(response.body());
            // Navigate to the "datos" array
            JsonNode datosArray = rootNode.path("conversion_rates");

            // Declaración y reinicialización de observableList
            ObservableList<ExchangeRate> observableList = FXCollections.observableArrayList();

            // Iterate through each object in the "datos" array

            String monedaBase = "1 ".concat(monedaReferencia) ;
            for (String monedaSeleccionada: listaMonedasSeleccionadas){
                for (Iterator<Map.Entry<String, JsonNode>> it = datosArray.fields(); it.hasNext(); ) {
                    Map.Entry<String, JsonNode> field = it.next();
                    String monedaObtenida = field.getKey();
                    JsonNode valorObtenido = field.getValue();

                    if(monedaSeleccionada.equals(monedaObtenida)){
                        // Tabla
                        observableList.add(new ExchangeRate(monedaBase, valorObtenido.asDouble(), monedaObtenida));
                    }
                }
            }
            exchangeTable.setItems(observableList);
            exchangeTable.setVisible(true);
            messageText.setText("Paso 2: Seleccione una opción dentro de la tabla y realice su conversión entre monedas");

        } catch (IOException e) {
            e.printStackTrace();
        }

    } // END jsonParsingGet()

    @FXML
    protected void convertirButton() {

        try {

            // Extracción del valor en el renglón seleccionado de la tabla
            ExchangeRate clickedRow = exchangeTable.getSelectionModel().getSelectedItem();
            Double rowValue = toValueCol.getCellData(clickedRow);

            // Extracción del valor a convertir
            double valorAConvertir = Double.parseDouble(valorAConvertirTextField.getText());
            String monedaAConvertir = monedaAConvertirChoiceBox.getValue();

            System.out.println(valorAConvertir);
            System.out.println(rowValue);
            System.out.println(monedaAConvertir);

            // Declaración del valor convertido
            double valorConvertido;

            // Conversión de tipos de cambio
            valorConvertido = valorAConvertir * rowValue;
            valorConvertidoLabel.setText(String.format("%.2f", valorConvertido));

        } catch (Exception e){
            e.printStackTrace();
        }

    } // END de convertirButton(
} // END de la Clase MultipleCurrenciesController

