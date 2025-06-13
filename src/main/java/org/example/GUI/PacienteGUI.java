package org.example.GUI;

import org.example.Entidad.Paciente;
import org.example.Logica_DAO.PacienteDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PacienteGUI extends JFrame {

    private JTextField txtNombre = new JTextField(15);
    private JTextField txtApellido = new JTextField(15);
    private JTextField txtDni = new JTextField(15);
    private JTextField txtFechaNacimiento = new JTextField(15);
    private JTable tabla = new JTable();
    private DefaultTableModel modelo = new DefaultTableModel();
    private PacienteDAO dao = new PacienteDAO();
    private int pacienteSeleccionado = -1;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public PacienteGUI() {
        setTitle("Gestión de Pacientes");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(700, 450);
        setLocationRelativeTo(null);

        modelo.setColumnIdentifiers(new String[]{"ID", "Nombre", "Apellido", "DNI", "Fecha Nacimiento"});
        tabla.setModel(modelo);

        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");

        JPanel form = new JPanel(new GridLayout(5, 2));
        form.add(new JLabel("Nombre:"));
        form.add(txtNombre);
        form.add(new JLabel("Apellido:"));
        form.add(txtApellido);
        form.add(new JLabel("DNI:"));
        form.add(txtDni);
        form.add(new JLabel("Fecha Nacimiento (dd/MM/yyyy):"));
        form.add(txtFechaNacimiento);

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
                pacienteSeleccionado = Integer.parseInt(tabla.getValueAt(fila, 0).toString());
                txtNombre.setText(tabla.getValueAt(fila, 1).toString());
                txtApellido.setText(tabla.getValueAt(fila, 2).toString());
                txtDni.setText(tabla.getValueAt(fila, 3).toString());
                txtFechaNacimiento.setText(tabla.getValueAt(fila, 4).toString());
            }
        });

        btnAgregar.addActionListener(e -> {
            if (validarCampos()) {
                try {
                    Date fecha = dateFormat.parse(txtFechaNacimiento.getText());
                    java.sql.Date sqlFecha = new java.sql.Date(fecha.getTime());
                    Paciente p = new Paciente(txtNombre.getText(), txtApellido.getText(), txtDni.getText(), sqlFecha);
                    dao.registrarPaciente(p);
                    cargarDatos();
                    limpiarCampos();
                } catch (ParseException ex) {
                    mostrarError("Formato de fecha incorrecto. Use dd/MM/yyyy.");
                }
            }
        });


        btnEditar.addActionListener(e -> {
            if (pacienteSeleccionado == -1) return;
            if (validarCampos()) {
                try {
                    Date fecha = dateFormat.parse(txtFechaNacimiento.getText());
                    java.sql.Date sqlFecha = new java.sql.Date(fecha.getTime());
                    Paciente p = new Paciente(txtNombre.getText(), txtApellido.getText(), txtDni.getText(), sqlFecha);
                    dao.actualizarPaciente(p);
                    cargarDatos();
                    limpiarCampos();
                } catch (ParseException ex) {
                    mostrarError("Formato de fecha incorrecto. Use dd/MM/yyyy.");
                }
            }
        });

        btnEliminar.addActionListener(e -> {
            if (pacienteSeleccionado == -1) return;
            dao.eliminarPaciente(pacienteSeleccionado);
            cargarDatos();
            limpiarCampos();
        });

        btnLimpiar.addActionListener(e -> limpiarCampos());

        setVisible(true);
    }

    private void cargarDatos() {
        modelo.setRowCount(0);
        List<Paciente> lista = dao.listarPacientes();
        for (Paciente p : lista) {
            modelo.addRow(new Object[]{
                    p.getIdPaciente(),
                    p.getNombre(),
                    p.getApellido(),
                    p.getDni(),
                    dateFormat.format(p.getFecha_nacimiento())
            });
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtDni.setText("");
        txtFechaNacimiento.setText("");
        pacienteSeleccionado = -1;
        tabla.clearSelection();
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty() ||
                txtApellido.getText().trim().isEmpty() ||
                txtDni.getText().trim().isEmpty() ||
                txtFechaNacimiento.getText().trim().isEmpty()) {
            mostrarError("Todos los campos son obligatorios.");
            return false;
        }

        if (!txtDni.getText().matches("\\d{8}")) {
            mostrarError("El DNI debe contener exactamente 8 dígitos numéricos.");
            return false;
        }

        try {
            dateFormat.setLenient(false);
            dateFormat.parse(txtFechaNacimiento.getText());
        } catch (ParseException e) {
            mostrarError("La fecha debe tener el formato dd/MM/yyyy.");
            return false;
        }

        Paciente existente = dao.obtenerPorDni(txtDni.getText());
        if (existente != null && existente.getIdPaciente() != pacienteSeleccionado) {
            mostrarError("Ya existe un paciente con ese DNI.");
            return false;
        }

        return true;
    }
}
