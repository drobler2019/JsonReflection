package com.app.service;

import com.app.interfaces.JsonReflectionInterface;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class JsonReflectionImpl implements JsonReflectionInterface {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T jsonToObject(String json, Class<T> clazz) {
        jsonValidate(json);
        Map<String, String> map = jsonStringToMap(json);
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        var constructor = getConstructor(constructors);
        try {
            T instance = (T) constructor.newInstance();
            if (map.isEmpty()) {
                return instance;
            }
            return instance;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Constructor<?> getConstructor(Constructor<?>[] constructors) {
        Optional<Constructor<?>> optionalConstructor = Stream.of(constructors)
                .filter(constructor -> constructor.getParameterCount() == 0)
                .findFirst();
        if (optionalConstructor.isEmpty()) {
            throw new IllegalArgumentException("Constructor vacio no encontrado");
        }
        return optionalConstructor.get();
    }

    private void jsonValidate(String json) {
        if (json == null || json.trim().isEmpty()) {
            throw new IllegalArgumentException("El JSON proporcionado es nulo o vacío");
        }
        if (!(json.startsWith("{") && json.endsWith("}"))) {
            throw new IllegalArgumentException("El JSON debe empezar con '{' y terminar con '}'");
        }
    }

    private Map<String, String> jsonStringToMap(String json) {
        var map = new HashMap<String, String>();
        var content = json.substring(1, json.length() - 1);
        if (content.isEmpty()) {
            return map;
        }
        var peers = content.split(",");
        for (String peer : peers) {
            String[] keyValue = peer.split(":");
            var key = keyValue[0].replace("\"", "").trim();
            var value = keyValue[1].replace("\"", "").trim();
            map.put(key, value);
        }
        return map;
    }

}
