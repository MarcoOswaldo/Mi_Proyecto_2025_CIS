package org.example.Logica_DAO;


import org.example.Conexion;
import org.example.Entidad.RegistroPrueba;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistroPruebaDAO {

    public boolean registrarRegistro(RegistroPrueba r) {
        String sql = "INSERT INTO RegistroPrueba (id_paciente, id_obstetra, id_programa, fecha, observaciones) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, r.getIdPaciente());
            ps.setInt(2, r.getIdObstetra());
            ps.setInt(3, r.getIdPrograma());
            ps.setDate(4, r.getFecha());
            ps.setString(5, r.getObservaciones());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarRegistro(RegistroPrueba r) {
        String sql = "UPDATE RegistroPrueba SET id_paciente = ?, id_obstetra = ?, id_programa = ?, fecha = ?, observaciones = ? WHERE id_registro = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, r.getIdPaciente());
            ps.setInt(2, r.getIdObstetra());
            ps.setInt(3, r.getIdPrograma());
            ps.setDate(4, r.getFecha());
            ps.setString(5, r.getObservaciones());
            ps.setInt(6, r.getIdRegistro());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarRegistro(int id) {
        String sql = "DELETE FROM RegistroPrueba WHERE id_registro = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public RegistroPrueba obtenerRegistroPorId(int id) {
        String sql = "SELECT * FROM RegistroPrueba WHERE id_registro = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new RegistroPrueba(
                        rs.getInt("id_registro"),
                        rs.getInt("id_paciente"),
                        rs.getInt("id_obstetra"),
                        rs.getInt("id_programa"),
                        rs.getDate("fecha"),
                        rs.getString("observaciones")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Métodos para listar registros por fechas, programa, obstetra, etc. pueden implementarse aquí para los informes

    public List<RegistroPrueba> listarRegistros() throws SQLException {
        List<RegistroPrueba> lista = new ArrayList<>();
        String sql = "SELECT * FROM RegistroPrueba";
        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                RegistroPrueba rp = new RegistroPrueba(
                        rs.getInt("id_registro"),
                        rs.getInt("id_paciente"),
                        rs.getInt("id_obstetra"),
                        rs.getInt("id_programa"),
                        rs.getDate("fecha"),
                        rs.getString("observaciones")
                );
                lista.add(rp);
            }
        }
        return lista;
    }


    public List<RegistroPrueba> listarRegistrosPorObstetra(int idObstetra) throws SQLException {
        List<RegistroPrueba> lista = new ArrayList<>();
        String sql = "SELECT * FROM RegistroPrueba WHERE id_obstetra = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idObstetra);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    RegistroPrueba rp = new RegistroPrueba(
                            rs.getInt("id_registro"),
                            rs.getInt("id_paciente"),
                            rs.getInt("id_obstetra"),
                            rs.getInt("id_programa"),
                            rs.getDate("fecha"),
                            rs.getString("observaciones")
                    );
                    lista.add(rp);
                }
            }
        }
        return lista;
    }

}
