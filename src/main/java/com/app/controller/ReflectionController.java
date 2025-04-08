package com.app.controller;

import com.app.interfaces.JsonReflectionInterface;
import com.app.service.JsonReflectionImpl;

public enum ReflectionController {

    INSTANCE(new JsonReflectionImpl());

    private final JsonReflectionInterface jsonReflectionInterface;

    ReflectionController(JsonReflectionInterface jsonReflectionInterface) {
        this.jsonReflectionInterface = jsonReflectionInterface;
    }

    public void init(String json, Class<?> clazz) {
        var object = jsonReflectionInterface.jsonToObject(json, clazz);
        System.out.println(object);
    }
}
