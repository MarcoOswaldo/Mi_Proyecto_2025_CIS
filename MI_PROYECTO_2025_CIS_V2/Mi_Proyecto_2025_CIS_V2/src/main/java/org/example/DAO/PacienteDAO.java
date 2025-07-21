package org.example.DAO;

import org.example.Entidades.Paciente;
import org.example.Utilidades.Conexion;
import org.example.Utilidades.ControlSesionUsuario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO {

    private static final Logger logger = LogManager.getLogger(PacienteDAO.class);

    private String obtenerUsuarioLogueado() {
        var usuario = ControlSesionUsuario.getUsuarioActual();
        return (usuario != null) ? usuario.getCorreo() : "desconocido";
    }

    public void agregar(Paciente paciente) throws SQLException {
        String sql = "INSERT INTO Paciente (nombre, apellido, dni, fecha_nacimiento) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, paciente.getNombre());
            stmt.setString(2, paciente.getApellido());
            stmt.setString(3, paciente.getDni());
            stmt.setDate(4, paciente.getFechaNacimiento());
            stmt.executeUpdate();

            logger.info("Usuario '{}' agregó paciente con DNI: {}", obtenerUsuarioLogueado(), paciente.getDni());
        } catch (SQLException e) {
            logger.error("Usuario '{}' intentó agregar paciente con DNI: {}, pero ocurrió un error: {}",
                    obtenerUsuarioLogueado(), paciente.getDni(), e.getMessage());
            throw e;
        }
    }

    public void editar(Paciente paciente) throws SQLException {
        String sql = "UPDATE Paciente SET nombre = ?, apellido = ?, dni = ?, fecha_nacimiento = ? WHERE id_paciente = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, paciente.getNombre());
            stmt.setString(2, paciente.getApellido());
            stmt.setString(3, paciente.getDni());
            stmt.setDate(4, paciente.getFechaNacimiento());
            stmt.setInt(5, paciente.getIdPaciente());
            stmt.executeUpdate();

            logger.info("Usuario '{}' editó paciente con ID: {}", obtenerUsuarioLogueado(), paciente.getIdPaciente());
        } catch (SQLException e) {
            logger.error("Usuario '{}' intentó editar paciente con ID: {}, pero ocurrió un error: {}",
                    obtenerUsuarioLogueado(), paciente.getIdPaciente(), e.getMessage());
            throw e;
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Paciente WHERE id_paciente = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                logger.info("Usuario '{}' eliminó paciente con ID: {}", obtenerUsuarioLogueado(), id);
            } else {
                logger.warn("Usuario '{}' intentó eliminar paciente con ID: {} pero no se encontró", obtenerUsuarioLogueado(), id);
            }
        } catch (SQLException e) {
            logger.error("Usuario '{}' intentó eliminar paciente con ID: {}, pero ocurrió un error: {}",
                    obtenerUsuarioLogueado(), id, e.getMessage());
            throw e;
        }
    }

    public List<Paciente> listar() throws SQLException {
        List<Paciente> lista = new ArrayList<>();
        String sql = "SELECT * FROM Paciente";
        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearPaciente(rs));
            }
        }
        return lista;
    }

    public Paciente buscarPorDni(String dni) throws SQLException {
        String sql = "SELECT * FROM Paciente WHERE dni = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dni);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearPaciente(rs);
                }
            }
        }
        return null;
    }

    public Paciente buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Paciente WHERE id_paciente = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearPaciente(rs);
                }
            }
        }
        return null;
    }

    private Paciente mapearPaciente(ResultSet rs) throws SQLException {
        Paciente paciente = new Paciente();
        paciente.setIdPaciente(rs.getInt("id_paciente"));
        paciente.setNombre(rs.getString("nombre"));
        paciente.setApellido(rs.getString("apellido"));
        paciente.setDni(rs.getString("dni"));
        paciente.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
        return paciente;
    }
}
