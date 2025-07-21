package org.example.GUI;

import org.example.Entidad.*;
import org.example.Logica_DAO.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.List;

public class RegistroPruebaGUI extends JFrame {
    private JComboBox<Obstetra> cmbObstetra;
    private JComboBox<Paciente> cmbPaciente;
    private JComboBox<Programa> cmbPrograma;
    private JTextField txtFecha;
    private JTextArea txtObservaciones;
    private JTable tablaRegistros;
    private DefaultTableModel modeloTabla;

    private RegistroPruebaDAO registroDAO = new RegistroPruebaDAO();
    private ObstetraDAO obstetraDAO = new ObstetraDAO();
    private PacienteDAO pacienteDAO = new PacienteDAO();
    private ProgramaDAO programaDAO = new ProgramaDAO();

    private int idRegistroSeleccionado = -1;

    public RegistroPruebaGUI() {
        setTitle("Gestión de Registros de Pruebas");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initComponentes();
        cargarCombos();
        cargarRegistros();

        setVisible(true);
    }

    private void initComponentes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // --- Obstetra ---
        JLabel lblObstetra = new JLabel("Obstetra:");
        cmbObstetra = new JComboBox<>();
        cmbObstetra.setPreferredSize(new Dimension(200, 25));

        cmbObstetra.addActionListener(e -> cargarRegistros());


        // --- Paciente ---
        JLabel lblPaciente = new JLabel("Paciente:");
        cmbPaciente = new JComboBox<>();
        cmbPaciente.setPreferredSize(new Dimension(200, 25));

        // --- Programa ---
        JLabel lblPrograma = new JLabel("Programa:");
        cmbPrograma = new JComboBox<>();
        cmbPrograma.setPreferredSize(new Dimension(200, 25));

        // --- Fecha ---
        JLabel lblFecha = new JLabel("Fecha (YYYY-MM-DD):");
        txtFecha = new JTextField();
        txtFecha.setPreferredSize(new Dimension(200, 25));

        // --- Observaciones ---
        JLabel lblObs = new JLabel("Observaciones:");
        txtObservaciones = new JTextArea(3, 20);
        JScrollPane scrollObs = new JScrollPane(txtObservaciones);

        // --- Botones ---
        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");

