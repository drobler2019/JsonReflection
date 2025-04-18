package com.app.service;


import com.app.interfaces.ValidateFormatJsonService;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class ValidateFormatJsonImplTest {

    private final ValidateFormatJsonService validateFormatJsonService = new ValidateFormatJsonImpl();
    private final Map<String, String> map = new HashMap<>();

    @BeforeEach
    public void init() {
        map.put("nombre", "Maria");
        map.put("apellido", "Rubio");
        map.put("edad", "15");
    }

    @Test
    public void getMapTest() {
        String key = "nombre";
        String values = "[matemáticas,sociales]";
        String object = "{\"value\": false}";
        String json = "{\"nombre\" : \"Diego\", \"subjects\": [\"matemáticas\",\"sociales\"],\"status\": {\"value\": false}}";
        String value = map.get(key);
        var mapResult = validateFormatJsonService.getMap("{\"nombre\" : \"Maria\"}");
        var otherResult = validateFormatJsonService.getMap("{\"nombre\" : \"Diego\"}");
        String subjects = validateFormatJsonService.getMap(json).get("subjects");
        String status = validateFormatJsonService.getMap(json).get("status");

        assertAll(() -> assertEquals(value, mapResult.get(key)),
                () -> assertNotEquals(value, otherResult.get(key)),
                () -> assertEquals(values, subjects),
                () -> assertEquals(object, status));
    }

    @Test
    void getListMapTest() {
        String object = "{\"id\":1,\"name\":\"Diego\",\"lastName\":\"Alejandro\"}";
        var objects = validateFormatJsonService.getMap(object);
        var array = validateFormatJsonService.getListMap("[" + object + "," + object + "]");
        Map<String, String> o = array.getFirst();
        assertAll(() -> assertEquals(objects, o), () -> assertThrows(IllegalArgumentException.class,
                        () -> validateFormatJsonService.getListMap("[")),
                () -> assertThrows(IllegalArgumentException.class, () -> validateFormatJsonService.getMap("}")));
    }

}