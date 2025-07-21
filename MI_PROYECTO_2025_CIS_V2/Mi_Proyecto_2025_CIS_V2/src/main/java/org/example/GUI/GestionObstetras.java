package org.example.GUI;

import org.example.Entidades.Obstetra;
import org.example.Entidades.Usuario;
import org.example.Entidades.EstablecimientoSalud;
import org.example.DAO.ObstetraDAO;
import org.example.DAO.UsuarioDAO;
import org.example.DAO.EstablecimientoSaludDAO;
import org.example.Utilidades.Validaciones;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class GestionObstetras extends JFrame {
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtNombre, txtApellido, txtDni, txtColegio, txtNumero;
    private JComboBox<Usuario> cbxUsuario;
    private JComboBox<EstablecimientoSalud> cbxEstablecimiento;
    private JButton btnGuardar, btnEliminar;
    private ObstetraDAO dao = new ObstetraDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private EstablecimientoSaludDAO establecimientoDAO = new EstablecimientoSaludDAO();
    private Obstetra obstetraSeleccionado = null;

    public GestionObstetras() throws SQLException {
        setTitle("Gestión de Obstetras");
        setSize(850, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        modelo = new DefaultTableModel(new String[]{"ID", "UsuarioID", "Nombre", "Apellido", "DNI", "Colegio", "N° Colegiatura", "EstablecimientoID"}, 0);
        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);

        JPanel formulario = crearFormulario();

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scroll, formulario);
        split.setDividerLocation(250);
        add(split);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() != -1) {
                try {
                    cargarSeleccionado();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        cargarDatos();
        setVisible(true);
    }

    private JPanel crearFormulario() throws SQLException {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Formulario de Obstetra"));

        JPanel campos = new JPanel(new GridLayout(4, 4, 10, 10));
        txtNombre = new JTextField();
        txtApellido = new JTextField();
        txtDni = new JTextField();
        txtColegio = new JTextField();
        txtNumero = new JTextField();
        cbxUsuario = new JComboBox<>();
        cbxEstablecimiento = new JComboBox<>();

        cargarUsuarios();
        cargarEstablecimientos();

        campos.add(new JLabel("Nombre:")); campos.add(txtNombre);
        campos.add(new JLabel("Apellido:")); campos.add(txtApellido);
        campos.add(new JLabel("DNI:")); campos.add(txtDni);
        campos.add(new JLabel("Colegio:")); campos.add(txtColegio);
        campos.add(new JLabel("N° Colegiatura:")); campos.add(txtNumero);
        campos.add(new JLabel("Usuario:")); campos.add(cbxUsuario);
        campos.add(new JLabel("Establecimiento:")); campos.add(cbxEstablecimiento);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnGuardar = new JButton("Guardar");
        btnEliminar = new JButton("Eliminar");
        botones.add(btnGuardar);
        botones.add(btnEliminar);

        btnGuardar.addActionListener(e -> guardar());
        btnEliminar.addActionListener(e -> {
            try {
                eliminar();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        panel.add(campos, BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);

        return panel;
    }

    private void cargarUsuarios() throws SQLException {
        cbxUsuario.removeAllItems();
        List<Usuario> usuarios = usuarioDAO.listar();
        for (Usuario u : usuarios) {
            cbxUsuario.addItem(u);
        }
    }

    private void cargarEstablecimientos() throws SQLException {
        cbxEstablecimiento.removeAllItems();
        List<EstablecimientoSalud> establecimientos = establecimientoDAO.listar();
        for (EstablecimientoSalud e : establecimientos) {
            cbxEstablecimiento.addItem(e);
        }
    }

    private void cargarDatos() throws SQLException {
        modelo.setRowCount(0);
        List<Obstetra> lista = dao.listar();
        for (Obstetra o : lista) {
            modelo.addRow(new Object[]{
                    o.getIdObstetra(), o.getIdUsuario(), o.getNombre(), o.getApellido(),
                    o.getDni(), o.getColegio(), o.getNumColegiatura(), o.getIdEstablecimiento()
            });
        }
    }

    private void guardar() {
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String dni = txtDni.getText().trim();
        String colegio = txtColegio.getText().trim();
        String numero = txtNumero.getText().trim();
        Usuario usuario = (Usuario) cbxUsuario.getSelectedItem();
        EstablecimientoSalud establecimiento = (EstablecimientoSalud) cbxEstablecimiento.getSelectedItem();

        if (!Validaciones.noVacio(nombre) || !Validaciones.contieneSoloLetras(nombre)) {
            JOptionPane.showMessageDialog(this, "Nombre inválido."); return;
        }
        if (!Validaciones.noVacio(apellido) || !Validaciones.contieneSoloLetras(apellido)) {
            JOptionPane.showMessageDialog(this, "Apellido inválido."); return;
        }
        if (!Validaciones.esDniValido(dni)) {
            JOptionPane.showMessageDialog(this, "DNI inválido."); return;
        }

        try {
            if (obstetraSeleccionado == null) {
                if (dao.buscarPorDni(dni) != null) {
                    JOptionPane.showMessageDialog(this, "DNI ya registrado."); return;
                }
                Obstetra nuevo = new Obstetra(0, usuario.getId(), nombre, apellido, dni, colegio, numero, establecimiento.getIdEstablecimiento());
                dao.agregar(nuevo);
            } else {
                obstetraSeleccionado.setNombre(nombre);
                obstetraSeleccionado.setApellido(apellido);
                obstetraSeleccionado.setDni(dni);
                obstetraSeleccionado.setColegio(colegio);
                obstetraSeleccionado.setNumColegiatura(numero);
                obstetraSeleccionado.setIdUsuario(usuario.getId());
                obstetraSeleccionado.setIdEstablecimiento(establecimiento.getIdEstablecimiento());
                dao.editar(obstetraSeleccionado);
            }
            limpiar();
            cargarDatos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void eliminar() throws SQLException {
        if (obstetraSeleccionado != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar obstetra?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dao.eliminar(obstetraSeleccionado.getIdObstetra());
                limpiar();
                cargarDatos();
            }
        }
    }

    private void cargarSeleccionado() throws SQLException {
        int fila = tabla.getSelectedRow();
        int id = (int) modelo.getValueAt(fila, 0);
        obstetraSeleccionado = dao.buscarPorId(id);
        txtNombre.setText(obstetraSeleccionado.getNombre());
        txtApellido.setText(obstetraSeleccionado.getApellido());
        txtDni.setText(obstetraSeleccionado.getDni());
        txtColegio.setText(obstetraSeleccionado.getColegio());
        txtNumero.setText(obstetraSeleccionado.getNumColegiatura());

        for (int i = 0; i < cbxUsuario.getItemCount(); i++) {
            if (cbxUsuario.getItemAt(i).getId() == obstetraSeleccionado.getIdUsuario()) {
                cbxUsuario.setSelectedIndex(i);
                break;
            }
        }
        for (int i = 0; i < cbxEstablecimiento.getItemCount(); i++) {
            if (cbxEstablecimiento.getItemAt(i).getIdEstablecimiento() == obstetraSeleccionado.getIdEstablecimiento()) {
                cbxEstablecimiento.setSelectedIndex(i);
                break;
            }
        }
    }

    private void limpiar() {
        obstetraSeleccionado = null;
        txtNombre.setText(""); txtApellido.setText(""); txtDni.setText("");
        txtColegio.setText(""); txtNumero.setText("");
        tabla.clearSelection();
    }
}
