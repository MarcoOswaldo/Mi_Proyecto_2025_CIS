package org.example.GUI;

import org.example.Entidades.RegistroPrueba;
import org.example.Entidades.Obstetra;
import org.example.Entidades.Paciente;
import org.example.Entidades.Programa;
import org.example.DAO.ObstetraDAO;
import org.example.DAO.PacienteDAO;
import org.example.DAO.ProgramaDAO;
import org.example.DAO.RegistroPruebaDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class ReportePruebas extends JFrame {
    private JTable tabla;
    private DefaultTableModel modelo;
    private JComboBox<Obstetra> cbxObstetra;
    private JComboBox<Programa> cbxPrograma;
    private JSpinner fechaInicio, fechaFin;
    private JSpinner edadMinSpinner, edadMaxSpinner;
    private JButton btnFiltrar, btnExportarPDF, btnExportarExcel, btnTrimestral, btnAnual;
    private RegistroPruebaDAO registroDAO = new RegistroPruebaDAO();
    private ObstetraDAO obstetraDAO = new ObstetraDAO();
    private ProgramaDAO programaDAO = new ProgramaDAO();
    private PacienteDAO pacienteDAO = new PacienteDAO();

    public ReportePruebas() throws SQLException {
        setTitle("Reporte de Pruebas Realizadas");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        modelo = new DefaultTableModel(new String[]{"Fecha", "Paciente", "Edad", "Obstetra", "Programa", "Observaciones"}, 0);
        tabla = new JTable(modelo);

        JPanel filtros = crearPanelFiltros();
        JPanel exportPanel = crearPanelExportar();

        add(filtros, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(exportPanel, BorderLayout.SOUTH);

        cargarFiltrosPorDefecto();

        setVisible(true);
    }

    private JPanel crearPanelFiltros() throws SQLException {
        JPanel panel = new JPanel(new GridLayout(4, 4, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Filtros"));

        cbxObstetra = new JComboBox<>();
        cbxPrograma = new JComboBox<>();
        fechaInicio = new JSpinner(new SpinnerDateModel());
        fechaFin = new JSpinner(new SpinnerDateModel());

        fechaInicio.setEditor(new JSpinner.DateEditor(fechaInicio, "yyyy-MM-dd"));
        fechaFin.setEditor(new JSpinner.DateEditor(fechaFin, "yyyy-MM-dd"));

        edadMinSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 150, 1)); // edad mínima 0 a 150
        edadMaxSpinner = new JSpinner(new SpinnerNumberModel(150, 0, 150, 1)); // edad máxima 0 a 150

        btnFiltrar = new JButton("Filtrar");
        btnFiltrar.addActionListener(e -> {
            try {
                filtrar();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al filtrar: " + ex.getMessage());
            }
        });

        btnTrimestral = new JButton("Reporte Trimestral");
        btnTrimestral.addActionListener(e -> {
            try {
                generarReporteTrimestral();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al generar reporte: " + ex.getMessage());
            }
        });

        btnAnual = new JButton("Reporte Anual");
        btnAnual.addActionListener(e -> {
            try {
                generarReporteAnual();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al generar reporte: " + ex.getMessage());
            }
        });

        cargarObstetras();
        cargarProgramas();

        panel.add(new JLabel("Obstetra:"));       panel.add(cbxObstetra);
        panel.add(new JLabel("Programa:"));       panel.add(cbxPrograma);
        panel.add(new JLabel("Desde:"));          panel.add(fechaInicio);
        panel.add(new JLabel("Hasta:"));           panel.add(fechaFin);
        panel.add(new JLabel("Edad Mínima:"));    panel.add(edadMinSpinner);
        panel.add(new JLabel("Edad Máxima:"));    panel.add(edadMaxSpinner);
        panel.add(btnFiltrar);
        panel.add(btnTrimestral);
        panel.add(btnAnual);

        return panel;
    }

    private JPanel crearPanelExportar() {
        JPanel panel = new JPanel();
        btnExportarPDF = new JButton("Exportar a PDF");
        btnExportarExcel = new JButton("Exportar a Excel");

        btnExportarPDF.addActionListener(e -> exportarPDF());
        btnExportarExcel.addActionListener(e -> exportarExcel());

        panel.add(btnExportarPDF);
        panel.add(btnExportarExcel);
        return panel;
    }

    private void cargarObstetras() throws SQLException {
        cbxObstetra.removeAllItems();
        cbxObstetra.addItem(null);
        for (Obstetra o : obstetraDAO.listar()) {
            cbxObstetra.addItem(o);
        }
    }

    private void cargarProgramas() throws SQLException {
        cbxPrograma.removeAllItems();
        cbxPrograma.addItem(null);
        for (Programa p : programaDAO.listar()) {
            cbxPrograma.addItem(p);
        }
    }

    private void cargarFiltrosPorDefecto() throws SQLException {
        // Cargar rango de fechas: desde el primer día del año hasta hoy
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, 1);
        fechaInicio.setValue(cal.getTime());
        fechaFin.setValue(new Date());

        // Edad mínima y máxima por defecto
        edadMinSpinner.setValue(0);
        edadMaxSpinner.setValue(150);

        // Seleccionar nada en los combobox
        cbxObstetra.setSelectedItem(null);
        cbxPrograma.setSelectedItem(null);

        // Mostrar todos los registros al iniciar
        filtrar();
    }

    private void filtrar() throws SQLException {
        modelo.setRowCount(0);
        Obstetra obstetra = (Obstetra) cbxObstetra.getSelectedItem();
        Programa programa = (Programa) cbxPrograma.getSelectedItem();
        Date desde = (Date) fechaInicio.getValue();
        Date hasta = (Date) fechaFin.getValue();

        int edadMin = (int) edadMinSpinner.getValue();
        int edadMax = (int) edadMaxSpinner.getValue();

        // Validar rango de edades
        if (edadMin > edadMax) {
            JOptionPane.showMessageDialog(this, "La edad mínima no puede ser mayor que la máxima.");
            return;
        }

        List<RegistroPrueba> lista = registroDAO.filtrar(obstetra, programa,
                new java.sql.Date(desde.getTime()), new java.sql.Date(hasta.getTime()),
                edadMin, edadMax);

        llenarTabla(lista);
    }

    private void generarReporteTrimestral() throws SQLException {
        modelo.setRowCount(0);
        Date hoy = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(hoy);

        int mes = cal.get(Calendar.MONTH);
        int trimestre = mes / 3;
        cal.set(Calendar.MONTH, trimestre * 3);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date fechaInicioTrimestre = cal.getTime();

        cal.add(Calendar.MONTH, 3);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date fechaFinTrimestre = cal.getTime();

        // Para reporte trimestral, no filtro edad (0-150)
        List<RegistroPrueba> lista = registroDAO.filtrar(null, null,
                new java.sql.Date(fechaInicioTrimestre.getTime()), new java.sql.Date(fechaFinTrimestre.getTime()),
                0, 150);

        llenarTabla(lista);
    }

    private void generarReporteAnual() throws SQLException {
        modelo.setRowCount(0);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, 1);
        Date fechaInicioAnual = cal.getTime();

        cal.set(Calendar.MONTH, 11); // diciembre
        cal.set(Calendar.DAY_OF_MONTH, 31);
        Date fechaFinAnual = cal.getTime();

        // Para reporte anual, no filtro edad (0-150)
        List<RegistroPrueba> lista = registroDAO.filtrar(null, null,
                new java.sql.Date(fechaInicioAnual.getTime()), new java.sql.Date(fechaFinAnual.getTime()),
                0, 150);

        llenarTabla(lista);
    }

    private void llenarTabla(List<RegistroPrueba> lista) throws SQLException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (RegistroPrueba r : lista) {
            Paciente p = pacienteDAO.buscarPorId(r.getIdPaciente());
            Obstetra o = obstetraDAO.buscarPorId(r.getIdObstetra());
            Programa prog = programaDAO.buscarPorId(r.getIdPrograma());

            modelo.addRow(new Object[]{
                    sdf.format(r.getFecha()),
                    p != null ? p.getNombre() + " " + p.getApellido() : "[Paciente]",
                    r.getEdad(),  // Edad
                    o != null ? o.getNombre() + " " + o.getApellido() : "[Obstetra]",
                    prog != null ? prog.getNombre() : "[Programa]",
                    r.getObservaciones()
            });
        }
    }

    private void exportarPDF() {
        try {
            Document doc = new Document();
            JFileChooser chooser = new JFileChooser();
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                PdfWriter.getInstance(doc, new FileOutputStream(chooser.getSelectedFile() + ".pdf"));
                doc.open();
                doc.add(new Paragraph("Reporte de Pruebas"));
                PdfPTable table = new PdfPTable(modelo.getColumnCount());
                for (int i = 0; i < modelo.getColumnCount(); i++) {
                    table.addCell(new PdfPCell(new Phrase(modelo.getColumnName(i))));
                }
                for (int i = 0; i < modelo.getRowCount(); i++) {
                    for (int j = 0; j < modelo.getColumnCount(); j++) {
                        table.addCell(modelo.getValueAt(i, j).toString());
                    }
                }
                doc.add(table);
                doc.close();
                JOptionPane.showMessageDialog(this, "PDF generado correctamente.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al exportar a PDF: " + e.getMessage());
        }
    }

    private void exportarExcel() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Reporte de Pruebas");
            Row header = sheet.createRow(0);
            for (int i = 0; i < modelo.getColumnCount(); i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(modelo.getColumnName(i));
            }
            for (int i = 0; i < modelo.getRowCount(); i++) {
                Row row = sheet.createRow(i + 1);
                for (int j = 0; j < modelo.getColumnCount(); j++) {
                    Cell cell = row.createCell(j);
                    Object value = modelo.getValueAt(i, j);
                    if (value instanceof Integer) {
                        cell.setCellValue((Integer) value);
                    } else {
                        cell.setCellValue(value != null ? value.toString() : "");
                    }
                }
            }
            JFileChooser chooser = new JFileChooser();
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try (FileOutputStream out = new FileOutputStream(chooser.getSelectedFile() + ".xlsx")) {
                    workbook.write(out);
                }
                JOptionPane.showMessageDialog(this, "Excel generado correctamente.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al exportar a Excel: " + e.getMessage());
        }
    }
}
