package org.example.Entidad;


public class Usuario {
    private int id;
    private String nombre;
    private String correo;
    private String password;
    private String tipoUsuario;
    private Integer idObstetra;


    public Usuario() {}

    public Usuario(String correo, String password, String tipoUsuario) {
        this.correo = correo;
        this.password = password;
        this.tipoUsuario = tipoUsuario;
    }

    public Usuario(int id, String nombre, String correo, String password, String tipoUsuario, Integer idObstetra) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.tipoUsuario = tipoUsuario;
        this.idObstetra = idObstetra;
    }

    public Usuario(String nombre, String correo, String password, String tipoUsuario) {
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.tipoUsuario = tipoUsuario;
    }

    public Usuario(String nombre, String correo, String password, String tipoUsuario, Integer idObstetra) {
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.tipoUsuario = tipoUsuario;
        this.idObstetra = idObstetra;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getIdObstetra() {
        return idObstetra;
    }

    public void setIdObstetra(Integer idObstetra) {
        this.idObstetra = idObstetra;
    }
}
