package com.app.controller;

import com.app.interfaces.JsonReflectionInterface;
import com.app.interfaces.LoadResourceInterface;
import com.app.service.JsonReflectionImpl;
import com.app.service.LoadResourceImpl;

import java.util.List;

public enum ReflectionController {

    INSTANCE(new JsonReflectionImpl());

    private final JsonReflectionInterface jsonReflectionInterface;

    ReflectionController(JsonReflectionInterface jsonReflectionInterface) {
        this.jsonReflectionInterface = jsonReflectionInterface;
    }

    public void init(Class<?> clazz) {
        LoadResourceInterface loadResourceInterface = new LoadResourceImpl();
        var json = loadResourceInterface.readFile("src/main/resources/data.json");
        if (clazz.isArray()) {
            List<?> objects = jsonReflectionInterface.jsonToList(json, clazz);
            objects.forEach(System.out::println);
            return;
        }
        var object = jsonReflectionInterface.jsonToObject(json, clazz);
        System.out.println(object);
    }
}
