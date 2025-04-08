package com.app;

import com.app.controller.ReflectionController;
import com.app.model.Person;

public class Main {
    public static void main(String[] args) {
        ReflectionController.INSTANCE.init("{}", Person.class);
    }
}