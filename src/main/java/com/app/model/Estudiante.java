package com.app.model;

public class Estudiante extends Person {

    private String identificacion;

    public Estudiante() {
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    @Override
    public String toString() {
        return "Estudiante{" +
                "identificacion='" + identificacion + '\'' + super.toString() +
                '}';
    }
}
