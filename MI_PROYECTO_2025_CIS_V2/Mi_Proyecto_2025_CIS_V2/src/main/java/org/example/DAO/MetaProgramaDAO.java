package org.example.DAO;

import org.example.Entidades.MetaPrograma;
import org.example.Entidades.Obstetra;
import org.example.Utilidades.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MetaProgramaDAO {

    public void agregar(MetaPrograma meta) throws SQLException {
        String sql = "INSERT INTO MetaPrograma (id_programa, anio, meta_anual, meta_mensual) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, meta.getIdPrograma());
            stmt.setInt(2, meta.getAnio());
            stmt.setInt(3, meta.getMetaAnual());
            stmt.setInt(4, meta.getMetaMensual());
            stmt.executeUpdate();
        }
    }

    public void editar(MetaPrograma meta) throws SQLException {
        String sql = "UPDATE MetaPrograma SET id_programa = ?, anio = ?, meta_anual = ?, meta_mensual = ? WHERE id_meta_programa = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, meta.getIdPrograma());
            stmt.setInt(2, meta.getAnio());
            stmt.setInt(3, meta.getMetaAnual());
            stmt.setInt(4, meta.getMetaMensual());
            stmt.setInt(5, meta.getIdMetaPrograma());
            stmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM MetaPrograma WHERE id_meta_programa = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<MetaPrograma> listar() throws SQLException {
        List<MetaPrograma> lista = new ArrayList<>();
        String sql = "SELECT * FROM MetaPrograma";
        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public MetaPrograma buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM MetaPrograma WHERE id_meta_programa = ?";
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

    private MetaPrograma mapear(ResultSet rs) throws SQLException {
        MetaPrograma meta = new MetaPrograma();
        meta.setIdMetaPrograma(rs.getInt("id_meta_programa"));
        meta.setIdPrograma(rs.getInt("id_programa"));
        meta.setAnio(rs.getInt("anio"));
        meta.setMetaAnual(rs.getInt("meta_anual"));
        meta.setMetaMensual(rs.getInt("meta_mensual"));
        return meta;
    }
}
