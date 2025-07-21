package org.example.DAO;

import org.example.Entidades.Obstetra;
import org.example.Entidades.Programa;
import org.example.Entidades.RegistroPrueba;
import org.example.Utilidades.Conexion;
import org.example.Utilidades.ControlSesionUsuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegistroPruebaDAO {

    private static final Logger logger = LogManager.getLogger(RegistroPruebaDAO.class);

    private String obtenerUsuarioLogueado() {
        var usuario = ControlSesionUsuario.getUsuarioActual();
        return (usuario != null) ? usuario.getCorreo() : "desconocido";
    }

    public void agregar(RegistroPrueba registro) throws SQLException {
        String sql = "INSERT INTO RegistroPrueba (id_paciente, id_obstetra, id_programa, fecha, observaciones, edad) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, registro.getIdPaciente());
            stmt.setInt(2, registro.getIdObstetra());
            stmt.setInt(3, registro.getIdPrograma());
            stmt.setDate(4, new java.sql.Date(registro.getFecha().getTime()));
            stmt.setString(5, registro.getObservaciones());
            stmt.setInt(6, registro.getEdad());
            stmt.executeUpdate();

            logger.info("Usuario '{}' agregó registro de prueba para paciente ID: {}, programa ID: {}",
                    obtenerUsuarioLogueado(), registro.getIdPaciente(), registro.getIdPrograma());
        } catch (SQLException e) {
            logger.error("Usuario '{}' intentó agregar registro de prueba pero falló: {}",
                    obtenerUsuarioLogueado(), e.getMessage());
            throw e;
        }
    }

    public void editar(RegistroPrueba registro) throws SQLException {
        String sql = "UPDATE RegistroPrueba SET id_paciente = ?, id_obstetra = ?, id_programa = ?, fecha = ?, observaciones = ?, edad = ? WHERE id_registro = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, registro.getIdPaciente());
            stmt.setInt(2, registro.getIdObstetra());
            stmt.setInt(3, registro.getIdPrograma());
            stmt.setDate(4, new java.sql.Date(registro.getFecha().getTime()));
            stmt.setString(5, registro.getObservaciones());
            stmt.setInt(6, registro.getEdad());
            stmt.setInt(7, registro.getIdRegistro());
            stmt.executeUpdate();

            logger.info("Usuario '{}' editó registro de prueba ID: {}", obtenerUsuarioLogueado(), registro.getIdRegistro());
        } catch (SQLException e) {
            logger.error("Usuario '{}' intentó editar registro de prueba ID: {} pero falló: {}",
                    obtenerUsuarioLogueado(), registro.getIdRegistro(), e.getMessage());
            throw e;
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM RegistroPrueba WHERE id_registro = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                logger.info("Usuario '{}' eliminó registro de prueba ID: {}", obtenerUsuarioLogueado(), id);
            } else {
                logger.warn("Usuario '{}' intentó eliminar registro de prueba ID: {} pero no fue encontrado", obtenerUsuarioLogueado(), id);
            }
        } catch (SQLException e) {
            logger.error("Usuario '{}' intentó eliminar registro de prueba ID: {} pero falló: {}",
                    obtenerUsuarioLogueado(), id, e.getMessage());
            throw e;
        }
    }

    public List<RegistroPrueba> listar() throws SQLException {
        List<RegistroPrueba> lista = new ArrayList<>();
        String sql = "SELECT * FROM RegistroPrueba ORDER BY fecha DESC";
        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearRegistro(rs));
            }
        }
        return lista;
    }

    public List<RegistroPrueba> listarPorFecha(Date fecha) throws SQLException {
        List<RegistroPrueba> lista = new ArrayList<>();
        String sql = "SELECT * FROM RegistroPrueba WHERE fecha = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(fecha.getTime()));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearRegistro(rs));
                }
            }
        }
        return lista;
    }

    public List<RegistroPrueba> listarPorPaciente(int idPaciente) throws SQLException {
        List<RegistroPrueba> lista = new ArrayList<>();
        String sql = "SELECT * FROM RegistroPrueba WHERE id_paciente = ? ORDER BY fecha DESC";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPaciente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearRegistro(rs));
                }
            }
        }
        return lista;
    }

    public List<RegistroPrueba> listarPorObstetra(int idObstetra) throws SQLException {
        List<RegistroPrueba> lista = new ArrayList<>();
        String sql = "SELECT * FROM RegistroPrueba WHERE id_obstetra = ? ORDER BY fecha DESC";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idObstetra);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearRegistro(rs));
                }
            }
        }
        return lista;
    }

    public List<RegistroPrueba> filtrar(Obstetra obstetra, Programa programa, Date desde, Date hasta, int edadMin, int edadMax) {
        List<RegistroPrueba> resultados = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM RegistroPrueba WHERE fecha BETWEEN ? AND ? AND edad BETWEEN ? AND ?");

        if (obstetra != null) sql.append(" AND id_obstetra = ?");
        if (programa != null) sql.append(" AND id_programa = ?");

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int index = 1;
            stmt.setDate(index++, new java.sql.Date(desde.getTime()));
            stmt.setDate(index++, new java.sql.Date(hasta.getTime()));
            stmt.setInt(index++, edadMin);
            stmt.setInt(index++, edadMax);

            if (obstetra != null) stmt.setInt(index++, obstetra.getIdObstetra());
            if (programa != null) stmt.setInt(index++, programa.getIdPrograma());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                resultados.add(mapearRegistro(rs));
            }
        } catch (SQLException e) {
            logger.error("Error en filtro de registros: {}", e.getMessage(), e);
        }
        return resultados;
    }

    public List<Programa> listarProgramasPorObstetra(int idObstetra) {
        List<Programa> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT p.id_programa, p.nombre, p.descripcion " +
                "FROM RegistroPrueba r " +
                "JOIN Programa p ON r.id_programa = p.id_programa " +
                "WHERE r.id_obstetra = ?";

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idObstetra);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Programa p = new Programa();
                p.setIdPrograma(rs.getInt("id_programa"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                lista.add(p);
            }
        } catch (SQLException e) {
            logger.error("Error al listar programas por obstetra ID {}: {}", idObstetra, e.getMessage(), e);
        }

        return lista;
    }

    public int contarPruebasPorObstetraYPrograma(int idObstetra, int idPrograma) {
        String sql = "SELECT COUNT(*) FROM RegistroPrueba WHERE id_obstetra = ? AND id_programa = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idObstetra);
            stmt.setInt(2, idPrograma);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.error("Error al contar pruebas por obstetra ID {} y programa ID {}: {}", idObstetra, idPrograma, e.getMessage(), e);
        }
        return 0;
    }

    public RegistroPrueba buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM RegistroPrueba WHERE id_registro = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearRegistro(rs);
                }
            }
        }
        return null;
    }

    public int contarPruebasPorObstetraYProgramaYAnio(int idObstetra, int idPrograma, int anio) {
        String sql = "SELECT COUNT(*) FROM RegistroPrueba " +
                "WHERE id_obstetra = ? AND id_programa = ? AND YEAR(fecha) = ?";

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idObstetra);
            ps.setInt(2, idPrograma);
            ps.setInt(3, anio);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException ex) {
            logger.error("Error al contar pruebas por obstetra ID {}, programa ID {} y año {}: {}", idObstetra, idPrograma, anio, ex.getMessage(), ex);
        }
        return 0;
    }

    public int contarPruebasPorObstetraYProgramaYAnioYMes(int idObstetra, int idPrograma, int anio, int mes) {
        String sql = "SELECT COUNT(*) FROM RegistroPrueba " +
                "WHERE id_obstetra = ? AND id_programa = ? " +
                "AND YEAR(fecha) = ? AND MONTH(fecha) = ?";

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idObstetra);
            ps.setInt(2, idPrograma);
            ps.setInt(3, anio);
            ps.setInt(4, mes);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException ex) {
            logger.error("Error al contar pruebas por obstetra ID {}, programa ID {}, año {} y mes {}: {}",
                    idObstetra, idPrograma, anio, mes, ex.getMessage(), ex);
        }
        return 0;
    }

    public int contarPruebasPorObstetraYProgramaYAnioHastaMes(int idObstetra, int idPrograma, int anio, int mesHasta) {
        String sql = "SELECT COUNT(*) FROM RegistroPrueba " +
                "WHERE id_obstetra = ? AND id_programa = ? " +
                "AND YEAR(fecha) = ? AND MONTH(fecha) <= ?";

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idObstetra);
            ps.setInt(2, idPrograma);
            ps.setInt(3, anio);
            ps.setInt(4, mesHasta);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException ex) {
            logger.error("Error al contar pruebas acumuladas hasta mes {} del año {}: {}", mesHasta, anio, ex.getMessage(), ex);
        }
        return 0;
    }



    private RegistroPrueba mapearRegistro(ResultSet rs) throws SQLException {
        RegistroPrueba reg = new RegistroPrueba();
        reg.setIdRegistro(rs.getInt("id_registro"));
        reg.setIdPaciente(rs.getInt("id_paciente"));
        reg.setIdObstetra(rs.getInt("id_obstetra"));
        reg.setIdPrograma(rs.getInt("id_programa"));
        reg.setFecha(rs.getDate("fecha"));
        reg.setObservaciones(rs.getString("observaciones"));
        reg.setEdad(rs.getInt("edad"));

        return reg;
    }
}
