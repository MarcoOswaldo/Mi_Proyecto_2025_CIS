package org.example.Entidades;

public class Obstetra {
    private int idObstetra;
    private int idUsuario;
    private String nombre;
    private String apellido;
    private String dni;
    private String colegio;
    private String numColegiatura;
    private int idEstablecimiento;

    public Obstetra() {
    }

    public Obstetra(int idObstetra, int idUsuario, String nombre, String apellido, String dni, String colegio, String numColegiatura, int idEstablecimiento) {
        this.idObstetra = idObstetra;
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.colegio = colegio;
        this.numColegiatura = numColegiatura;
        this.idEstablecimiento = idEstablecimiento;
    }

    public int getIdObstetra() {
        return idObstetra;
    }

    public void setIdObstetra(int idObstetra) {
        this.idObstetra = idObstetra;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
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

    public String getColegio() {
        return colegio;
    }

    public void setColegio(String colegio) {
        this.colegio = colegio;
    }

    public String getNumColegiatura() {
        return numColegiatura;
    }

    public void setNumColegiatura(String numColegiatura) {
        this.numColegiatura = numColegiatura;
    }

    public int getIdEstablecimiento() {
        return idEstablecimiento;
    }

    public void setIdEstablecimiento(int idEstablecimiento) {
        this.idEstablecimiento = idEstablecimiento;
    }

    public String toString() {
        return nombre + " "+apellido;
    }
}
