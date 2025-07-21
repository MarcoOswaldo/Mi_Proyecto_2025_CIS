package org.example.Utilidades;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conexion {
    private static String URL;
    private static String USUARIO;
    private static String CLAVE;

    static {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("db.properties")) {
            props.load(fis);
            URL = props.getProperty("db.url");
            USUARIO = props.getProperty("db.user");
            CLAVE = props.getProperty("db.password");
        } catch (IOException e) {
            System.err.println("No se pudo cargar la configuración de la base de datos.");
            e.printStackTrace();
            // Opcional: asignar valores por defecto o lanzar excepción
        }
    }

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
}
