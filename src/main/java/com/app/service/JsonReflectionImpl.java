package com.app.service;

import com.app.interfaces.JsonReflectionInterface;

import java.lang.reflect.Constructor;
import java.util.Optional;
import java.util.stream.Stream;

public class JsonReflectionImpl implements JsonReflectionInterface {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T jsonToObject(String json, Class<T> clazz) {
        jsonValidate(json);
        try {
            Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            Optional<Constructor<?>> optionalConstructor = Stream.of(constructors)
                    .filter(constructor -> constructor.getParameterCount() == 0)
                    .findFirst();
            if (optionalConstructor.isEmpty()) {
                return (T) constructors[0].newInstance();
            }
            T instance = (T) optionalConstructor.get().newInstance();
            return instance;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void jsonValidate(String json) {
        if (json == null || json.trim().isEmpty()) {
            throw new IllegalArgumentException("El JSON proporcionado es nulo o vacío");
        }
        if (!(json.startsWith("{") && json.endsWith("}"))) {
            throw new IllegalArgumentException("El JSON debe empezar con '{' y terminar con '}'");
        }
    }

}
