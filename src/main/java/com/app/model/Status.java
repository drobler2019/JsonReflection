package com.app.model;

public class Status {
    private long id;
    private String name;

    public Status() {}

    @Override
    public String toString() {
        return "Status{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
