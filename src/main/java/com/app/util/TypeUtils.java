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
        MAP_NUMERIC_TYPES.put(long.class, Long::parseLong);
        MAP_NUMERIC_TYPES.put(Long.class, Long::valueOf);
    }
}
