package com.app.service;

import com.app.interfaces.ReflectionInstanceService;
import com.app.interfaces.ValidateFormatJsonService;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.Optional;
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
    @SuppressWarnings("unchecked")
    public <T> List<T> jsonToList(String json, Class<T> clazz) {
        if (!clazz.isArray()) {
            throw new IllegalArgumentException("El tipo de clase debe ser un array");
        }
        if (!json.contains("{")) {
            return (List<T>) validateFormatJsonService.getArrayJsonToList(json).stream()
                    .map(o -> typesServiceImpl.convertValue(clazz.getComponentType(), o))
                    .collect(Collectors.toList());
        }
        var mapList = validateFormatJsonService.getListMap(json);
        var response = new ArrayList<T>();
        for (Map<String, String> map : mapList) {
            this.map = map;
            response.add(jsonToObject(getInstanceOfClass(clazz.getComponentType())));
        }
        return response;
    }

    private <T> T jsonToObject(T instance) {
        var fields = getFields(instance.getClass());
        Class<?> superclass = instance.getClass().getSuperclass();
        while (!superclass.equals(Object.class)) {
            fields.addAll(List.of(superclass.getDeclaredFields()));
            superclass = superclass.getSuperclass();
        }
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            if (map.containsKey(fieldName)) {
                try {
                    Object value = typesServiceImpl.convertValue(field.getType(), map.get(fieldName));
                    setObject(value);
                    field.set(instance, value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    private <T> void setObject(Object object) throws IllegalAccessException {
        if (typesServiceImpl.isPresent(object.getClass())) {
            return;
        }
        T subInstance = (T) object;
        var fields = subInstance.getClass().getDeclaredFields();
        var className = subInstance.getClass().getSimpleName().toLowerCase();
        String json = map.get(className);
        if (json != null) {
            submap = validateFormatJsonService.getMap(json);
        }
        for (Field field : fields) {
            field.setAccessible(true);
            if (submap.containsKey(field.getName())) {
                var objectValue = typesServiceImpl.convertValue(field.getType(), submap.get(field.getName()));
                if (!typesServiceImpl.isPresent(objectValue.getClass())) {
                    submap = validateFormatJsonService.getMap(submap.get(objectValue.getClass().getSimpleName().toLowerCase()));
                }
                setObject(objectValue);
                field.set(subInstance, objectValue);
            }
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

    private List<Field> getFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.toList());
    }

}
