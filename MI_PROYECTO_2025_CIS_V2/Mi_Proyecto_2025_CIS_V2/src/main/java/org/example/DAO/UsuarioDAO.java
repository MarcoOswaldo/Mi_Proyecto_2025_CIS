package org.example.DAO;

import org.example.Entidades.Usuario;

import org.example.Utilidades.Conexion;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.example.Utilidades.ControlSesionUsuario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UsuarioDAO {

    private static final Logger logger = LogManager.getLogger(UsuarioDAO.class);

    private String obtenerUsuarioLogueado() {
        Usuario usuarioLogueado = ControlSesionUsuario.getUsuarioActual();
        return (usuarioLogueado != null) ? usuarioLogueado.getCorreo() : "desconocido";
    }

    public void agregar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO Usuario (nombre, correo, password, tipo_usuario) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getCorreo());
            stmt.setString(3, usuario.getPassword());
            stmt.setString(4, usuario.getTipoUsuario());
            stmt.executeUpdate();

            logger.info("Usuario '{}' creó un nuevo usuario: {}", obtenerUsuarioLogueado(), usuario.getCorreo());

        } catch (SQLException e) {
            logger.error("Usuario '{}' intentó crear un usuario, pero ocurrió un error: {}", obtenerUsuarioLogueado(), e.getMessage());
            throw e;
        }
    }

    public boolean registrarUsuario(Usuario usuario) {
        String usuarioLogueado = obtenerUsuarioLogueado();
        try (Connection conn = Conexion.obtenerConexion()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO Usuario (nombre, correo, password, tipo_usuario) VALUES (?, ?, ?, ?)"
            );
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getCorreo());
            stmt.setString(3, usuario.getPassword());
            stmt.setString(4, usuario.getTipoUsuario());
            int filas = stmt.executeUpdate();

            if (filas > 0) {
                logger.info("Usuario '{}' registró un nuevo usuario: {}", usuarioLogueado, usuario.getCorreo());
            }

            return filas > 0;
        } catch (SQLException e) {
            logger.error("Usuario '{}' intentó registrar un usuario, pero ocurrió un error: {}", usuarioLogueado, e.getMessage());
            return false;
        }
    }

    public boolean validarLogin(String correo, String password) throws SQLException {
        String sql = "SELECT password FROM Usuario WHERE correo = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, correo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String hash = rs.getString("password");
                    boolean valido = BCrypt.checkpw(password, hash);
                    logger.info("Intento de login para usuario '{}', resultado: {}", correo, valido ? "exitoso" : "fallido");
                    return valido;
                }
            }
        }
        logger.warn("Intento de login para usuario '{}', no encontrado", correo);
        return false;
    }

    public Usuario buscarPorCorreo(String correo) throws SQLException {
        String sql = "SELECT * FROM Usuario WHERE correo = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, correo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        }
        return null;
    }

    public Usuario buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Usuario WHERE id_usuario = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        }
        return null;
    }

    public List<Usuario> listar() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM Usuario ORDER BY fecha_creacion DESC";
        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }
        }
        return lista;
    }

    public void editar(Usuario usuario) throws SQLException {
        String sql = "UPDATE Usuario SET nombre = ?, correo = ?, password = ?, tipo_usuario = ? WHERE id_usuario = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getCorreo());
            stmt.setString(3, usuario.getPassword());
            stmt.setString(4, usuario.getTipoUsuario());
            stmt.setInt(5, usuario.getId());
            stmt.executeUpdate();

            logger.info("Usuario '{}' editó el usuario con ID: {}", obtenerUsuarioLogueado(), usuario.getId());

        } catch (SQLException e) {
            logger.error("Usuario '{}' intentó editar el usuario con ID: {}, pero ocurrió un error: {}", obtenerUsuarioLogueado(), usuario.getId(), e.getMessage());
            throw e;
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Usuario WHERE id_usuario = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                logger.info("Usuario '{}' eliminó al usuario con ID: {}", obtenerUsuarioLogueado(), id);
            } else {
                logger.warn("Usuario '{}' intentó eliminar al usuario con ID: {} pero no se encontró", obtenerUsuarioLogueado(), id);
            }

        } catch (SQLException e) {
            logger.error("Usuario '{}' intentó eliminar al usuario con ID: {}, pero ocurrió un error: {}", obtenerUsuarioLogueado(), id, e.getMessage());
            throw e;
        }
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id_usuario"));
        u.setNombre(rs.getString("nombre"));
        u.setCorreo(rs.getString("correo"));
        u.setPassword(rs.getString("password")); // ¡NO mostrar esto en la interfaz!
        u.setTipoUsuario(rs.getString("tipo_usuario"));
        u.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
        return u;
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
            logger.error("Error al verificar existencia de correo: {}", e.getMessage());
        }
        return false;
    }

    public boolean cambiarPasswordUsuarioActual(String passwordActual, String nuevoPassword) {
        Usuario usuarioLogueado = ControlSesionUsuario.getUsuarioActual();

        if (usuarioLogueado == null) {
            logger.warn("Intento de cambio de contraseña sin usuario logueado");
            return false;
        }

        String correo = usuarioLogueado.getCorreo();

        try (Connection conn = Conexion.obtenerConexion()) {
            // Obtener hash actual
            PreparedStatement selectStmt = conn.prepareStatement("SELECT password FROM Usuario WHERE correo = ?");
            selectStmt.setString(1, correo);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                String hashActual = rs.getString("password");
                if (!BCrypt.checkpw(passwordActual, hashActual)) {
                    logger.warn("Usuario '{}' intentó cambiar la contraseña, pero la contraseña actual no coincide", correo);
                    return false;
                }

                // Generar nuevo hash y actualizar
                String nuevoHash = BCrypt.hashpw(nuevoPassword, BCrypt.gensalt());
                PreparedStatement updateStmt = conn.prepareStatement("UPDATE Usuario SET password = ? WHERE correo = ?");
                updateStmt.setString(1, nuevoHash);
                updateStmt.setString(2, correo);
                int filas = updateStmt.executeUpdate();

                if (filas > 0) {
                    logger.info("Usuario '{}' cambió su contraseña exitosamente", correo);
                    return true;
                }
            }
        } catch (SQLException e) {
            logger.error("Error al cambiar la contraseña del usuario '{}': {}", correo, e.getMessage());
        }

        return false;
    }

}


