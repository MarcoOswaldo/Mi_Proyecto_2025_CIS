package org.example.Utilidades;


import java.util.regex.Pattern;

public class Validaciones {

    // --- GENERALES ---
    public static boolean noVacio(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    public static boolean longitudMinima(String texto, int longitud) {
        return texto != null && texto.trim().length() >= longitud;
    }

    public static boolean contieneSoloLetras(String texto) {
        return texto != null && texto.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+");
    }

    // --- USUARIO ---
    public static boolean esCorreoValido(String correo) {
        String regex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(regex, correo);
    }

    public static boolean esPasswordSegura(String pass) {
        return pass != null && pass.length() >= 6;
    }

    // --- DNI ---
    public static boolean esDniValido(String dni) {
        return dni != null && dni.matches("\\d{8}");
    }

    // --- COLEGIATURA ---
    public static boolean esNumeroColegiaturaValido(String numero) {
        return numero != null && numero.matches("\\d{4,10}");
    }

    // --- FECHAS ---
    public static boolean esAnioValido(int anio) {
        return anio >= 1900 && anio <= 2100;
    }

    // --- NUMEROS ---
    public static boolean esNumeroPositivo(int numero) {
        return numero >= 0;
    }

    public static boolean esMetaMensualValida(int mensual, int anual) {
        return mensual <= anual && mensual >= 0;
    }

    public static boolean esNumero(String cadena) {
        if (cadena == null || cadena.trim().isEmpty()) {
            return false;
        }

        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

