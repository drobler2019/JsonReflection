package com.app.model;

import java.util.List;

public class Person extends SuperClass {

    private String lastName;
    private List<Status> statuses;
    private List<Subject> subjects;

    public Person() {
    }

    @Override
    public String toString() {
        return "Person{id=" + id +
                ", name='" + name + '\'' +
                ",lastName='" + lastName + '\'' +
                ",subjects='" + subjects + '\'' +
                ",statuses=" + statuses +'}';
    }
}
