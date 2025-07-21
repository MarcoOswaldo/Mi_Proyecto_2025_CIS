package org.example.GUI;

import org.example.CambiarContrasena;
import org.example.Entidad.Usuario;
import org.example.Logica_DAO.ControlSesionUsuario;
import org.example.Logica_DAO.UsuarioDAO;
import org.mindrot.jbcrypt.BCrypt;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Login extends JFrame {
    private JTextField userField;
    private JPasswordField passwordField;
    private JTextField captchaField;
    private JLabel captchaLabel;
    private JLabel attemptsLabel;
    private int attempts = 3;
    private String captchaText;

    public Login() {
        setTitle("Inicio de Sesión");
        setSize(450, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);

        mainPanel.add(createLabeledField("Correo:", userField = new JTextField(), labelFont));
        mainPanel.add(createLabeledField("Contraseña:", passwordField = new JPasswordField(), labelFont));

        JPanel captchaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        captchaPanel.add(new JLabel("CAPTCHA:", JLabel.LEFT));
        captchaLabel = new JLabel();
        captchaLabel.setFont(new Font("Consolas", Font.BOLD, 20));
        captchaLabel.setForeground(new Color(30, 144, 255));
        generarCaptcha();
        captchaPanel.add(captchaLabel);
        mainPanel.add(captchaPanel);

        mainPanel.add(createLabeledField("Ingrese CAPTCHA:", captchaField = new JTextField(), labelFont));

        attemptsLabel = new JLabel("Intentos restantes: " + attempts);
        attemptsLabel.setForeground(Color.GRAY);
        attemptsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(attemptsLabel);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        JButton loginButton = new JButton("Iniciar sesión");
        loginButton.addActionListener(new LoginListener());

        JButton registerButton = new JButton("Registrar");
        registerButton.addActionListener(e -> new Registro());

        JButton forgotButton = new JButton("¿Olvidó la contraseña?");
        forgotButton.addActionListener(e -> new CambiarContrasena());

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(forgotButton);

        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(buttonPanel);

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createLabeledField(String labelText, JTextField field, Font font) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JLabel label = new JLabel(labelText);
        label.setFont(font);
        panel.add(label, BorderLayout.NORTH);
        field.setFont(font);
        panel.add(field, BorderLayout.CENTER);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        return panel;
    }

    private void generarCaptcha() {
        captchaText = String.valueOf((char) (new Random().nextInt(26) + 'A')) +
                new Random().nextInt(10) +
                (char) (new Random().nextInt(26) + 'A');
        captchaLabel.setText(captchaText);
    }

    private class LoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String correo = userField.getText().trim();
            String contrasena = new String(passwordField.getPassword()).trim();
            String captchaIngresado = captchaField.getText().trim();

            if (!captchaIngresado.equals(captchaText)) {
                JOptionPane.showMessageDialog(Login.this, "CAPTCHA incorrecto.");
                generarCaptcha();
                captchaField.setText("");
                return;
            }

            UsuarioDAO usuarioDAO = new UsuarioDAO();
            Usuario usuario = usuarioDAO.obtenerPorCorreo(correo);

            if (usuario != null && BCrypt.checkpw(contrasena, usuario.getPassword())) {
                ControlSesionUsuario.iniciarSesion(usuario);
                dispose();  // Cierra ventana de login

                if (ControlSesionUsuario.esAdmin()) {
                    new MenuAdministrador(); // Crea esta clase luego
                } else {
                    new MenuObstetra(); // Crea esta clase luego
                }
            }
            else {
                mostrarError();
            }

            generarCaptcha();
            captchaField.setText("");
        }

        private void mostrarError() {
            attempts--;
            if (attempts <= 0) {
                JOptionPane.showMessageDialog(Login.this, "Demasiados intentos. Bloqueado.");
                System.exit(0);
            } else {
                JOptionPane.showMessageDialog(Login.this, "Correo o contraseña incorrectos.");
                attemptsLabel.setText("Intentos restantes: " + attempts);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Login::new);
    }
}
