package org.example.Entidades;

import java.util.Date;

public class RegistroPrueba {
    private int idRegistro;
    private int idPaciente;
    private int idObstetra;
    private int idPrograma;
    private Date fecha;
    private String observaciones;
    private int edad;

    public RegistroPrueba() {
    }

    public RegistroPrueba(int idRegistro, int idPaciente, int idObstetra, int idPrograma, Date fecha, String observaciones, int edad) {
        this.idRegistro = idRegistro;
        this.idPaciente = idPaciente;
        this.idObstetra = idObstetra;
        this.idPrograma = idPrograma;
        this.fecha = fecha;
        this.observaciones = observaciones;
        this.edad = edad;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public int getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(int idRegistro) {
        this.idRegistro = idRegistro;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public int getIdObstetra() {
        return idObstetra;
    }

    public void setIdObstetra(int idObstetra) {
        this.idObstetra = idObstetra;
    }

    public int getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(int idPrograma) {
        this.idPrograma = idPrograma;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
