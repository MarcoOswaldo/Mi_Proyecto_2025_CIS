package org.example.GUI;

import org.example.Entidades.MetaPrograma;
import org.example.Entidades.Programa;
import org.example.DAO.MetaProgramaDAO;
import org.example.DAO.ProgramaDAO;
import org.example.Utilidades.Validaciones;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class GestionMetaPrograma extends JFrame {
    private JTable tabla;
    private DefaultTableModel modelo;
    private JComboBox<Programa> cbxPrograma;
    private JTextField txtAnio, txtMetaAnual, txtMetaMensual;
    private JButton btnGuardar, btnEliminar;
    private MetaProgramaDAO dao = new MetaProgramaDAO();
    private ProgramaDAO programaDAO = new ProgramaDAO();
    private MetaPrograma metaSeleccionada = null;

    public GestionMetaPrograma() throws SQLException {
        setTitle("Gestión de Metas por Programa");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        modelo = new DefaultTableModel(new String[]{"ID", "Programa", "Año", "Meta Anual", "Meta Mensual"}, 0);
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
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Formulario de Meta del Programa"));

        cbxPrograma = new JComboBox<>();
        txtAnio = new JTextField();
        txtMetaAnual = new JTextField();
        txtMetaMensual = new JTextField();
        btnGuardar = new JButton("Guardar");
        btnEliminar = new JButton("Eliminar");

        cargarProgramas();

        btnGuardar.addActionListener(e -> guardar());
        btnEliminar.addActionListener(e -> {
            try {
                eliminar();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        // Agregar un DocumentListener al campo txtMetaAnual
        txtMetaAnual.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                actualizarMetaMensual();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                actualizarMetaMensual();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                actualizarMetaMensual();
            }
        });

        panel.add(new JLabel("Programa:")); panel.add(cbxPrograma);
        panel.add(new JLabel("Año:")); panel.add(txtAnio);
        panel.add(new JLabel("Meta Anual:")); panel.add(txtMetaAnual);
        panel.add(new JLabel("Meta Mensual:")); panel.add(txtMetaMensual);
        panel.add(btnGuardar); panel.add(btnEliminar);

        return panel;
    }

    private void actualizarMetaMensual() {
        // Verificamos si el campo txtMetaAnual contiene un valor numérico
        String metaAnualStr = txtMetaAnual.getText().trim();
        if (Validaciones.esNumero(metaAnualStr)) {
            // Convertimos el valor a un número
            double metaAnual = Double.parseDouble(metaAnualStr);

            // Calculamos la meta mensual (metaAnual / 12)
            double metaMensual = Math.ceil(metaAnual / 12);  // Usamos Math.ceil para redondear hacia arriba

            // Actualizamos el valor en txtMetaMensual
            txtMetaMensual.setText(String.valueOf((int) metaMensual));  // Convertimos a entero ya que la meta mensual es un valor entero
        }
    }



    private void cargarProgramas() throws SQLException {
        cbxPrograma.removeAllItems();
        List<Programa> programas = programaDAO.listar();
        for (Programa p : programas) {
            cbxPrograma.addItem(p);
        }
    }

    private void cargarDatos() throws SQLException {
        modelo.setRowCount(0);
        List<MetaPrograma> lista = dao.listar();
        for (MetaPrograma m : lista) {
            Programa prog = programaDAO.buscarPorId(m.getIdPrograma());
            modelo.addRow(new Object[]{
                    m.getIdMetaPrograma(),
                    prog != null ? prog.getNombre() : "[Sin nombre]",
                    m.getAnio(),
                    m.getMetaAnual(),
                    m.getMetaMensual()
            });
        }
    }

    private void guardar() {
        Programa programa = (Programa) cbxPrograma.getSelectedItem();
        String anioStr = txtAnio.getText().trim();
        String anualStr = txtMetaAnual.getText().trim();
        String mensualStr = txtMetaMensual.getText().trim();

        if (!Validaciones.esNumero(anioStr) || !Validaciones.esNumero(anualStr) || !Validaciones.esNumero(mensualStr)) {
            JOptionPane.showMessageDialog(this, "Todos los campos deben ser numéricos.");
            return;
        }

        int anio = Integer.parseInt(anioStr);
        int metaAnual = Integer.parseInt(anualStr);
        int metaMensual = Integer.parseInt(mensualStr);

        try {
            if (metaSeleccionada == null) {
                MetaPrograma nueva = new MetaPrograma(0, programa.getIdPrograma(), anio, metaAnual, metaMensual);
                dao.agregar(nueva);
            } else {
                metaSeleccionada.setIdPrograma(programa.getIdPrograma());
                metaSeleccionada.setAnio(anio);
                metaSeleccionada.setMetaAnual(metaAnual);
                metaSeleccionada.setMetaMensual(metaMensual);
                dao.editar(metaSeleccionada);
            }
            limpiar();
            cargarDatos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void eliminar() throws SQLException {
        if (metaSeleccionada != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar esta meta?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dao.eliminar(metaSeleccionada.getIdMetaPrograma());
                limpiar();
                cargarDatos();
            }
        }
    }

    private void cargarSeleccionado() throws SQLException {
        int fila = tabla.getSelectedRow();
        int id = (int) modelo.getValueAt(fila, 0);
        metaSeleccionada = dao.buscarPorId(id);
        txtAnio.setText(String.valueOf(metaSeleccionada.getAnio()));
        txtMetaAnual.setText(String.valueOf(metaSeleccionada.getMetaAnual()));
        txtMetaMensual.setText(String.valueOf(metaSeleccionada.getMetaMensual()));

        for (int i = 0; i < cbxPrograma.getItemCount(); i++) {
            if (cbxPrograma.getItemAt(i).getIdPrograma() == metaSeleccionada.getIdPrograma()) {
                cbxPrograma.setSelectedIndex(i);
                break;
            }
        }
    }

    private void limpiar() {
        metaSeleccionada = null;
        txtAnio.setText("");
        txtMetaAnual.setText("");
        txtMetaMensual.setText("");
        tabla.clearSelection();
    }


}
