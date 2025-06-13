package org.example.GUI;

import org.example.Entidad.Obstetra;
import org.example.Logica_DAO.ObstetraDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ObstetraGUI extends JFrame {

    private JTextField txtNombre = new JTextField(15);
    private JTextField txtApellido = new JTextField(15);
    private JTextField txtDni = new JTextField(15);
    private JTable tabla = new JTable();
    private DefaultTableModel modelo = new DefaultTableModel();
    private ObstetraDAO dao = new ObstetraDAO();
    private int obstetraSeleccionado = -1;

    public ObstetraGUI() {
        setTitle("Gestión de Obstetras");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        modelo.setColumnIdentifiers(new String[] { "ID", "Nombre", "Apellido", "DNI" });
        tabla.setModel(modelo);

        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");

        JPanel form = new JPanel(new GridLayout(4, 2));
        form.add(new JLabel("Nombre:"));
        form.add(txtNombre);
        form.add(new JLabel("Apellido:"));
        form.add(txtApellido);
        form.add(new JLabel("DNI:"));
        form.add(txtDni);

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
                obstetraSeleccionado = Integer.parseInt(tabla.getValueAt(fila, 0).toString());
                txtNombre.setText(tabla.getValueAt(fila, 1).toString());
                txtApellido.setText(tabla.getValueAt(fila, 2).toString());
                txtDni.setText(tabla.getValueAt(fila, 3).toString());
            }
        });

        btnAgregar.addActionListener(e -> {
            Obstetra o = new Obstetra(txtNombre.getText(), txtApellido.getText(), txtDni.getText());
            dao.registrarObstetra(o);
            cargarDatos();
            limpiarCampos();
        });

        btnEditar.addActionListener(e -> {
            if (obstetraSeleccionado == -1) return;
            Obstetra o = new Obstetra(obstetraSeleccionado, txtNombre.getText(), txtApellido.getText(), txtDni.getText());
            dao.actualizarObstetra(o);
            cargarDatos();
            limpiarCampos();
        });

        btnEliminar.addActionListener(e -> {
            if (obstetraSeleccionado == -1) return;
            dao.eliminarObstetra(obstetraSeleccionado);
            cargarDatos();
            limpiarCampos();
        });

        btnLimpiar.addActionListener(e -> limpiarCampos());

        setVisible(true);
    }

    private void cargarDatos() {
        modelo.setRowCount(0);
        List<Obstetra> lista = dao.listarObstetras();
        for (Obstetra o : lista) {
            modelo.addRow(new Object[] { o.getIdObstetra(), o.getNombre(), o.getApellido(), o.getDni() });
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtDni.setText("");
        obstetraSeleccionado = -1;
        tabla.clearSelection();
    }

    private void mostrarError(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }


}
