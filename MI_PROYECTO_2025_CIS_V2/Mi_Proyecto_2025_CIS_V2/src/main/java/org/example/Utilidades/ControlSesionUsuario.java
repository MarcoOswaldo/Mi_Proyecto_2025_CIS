package org.example.Utilidades;

import org.example.Entidades.Usuario;

import java.time.LocalDate;

public class ControlSesionUsuario {
    private static Usuario usuarioActual;

    public static void iniciarSesion(Usuario usuario) {
        usuarioActual = usuario;
    }

    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public static boolean esAdmin() {
        return usuarioActual != null && "administrador".equalsIgnoreCase(usuarioActual.getTipoUsuario());
    }

    public static boolean esObstetra() {
        return usuarioActual != null && "obstetra".equalsIgnoreCase(usuarioActual.getTipoUsuario());
    }

    public static void cerrarSesion() {
        usuarioActual = null;
    }
    public static int getAnioActual() {
        return LocalDate.now().getYear();
    }
}
