package org.example.GUI;

import javax.swing.*;
import java.awt.*;

public class MenuObstetra extends JFrame {
    public MenuObstetra() {
        setTitle("Menú Obstetra");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton btnRegistros = new JButton("Registrar Pruebas");
        JButton btnReportes = new JButton("Reportes");
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");

        btnRegistros.addActionListener(e -> new RegistroPruebaGUI());
        btnCerrarSesion.addActionListener(e -> {
            org.example.Logica_DAO.ControlSesionUsuario.cerrarSesion();
            dispose();
            new Login();
        });

        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.add(btnRegistros);
        panel.add(btnReportes);
        panel.add(btnCerrarSesion);

        add(panel);
        setVisible(true);
    }
}
