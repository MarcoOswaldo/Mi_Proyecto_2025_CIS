package org.example.GUI;

import org.example.Entidad.Usuario;
import org.example.Logica_DAO.UsuarioDAO;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Registro extends JFrame {
    private JTextField nombreField;
    private JTextField correoField;
    private JPasswordField passwordField;
    private JPasswordField repeatPasswordField;
    private JLabel fuerzaLabel;
    private JProgressBar barraFuerza;
    private JButton registrarButton;

    public Registro() {
        setTitle("Registro de Usuario");
        setSize(450, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        // Panel principal con BoxLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(new Color(245, 245, 245)); // Fondo claro

        Font fontLabel = new Font("Segoe UI", Font.PLAIN, 14);
        Font fontField = new Font("Segoe UI", Font.PLAIN, 14);

        // Campo nombre
        mainPanel.add(crearCampo("Nombre:", nombreField = new JTextField(), fontLabel, fontField));
        // Campo correo
        mainPanel.add(crearCampo("Correo:", correoField = new JTextField(), fontLabel, fontField));
        // Campo contraseña
        mainPanel.add(crearCampo("Contraseña:", passwordField = new JPasswordField(), fontLabel, fontField));
        // Campo repetir contraseña
        mainPanel.add(crearCampo("Repetir Contraseña:", repeatPasswordField = new JPasswordField(), fontLabel, fontField));

        // Fuerza contraseña
        JPanel fuerzaPanel = new JPanel(new BorderLayout(5, 5));
        fuerzaPanel.setBackground(mainPanel.getBackground());
        fuerzaPanel.add(new JLabel("Fuerza de contraseña:"), BorderLayout.WEST);
        fuerzaLabel = new JLabel(" ");
        fuerzaPanel.add(fuerzaLabel, BorderLayout.CENTER);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(fuerzaPanel);

        // Barra de fuerza
        barraFuerza = new JProgressBar(0, 100);
        barraFuerza.setStringPainted(true);
        mainPanel.add(barraFuerza);

        // Botón registrar
        registrarButton = new JButton("Registrar");
        registrarButton.setEnabled(false);
        registrarButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registrarButton.setBackground(new Color(30, 144, 255));
        registrarButton.setForeground(Color.WHITE);
        registrarButton.setFocusPainted(false);
        registrarButton.addActionListener(new RegistrarListener());
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(registrarButton);

        add(mainPanel);
        addEventos();
        setVisible(true);
    }

    private JPanel crearCampo(String texto, JTextField campo, Font fontLabel, Font fontField) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(245, 245, 245));
        JLabel label = new JLabel(texto);
        label.setFont(fontLabel);
        campo.setFont(fontField);
        campo.setPreferredSize(new Dimension(300, 30));
        panel.add(label, BorderLayout.NORTH);
        panel.add(campo, BorderLayout.CENTER);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        return panel;
    }

    private void addEventos() {
        KeyAdapter validador = new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                validarFormulario();
            }
        };

        nombreField.addKeyListener(validador);
        correoField.addKeyListener(validador);
        passwordField.addKeyListener(validador);
        repeatPasswordField.addKeyListener(validador);

        passwordField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                evaluarFuerza(new String(passwordField.getPassword()));
            }
        });
    }

    private void validarFormulario() {
        String nombre = nombreField.getText().trim();
        String correo = correoField.getText().trim();
        String pass = new String(passwordField.getPassword()).trim();
        String repetir = new String(repeatPasswordField.getPassword()).trim();

        boolean camposLlenos = !nombre.isEmpty() && !correo.isEmpty() && !pass.isEmpty() && !repetir.isEmpty();
        boolean contrasenaOk = pass.length() >= 8 && pass.equals(repetir);
        boolean correoValido = correo.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$");

        registrarButton.setEnabled(camposLlenos && contrasenaOk && correoValido);
    }

    private void evaluarFuerza(String password) {
        int fuerza = 0;
        if (password.length() >= 8) fuerza += 30;
        if (password.matches(".*[A-Z].*")) fuerza += 20;
        if (password.matches(".*[a-z].*")) fuerza += 20;
        if (password.matches(".*[0-9].*")) fuerza += 15;
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) fuerza += 15;

        barraFuerza.setValue(fuerza);

        if (fuerza < 40) {
            fuerzaLabel.setText("Débil");
            fuerzaLabel.setForeground(Color.RED);
        } else if (fuerza < 70) {
            fuerzaLabel.setText("Media");
            fuerzaLabel.setForeground(Color.ORANGE);
        } else {
            fuerzaLabel.setText("Fuerte");
            fuerzaLabel.setForeground(new Color(0, 128, 0));
        }
    }

    private class RegistrarListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String nombre = nombreField.getText().trim();
            String correo = correoField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            UsuarioDAO usuarioDAO = new UsuarioDAO();

            if (usuarioDAO.correoExiste(correo)) {
                JOptionPane.showMessageDialog(Registro.this, "Este correo ya está registrado.");
                return;
            }

            String hash = BCrypt.hashpw(password, BCrypt.gensalt());
            Usuario nuevoUsuario = new Usuario(nombre, correo, hash, "normal");

            if (usuarioDAO.registrarUsuario(nuevoUsuario)) {
                JOptionPane.showMessageDialog(Registro.this, "Registro exitoso.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(Registro.this, "Error al registrar.");
            }
        }
    }
}
