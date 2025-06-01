package com.app.model;

import java.util.List;

public class Subject {
    private int id;
    private String name;
    private List<Status> statuses;

    public Subject() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Status> getStatus() {
        return statuses;
    }

    public void setStatus(List<Status> statuses) {
        this.statuses = statuses;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + statuses +
                '}';
    }
}
