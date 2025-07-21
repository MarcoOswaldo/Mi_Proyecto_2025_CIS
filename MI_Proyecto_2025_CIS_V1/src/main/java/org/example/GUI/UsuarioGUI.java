package org.example.GUI;

import org.example.Entidad.Usuario;
import org.example.Logica_DAO.UsuarioDAO;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class UsuarioGUI extends JFrame {

    private JTextField txtNombre = new JTextField(15);
    private JTextField txtCorreo = new JTextField(15);
    private JTextField txtPassword = new JTextField(15);
    private JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"administrador", "normal"});
    private JTextField txtIdObstetra = new JTextField(15);
    private JTable tabla = new JTable();
    private DefaultTableModel modelo = new DefaultTableModel();
    private UsuarioDAO dao = new UsuarioDAO();
    private int usuarioSeleccionado = -1;

    public UsuarioGUI() {
        setTitle("Gestión de Usuarios");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 400); // Ajustado para nueva columna
        setLocationRelativeTo(null);

        modelo.setColumnIdentifiers(new String[] {"ID", "Nombre", "Correo", "Password (hash)", "Tipo", "ID Obstetra"});
        tabla.setModel(modelo);

        JPanel form = new JPanel(new GridLayout(5, 2));
        form.add(new JLabel("Nombre:")); form.add(txtNombre);
        form.add(new JLabel("Correo:")); form.add(txtCorreo);
        form.add(new JLabel("Password:")); form.add(txtPassword);
        form.add(new JLabel("Tipo Usuario:")); form.add(cmbTipo);
        form.add(new JLabel("ID Obstetra (solo para normal):")); form.add(txtIdObstetra);

        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");

        JPanel botones = new JPanel();
        botones.add(btnAgregar);
        botones.add(btnEditar);
        botones.add(btnEliminar);
        botones.add(btnLimpiar);

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);

        cargarDatos();

        tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = tabla.getSelectedRow();
                usuarioSeleccionado = Integer.parseInt(tabla.getValueAt(fila, 0).toString());
                txtNombre.setText(tabla.getValueAt(fila, 1).toString());
                txtCorreo.setText(tabla.getValueAt(fila, 2).toString());
                // Nota: No se carga el password hasheado
                cmbTipo.setSelectedItem(tabla.getValueAt(fila, 4).toString());
                txtIdObstetra.setText(tabla.getValueAt(fila, 5) != null ? tabla.getValueAt(fila, 5).toString() : "");
            }
        });

        btnAgregar.addActionListener(e -> {
            try {
                Usuario u = obtenerDesdeFormulario(true); // true para cifrar
                dao.registrarUsuario(u);
                cargarDatos();
                limpiarFormulario();
            } catch (Exception ex) {
                mostrarError(ex);
            }
        });

        btnEditar.addActionListener(e -> {
            if (usuarioSeleccionado == -1) return;
            try {
                Usuario u = obtenerDesdeFormulario(true); // true para cifrar
                u.setId(usuarioSeleccionado);
                dao.actualizarUsuario(u);
                cargarDatos();
                limpiarFormulario();
            } catch (Exception ex) {
                mostrarError(ex);
            }
        });

        btnEliminar.addActionListener(e -> {
            if (usuarioSeleccionado == -1) return;
            try {
                dao.eliminarUsuario(usuarioSeleccionado);
                cargarDatos();
                limpiarFormulario();
            } catch (SQLException ex) {
                mostrarError(ex);
            }
        });

        btnLimpiar.addActionListener(e -> limpiarFormulario());

        setVisible(true);
    }

    private void cargarDatos() {
        try {
            modelo.setRowCount(0);
            List<Usuario> lista = dao.listarUsuarios();
            for (Usuario u : lista) {
                modelo.addRow(new Object[]{
                        u.getId(), u.getNombre(), u.getCorreo(), u.getPassword(),
                        u.getTipoUsuario(), u.getIdObstetra() != null ? u.getIdObstetra().toString() : ""
                });
            }
        } catch (SQLException ex) {
            mostrarError(ex);
        }
    }

    private Usuario obtenerDesdeFormulario(boolean cifrarPassword) {
        String nombre = txtNombre.getText();
        String correo = txtCorreo.getText();
        String password = txtPassword.getText();
        String tipo = cmbTipo.getSelectedItem().toString();
        Integer idObstetra = null;

        if (tipo.equals("normal") && !txtIdObstetra.getText().isEmpty()) {
            idObstetra = Integer.parseInt(txtIdObstetra.getText());
        }

        if (cifrarPassword && !password.isEmpty()) {
            password = BCrypt.hashpw(password, BCrypt.gensalt(12));
        }

        return new Usuario(nombre, correo, password, tipo, idObstetra);
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtCorreo.setText("");
        txtPassword.setText("");
        txtIdObstetra.setText("");
        usuarioSeleccionado = -1;
        tabla.clearSelection();
    }

    private void mostrarError(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

}
