package org.example.DAO;

import org.example.Entidades.MetaObstetra;
import org.example.Entidades.MetaPrograma;
import org.example.Entidades.Obstetra;
import org.example.Utilidades.Conexion;
import org.example.Utilidades.ControlSesionUsuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ObstetraDAO {

    private static final Logger logger = LogManager.getLogger(ObstetraDAO.class);

    private String obtenerUsuarioLogueado() {
        var usuario = ControlSesionUsuario.getUsuarioActual();
        return (usuario != null) ? usuario.getCorreo() : "desconocido";
    }

    /*public void agregar(Obstetra obstetra) throws SQLException {
        String sql = "INSERT INTO Obstetra (id_usuario, nombre, apellido, dni, colegio, numero_colegiatura, id_establecimiento) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, obstetra.getIdUsuario());
            stmt.setString(2, obstetra.getNombre());
            stmt.setString(3, obstetra.getApellido());
            stmt.setString(4, obstetra.getDni());
            stmt.setString(5, obstetra.getColegio());
            stmt.setString(6, obstetra.getNumColegiatura());
            stmt.setInt(7, obstetra.getIdEstablecimiento());
            stmt.executeUpdate();

            logger.info("Usuario '{}' agregó obstetra: {} {}", obtenerUsuarioLogueado(), obstetra.getNombre(), obstetra.getApellido());
        } catch (SQLException e) {
            logger.error("Usuario '{}' intentó agregar obstetra pero falló: {}", obtenerUsuarioLogueado(), e.getMessage());
            throw e;
        }
    }*/


    public void agregar(Obstetra obstetra) throws SQLException {
        Connection conn = null;
        try {
            conn = Conexion.obtenerConexion();
            conn.setAutoCommit(false); // iniciar transacción

            // 1. Insertar obstetra
            String insertSql = "INSERT INTO Obstetra (id_usuario, nombre, apellido, dni, colegio, numero_colegiatura, id_establecimiento) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                stmt.setInt(1, obstetra.getIdUsuario());
                stmt.setString(2, obstetra.getNombre());
                stmt.setString(3, obstetra.getApellido());
                stmt.setString(4, obstetra.getDni());
                stmt.setString(5, obstetra.getColegio());
                stmt.setString(6, obstetra.getNumColegiatura());
                stmt.setInt(7, obstetra.getIdEstablecimiento());
                stmt.executeUpdate();
            }

            // 2. Obtener todos los programas y sus metas del año actual
            int anioActual = java.time.LocalDate.now().getYear();
            MetaProgramaDAO metaProgramaDAO = new MetaProgramaDAO();
            List<MetaPrograma> metasPrograma = metaProgramaDAO.listar();

            // 3. Obtener todos los obstetras
            List<Obstetra> obstetras = this.listar();
            int totalObstetras = obstetras.size();

            // 4. Recalcular metas y actualizar tabla MetaObstetra
            MetaObstetraDAO metaObstetraDAO = new MetaObstetraDAO();

            try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM MetaObstetra WHERE anio = ?")) {
                deleteStmt.setInt(1, anioActual);
                deleteStmt.executeUpdate();
            }

            for (MetaPrograma mp : metasPrograma) {
                if (mp.getAnio() != anioActual) continue;

                int metaAnual = (int) Math.ceil((double) mp.getMetaAnual() / totalObstetras);
                int metaMensual = (int) Math.ceil((double) mp.getMetaMensual() / totalObstetras);


                for (Obstetra obs : obstetras) {
                    MetaObstetra metaObstetra = new MetaObstetra();
                    metaObstetra.setIdObstetra(obs.getIdObstetra());
                    metaObstetra.setIdPrograma(mp.getIdPrograma());
                    metaObstetra.setAnio(anioActual);
                    metaObstetra.setMetaAnual(metaAnual);
                    metaObstetra.setMetaMensual(metaMensual);

                    metaObstetraDAO.agregarConConexion(metaObstetra, conn);

                }
            }

            conn.commit(); // confirmar transacción
            logger.info("Usuario '{}' agregó obstetra y recalculó metas.", obtenerUsuarioLogueado());

        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            logger.error("Error al agregar obstetra con recálculo de metas: {}", e.getMessage(), e);
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
        }
    }


    public void editar(Obstetra obstetra) throws SQLException {
        String sql = "UPDATE Obstetra SET id_usuario = ?, nombre = ?, apellido = ?, dni = ?, colegio = ?, numero_colegiatura = ?, id_establecimiento = ? " +
                "WHERE id_obstetra = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, obstetra.getIdUsuario());
            stmt.setString(2, obstetra.getNombre());
            stmt.setString(3, obstetra.getApellido());
            stmt.setString(4, obstetra.getDni());
            stmt.setString(5, obstetra.getColegio());
            stmt.setString(6, obstetra.getNumColegiatura());
            stmt.setInt(7, obstetra.getIdEstablecimiento());
            stmt.setInt(8, obstetra.getIdObstetra());
            stmt.executeUpdate();

            logger.info("Usuario '{}' editó obstetra ID: {}", obtenerUsuarioLogueado(), obstetra.getIdObstetra());
        } catch (SQLException e) {
            logger.error("Usuario '{}' intentó editar obstetra ID: {} pero falló: {}", obtenerUsuarioLogueado(), obstetra.getIdObstetra(), e.getMessage());
            throw e;
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Obstetra WHERE id_obstetra = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                logger.info("Usuario '{}' eliminó obstetra ID: {}", obtenerUsuarioLogueado(), id);
            } else {
                logger.warn("Usuario '{}' intentó eliminar obstetra ID: {} pero no fue encontrado", obtenerUsuarioLogueado(), id);
            }
        } catch (SQLException e) {
            logger.error("Usuario '{}' intentó eliminar obstetra ID: {} pero falló: {}", obtenerUsuarioLogueado(), id, e.getMessage());
            throw e;
        }
    }

    public List<Obstetra> listar() throws SQLException {
        List<Obstetra> lista = new ArrayList<>();
        String sql = "SELECT * FROM Obstetra";
        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearObstetra(rs));
            }
        }
        return lista;
    }

    public Obstetra buscarPorDni(String dni) throws SQLException {
        String sql = "SELECT * FROM Obstetra WHERE dni = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dni);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearObstetra(rs);
                }
            }
        }
        return null;
    }

    public Obstetra buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Obstetra WHERE id_obstetra = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearObstetra(rs);
                }
            }
        }
        return null;
    }

    public Obstetra buscarPorUsuarioId(int idUsuario) throws SQLException {
        String sql = "SELECT * FROM Obstetra WHERE id_usuario = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearObstetra(rs);
                }
            }
        }
        return null;
    }

    public int obtenerIdObstetraPorIdUsuario(int idUsuario) {
        int idObstetra = -1;
        String sql = "SELECT id_obstetra FROM Obstetra WHERE id_usuario = ?";

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                idObstetra = rs.getInt("id_obstetra");
            }
        } catch (SQLException e) {
            logger.error("Error al obtener id obstetra por usuario ID {}: {}", idUsuario, e.getMessage(), e);
        }

        return idObstetra;
    }

    public Obstetra obtenerObstetraPorIdUsuario(int idUsuario) throws SQLException {
        Obstetra obstetra = null;
        String sql = "SELECT * FROM Obstetra WHERE id_usuario = ?";

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                obstetra = mapearObstetra(rs);
            }
        } catch (SQLException e) {
            logger.error("Error al obtener obstetra por usuario ID {}: {}", idUsuario, e.getMessage(), e);
            throw new SQLException("Error al obtener obstetra");
        }

        return obstetra;
    }

    private Obstetra mapearObstetra(ResultSet rs) throws SQLException {
        Obstetra obs = new Obstetra();
        obs.setIdObstetra(rs.getInt("id_obstetra"));
        obs.setIdUsuario(rs.getInt("id_usuario"));
        obs.setNombre(rs.getString("nombre"));
        obs.setApellido(rs.getString("apellido"));
        obs.setDni(rs.getString("dni"));
        obs.setColegio(rs.getString("colegio"));
        obs.setNumColegiatura(rs.getString("numero_colegiatura"));
        obs.setIdEstablecimiento(rs.getInt("id_establecimiento"));
        return obs;
    }
}