        btnAgregar.addActionListener(e -> agregarRegistro());
        btnEditar.addActionListener(e -> editarRegistro());
        btnEliminar.addActionListener(e -> eliminarRegistro());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        // --- Tabla ---
        modeloTabla = new DefaultTableModel(new Object[]{
                "ID", "Paciente", "Obstetra", "Programa", "Fecha", "Observaciones"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // tabla solo lectura
            }
        };
        tablaRegistros = new JTable(modeloTabla);
        tablaRegistros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaRegistros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tablaRegistros.getSelectedRow();
                if (fila != -1) {
                    cargarRegistroSeleccionado(fila);
                }
            }
        });
        JScrollPane scrollTabla = new JScrollPane(tablaRegistros);

        // --- Layout Formulario ---
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; panelFormulario.add(lblObstetra, gbc);
        gbc.gridx = 1; panelFormulario.add(cmbObstetra, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelFormulario.add(lblPaciente, gbc);
        gbc.gridx = 1; panelFormulario.add(cmbPaciente, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelFormulario.add(lblPrograma, gbc);
        gbc.gridx = 1; panelFormulario.add(cmbPrograma, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panelFormulario.add(lblFecha, gbc);
        gbc.gridx = 1; panelFormulario.add(txtFecha, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panelFormulario.add(lblObs, gbc);
        gbc.gridx = 1; panelFormulario.add(scrollObs, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        JPanel panelBotones = new JPanel();
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        panelFormulario.add(panelBotones, gbc);

        panel.add(panelFormulario, BorderLayout.NORTH);
        panel.add(scrollTabla, BorderLayout.CENTER);

        setContentPane(panel);
    }

    private void cargarCombos() {
        try {
            if (cmbObstetra.getItemCount() == 0) {
                for (Obstetra o : obstetraDAO.listarObstetras()) {
                    cmbObstetra.addItem(o);
                }
            }

            // Pacientes (todos)
            cmbPaciente.removeAllItems();
            for (Paciente p : pacienteDAO.listarPacientes()) {
                cmbPaciente.addItem(p);
            }

            // Programas (todos)
            cmbPrograma.removeAllItems();
            for (Programa pr : programaDAO.listarTodos()) {
                cmbPrograma.addItem(pr);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando datos: " + e.getMessage());
        }
    }

    private void cargarRegistros() {
        try {
            List<RegistroPrueba> registros;
            Obstetra seleccionado = (Obstetra) cmbObstetra.getSelectedItem();
            if (seleccionado != null) {
                registros = registroDAO.listarRegistrosPorObstetra(seleccionado.getIdObstetra());
            } else {
                registros = registroDAO.listarRegistros(); //
            }

            modeloTabla.setRowCount(0);
            for (RegistroPrueba r : registros) {
                modeloTabla.addRow(new Object[]{
                        r.getIdRegistro(),
                        pacienteDAO.obtenerPacientePorId(r.getIdPaciente()),
                        obstetraDAO.obtenerPorId(r.getIdObstetra()),
                        programaDAO.obtenerPorId(r.getIdPrograma()),
                        r.getFecha(),
                        r.getObservaciones()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando registros: " + e.getMessage());
        }
    }

    private void cargarRegistroSeleccionado(int fila) {
        idRegistroSeleccionado = (int) modeloTabla.getValueAt(fila, 0);

        Paciente paciente = (Paciente) modeloTabla.getValueAt(fila, 1);
        Obstetra obstetra = (Obstetra) modeloTabla.getValueAt(fila, 2);
        Programa programa = (Programa) modeloTabla.getValueAt(fila, 3);
        Date fecha = (Date) modeloTabla.getValueAt(fila, 4);
        String obs = (String) modeloTabla.getValueAt(fila, 5);

        cmbPaciente.setSelectedItem(paciente);
        cmbPrograma.setSelectedItem(programa);
        txtFecha.setText(fecha.toString());
        txtObservaciones.setText(obs);


        cmbObstetra.setSelectedItem(obstetra);

    }

    private void limpiarFormulario() {
        idRegistroSeleccionado = -1;

        cmbObstetra.setSelectedIndex(-1);

        cmbPaciente.setSelectedIndex(-1);
        cmbPrograma.setSelectedIndex(-1);
        txtFecha.setText("");
        txtObservaciones.setText("");
    }

    private void agregarRegistro() {
        try {
            if (!validarFormulario()) return;

            int idObstetra;
            Obstetra obstetra = (Obstetra) cmbObstetra.getSelectedItem();
            idObstetra = obstetra.getIdObstetra();

            RegistroPrueba nuevo = new RegistroPrueba(
                    0,
                    ((Paciente) cmbPaciente.getSelectedItem()).getIdPaciente(),
                    idObstetra,
                    ((Programa) cmbPrograma.getSelectedItem()).getIdPrograma(),
                    Date.valueOf(txtFecha.getText().trim()),
                    txtObservaciones.getText().trim()
            );

            registroDAO.registrarRegistro(nuevo);
            JOptionPane.showMessageDialog(this, "Registro agregado exitosamente.");
            limpiarFormulario();
            cargarRegistros();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al agregar registro: " + e.getMessage());
        }
    }

    private void editarRegistro() {
        if (idRegistroSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para editar.");
            return;
        }
        try {
            if (!validarFormulario()) return;

            int idObstetra;
            Obstetra obstetra = (Obstetra) cmbObstetra.getSelectedItem();
            idObstetra = obstetra.getIdObstetra();

            RegistroPrueba editado = new RegistroPrueba(
                    idRegistroSeleccionado,
                    ((Paciente) cmbPaciente.getSelectedItem()).getIdPaciente(),
                    idObstetra,
                    ((Programa) cmbPrograma.getSelectedItem()).getIdPrograma(),
                    Date.valueOf(txtFecha.getText().trim()),
                    txtObservaciones.getText().trim()
            );

            registroDAO.actualizarRegistro(editado);
            JOptionPane.showMessageDialog(this, "Registro actualizado exitosamente.");
            limpiarFormulario();
            cargarRegistros();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al editar registro: " + e.getMessage());
        }
    }

    private void eliminarRegistro() {
        if (idRegistroSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para eliminar.");
            return;
        }
        int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar el registro seleccionado?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {
            try {
                registroDAO.eliminarRegistro(idRegistroSeleccionado);
                JOptionPane.showMessageDialog(this, "Registro eliminado exitosamente.");
                limpiarFormulario();
                cargarRegistros();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar registro: " + e.getMessage());
            }
        }
    }

    private boolean validarFormulario() {
        if (cmbPaciente.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un paciente.");
            return false;
        }
        if (ControlSesionUsuario.esAdmin() && cmbObstetra.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un obstetra.");
            return false;
        }
        if (cmbPrograma.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un programa.");
            return false;
        }
        if (txtFecha.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una fecha válida.");
            return false;
        }
        try {
            Date.valueOf(txtFecha.getText().trim()); // valida formato yyyy-MM-dd
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido (debe ser YYYY-MM-DD).");
            return false;
        }
        return true;
    }


}
