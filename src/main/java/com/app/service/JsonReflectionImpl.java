package com.app.service;

import com.app.interfaces.JsonReflectionInterface;
import com.app.util.TypeUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonReflectionImpl implements JsonReflectionInterface {

    private final Map<Class<?>, List<Field>> MAP = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> T jsonToObject(String json, Class<T> clazz) {
        Map<String, String> map = jsonStringToMap(json);
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        var constructor = getConstructor(constructors);
        try {
            T instance = (T) constructor.newInstance();
            if (map.isEmpty()) {
                return instance;
            }
            var fields = getFields(clazz);
            Class<?> superclass = clazz.getSuperclass();
            while (!superclass.equals(Object.class)) {
                fields.addAll(List.of(clazz.getDeclaredFields()));
                superclass = superclass.getSuperclass();
            }
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                if (map.containsKey(fieldName)) {
                    try {
                        Object value = TypeUtils.convertValue(field.getType(), map.get(fieldName));
                        field.set(instance, value);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }
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

    private Map<String, String> jsonStringToMap(String json) {
        jsonValidate(json);
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

    private void jsonValidate(String json) {
        if (json == null || json.trim().isEmpty()) {
            throw new IllegalArgumentException("El JSON proporcionado es nulo o vacío");
        }
        if (!(json.startsWith("{") && json.endsWith("}"))) {
            throw new IllegalArgumentException("El JSON debe empezar con '{' y terminar con '}'");
        }
    }

    private List<Field> getFields(Class<?> clazz) {
        return MAP.computeIfAbsent(clazz, c -> Arrays.stream(c.getDeclaredFields()).collect(Collectors.toList()));
    }

}
