package org.example;

import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CambiarContrasena extends JFrame {
    private JTextField correoField;
    private JPasswordField nuevaPassField;

    public CambiarContrasena() {
        setTitle("Cambiar Contraseña");
        setSize(350, 200);
        setLayout(new GridLayout(3, 2));
        setLocationRelativeTo(null);

        add(new JLabel("Correo:"));
        correoField = new JTextField();
        add(correoField);

        add(new JLabel("Nueva contraseña:"));
        nuevaPassField = new JPasswordField();
        add(nuevaPassField);

        JButton cambiarBtn = new JButton("Cambiar");
        cambiarBtn.addActionListener(e -> cambiar());
        add(cambiarBtn);

        setVisible(true);
    }

    private void cambiar() {
        String correo = correoField.getText().trim();
        String nuevaPass = new String(nuevaPassField.getPassword());

        if (correo.isEmpty() || nuevaPass.length() < 8) {
            JOptionPane.showMessageDialog(this, "Correo o contraseña inválida.");
            return;
        }

        String hashed = BCrypt.hashpw(nuevaPass, BCrypt.gensalt());

        try (Connection conn = Conexion.obtenerConexion()) {
            String sql = "UPDATE Usuario SET password = ? WHERE correo = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, hashed);
            stmt.setString(2, correo);

            int updated = stmt.executeUpdate();
            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Contraseña actualizada.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Usuario no encontrado.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cambiar contraseña.");
        }
    }
}
