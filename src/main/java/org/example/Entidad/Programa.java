package org.example.Entidad;

public class Programa {
    private int idPrograma;
    private String nombre;

    public Programa() {
    }

    public Programa(int idPrograma, String nombre) {
        this.idPrograma = idPrograma;
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(int idPrograma) {
        this.idPrograma = idPrograma;
    }

    public String toString() {
        return nombre;
    }
}
