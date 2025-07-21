package org.example.Logica_DAO;


import org.example.Conexion;
import org.example.Entidad.MetaObstetra;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MetaObstetraDAO {

    public boolean insertar(MetaObstetra meta) {
        String sql = "INSERT INTO MetaObstetra (id_obstetra, id_programa, anio, meta_anual) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, meta.getIdObstetra());
            ps.setInt(2, meta.getIdPrograma());
            ps.setInt(3, meta.getAnio());
            ps.setInt(4, meta.getMetaAnual());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean actualizar(MetaObstetra meta) {
        String sql = "UPDATE MetaObstetra SET meta_anual = ? WHERE id_obstetra = ? AND id_programa = ? AND anio = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, meta.getMetaAnual());
            ps.setInt(2, meta.getIdObstetra());
            ps.setInt(3, meta.getIdPrograma());
            ps.setInt(4, meta.getAnio());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM MetaObstetra WHERE id_meta_obstetra = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public List<MetaObstetra> listarTodos() {
        List<MetaObstetra> lista = new ArrayList<>();
        String sql = "SELECT * FROM MetaObstetra";
        try (Connection conn = Conexion.obtenerConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                MetaObstetra meta = new MetaObstetra(
                        rs.getInt("id_meta_obstetra"),
                        rs.getInt("id_obstetra"),
                        rs.getInt("id_programa"),
                        rs.getInt("anio"),
                        rs.getInt("meta_anual"),
                        rs.getInt("meta_mensual")
                );
                lista.add(meta);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }
}
