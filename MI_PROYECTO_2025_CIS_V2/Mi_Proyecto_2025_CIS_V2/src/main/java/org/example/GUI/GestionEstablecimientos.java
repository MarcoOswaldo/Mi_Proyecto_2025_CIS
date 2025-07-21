package org.example.GUI;

import org.example.Entidades.EstablecimientoSalud;
import org.example.DAO.EstablecimientoSaludDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class GestionEstablecimientos extends JFrame {
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtRed, txtMicrored, txtRenipress, txtNombre, txtCategoria;
    private JButton btnGuardar, btnEliminar;
    private EstablecimientoSaludDAO dao = new EstablecimientoSaludDAO();
    private EstablecimientoSalud seleccionado = null;

    public GestionEstablecimientos() throws SQLException {
        setTitle("Gestión de Establecimientos de Salud");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        modelo = new DefaultTableModel(new String[]{"ID", "Red", "Microred", "RENIPRESS", "Nombre", "Categoría"}, 0);
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

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Formulario de Establecimiento"));

        txtRed = new JTextField();
        txtMicrored = new JTextField();
        txtRenipress = new JTextField();
        txtNombre = new JTextField();
        txtCategoria = new JTextField();
        btnGuardar = new JButton("Guardar");
        btnEliminar = new JButton("Eliminar");

        btnGuardar.addActionListener(e -> guardar());
        btnEliminar.addActionListener(e -> {
            try {
                eliminar();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        panel.add(new JLabel("Red de Salud:")); panel.add(txtRed);
        panel.add(new JLabel("Microred:")); panel.add(txtMicrored);
        panel.add(new JLabel("RENIPRESS:")); panel.add(txtRenipress);
        panel.add(new JLabel("Nombre:")); panel.add(txtNombre);
        panel.add(new JLabel("Categoría:")); panel.add(txtCategoria);
        panel.add(btnGuardar); panel.add(btnEliminar);

        return panel;
    }

    private void cargarDatos() throws SQLException {
        modelo.setRowCount(0);
        List<EstablecimientoSalud> lista = dao.listar();
        for (EstablecimientoSalud e : lista) {
            modelo.addRow(new Object[]{
                    e.getIdEstablecimiento(),
                    e.getRedSalud(),
                    e.getMicroRed(),
                    e.getRenipress(),
                    e.getNombre(),
                    e.getCategoria()
            });
        }
    }

    private void guardar() {
        String red = txtRed.getText().trim();
        String micro = txtMicrored.getText().trim();
        String renipress = txtRenipress.getText().trim();
        String nombre = txtNombre.getText().trim();
        String categoria = txtCategoria.getText().trim();

        if (red.isEmpty() || micro.isEmpty() || renipress.isEmpty() || nombre.isEmpty() || categoria.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa todos los campos.");
            return;
        }

        try {
            if (seleccionado == null) {
                EstablecimientoSalud nuevo = new EstablecimientoSalud(0, red, micro, renipress, nombre, categoria);
                dao.agregar(nuevo);
            } else {
                seleccionado.setRedSalud(red);
                seleccionado.setMicroRed(micro);
                seleccionado.setRenipress(renipress);
                seleccionado.setNombre(nombre);
                seleccionado.setCategoria(categoria);
                dao.editar(seleccionado);
            }
            limpiar();
            cargarDatos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void eliminar() throws SQLException {
        if (seleccionado != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar este establecimiento?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dao.eliminar(seleccionado.getIdEstablecimiento());
                limpiar();
                cargarDatos();
            }
        }
    }

    private void cargarSeleccionado() throws SQLException {
        int fila = tabla.getSelectedRow();
        int id = (int) modelo.getValueAt(fila, 0);
        seleccionado = dao.buscarPorId(id);
        txtRed.setText(seleccionado.getRedSalud());
        txtMicrored.setText(seleccionado.getMicroRed());
        txtRenipress.setText(seleccionado.getRenipress());
        txtNombre.setText(seleccionado.getNombre());
        txtCategoria.setText(seleccionado.getCategoria());
    }

    private void limpiar() {
        seleccionado = null;
        txtRed.setText("");
        txtMicrored.setText("");
        txtRenipress.setText("");
        txtNombre.setText("");
        txtCategoria.setText("");
        tabla.clearSelection();
    }

}
