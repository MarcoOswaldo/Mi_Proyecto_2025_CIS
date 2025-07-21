package org.example.GUI;

import org.example.Entidades.Programa;
import org.example.DAO.ProgramaDAO;
import org.example.Utilidades.Validaciones;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class GestionProgramas extends JFrame {
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtNombre;
    private JTextArea txtDescripcion;
    private JButton btnGuardar, btnEliminar;
    private ProgramaDAO dao = new ProgramaDAO();
    private Programa programaSeleccionado = null;

    public GestionProgramas() throws SQLException {
        setTitle("Gestión de Programas");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        modelo = new DefaultTableModel(new String[]{"ID", "Nombre", "Descripción"}, 0);
        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);

        JPanel formulario = crearFormulario();

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scroll, formulario);
        split.setDividerLocation(200);
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
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Formulario de Programa"));

        txtNombre = new JTextField();
        txtDescripcion = new JTextArea(3, 20);
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);

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

        panel.add(new JLabel("Nombre:")); panel.add(txtNombre);
        panel.add(new JLabel("Descripción:")); panel.add(scrollDesc);
        panel.add(btnGuardar); panel.add(btnEliminar);

        return panel;
    }

    private void cargarDatos() throws SQLException {
        modelo.setRowCount(0);
        List<Programa> lista = dao.listar();
        for (Programa p : lista) {
            modelo.addRow(new Object[]{p.getIdPrograma(), p.getNombre(), p.getDescripcion()});
        }
    }

    private void guardar() {
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();

        if (!Validaciones.noVacio(nombre)) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.");
            return;
        }

        try {
            if (programaSeleccionado == null) {
                Programa nuevo = new Programa(0, nombre, descripcion);
                dao.agregar(nuevo);
            } else {
                programaSeleccionado.setNombre(nombre);
                programaSeleccionado.setDescripcion(descripcion);
                dao.editar(programaSeleccionado);
            }
            limpiar();
            cargarDatos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void eliminar() throws SQLException {
        if (programaSeleccionado != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar programa?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dao.eliminar(programaSeleccionado.getIdPrograma());
                limpiar();
                cargarDatos();
            }
        }
    }

    private void cargarSeleccionado() throws SQLException {
        int fila = tabla.getSelectedRow();
        int id = (int) modelo.getValueAt(fila, 0);
        programaSeleccionado = dao.buscarPorId(id);
        txtNombre.setText(programaSeleccionado.getNombre());
        txtDescripcion.setText(programaSeleccionado.getDescripcion());
    }

    private void limpiar() {
        programaSeleccionado = null;
        txtNombre.setText("");
        txtDescripcion.setText("");
        tabla.clearSelection();
    }

}
