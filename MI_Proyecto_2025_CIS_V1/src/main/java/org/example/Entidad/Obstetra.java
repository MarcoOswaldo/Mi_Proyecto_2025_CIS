package org.example.Entidad;

public class Obstetra {
    private int idObstetra;
    private String nombre;
    private String apellido;
    private String dni;

    public Obstetra() {
    }

    public Obstetra(int idObstetra, String nombre, String apellido, String dni) {
        this.idObstetra = idObstetra;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
    }

    public Obstetra(String nombre, String apellido, String dni) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
    }

    public int getIdObstetra() {
        return idObstetra;
    }

    public void setIdObstetra(int idObstetra) {
        this.idObstetra = idObstetra;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    // En la clase Obstetra.java
    @Override
    public String toString() {
        return nombre + " " + apellido; // o lo que quieras mostrar
    }

}
