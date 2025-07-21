package org.example.Logica_DAO;

import org.example.Conexion;
import org.example.Entidad.SesionUsuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SesionUsuarioDAO {

    public boolean insertarSesion(SesionUsuario sesion) {
        String sql = "INSERT INTO SesionUsuario (id_usuario, token, fecha_expiracion) VALUES (?, ?, ?)";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sesion.getIdUsuario());
            ps.setString(2, sesion.getToken());
            ps.setTimestamp(3, sesion.getFechaExpiracion());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean eliminarSesionPorId(int idSesion) {
        String sql = "DELETE FROM SesionUsuario WHERE id_sesion = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSesion);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public SesionUsuario obtenerSesionPorToken(String token) {
        String sql = "SELECT * FROM SesionUsuario WHERE token = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new SesionUsuario(
                        rs.getInt("id_sesion"),
                        rs.getInt("id_usuario"),
                        rs.getString("token"),
                        rs.getTimestamp("fecha_inicio"),
                        rs.getTimestamp("fecha_expiracion")
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<SesionUsuario> listarSesionesPorUsuario(int idUsuario) {
        List<SesionUsuario> sesiones = new ArrayList<>();
        String sql = "SELECT * FROM SesionUsuario WHERE id_usuario = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SesionUsuario sesion = new SesionUsuario(
                        rs.getInt("id_sesion"),
                        rs.getInt("id_usuario"),
                        rs.getString("token"),
                        rs.getTimestamp("fecha_inicio"),
                        rs.getTimestamp("fecha_expiracion")
                );
                sesiones.add(sesion);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return sesiones;
    }
}
