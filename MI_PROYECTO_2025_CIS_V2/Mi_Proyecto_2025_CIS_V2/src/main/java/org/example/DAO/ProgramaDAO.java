package org.example.DAO;


import org.example.Entidades.Programa;
import org.example.Utilidades.Conexion;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProgramaDAO {

    public void agregar(Programa programa) throws SQLException {
        String sql = "INSERT INTO Programa (nombre, descripcion) VALUES (?, ?)";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, programa.getNombre());
            stmt.setString(2, programa.getDescripcion());
            stmt.executeUpdate();
        }
    }

    public void editar(Programa programa) throws SQLException {
        String sql = "UPDATE Programa SET nombre = ?, descripcion = ? WHERE id_programa = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, programa.getNombre());
            stmt.setString(2, programa.getDescripcion());
            stmt.setInt(3, programa.getIdPrograma());
            stmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Programa WHERE id_programa = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Programa> listar() throws SQLException {
        List<Programa> lista = new ArrayList<>();
        String sql = "SELECT * FROM Programa";
        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearPrograma(rs));
            }
        }
        return lista;
    }

    public Programa buscarPorNombre(String nombre) throws SQLException {
        String sql = "SELECT * FROM Programa WHERE nombre = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearPrograma(rs);
                }
            }
        }
        return null;
    }

    public Programa buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Programa WHERE id_programa = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearPrograma(rs);
                }
            }
        }
        return null;
    }

    private Programa mapearPrograma(ResultSet rs) throws SQLException {
        Programa programa = new Programa();
        programa.setIdPrograma(rs.getInt("id_programa"));
        programa.setNombre(rs.getString("nombre"));
        programa.setDescripcion(rs.getString("descripcion"));
        return programa;
    }
}
