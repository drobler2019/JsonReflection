package com.app.interfaces;

import java.util.List;

public interface ReflectionInstanceService {
    <T> T getInstanceOfClass(Class<?> clazz);
    <T> T jsonToObject(String json, Class<T> clazz);
    <T> List<T> jsonToList(String json, Class<T> clazz);
}
