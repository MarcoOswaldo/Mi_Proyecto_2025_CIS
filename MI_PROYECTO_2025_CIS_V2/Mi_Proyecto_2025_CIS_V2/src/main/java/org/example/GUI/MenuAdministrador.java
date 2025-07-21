package org.example.GUI;

import org.example.Entidades.Usuario;
import org.example.Utilidades.ControlSesionUsuario;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class MenuAdministrador extends JFrame {

    public MenuAdministrador() {
        setTitle("Panel del Administrador");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        Usuario admin = ControlSesionUsuario.getUsuarioActual();

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JLabel bienvenida = new JLabel("Bienvenido, " + admin.getNombre(), SwingConstants.CENTER);
        bienvenida.setFont(new Font("Segoe UI", Font.BOLD, 20));
        panel.add(bienvenida, BorderLayout.NORTH);

        JPanel botonesPanel = new JPanel(new GridLayout(4, 2, 15, 15));
        botonesPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Botones principales
        botonesPanel.add(crearBoton("Gestionar Usuarios", () -> {
            try {
                abrir("usuarios");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }));
        botonesPanel.add(crearBoton("Gestionar Pacientes", () -> {
            try {
                abrir("pacientes");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }));
        botonesPanel.add(crearBoton("Gestionar Obstetras", () -> {
            try {
                abrir("obstetras");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }));
        botonesPanel.add(crearBoton("Gestionar Programas", () -> {
            try {
                abrir("programas");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }));
        botonesPanel.add(crearBoton("Gestionar Establecimientos", () -> {
            try {
                abrir("establecimientos");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }));
        botonesPanel.add(crearBoton("Metas por Programa", () -> {
            try {
                abrir("metas_programa");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }));
        botonesPanel.add(crearBoton("Metas por Obstetras", () -> {
            try {
                abrir("metas_obstetras");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }));
        botonesPanel.add(crearBoton("Ver Reportes", () -> {
            try {
                abrir("reportes");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }));

        // Botón de cerrar sesión
        JButton cerrarSesion = new JButton("Cerrar Sesión");
        cerrarSesion.addActionListener(e -> cerrarSesion());

        // Botón de cambiar contraseña
        JButton cambiarPassword = new JButton("Cambiar Contraseña");
        cambiarPassword.addActionListener(e -> abrirDialogoCambiarPassword());

        JPanel abajoPanel = new JPanel();
        abajoPanel.add(cambiarPassword);
        abajoPanel.add(cerrarSesion);


        panel.add(botonesPanel, BorderLayout.CENTER);
        panel.add(abajoPanel, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }

    private JButton crearBoton(String texto, Runnable onClick) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        boton.addActionListener(e -> onClick.run());
        return boton;
    }


    private void abrir(String modulo) throws SQLException {
        switch (modulo) {
            case "usuarios":
                new GestionUsuarios();
                break;
            case "pacientes":
                new GestionPacientes();
                break;
            case "obstetras":
                new GestionObstetras();
                break;
            case "programas":
                new GestionProgramas();
                break;
            case "establecimientos":
                new GestionEstablecimientos();
                break;
            case "metas_programa":
                new GestionMetaPrograma();
                break;
            case "metas_obstetras":
                new GestionMetaObstetra();
                break;
            case "reportes":
                new ReportePruebas();
                break;
            default:
                JOptionPane.showMessageDialog(this, "Módulo no implementado: " + modulo);
                break;
        }
    }

    private void abrirDialogoCambiarPassword() {
        JDialog dialog = new JDialog(this, "Cambiar Contraseña", true); // modal
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.getContentPane().add(new CambiarPassword());
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }



    private void cerrarSesion() {
        ControlSesionUsuario.cerrarSesion();
        dispose();
        new Login();
    }
}
