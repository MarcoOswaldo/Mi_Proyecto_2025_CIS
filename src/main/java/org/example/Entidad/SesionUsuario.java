package org.example.Entidad;

import java.sql.Timestamp;
import java.util.Date;

public class SesionUsuario {
    private int idSesion;
    private int idUsuario;
    private String token;
    private Date fechaInicio;
    private Date fechaExpiracion;

    public SesionUsuario() {
    }

    public SesionUsuario(int idSesion, int idUsuario, String token, Date fechaInicio, Date fechaExpiracion) {
        this.idSesion = idSesion;
        this.idUsuario = idUsuario;
        this.token = token;
        this.fechaInicio = fechaInicio;
        this.fechaExpiracion = fechaExpiracion;
    }
    // Getters y Setters

    public int getIdSesion() {
        return idSesion;
    }

    public void setIdSesion(int idSesion) {
        this.idSesion = idSesion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Timestamp getFechaExpiracion() {
        return (Timestamp) fechaExpiracion;
    }

    public void setFechaExpiracion(Date fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }
}
