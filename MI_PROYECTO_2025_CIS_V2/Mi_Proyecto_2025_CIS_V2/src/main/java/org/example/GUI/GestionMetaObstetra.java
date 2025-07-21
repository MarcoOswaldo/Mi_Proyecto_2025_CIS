package org.example.GUI;

import org.example.Entidades.MetaObstetra;
import org.example.Entidades.Obstetra;
import org.example.Entidades.Programa;
import org.example.DAO.MetaObstetraDAO;
import org.example.DAO.ObstetraDAO;
import org.example.DAO.ProgramaDAO;
import org.example.Utilidades.Validaciones;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class GestionMetaObstetra extends JFrame {
    private JTable tabla;
    private DefaultTableModel modelo;
    private JComboBox<Obstetra> cbxObstetra;
    private JComboBox<Programa> cbxPrograma;
    private JTextField txtAnio, txtMetaAnual, txtMetaMensual;
    private JButton btnGuardar, btnEliminar;
    private MetaObstetraDAO dao = new MetaObstetraDAO();
    private ObstetraDAO obstetraDAO = new ObstetraDAO();
    private ProgramaDAO programaDAO = new ProgramaDAO();
    private MetaObstetra metaSeleccionada = null;

    public GestionMetaObstetra() throws SQLException {
        setTitle("Gestión de Metas por Obstetra");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        modelo = new DefaultTableModel(new String[]{"ID", "Obstetra", "Programa", "Año", "Meta Anual", "Meta Mensual"}, 0);
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
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Formulario de Meta por Obstetra"));

        cbxObstetra = new JComboBox<>();
        cbxPrograma = new JComboBox<>();
        txtAnio = new JTextField();
        txtMetaAnual = new JTextField();
        txtMetaMensual = new JTextField();
        btnGuardar = new JButton("Guardar");
        btnEliminar = new JButton("Eliminar");

        cargarObstetras();
        cargarProgramas();

        btnGuardar.addActionListener(e -> guardar());
        btnEliminar.addActionListener(e -> {
            try {
                eliminar();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        panel.add(new JLabel("Obstetra:")); panel.add(cbxObstetra);
        panel.add(new JLabel("Programa:")); panel.add(cbxPrograma);
        panel.add(new JLabel("Año:")); panel.add(txtAnio);
        panel.add(new JLabel("Meta Anual:")); panel.add(txtMetaAnual);
        panel.add(new JLabel("Meta Mensual:")); panel.add(txtMetaMensual);
        panel.add(btnGuardar); panel.add(btnEliminar);

        return panel;
    }

    private void cargarObstetras() throws SQLException {
        cbxObstetra.removeAllItems();
        List<Obstetra> obstetras = obstetraDAO.listar();
        for (Obstetra o : obstetras) {
            cbxObstetra.addItem(o);
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
        List<MetaObstetra> lista = dao.listar();
        for (MetaObstetra m : lista) {
            Obstetra obs = obstetraDAO.buscarPorId(m.getIdObstetra());
            Programa prog = programaDAO.buscarPorId(m.getIdPrograma());
            modelo.addRow(new Object[]{
                    m.getIdMetaObstetra(),
                    obs != null ? obs.getNombre() + " " + obs.getApellido() : "[Obstetra]",
                    prog != null ? prog.getNombre() : "[Programa]",
                    m.getAnio(),
                    m.getMetaAnual(),
                    m.getMetaMensual()
            });
        }
    }

    private void guardar() {
        Obstetra obstetra = (Obstetra) cbxObstetra.getSelectedItem();
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
                MetaObstetra nueva = new MetaObstetra(0, obstetra.getIdObstetra(), programa.getIdPrograma(), anio, metaAnual, metaMensual);
                dao.agregar(nueva);
            } else {
                metaSeleccionada.setIdObstetra(obstetra.getIdObstetra());
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
                dao.eliminar(metaSeleccionada.getIdMetaObstetra());
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

        for (int i = 0; i < cbxObstetra.getItemCount(); i++) {
            if (cbxObstetra.getItemAt(i).getIdObstetra() == metaSeleccionada.getIdObstetra()) {
                cbxObstetra.setSelectedIndex(i);
                break;
            }
        }

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
