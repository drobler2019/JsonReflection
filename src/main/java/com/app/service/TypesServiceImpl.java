package com.app.service;

import com.app.interfaces.ValueNumberService;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class TypesServiceImpl {

    private final Map<Class<?>, ValueNumberService<Number>> mapNumericTypes = new HashMap<>();
    private final Map<Class<?>, Function<String, Object>> mapGeneric = new HashMap<>();

    public TypesServiceImpl() {
        mapNumericTypes.put(int.class, Integer::parseInt);
        mapNumericTypes.put(Integer.class, Integer::valueOf);
        mapNumericTypes.put(double.class, Double::parseDouble);
        mapNumericTypes.put(Double.class, Double::valueOf);
        mapNumericTypes.put(float.class, Float::parseFloat);
        mapNumericTypes.put(Float.class, Float::valueOf);
        mapNumericTypes.put(long.class, Long::parseLong);
        mapNumericTypes.put(Long.class, Long::valueOf);
        mapGeneric.put(String.class, value -> value);
        mapGeneric.put(boolean.class, Boolean::parseBoolean);
        mapGeneric.put(Boolean.class, Boolean::valueOf);
    }

    public Object convertValue(Class<?> fieldType, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        ValueNumberService<Number> converter = mapNumericTypes.get(fieldType);
        if (converter != null) {
            return converter.getNumericValue(value);
        }
        var functionGeneric = mapGeneric.get(fieldType);
        if (functionGeneric != null) {
            return functionGeneric.apply(value);
        }
        return new ReflectionInstanceServiceImpl().getInstanceOfClass(fieldType);
    }

    public boolean isPresent(Class<?> fieldType) {
        return mapNumericTypes.containsKey(fieldType) || mapGeneric.containsKey(fieldType);
    }

}
