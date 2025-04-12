package com.app.service;

import com.app.interfaces.ReflectionInstanceService;
import com.app.interfaces.ValidateFormatJsonService;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReflectionInstanceServiceImpl implements ReflectionInstanceService {

    private final ValidateFormatJsonService validateFormatJsonService;
    private final TypesServiceImpl typesServiceImpl;
    private Map<String, String> map = null;
    private Map<String, String> submap = null;

    public ReflectionInstanceServiceImpl() {
        validateFormatJsonService = new ValidateFormatJsonImpl();
        typesServiceImpl = new TypesServiceImpl();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getInstanceOfClass(Class<?> clazz) {
        Constructor<?> constructor = getConstructor(clazz.getDeclaredConstructors());
        try {
            return (T) constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public <T> T jsonToObject(String json, Class<T> clazz) {
        map = validateFormatJsonService.getMap(json);
        T instance = getInstanceOfClass(clazz);
        return jsonToObject(instance);
    }

    @Override
    public <T> List<T> jsonToList(String json, Class<T> clazz) {
        if (!clazz.isArray()) {
            throw new IllegalArgumentException("El tipo de clase debe ser un array");
        }
        if (!json.contains("{")) {
            return getCollect(json, clazz);
        }
        var mapList = validateFormatJsonService.getListMap(json);
        var response = new ArrayList<T>();
        for (Map<String, String> map : mapList) {
            this.map = map;
            response.add(jsonToObject(getInstanceOfClass(clazz.getComponentType())));
        }
        return response;
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> getCollect(String json, Class<T> clazz) {
        return (List<T>) validateFormatJsonService.getArrayJsonToList(json).stream()
                .map(o -> typesServiceImpl.convertValue(clazz.getComponentType(), o))
                .toList();
    }

    private <T> T jsonToObject(T instance) {
        var fields = getFields(instance.getClass());
        Class<?> superclass = instance.getClass().getSuperclass();
        setFieldInherit(superclass, fields);
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            if (map.containsKey(fieldName)) {
                try {
                    submap = map;
                    Object value = typesServiceImpl.convertValue(field.getType(), map.get(fieldName));
                    setObject(value, field);
                    field.set(instance, value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
        map.clear();
        submap.clear();
        return instance;
    }

    @SuppressWarnings("unchecked")
    private <T> void setObject(Object object, Field field) throws IllegalAccessException {
        if (typesServiceImpl.isPresent(object.getClass())) {
            return;
        }
        T subInstance = (T) object;
        var fields = getFields(subInstance.getClass());

        Class<?> superclass = subInstance.getClass().getSuperclass();
        setFieldInherit(superclass, fields);

        if (!typesServiceImpl.isPresent(field.getType())) {
            String json = submap.get(field.getName());
            submap = validateFormatJsonService.getMap(json);
        }
        for (Field subField : fields) {
            subField.setAccessible(true);
            if (submap.containsKey(subField.getName())) {
                set(subField, subInstance);
            } else {
                submap = validateFormatJsonService.getMap(map.get(field.getName()));
                set(subField, subInstance);
            }
        }
    }

    private void setFieldInherit(Class<?> superclass, List<Field> fields) {
        while (!superclass.equals(Object.class)) {
            fields.addAll(List.of(superclass.getDeclaredFields()));
            superclass = superclass.getSuperclass();
        }
    }

    private <T> void set(Field subField, T subInstance) throws IllegalAccessException {
        var objectValue = typesServiceImpl.convertValue(subField.getType(), submap.get(subField.getName()));
        setObject(objectValue, subField);
        subField.set(subInstance, objectValue);
    }

    private Constructor<?> getConstructor(Constructor<?>[] constructors) {
        return Stream.of(constructors)
                .filter(constructor -> constructor.getParameterCount() == 0)
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Constructor vacio no encontrado"));
    }

    private List<Field> getFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.toList());
    }

}
