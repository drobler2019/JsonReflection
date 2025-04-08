package com.app.interfaces;

public interface ValueNumberInterface<E extends Number> {
    E getNumericValue(String value);
}
