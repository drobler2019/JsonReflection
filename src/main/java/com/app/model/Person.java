package com.app.model;

public class Person {

    private long id;
    private String name;
    private String lastName;
    private Status status;

    public Person() {}

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", status=" + status +
                '}';
    }
}
