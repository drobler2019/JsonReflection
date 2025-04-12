package com.app;

import com.app.controller.ReflectionController;
import com.app.model.Person;

public class Main {
    public static void main(String[] args) {
        //todo: compatiblidad con arreglos literales aninados en objetos
        ReflectionController.INSTANCE.init(Person.class);
    }
}