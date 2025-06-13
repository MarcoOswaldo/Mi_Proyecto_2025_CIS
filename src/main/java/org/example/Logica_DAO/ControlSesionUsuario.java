package org.example.Logica_DAO;

import org.example.Entidad.Usuario;

public class ControlSesionUsuario {
    private static Usuario usuarioActual;

    public static void iniciarSesion(Usuario u) {
        usuarioActual = u;
    }

    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public static boolean esAdmin() {
        return usuarioActual != null && "administrador".equals(usuarioActual.getTipoUsuario());
    }

   /* public static boolean esObstetra(){
        return usuarioActual.getIdObstetra()==0;
    }*/

    public static boolean esNormal() {
        return usuarioActual != null && "normal".equals(usuarioActual.getTipoUsuario());
    }

    public static void cerrarSesion() {
        usuarioActual = null;
    }
}
