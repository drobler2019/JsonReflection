package com.app;

import com.app.controller.ReflectionController;

public class Main {
    public static void main(String[] args) {
        ReflectionController.INSTANCE.init(boolean[].class);
    }
}