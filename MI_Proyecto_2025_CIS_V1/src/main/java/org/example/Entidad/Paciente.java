package org.example.Entidad;

import java.sql.Date;

public class Paciente {
    private int idPaciente;
    private String nombre;
    private String apellido;
    private String dni;
    private Date fecha_nacimiento;

    // Constructores, getters y setters
    public Paciente() {}


    public Paciente(int idPaciente, String nombre, String apellido, String dni, Date fecha_nacimiento) {
        this.idPaciente = idPaciente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.fecha_nacimiento = fecha_nacimiento;  // asignado
    }

    public Paciente(String nombre, String apellido, String dni, Date fecha_nacimiento) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.fecha_nacimiento = fecha_nacimiento;  // asignado
    }
    // getters y setters


    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
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

    public Date getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(Date fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    @Override
    public String toString() {
        return nombre + " " + apellido;
    }
}
