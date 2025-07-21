package org.example.GUI;

import org.example.Entidades.Paciente;
import org.example.DAO.PacienteDAO;
import org.example.Utilidades.Validaciones;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class GestionPacientes extends JFrame {
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtNombre, txtApellido, txtDni;
    private JSpinner spinnerFecha;
    private JButton btnGuardar, btnEliminar, btnLimpiar;
    private PacienteDAO dao = new PacienteDAO();
    private Paciente pacienteSeleccionado = null;

    public GestionPacientes() {
        setTitle("Gestión de Pacientes");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        modelo = new DefaultTableModel(new String[]{"ID", "Nombre", "Apellido", "DNI", "Nacimiento"}, 0);
        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);

        JPanel formulario = crearFormulario();

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scroll, formulario);
        split.setDividerLocation(250);

        add(split, BorderLayout.CENTER);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() != -1) {
                cargarPacienteSeleccionado();
            }
        });

        cargarPacientes();
        setVisible(true);
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Formulario de Paciente"));

        txtNombre = new JTextField();
        txtApellido = new JTextField();
        txtDni = new JTextField();

        spinnerFecha = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerFecha, "yyyy-MM-dd");
        spinnerFecha.setEditor(dateEditor);

        btnGuardar = new JButton("Guardar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");

        btnGuardar.addActionListener(e -> guardarPaciente());
        btnEliminar.addActionListener(e -> eliminarPaciente());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Apellido:"));
        panel.add(txtApellido);
        panel.add(new JLabel("DNI:"));
        panel.add(txtDni);
        panel.add(new JLabel("Fecha de Nacimiento:"));
        panel.add(spinnerFecha);
        panel.add(btnGuardar);
        panel.add(btnEliminar);

        return panel;
    }

    private void cargarPacientes() {
        modelo.setRowCount(0);
        try {
            List<Paciente> lista = dao.listar();
            for (Paciente p : lista) {
                modelo.addRow(new Object[]{
                        p.getIdPaciente(),
                        p.getNombre(),
                        p.getApellido(),
                        p.getDni(),
                        p.getFechaNacimiento()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar pacientes: " + e.getMessage());
        }
    }

    private void guardarPaciente() {
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String dni = txtDni.getText().trim();
        java.util.Date fechaUtil = (java.util.Date) spinnerFecha.getValue();
        Date fecha = new Date(fechaUtil.getTime());

        // Validaciones
        if (!Validaciones.noVacio(nombre) || !Validaciones.contieneSoloLetras(nombre)) {
            JOptionPane.showMessageDialog(this, "Nombre inválido.");
            return;
        }

        if (!Validaciones.noVacio(apellido) || !Validaciones.contieneSoloLetras(apellido)) {
            JOptionPane.showMessageDialog(this, "Apellido inválido.");
            return;
        }

        if (!Validaciones.esDniValido(dni)) {
            JOptionPane.showMessageDialog(this, "DNI inválido. Debe tener 8 dígitos.");
            return;
        }

        try {
            if (pacienteSeleccionado == null) {
                if (dao.buscarPorDni(dni) != null) {
                    JOptionPane.showMessageDialog(this, "Ya existe un paciente con ese DNI.");
                    return;
                }

                Paciente nuevo = new Paciente(0, nombre, apellido, dni, fecha);
                dao.agregar(nuevo);
            } else {
                pacienteSeleccionado.setNombre(nombre);
                pacienteSeleccionado.setApellido(apellido);
                pacienteSeleccionado.setDni(dni);
                pacienteSeleccionado.setFechaNacimiento(fecha);
                dao.editar(pacienteSeleccionado);
            }

            limpiarFormulario();
            cargarPacientes();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage());
        }
    }

    private void eliminarPaciente() {
        int fila = tabla.getSelectedRow();
        if (fila != -1) {
            int id = (int) modelo.getValueAt(fila, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar paciente?", "Confirmación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    dao.eliminar(id);
                    limpiarFormulario();
                    cargarPacientes();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un paciente para eliminar.");
        }
    }

    private void cargarPacienteSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila != -1) {
            int id = (int) modelo.getValueAt(fila, 0);
            try {
                pacienteSeleccionado = dao.buscarPorId(id);
                txtNombre.setText(pacienteSeleccionado.getNombre());
                txtApellido.setText(pacienteSeleccionado.getApellido());
                txtDni.setText(pacienteSeleccionado.getDni());
                spinnerFecha.setValue(pacienteSeleccionado.getFechaNacimiento());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al cargar paciente: " + e.getMessage());
            }
        }
    }

    private void limpiarFormulario() {
        pacienteSeleccionado = null;
        txtNombre.setText("");
        txtApellido.setText("");
        txtDni.setText("");
        spinnerFecha.setValue(java.sql.Date.valueOf(LocalDate.now()));
        tabla.clearSelection();
    }
}
