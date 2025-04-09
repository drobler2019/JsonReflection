package com.app.util;

import com.app.interfaces.ValueNumberInterface;

import java.util.HashMap;
import java.util.Map;

public class TypeUtils {

    private static final Map<Class<?>, ValueNumberInterface<Number>> MAP_NUMERIC_TYPES = new HashMap<>();

    static {
        MAP_NUMERIC_TYPES.put(int.class, Integer::parseInt);
        MAP_NUMERIC_TYPES.put(Integer.class, Integer::valueOf);
        MAP_NUMERIC_TYPES.put(double.class, Double::parseDouble);
        MAP_NUMERIC_TYPES.put(Double.class, Double::valueOf);
        MAP_NUMERIC_TYPES.put(float.class, Float::parseFloat);
        MAP_NUMERIC_TYPES.put(Float.class, Float::valueOf);
        MAP_NUMERIC_TYPES.put(long.class, Long::parseLong);
        MAP_NUMERIC_TYPES.put(Long.class, Long::valueOf);
    }

    public static Object convertValue(Class<?> fieldType, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        ValueNumberInterface<Number> converter = MAP_NUMERIC_TYPES.get(fieldType);
        if (converter != null) {
            return converter.getNumericValue(value);
        }
        if (fieldType.equals(String.class)) {
            return value;
        }
        if (fieldType.equals(boolean.class) || fieldType.equals(Boolean.class)) {
            return Boolean.valueOf(value);
        }
        throw new UnsupportedOperationException("Tipo no soportado: " + fieldType);
    }

}
