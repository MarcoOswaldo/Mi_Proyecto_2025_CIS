package org.example.GUI;

import org.example.Utilidades.ControlSesionUsuario;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class MenuObstetra extends JFrame {

    public MenuObstetra() {
        setTitle("Menú Obstetra");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Título con padding
        JLabel titulo = new JLabel("Bienvenido/a - Menú Obstetra", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(titulo, BorderLayout.NORTH);

        // Panel central con BoxLayout vertical
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50)); // margen

        // Crear botones y agregar con espacio
        JButton btnRegistrarPrueba = new JButton("Registrar Prueba");
        btnRegistrarPrueba.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegistrarPrueba.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnRegistrarPrueba.addActionListener(e -> {
            try {
                new RegistrarPrueba();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        JButton btnVerAvanceMetas = new JButton("Ver Avance de Metas");
        btnVerAvanceMetas.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVerAvanceMetas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnVerAvanceMetas.addActionListener(e -> new VerAvanceMetas());

        JButton btnCambiarPassword = new JButton("Cambiar Contraseña");
        btnCambiarPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCambiarPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnCambiarPassword.addActionListener(e -> mostrarDialogoCambiarPassword());


        JButton cerrarSesion = new JButton("Cerrar Sesión");
        cerrarSesion.setAlignmentX(Component.CENTER_ALIGNMENT);
        cerrarSesion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cerrarSesion.addActionListener(e -> cerrarSesion());

        // Añadir botones con espacio vertical entre ellos
        panelCentral.add(btnRegistrarPrueba);
        panelCentral.add(Box.createVerticalStrut(15)); // espacio
        panelCentral.add(btnVerAvanceMetas);
        panelCentral.add(Box.createVerticalStrut(15));
        panelCentral.add(btnCambiarPassword);
        panelCentral.add(Box.createVerticalStrut(15));
        panelCentral.add(cerrarSesion);

        add(panelCentral, BorderLayout.CENTER);

        setVisible(true);
    }

    private void mostrarDialogoCambiarPassword() {
        JDialog dialogo = new JDialog(this, "Cambiar Contraseña", true);
        dialogo.setContentPane(new CambiarPassword());
        dialogo.pack();
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }


    private void cerrarSesion() {
        ControlSesionUsuario.cerrarSesion();
        dispose();
        new Login();
    }
}
