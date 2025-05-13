package org.modelo;

public class ExchangeRate {
    // Variables para HistoricDolarMenuController
    private String date;
    private double value;

    // Variables para StandardEndpointMenuController
    private String from;
    private Double toValue;
    private String toCurrency;

    // Getters y Setters para HistoricDolarMenuController
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    // Getters y Setters para StandardEndpointMenuController

    public String getFrom() {
        return from;
    }

    public void setFrom(String moneda) {
        this.from = moneda;
    }

    public Double getToValue() {
        return toValue;
    }

    public void setToValue(Double toValue) {
        this.toValue = toValue;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }


    /**
     * Método Constructor de la Clase ExchangeRate que se usará en la exchangeTable de HistoricDolarMenu
     * @param date String - Columna 0 de exchangeTable. Fecha del tipo de cambio solicitado
     * @param value Double - Columna 1 de exchangeTable. Valor del tipo de cambio del dólar estadounidense
     */
    public ExchangeRate(String date, double value){
        this.date = date;
        this.value = value;
    }

    /**
     * Método Constructor de la Clase ExchangeRate que se usará en la exchangeTable de StandarEndpointMenu
     * @param monedaAConvertir String - Columna 0 de exchangeTable. Moneda Base en cantidad 1
     * @param toValue Double - Columna 1 de exchangeTable. Valor del tipo de cambio
     * @param monedaConvertida String - Moneda a la que se desea convertir

     */
    public ExchangeRate(String monedaAConvertir, double toValue, String monedaConvertida){
        this.from = monedaAConvertir ;
        this.toValue = toValue ;
        this.toCurrency = monedaConvertida;
    }


}
