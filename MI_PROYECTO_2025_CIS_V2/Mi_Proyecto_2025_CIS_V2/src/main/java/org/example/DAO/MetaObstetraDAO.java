package org.example.DAO;

import org.example.Entidades.MetaObstetra;
import org.example.Entidades.MetaPrograma;
import org.example.Utilidades.Conexion;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MetaObstetraDAO {

    public void agregar(MetaObstetra meta) throws SQLException {
        String sql = "INSERT INTO MetaObstetra (id_obstetra, id_programa, anio, meta_anual, meta_mensual) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, meta.getIdObstetra());
            stmt.setInt(2, meta.getIdPrograma());
            stmt.setInt(3, meta.getAnio());
            stmt.setInt(4, meta.getMetaAnual());
            stmt.setInt(5, meta.getMetaMensual());
            stmt.executeUpdate();
        }
    }

    public void agregarConConexion(MetaObstetra meta, Connection conn) throws SQLException {
        String sql = "INSERT INTO MetaObstetra (id_obstetra, id_programa, anio, meta_anual, meta_mensual) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, meta.getIdObstetra());
            stmt.setInt(2, meta.getIdPrograma());
            stmt.setInt(3, meta.getAnio());
            stmt.setInt(4, meta.getMetaAnual());
            stmt.setInt(5, meta.getMetaMensual());
            stmt.executeUpdate();
        }
    }


    public void editar(MetaObstetra meta) throws SQLException {
        String sql = "UPDATE MetaObstetra SET id_obstetra = ?, id_programa = ?, anio = ?, meta_anual = ?, meta_mensual = ? WHERE id_meta_obstetra = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, meta.getIdObstetra());
            stmt.setInt(2, meta.getIdPrograma());
            stmt.setInt(3, meta.getAnio());
            stmt.setInt(4, meta.getMetaAnual());
            stmt.setInt(5, meta.getMetaMensual());
            stmt.setInt(6, meta.getIdMetaObstetra());
            stmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM MetaObstetra WHERE id_meta_obstetra = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<MetaObstetra> listar() throws SQLException {
        List<MetaObstetra> lista = new ArrayList<>();
        String sql = "SELECT * FROM MetaObstetra";
        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public MetaObstetra buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM MetaObstetra WHERE id_meta_obstetra = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    public MetaObstetra obtenerMetaObstetra(int idObstetra, int idPrograma, int anio) {


        String sql = "SELECT meta_anual, meta_mensual FROM MetaObstetra WHERE id_obstetra = ? AND id_programa = ? AND anio = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idObstetra);
            stmt.setInt(2, idPrograma);
            stmt.setInt(3, anio);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new MetaObstetra(
                        rs.getInt("meta_anual"),
                        rs.getInt("meta_mensual")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Si no se encuentra la meta
    }

    private MetaObstetra mapear(ResultSet rs) throws SQLException {
        MetaObstetra meta = new MetaObstetra();
        meta.setIdMetaObstetra(rs.getInt("id_meta_obstetra"));
        meta.setIdObstetra(rs.getInt("id_obstetra"));
        meta.setIdPrograma(rs.getInt("id_programa"));
        meta.setAnio(rs.getInt("anio"));
        meta.setMetaAnual(rs.getInt("meta_anual"));
        meta.setMetaMensual(rs.getInt("meta_mensual"));
        return meta;
    }
}
