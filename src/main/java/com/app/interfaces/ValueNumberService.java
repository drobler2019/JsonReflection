package com.app.interfaces;

public interface ValueNumberService<E extends Number> {
    E getNumericValue(String value);
}
