package org.example.Entidades;

public class Programa {
    private int idPrograma;
    private String nombre;
    private String descripcion;

    public Programa() {
    }

    public Programa(int idPrograma, String nombre, String descripcion) {
        this.idPrograma = idPrograma;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(int idPrograma) {
        this.idPrograma = idPrograma;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String toString() {
        return nombre;
    }
}
