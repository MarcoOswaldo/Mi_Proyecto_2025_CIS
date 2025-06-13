package org.example.Logica_DAO;


import org.example.Conexion;
import org.example.Entidad.Obstetra;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ObstetraDAO {

    // Insertar obstetra
    public boolean registrarObstetra(Obstetra obstetra) {
        String sql = "INSERT INTO Obstetra (nombre, apellido, dni) VALUES (?, ?, ?)";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, obstetra.getNombre());
            ps.setString(2, obstetra.getApellido());
            ps.setString(3, obstetra.getDni());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Actualizar obstetra
    public boolean actualizarObstetra(Obstetra obstetra) {
        String sql = "UPDATE Obstetra SET nombre = ?, apellido = ?, dni = ? WHERE id_obstetra = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, obstetra.getNombre());
            ps.setString(2, obstetra.getApellido());
            ps.setString(3, obstetra.getDni());
            ps.setInt(4, obstetra.getIdObstetra());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar obstetra
    public boolean eliminarObstetra(int id) {
        String sql = "DELETE FROM Obstetra WHERE id_obstetra = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Obtener un obstetra por ID
    public Obstetra obtenerPorId(int id) {
        String sql = "SELECT * FROM Obstetra WHERE id_obstetra = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Obstetra(
                        rs.getInt("id_obstetra"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("dni")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Listar todos las obstetras
    public List<Obstetra> listarObstetras() {
        List<Obstetra> lista = new ArrayList<>();
        String sql = "SELECT * FROM Obstetra";
        try (Connection conn = Conexion.obtenerConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Obstetra(
                        rs.getInt("id_obstetra"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("dni")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
