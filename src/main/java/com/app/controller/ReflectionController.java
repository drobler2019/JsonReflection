package com.app.controller;

import com.app.interfaces.ReflectionInstanceService;
import com.app.interfaces.LoadFileService;
import com.app.service.ReflectionInstanceServiceImpl;
import com.app.service.LoadResourceServiceImpl;

import java.util.List;

public enum ReflectionController {

    INSTANCE(new ReflectionInstanceServiceImpl());

    private final ReflectionInstanceService reflectionInstanceService;

    ReflectionController(ReflectionInstanceService reflectionInstanceService) {
        this.reflectionInstanceService = reflectionInstanceService;
    }

    public void init(Class<?> clazz) {
        LoadFileService loadFileService = new LoadResourceServiceImpl();
        var json = loadFileService.readFile("src/main/resources/arrays.json");
        if (clazz.isArray()) {
            List<?> objects = reflectionInstanceService.jsonToList(json, clazz);
            objects.forEach(System.out::println);
            return;
        }
        var object = reflectionInstanceService.jsonToObject(json, clazz);
        System.out.println(object);
    }
}
