package com.app.interfaces;

public interface JsonReflectionInterface {
    <T> T jsonToObject(String json, Class<T> clazz);
}
