package org.example.DAO;

import org.example.Entidades.EstablecimientoSalud;
import org.example.Entidades.MetaObstetra;
import org.example.Utilidades.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstablecimientoSaludDAO {

    public void agregar(EstablecimientoSalud est) throws SQLException {
        String sql = "INSERT INTO EstablecimientoSalud (red_salud, microred, renipress, nombre, categoria) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, est.getRedSalud());
            stmt.setString(2, est.getMicroRed());
            stmt.setString(3, est.getRenipress());
            stmt.setString(4, est.getNombre());
            stmt.setString(5, est.getCategoria());
            stmt.executeUpdate();
        }
    }

    public void editar(EstablecimientoSalud est) throws SQLException {
        String sql = "UPDATE EstablecimientoSalud SET red_salud = ?, microred = ?, renipress = ?, nombre = ?, categoria = ? WHERE id_establecimiento = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, est.getRedSalud());
            stmt.setString(2, est.getMicroRed());
            stmt.setString(3, est.getRenipress());
            stmt.setString(4, est.getNombre());
            stmt.setString(5, est.getCategoria());
            stmt.setInt(6, est.getIdEstablecimiento());
            stmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM EstablecimientoSalud WHERE id_establecimiento = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<EstablecimientoSalud> listar() throws SQLException {
        List<EstablecimientoSalud> lista = new ArrayList<>();
        String sql = "SELECT * FROM EstablecimientoSalud";
        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public EstablecimientoSalud buscarPorId(int id) throws SQLException {
        String sql = "SELECT * EstablecimientoSalud WHERE id_establecimiento = ?";
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

    private EstablecimientoSalud mapear(ResultSet rs) throws SQLException {
        EstablecimientoSalud est = new EstablecimientoSalud();
        est.setIdEstablecimiento(rs.getInt("id_establecimiento"));
        est.setRedSalud(rs.getString("red_salud"));
        est.setMicroRed(rs.getString("microred"));
        est.setRenipress(rs.getString("renipress"));
        est.setNombre(rs.getString("nombre"));
        est.setCategoria(rs.getString("categoria"));
        return est;
    }
}
