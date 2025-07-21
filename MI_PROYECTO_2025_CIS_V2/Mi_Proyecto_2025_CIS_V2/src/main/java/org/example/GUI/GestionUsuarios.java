package org.example.GUI;

import org.example.Entidades.Usuario;
import org.example.DAO.UsuarioDAO;
import org.example.Utilidades.Validaciones;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class GestionUsuarios extends JFrame {
    private JTable tablaUsuarios;
    private DefaultTableModel modelo;
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    private JTextField txtNombre, txtCorreo;
    private JPasswordField txtPassword;
    private JComboBox<String> comboTipo;
    private JButton btnGuardar, btnLimpiar;
    private Usuario usuarioSeleccionado = null;

    public GestionUsuarios() {
        setTitle("Gestión de Usuarios");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        modelo = new DefaultTableModel(new String[]{"ID", "Nombre", "Correo", "Tipo", "Fecha"}, 0);
        tablaUsuarios = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tablaUsuarios);

        JPanel formularioPanel = crearFormularioPanel();

        JPanel accionesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnRecargar = new JButton("Recargar");

        btnEliminar.addActionListener(e -> eliminarUsuario());
        btnRecargar.addActionListener(e -> {
            limpiarFormulario();
            cargarUsuarios();
        });

        accionesPanel.add(btnEliminar);
        accionesPanel.add(btnRecargar);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scroll, formularioPanel);
        split.setDividerLocation(250);
        split.setResizeWeight(0.5);

        add(split, BorderLayout.CENTER);
        add(accionesPanel, BorderLayout.SOUTH);

        tablaUsuarios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaUsuarios.getSelectedRow() != -1) {
                cargarUsuarioSeleccionado();
            }
        });

        cargarUsuarios();
        setVisible(true);
    }

    private JPanel crearFormularioPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Formulario de Usuario"));

        txtNombre = new JTextField();
        txtCorreo = new JTextField();
        txtPassword = new JPasswordField();
        comboTipo = new JComboBox<>(new String[]{"administrador", "obstetra"});

        btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> {
            try {
                guardarUsuario();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Correo:"));
        panel.add(txtCorreo);
        panel.add(new JLabel("Contraseña:"));
        panel.add(txtPassword);
        panel.add(new JLabel("Tipo:"));
        panel.add(comboTipo);
        panel.add(btnGuardar);
        panel.add(btnLimpiar);

        return panel;
    }

    private void cargarUsuarios() {
        modelo.setRowCount(0);
        try {
            List<Usuario> lista = usuarioDAO.listar();
            for (Usuario u : lista) {
                modelo.addRow(new Object[]{
                        u.getId(),
                        u.getNombre(),
                        u.getCorreo(),
                        u.getTipoUsuario(),
                        u.getFechaCreacion()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar usuarios: " + e.getMessage());
        }
    }

    private void guardarUsuario() throws SQLException {
        String nombre = txtNombre.getText().trim();
        String correo = txtCorreo.getText().trim();
        String clave = new String(txtPassword.getPassword()).trim();
        String tipo = comboTipo.getSelectedItem().toString();


        if (!Validaciones.noVacio(nombre) || !Validaciones.noVacio(correo)) {
            JOptionPane.showMessageDialog(this, "Nombre y correo son obligatorios.");
            return;
        }

        if (!Validaciones.esCorreoValido(correo)) {
            JOptionPane.showMessageDialog(this, "Correo no tiene un formato válido.");
            return;
        }

        if (usuarioSeleccionado == null && usuarioDAO.buscarPorCorreo(correo) != null) {
            JOptionPane.showMessageDialog(this, "Este correo ya está registrado.");
            return;
        }


        if (usuarioSeleccionado == null && !Validaciones.esPasswordSegura(clave)) {
            JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 6 caracteres.");
            return;
        }


        try {
            if (usuarioSeleccionado == null) {
                Usuario nuevo = new Usuario();
                nuevo.setNombre(nombre);
                nuevo.setCorreo(correo);
                nuevo.setPassword(BCrypt.hashpw(clave, BCrypt.gensalt()));
                nuevo.setTipoUsuario(tipo);
                usuarioDAO.agregar(nuevo);
            } else {
                usuarioSeleccionado.setNombre(nombre);
                usuarioSeleccionado.setCorreo(correo);
                usuarioSeleccionado.setTipoUsuario(tipo);
                if (!clave.isEmpty()) {
                    usuarioSeleccionado.setPassword(BCrypt.hashpw(clave, BCrypt.gensalt()));
                }
                usuarioDAO.editar(usuarioSeleccionado);
            }

            limpiarFormulario();
            cargarUsuarios();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage());
        }
    }

    private void eliminarUsuario() {
        int fila = tablaUsuarios.getSelectedRow();
        if (fila != -1) {
            int id = (int) modelo.getValueAt(fila, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar usuario?", "Confirmación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    usuarioDAO.eliminar(id);
                    limpiarFormulario();
                    cargarUsuarios();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para eliminar.");
        }
    }

    private void cargarUsuarioSeleccionado() {
        int fila = tablaUsuarios.getSelectedRow();
        if (fila != -1) {
            int id = (int) modelo.getValueAt(fila, 0);
            try {
                usuarioSeleccionado = usuarioDAO.buscarPorId(id);
                txtNombre.setText(usuarioSeleccionado.getNombre());
                txtCorreo.setText(usuarioSeleccionado.getCorreo());
                txtPassword.setText("");
                comboTipo.setSelectedItem(usuarioSeleccionado.getTipoUsuario());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al cargar usuario: " + e.getMessage());
            }
        }
    }

    private void limpiarFormulario() {
        usuarioSeleccionado = null;
        txtNombre.setText("");
        txtCorreo.setText("");
        txtPassword.setText("");
        comboTipo.setSelectedIndex(0);
        tablaUsuarios.clearSelection();
    }
}
