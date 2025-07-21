package org.example.Entidades;

public class EstablecimientoSalud {
    private int idEstablecimiento;
    private String redSalud;
    private String microRed;
    private String renipress;
    private String nombre;
    private String categoria;

    public EstablecimientoSalud() {
    }

    public EstablecimientoSalud(int idEstablecimiento, String redSalud, String microRed, String renipress, String nombre, String categoria) {
        this.idEstablecimiento = idEstablecimiento;
        this.redSalud = redSalud;
        this.microRed = microRed;
        this.renipress = renipress;
        this.nombre = nombre;
        this.categoria = categoria;
    }

    public int getIdEstablecimiento() {
        return idEstablecimiento;
    }

    public void setIdEstablecimiento(int idEstablecimiento) {
        this.idEstablecimiento = idEstablecimiento;
    }

    public String getRedSalud() {
        return redSalud;
    }

    public void setRedSalud(String redSalud) {
        this.redSalud = redSalud;
    }

    public String getMicroRed() {
        return microRed;
    }

    public void setMicroRed(String microRed) {
        this.microRed = microRed;
    }

    public String getRenipress() {
        return renipress;
    }

    public void setRenipress(String renipress) {
        this.renipress = renipress;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return nombre;
    }

}
