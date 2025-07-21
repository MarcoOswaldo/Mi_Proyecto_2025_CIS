package org.example.GUI;

import org.example.DAO.ObstetraDAO;
import org.example.DAO.PacienteDAO;
import org.example.DAO.ProgramaDAO;
import org.example.DAO.RegistroPruebaDAO;
import org.example.Entidades.Obstetra;
import org.example.Entidades.Paciente;
import org.example.Entidades.Programa;
import org.example.Entidades.RegistroPrueba;
import org.example.Utilidades.ControlSesionUsuario;
import org.example.Utilidades.Validaciones;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class RegistrarPrueba extends JFrame {
    private JTextField txtBuscarPaciente;
    private JPopupMenu popupPacientes;
    private List<Paciente> listaPacientes;
    private Paciente pacienteSeleccionado = null;

    private JComboBox<Obstetra> comboObstetra;
    private JComboBox<Programa> comboPrograma;
    private JTextArea txtObservaciones;
    private JSpinner spinnerFecha;
    private JTable tablaRegistros;
    private DefaultTableModel modeloTabla;
    private JButton btnRegistrar, btnEditar, btnEliminar;
    private JLabel lblEdad;  // NUEVO

    private RegistroPruebaDAO dao = new RegistroPruebaDAO();
    private RegistroPrueba registroSeleccionado = null;

    public RegistrarPrueba() throws SQLException {
        setTitle("Registrar Prueba");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ----------- PANEL FORMULARIO (SUPERIOR) -----------
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos de Registro"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        txtBuscarPaciente = new JTextField(20);
        popupPacientes = new JPopupMenu();

        comboObstetra = new JComboBox<>();
        comboPrograma = new JComboBox<>();
        txtObservaciones = new JTextArea(3, 20);
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);
        txtObservaciones.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane scrollObservaciones = new JScrollPane(txtObservaciones);

        spinnerFecha = new JSpinner(new SpinnerDateModel());
        spinnerFecha.setEditor(new JSpinner.DateEditor(spinnerFecha, "yyyy-MM-dd"));
        spinnerFecha.setValue(new Date());

        lblEdad = new JLabel("Edad: -");

        // Fila 0
        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(new JLabel("Paciente (DNI):"), gbc);
        gbc.gridx = 1;
        panelForm.add(txtBuscarPaciente, gbc);

        // Fila 1
        gbc.gridx = 0; gbc.gridy++;
        panelForm.add(new JLabel("Obstetra:"), gbc);
        gbc.gridx = 1;
        panelForm.add(comboObstetra, gbc);

        // Fila 2
        gbc.gridx = 0; gbc.gridy++;
        panelForm.add(new JLabel("Programa:"), gbc);
        gbc.gridx = 1;
        panelForm.add(comboPrograma, gbc);

        // Fila 3
        gbc.gridx = 0; gbc.gridy++;
        panelForm.add(new JLabel("Fecha:"), gbc);
        gbc.gridx = 1;
        panelForm.add(spinnerFecha, gbc);

        // Fila 4
        gbc.gridx = 0; gbc.gridy++;
        panelForm.add(new JLabel("Edad al registro:"), gbc);
        gbc.gridx = 1;
        panelForm.add(lblEdad, gbc);

        // Fila 5
        gbc.gridx = 0; gbc.gridy++;
        panelForm.add(new JLabel("Observaciones:"), gbc);
        gbc.gridx = 1;
        panelForm.add(scrollObservaciones, gbc);

        // ----------- BOTONES -----------
        btnRegistrar = new JButton("Registrar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        // ----------- PANEL SUPERIOR (FORM + BOTONES) -----------
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BorderLayout(10, 10));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelSuperior.add(panelForm, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);

        // ----------- TABLA DE REGISTROS -----------
        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Paciente", "Edad", "Obstetra", "Programa", "Fecha", "Observaciones"}, 0);
        tablaRegistros = new JTable(modeloTabla);
        tablaRegistros.setFillsViewportHeight(true);
        tablaRegistros.setRowHeight(25);
        add(new JScrollPane(tablaRegistros), BorderLayout.CENTER);

        // ----------- EVENTOS Y DATOS INICIALES -----------
        configurarEventos();
        cargarListaPacientes();
        cargarCombos();
        cargarRegistros();

        setVisible(true);
    }

    private void configurarEventos() {
        txtBuscarPaciente.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { mostrarCoincidencias(); }
            @Override public void removeUpdate(DocumentEvent e) { mostrarCoincidencias(); }
            @Override public void changedUpdate(DocumentEvent e) { mostrarCoincidencias(); }

            private void mostrarCoincidencias() {
                String texto = txtBuscarPaciente.getText().trim().toLowerCase();
                popupPacientes.removeAll();

                if (texto.isEmpty()) {
                    popupPacientes.setVisible(false);
                    pacienteSeleccionado = null;
                    actualizarEdad();
                    return;
                }

                int maxResultados = 10;
                int count = 0;

                for (Paciente p : listaPacientes) {
                    if (p.getDni().toLowerCase().contains(texto)) {
                        JMenuItem item = new JMenuItem(p.getDni() + " - " + p.getNombre() + " " + p.getApellido());
                        item.addActionListener(ev -> {
                            txtBuscarPaciente.setText(p.getDni() + " - " + p.getNombre() + " " + p.getApellido());
                            popupPacientes.setVisible(false);
                            pacienteSeleccionado = p;
                            actualizarEdad();
                        });
                        popupPacientes.add(item);
                        count++;
                        if (count >= maxResultados) break;
                    }
                }

                if (count > 0) {
                    popupPacientes.setFocusable(false);
                    popupPacientes.show(txtBuscarPaciente, 0, txtBuscarPaciente.getHeight());
                    popupPacientes.setInvoker(txtBuscarPaciente);
                } else {
                    popupPacientes.setVisible(false);
                    pacienteSeleccionado = null;
                    actualizarEdad();
                }
            }
        });

        spinnerFecha.addChangeListener(e -> actualizarEdad());

        btnRegistrar.addActionListener(e -> {
            try {
                registrar();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        btnEditar.addActionListener(e -> editar());
        btnEliminar.addActionListener(e -> eliminar());

        tablaRegistros.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaRegistros.getSelectedRow() != -1) {
                cargarSeleccionado();
            }
        });
    }



    private void actualizarEdad() {
        if (pacienteSeleccionado != null) {
            Date fechaRegistro = (Date) spinnerFecha.getValue();
            int edad = calcularEdad(pacienteSeleccionado.getFechaNacimiento(), fechaRegistro);
            lblEdad.setText("Edad: " + edad + " años");
        } else {
            lblEdad.setText("Edad: -");
        }
    }

    private int calcularEdad(Date nacimiento, Date fechaRegistro) {
        Calendar nac = Calendar.getInstance();
        nac.setTime(nacimiento);
        Calendar reg = Calendar.getInstance();
        reg.setTime(fechaRegistro);

        int edad = reg.get(Calendar.YEAR) - nac.get(Calendar.YEAR);
        if (reg.get(Calendar.DAY_OF_YEAR) < nac.get(Calendar.DAY_OF_YEAR)) {
            edad--;
        }
        return edad;
    }

    private void cargarListaPacientes() throws SQLException {
        PacienteDAO pacienteDAO = new PacienteDAO();
        listaPacientes = pacienteDAO.listar();
    }

    private void cargarCombos() throws SQLException {
        ObstetraDAO obstetraDAO = new ObstetraDAO();
        ProgramaDAO programaDAO = new ProgramaDAO();

        List<Programa> programas = programaDAO.listar();
        for (Programa pr : programas) {
            comboPrograma.addItem(pr);
        }

        int idUsuario = ControlSesionUsuario.getUsuarioActual().getId();
        Obstetra obstetraLogeado = obstetraDAO.obtenerObstetraPorIdUsuario(idUsuario);

        comboObstetra.addItem(obstetraLogeado);
        comboObstetra.setSelectedItem(obstetraLogeado);
        comboObstetra.setEnabled(false);
    }

    private void cargarRegistros() throws SQLException {
        modeloTabla.setRowCount(0);

        ObstetraDAO obstetraDAO = new ObstetraDAO();
        int idUsuario = ControlSesionUsuario.getUsuarioActual().getId();
        Obstetra obstetraLogeado = obstetraDAO.obtenerObstetraPorIdUsuario(idUsuario);

        List<RegistroPrueba> lista = dao.listarPorObstetra(obstetraLogeado.getIdObstetra());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (RegistroPrueba r : lista) {
            Paciente p = new PacienteDAO().buscarPorId(r.getIdPaciente());
            Obstetra o = new ObstetraDAO().buscarPorId(r.getIdObstetra());
            Programa pr = new ProgramaDAO().buscarPorId(r.getIdPrograma());
            modeloTabla.addRow(new Object[]{
                    r.getIdRegistro(),
                    p != null ? p.getNombre() + " " + p.getApellido() : "",
                    r.getEdad(), // nueva columna
                    o != null ? o.getNombre() + " " + o.getApellido() : "",
                    pr != null ? pr.getNombre() : "",
                    sdf.format(r.getFecha()),
                    r.getObservaciones()
            });

        }
    }

    private void registrar() throws SQLException {
        if (pacienteSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un paciente válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Obstetra obstetra = (Obstetra) comboObstetra.getSelectedItem();
        Programa programa = (Programa) comboPrograma.getSelectedItem();
        Date fecha = (Date) spinnerFecha.getValue();
        String observaciones = txtObservaciones.getText();
        int edad = calcularEdad(pacienteSeleccionado.getFechaNacimiento(), fecha);

        if (!Validaciones.esDniValido(pacienteSeleccionado.getDni())) {
            JOptionPane.showMessageDialog(this, "DNI inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        RegistroPrueba registro = new RegistroPrueba();
        registro.setIdPaciente(pacienteSeleccionado.getIdPaciente());
        registro.setIdObstetra(obstetra.getIdObstetra());
        registro.setIdPrograma(programa.getIdPrograma());
        registro.setFecha(fecha);
        registro.setObservaciones(observaciones);
        registro.setEdad(edad);

        dao.agregar(registro);
        limpiarFormulario();
        cargarRegistros();
    }

    private void cargarSeleccionado() {
        int fila = tablaRegistros.getSelectedRow();
        if (fila >= 0) {
            int id = (int) modeloTabla.getValueAt(fila, 0);
            try {
                registroSeleccionado = dao.buscarPorId(id);

                // Buscar y asignar paciente seleccionado a pacienteSeleccionado y texto
                Paciente p = new PacienteDAO().buscarPorId(registroSeleccionado.getIdPaciente());
                if (p != null) {
                    pacienteSeleccionado = p;
                    txtBuscarPaciente.setText(p.getDni() + " - " + p.getNombre() + " " + p.getApellido());
                } else {
                    pacienteSeleccionado = null;
                    txtBuscarPaciente.setText("");
                }

                // Establecer obstetra y programa en combos
                for (int i = 0; i < comboObstetra.getItemCount(); i++) {
                    if (comboObstetra.getItemAt(i).getIdObstetra() == registroSeleccionado.getIdObstetra()) {
                        comboObstetra.setSelectedIndex(i);
                        break;
                    }
                }
                for (int i = 0; i < comboPrograma.getItemCount(); i++) {
                    if (comboPrograma.getItemAt(i).getIdPrograma() == registroSeleccionado.getIdPrograma()) {
                        comboPrograma.setSelectedIndex(i);
                        break;
                    }
                }

                spinnerFecha.setValue(registroSeleccionado.getFecha());
                txtObservaciones.setText(registroSeleccionado.getObservaciones());

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }


    private void editar() {
        if (registroSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro primero.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (pacienteSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un paciente válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Obstetra obstetra = (Obstetra) comboObstetra.getSelectedItem();
            Programa programa = (Programa) comboPrograma.getSelectedItem();
            Date fecha = (Date) spinnerFecha.getValue();
            String observaciones = txtObservaciones.getText();

            if (!Validaciones.esDniValido(pacienteSeleccionado.getDni())) {
                JOptionPane.showMessageDialog(this, "DNI del paciente inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Calcular la edad nuevamente
            int edadCalculada = calcularEdad(pacienteSeleccionado.getFechaNacimiento(), fecha);
            registroSeleccionado.setEdad(edadCalculada);

            registroSeleccionado.setIdPaciente(pacienteSeleccionado.getIdPaciente());
            registroSeleccionado.setIdObstetra(obstetra.getIdObstetra());
            registroSeleccionado.setIdPrograma(programa.getIdPrograma());
            registroSeleccionado.setFecha(fecha);
            registroSeleccionado.setObservaciones(observaciones);

            dao.editar(registroSeleccionado);
            JOptionPane.showMessageDialog(this, "Registro actualizado.");
            limpiarFormulario();
            cargarRegistros();


        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void eliminar() {
        if (registroSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro primero.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar el registro?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                dao.eliminar(registroSeleccionado.getIdRegistro());
                JOptionPane.showMessageDialog(this, "Registro eliminado.");
                limpiarFormulario();
                cargarRegistros();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarFormulario() {
        txtBuscarPaciente.setText("");
        pacienteSeleccionado = null;
        comboObstetra.setSelectedIndex(0);
        comboPrograma.setSelectedIndex(0);
        spinnerFecha.setValue(new Date());
        txtObservaciones.setText("");
        lblEdad.setText("Edad: -");
        tablaRegistros.clearSelection();
        registroSeleccionado = null;
    }
}

