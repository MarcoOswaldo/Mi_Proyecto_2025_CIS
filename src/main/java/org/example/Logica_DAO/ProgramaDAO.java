package org.example.Logica_DAO;


import org.example.Conexion;
import org.example.Entidad.Programa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProgramaDAO {

    // Insertar programa
    public boolean insertarPrograma(Programa programa) {
        String sql = "INSERT INTO Programa (nombre) VALUES (?)";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, programa.getNombre());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Actualizar programa
    public boolean actualizarPrograma(Programa programa) {
        String sql = "UPDATE Programa SET nombre = ? WHERE id_programa = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, programa.getNombre());
            ps.setInt(2, programa.getIdPrograma());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar programa
    public boolean eliminarPrograma(int id) {
        String sql = "DELETE FROM Programa WHERE id_programa = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Obtener programa por ID
    public Programa obtenerPorId(int id) {
        String sql = "SELECT * FROM Programa WHERE id_programa = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Programa(rs.getInt("id_programa"), rs.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Listar todos los programas
    public List<Programa> listarTodos() {
        List<Programa> programas = new ArrayList<>();
        String sql = "SELECT * FROM Programa";
        try (Connection conn = Conexion.obtenerConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                programas.add(new Programa(rs.getInt("id_programa"), rs.getString("nombre")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return programas;
    }
}
