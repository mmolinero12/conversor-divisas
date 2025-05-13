package org.modelo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Json {

    /**
     * Crea un ObjectMapper, llamando al método getDefaultObjectMapper()
     */
    private static ObjectMapper objectmapper = getDefaultObjectMapper();

    /**
     * Método pseudo constructor
     * @return defaultObjectMapper ObjectMapper
     */
    private static ObjectMapper getDefaultObjectMapper(){
        ObjectMapper defaultObjectMapper = new ObjectMapper();
        // ....
        return defaultObjectMapper;
    }

    /**
     * Método parse
     * @param src String - Convierte un String que corresponde a una ruta
     * @return objectmapper.readTree(src) JsonNode - Devuelve un objeto JsonNode
     * @throws JsonProcessingException
     */
    public static JsonNode parse(String src) throws JsonProcessingException {
        // Read the JSON file into a JsonNode
        return objectmapper.readTree(src);
    }

}
