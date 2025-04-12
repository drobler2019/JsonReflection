package com.app.model;

public class Person extends SuperClass {

    private String lastName;
    private Item item;
    private Product product;

    public Person() {
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", item=" + item +
                ", product=" + product +
                '}';
    }
}
