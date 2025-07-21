package org.example.Logica_DAO;

import org.example.Conexion;
import org.example.Entidad.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public Usuario obtenerPorCorreo(String correo) {
        try (Connection conn = Conexion.obtenerConexion()) {
            String sql = "SELECT password, tipo_usuario FROM Usuario WHERE correo = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String password = rs.getString("password");
                String tipoUsuario = rs.getString("tipo_usuario");
                return new Usuario(correo, password, tipoUsuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean correoExiste(String correo) {
        try (Connection conn = Conexion.obtenerConexion()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM Usuario WHERE correo = ?");
            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean registrarUsuario(Usuario usuario) {
        try (Connection conn = Conexion.obtenerConexion()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO Usuario (nombre, correo, password, tipo_usuario) VALUES (?, ?, ?, ?)"
            );
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getCorreo());
            stmt.setString(3, usuario.getPassword());
            stmt.setString(4, usuario.getTipoUsuario());
            int filas = stmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void actualizarUsuario(Usuario u) throws SQLException {
        String sql = "UPDATE Usuario SET nombre = ?, correo = ?, password = ?, tipo_usuario = ?, id_obstetra = ? WHERE id_usuario = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, u.getNombre());
            stmt.setString(2, u.getCorreo());
            stmt.setString(3, u.getPassword());
            stmt.setString(4, u.getTipoUsuario());
            if (u.getIdObstetra() != null)
                stmt.setInt(5, u.getIdObstetra());
            else
                stmt.setNull(5, Types.INTEGER);
            stmt.setInt(6, u.getId());
            stmt.executeUpdate();
        }
    }

    public void eliminarUsuario(int id) throws SQLException {
        String sql = "DELETE FROM Usuario WHERE id_usuario = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Usuario> listarUsuarios() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM Usuario";
        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Usuario u = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("password"),
                        rs.getString("tipo_usuario"),
                        rs.getObject("id_obstetra") != null ? rs.getInt("id_obstetra") : null
                );
                lista.add(u);
            }
        }
        return lista;
    }
}
