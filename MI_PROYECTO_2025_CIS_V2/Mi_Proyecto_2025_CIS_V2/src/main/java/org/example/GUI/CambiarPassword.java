package org.example.GUI;

import org.example.DAO.UsuarioDAO;

import javax.swing.*;
import java.awt.*;

public class CambiarPassword extends JPanel {

    private JPasswordField txtPasswordActual;
    private JPasswordField txtNuevoPassword;
    private JPasswordField txtConfirmarPassword;
    private JButton btnCambiar;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public CambiarPassword() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblActual = new JLabel("Contraseña actual:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(lblActual, gbc);

        txtPasswordActual = new JPasswordField(20);
        gbc.gridx = 1;
        add(txtPasswordActual, gbc);

        JLabel lblNueva = new JLabel("Nueva contraseña:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(lblNueva, gbc);

        txtNuevoPassword = new JPasswordField(20);
        gbc.gridx = 1;
        add(txtNuevoPassword, gbc);

        JLabel lblConfirmar = new JLabel("Confirmar nueva contraseña:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(lblConfirmar, gbc);

        txtConfirmarPassword = new JPasswordField(20);
        gbc.gridx = 1;
        add(txtConfirmarPassword, gbc);

        btnCambiar = new JButton("Cambiar Contraseña");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(btnCambiar, gbc);

        btnCambiar.addActionListener(e -> cambiarPassword());
    }

    private void cambiarPassword() {
        String actual = new String(txtPasswordActual.getPassword());
        String nueva = new String(txtNuevoPassword.getPassword());
        String confirmar = new String(txtConfirmarPassword.getPassword());

        if (actual.isEmpty() || nueva.isEmpty() || confirmar.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!nueva.equals(confirmar)) {
            JOptionPane.showMessageDialog(this, "La nueva contraseña no coincide con la confirmación", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean exito = usuarioDAO.cambiarPasswordUsuarioActual(actual, nueva);
        if (exito) {
            JOptionPane.showMessageDialog(this, "Contraseña actualizada con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al cambiar la contraseña. Verifica la contraseña actual.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtPasswordActual.setText("");
        txtNuevoPassword.setText("");
        txtConfirmarPassword.setText("");
    }
}
