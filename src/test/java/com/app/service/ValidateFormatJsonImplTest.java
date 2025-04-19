package com.app.service;


import com.app.interfaces.ValidateFormatJsonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

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
     void getMapTest() {
        String jsonEmpty = "{}";
        String key = "nombre";
        String values = "[matemáticas,sociales]";
        String object = "{\"value\": false}";
        String json = "{\"nombre\" : \"Diego\", \"subjects\": [\"matemáticas\",\"sociales\"],\"status\": {\"value\": false}}";
        String value = map.get(key);
        var mapResult = validateFormatJsonService.getMap("{\"nombre\" : \"Maria\"}");
        var otherResult = validateFormatJsonService.getMap("{\"nombre\" : \"Diego\"}");
        String subjects = validateFormatJsonService.getMap(json).get("subjects");
        String string = validateFormatJsonService.getMap(jsonEmpty).get("empty");
        String status = validateFormatJsonService.getMap(json).get("status");

        assertAll(() -> assertEquals(value, mapResult.get(key)),
                () -> assertNotEquals(value, otherResult.get(key)),
                () -> assertEquals(values, subjects),
                () -> assertEquals(object, status),
                () -> assertEquals(jsonEmpty, string));
    }

    @Test
    void getMapThrowExceptionTest() {
        Executable nullJson = () -> assertThrows(IllegalArgumentException.class, () -> validateFormatJsonService.getMap(null));
        Executable emptyJson = () -> assertThrows(IllegalArgumentException.class, () -> validateFormatJsonService.getMap(""));
        Executable stringEmpty = () -> assertThrows(IllegalArgumentException.class, () -> validateFormatJsonService.getMap("   "));
        Executable invalidJson = () -> assertThrows(IllegalArgumentException.class, () -> validateFormatJsonService.getMap("{"));
        var stream = Stream.<Executable>builder().add(nullJson).add(emptyJson).add(stringEmpty).add(invalidJson).build();
        assertAll(stream);
    }

    @Test
    void getListMapTest() {
        String object = "{\"id\":1,\"name\":\"Diego\",\"lastName\":\"Alejandro\"}";
        var objects = validateFormatJsonService.getMap(object);
        var listMap = validateFormatJsonService.getListMap("[" + object + "," + object + "]");
        var o = listMap.getFirst();
        assertEquals(objects, o);
    }

    @Test
    void getListMapThrowExceptionTest() {
        Executable nullJson = () -> assertThrows(IllegalArgumentException.class, () -> validateFormatJsonService.getListMap(null));
        Executable emptyJson = () -> assertThrows(IllegalArgumentException.class, () -> validateFormatJsonService.getListMap(""));
        Executable stringEmpty = () -> assertThrows(IllegalArgumentException.class, () -> validateFormatJsonService.getListMap("   "));
        Executable invalidJson = () -> assertThrows(IllegalArgumentException.class, () -> validateFormatJsonService.getListMap("["));
        var stream = Stream.<Executable>builder().add(nullJson).add(emptyJson).add(stringEmpty).add(invalidJson).build();
        assertAll(stream);
    }

    @Test
    void getArrayJsonToListTest() {
        String json = "[]";
        var response = validateFormatJsonService.getArrayJsonToList(json).toArray(String[]::new);
        assertEquals("", response[0]);
    }

}