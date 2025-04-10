package com.app.service;

import com.app.interfaces.LoadFileService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoadResourceServiceImpl implements LoadFileService {

    @Override
    public String readFile(String path) {
        var StringBuilder = new StringBuilder();
        try (var reader = new FileReader(path);
             var buffer = new BufferedReader(reader)) {
            String line;
            while ((line = buffer.readLine()) != null)
                StringBuilder.append(line.trim());

            return StringBuilder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
