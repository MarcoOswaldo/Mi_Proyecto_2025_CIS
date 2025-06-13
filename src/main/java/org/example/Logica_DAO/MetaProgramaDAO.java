package org.example.Logica_DAO;


import org.example.Conexion;
import org.example.Entidad.MetaPrograma;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MetaProgramaDAO {

    // Inserta una nueva meta para un programa y año
    public boolean insertarMetaPrograma(MetaPrograma meta) {
        String sql = "INSERT INTO MetaPrograma (id_programa, anio, meta_anual) VALUES (?, ?, ?)";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, meta.getIdPrograma());
            ps.setInt(2, meta.getAnio());
            ps.setInt(3, meta.getMetaAnual());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Actualiza la meta anual de un programa para un año específico
    public boolean actualizarMetaPrograma(MetaPrograma meta) {
        String sql = "UPDATE MetaPrograma SET meta_anual = ? WHERE id_programa = ? AND anio = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, meta.getMetaAnual());
            ps.setInt(2, meta.getIdPrograma());
            ps.setInt(3, meta.getAnio());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Elimina la meta de un programa para un año específico
    public boolean eliminarMetaPrograma(int idPrograma, int anio) {
        String sql = "DELETE FROM MetaPrograma WHERE id_programa = ? AND anio = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPrograma);
            ps.setInt(2, anio);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Obtiene la meta de un programa para un año específico
    public MetaPrograma obtenerMetaPrograma(int idPrograma, int anio) {
        String sql = "SELECT * FROM MetaPrograma WHERE id_programa = ? AND anio = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPrograma);
            ps.setInt(2, anio);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                MetaPrograma meta = new MetaPrograma();
                meta.setIdMetaPrograma(rs.getInt("id_meta_programa"));
                meta.setIdPrograma(rs.getInt("id_programa"));
                meta.setAnio(rs.getInt("anio"));
                meta.setMetaAnual(rs.getInt("meta_anual"));
                return meta;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lista todas las metas para un año específico
    public List<MetaPrograma> listarMetasPorAnio(int anio) {
        List<MetaPrograma> metas = new ArrayList<>();
        String sql = "SELECT * FROM MetaPrograma WHERE anio = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, anio);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MetaPrograma meta = new MetaPrograma();
                meta.setIdMetaPrograma(rs.getInt("id_meta_programa"));
                meta.setIdPrograma(rs.getInt("id_programa"));
                meta.setAnio(rs.getInt("anio"));
                meta.setMetaAnual(rs.getInt("meta_anual"));
                metas.add(meta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return metas;
    }
}
