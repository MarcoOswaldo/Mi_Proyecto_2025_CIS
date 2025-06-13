package org.example.GUI;

import javax.swing.*;
import java.awt.*;

public class MenuAdministrador extends JFrame {
    public MenuAdministrador() {
        setTitle("Menú Administrador");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton btnUsuarios = new JButton("Gestión de Usuarios");
        JButton btnPacientes = new JButton("Gestión de Pacientes");
        JButton btnRegistros = new JButton("Registros de Pruebas");
        JButton btnObstetras = new JButton("Gestión Obstetras");
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");

        btnUsuarios.addActionListener(e -> new UsuarioGUI());
        btnPacientes.addActionListener(e -> new PacienteGUI());
        btnRegistros.addActionListener(e -> new RegistroPruebaGUI());
        btnObstetras.addActionListener(e-> new ObstetraGUI());
        btnCerrarSesion.addActionListener(e -> {
            org.example.Logica_DAO.ControlSesionUsuario.cerrarSesion();
            dispose();
            new Login();
        });

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.add(btnUsuarios);
        panel.add(btnPacientes);
        panel.add(btnRegistros);
        panel.add(btnObstetras);
        panel.add(btnCerrarSesion);

        add(panel);
        setVisible(true);
    }
}
