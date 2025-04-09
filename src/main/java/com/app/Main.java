package com.app;

import com.app.controller.ReflectionController;
import com.app.model.Estudiante;

public class Main {
    public static void main(String[] args) {
        ReflectionController.INSTANCE.init(Estudiante[].class);
    }
}