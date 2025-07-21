package org.example.GUI;

import org.example.DAO.MetaObstetraDAO;
import org.example.DAO.ObstetraDAO;
import org.example.DAO.RegistroPruebaDAO;
import org.example.Entidades.MetaObstetra;
import org.example.Entidades.Programa;
import org.example.Utilidades.ControlSesionUsuario;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.util.List;

public class VerAvanceMetas extends JFrame {
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JSpinner spinnerAnio;  // Spinner para seleccionar año
    private JComboBox<String> comboMes;


    public VerAvanceMetas() {
        setTitle("Avance de Metas");
        setSize(800, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel superior para selector de año
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSuperior.add(new JLabel("Año:"));

        int anioActual = ControlSesionUsuario.getAnioActual();

        // Configurar modelo para el spinner: mínimo 2000, máximo 2100, paso 1
        SpinnerNumberModel modeloSpinner = new SpinnerNumberModel(anioActual, 2000, 2100, 1);
        spinnerAnio = new JSpinner(modeloSpinner);

        // Agregar ChangeListener para recargar datos al cambiar año
        spinnerAnio.addChangeListener(e -> cargarDatos());

        panelSuperior.add(spinnerAnio);

        panelSuperior.add(new JLabel("Mes:"));

        String[] meses = {
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        };

        comboMes = new JComboBox<>(meses);

        // Selecciona el mes actual
        int mesActual = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH);
        comboMes.setSelectedIndex(mesActual);

        // Listener para actualizar la tabla cuando se cambia el mes
        comboMes.addActionListener(e -> cargarDatos());

        panelSuperior.add(comboMes);


        add(panelSuperior, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel();
        modeloTabla.setColumnIdentifiers(new String[]{
                "<html>Programa</html>",
                "<html>N° de pruebas <br>realizadas en el mes</html>",
                "<html>Meta<br>Mensual</html>",
                "<html>Avance<br>Mensual (%)</html>",
                "<html>N° de pruebas <br> realizadas en el año</html>",
                "<html>Meta<br>Anual</html>",
                "<html>Avance<br>Anual (%)</html>",
                "<html>Año</html>"
        });

        tabla = new JTable(modeloTabla);
        tabla.getTableHeader().setPreferredSize(new Dimension(100, 50)); // Ajusta altura a 50 px
        tabla.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel();
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.CENTER);
                label.setFont(label.getFont().deriveFont(Font.BOLD));
                label.setOpaque(true);
                label.setBackground(UIManager.getColor("TableHeader.background"));
                label.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
                label.setText(value != null ? value.toString() : "");
                return label;
            }
        });



        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JButton btnExportarExcel = new JButton("Exportar a Excel");
        btnExportarExcel.addActionListener(e -> exportarAExcel());

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelInferior.add(btnExportarExcel);

        JButton btnExportarPDF = new JButton("Exportar a PDF");
        btnExportarPDF.addActionListener(e -> exportarAPDF());
        panelInferior.add(btnExportarPDF);

        add(panelInferior, BorderLayout.SOUTH);


        cargarDatos();

        setVisible(true);
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);

        RegistroPruebaDAO dao = new RegistroPruebaDAO();
        ObstetraDAO obstetraDAO = new ObstetraDAO();
        MetaObstetraDAO metaObstetraDAO = new MetaObstetraDAO();

        int idUsuario = ControlSesionUsuario.getUsuarioActual().getId();
        int idObstetra = obstetraDAO.obtenerIdObstetraPorIdUsuario(idUsuario);

        if (idObstetra == -1) {
            JOptionPane.showMessageDialog(this, "No se encontró el obstetra para el usuario actual");
            return;
        }

        int anioSeleccionado = (Integer) spinnerAnio.getValue();
        int mesSeleccionado = comboMes.getSelectedIndex() + 1; // Índice 0 = Enero → Mes 1


        List<Programa> programas = dao.listarProgramasPorObstetra(idObstetra);
        for (Programa p : programas) {
            int cantidadMensual = dao.contarPruebasPorObstetraYProgramaYAnioYMes(idObstetra, p.getIdPrograma(), anioSeleccionado, mesSeleccionado);
            int cantidadAnualAcumulada = dao.contarPruebasPorObstetraYProgramaYAnioHastaMes(idObstetra, p.getIdPrograma(), anioSeleccionado, mesSeleccionado);

            MetaObstetra meta = metaObstetraDAO.obtenerMetaObstetra(idObstetra, p.getIdPrograma(), anioSeleccionado);

            String metaMensualStr, avanceMensualStr, metaAnualStr, avanceAnualStr;

            if (meta == null) {
                metaMensualStr = "No definida";
                avanceMensualStr = "N/A";
                metaAnualStr = "No definida";
                avanceAnualStr = "N/A";
            } else {
                int metaMensual = meta.getMetaMensual();
                int metaAnual = meta.getMetaAnual();

                double avanceMensual = (metaMensual > 0) ? ((double) cantidadMensual / metaMensual) * 100 : 0;
                double avanceAnual = (metaAnual > 0) ? ((double) cantidadAnualAcumulada / metaAnual) * 100 : 0;

                metaMensualStr = String.valueOf(metaMensual);
                avanceMensualStr = String.format("%.2f%%", avanceMensual);
                metaAnualStr = String.valueOf(metaAnual);
                avanceAnualStr = String.format("%.2f%%", avanceAnual);
            }


            modeloTabla.addRow(new Object[]{
                    p.getNombre(),
                    cantidadMensual,
                    metaMensualStr,
                    avanceMensualStr,
                    cantidadAnualAcumulada,
                    metaAnualStr,
                    avanceAnualStr,
                    anioSeleccionado
            });
        }
    }

    private void exportarAExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar como Excel");
        fileChooser.setSelectedFile(new java.io.File("avance_metas.xlsx"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection != JFileChooser.APPROVE_OPTION) return;

        try (org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook()) {
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Avance Metas");

            // Crear encabezados
            org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
            for (int i = 0; i < modeloTabla.getColumnCount(); i++) {
                header.createCell(i).setCellValue(modeloTabla.getColumnName(i));
            }

            // Agregar datos
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(i + 1);
                for (int j = 0; j < modeloTabla.getColumnCount(); j++) {
                    Object value = modeloTabla.getValueAt(i, j);
                    row.createCell(j).setCellValue(value != null ? value.toString() : "");
                }
            }

            try (java.io.FileOutputStream out = new java.io.FileOutputStream(fileChooser.getSelectedFile())) {
                workbook.write(out);
                JOptionPane.showMessageDialog(this, "Archivo Excel guardado correctamente.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al exportar: " + e.getMessage());
        }
    }

    private void exportarAPDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar como PDF");
        fileChooser.setSelectedFile(new java.io.File("avance_metas.pdf"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection != JFileChooser.APPROVE_OPTION) return;

        try {
            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(document, new java.io.FileOutputStream(fileChooser.getSelectedFile()));
            document.open();

            com.itextpdf.text.pdf.PdfPTable pdfTable = new com.itextpdf.text.pdf.PdfPTable(modeloTabla.getColumnCount());
            pdfTable.setWidthPercentage(100);

            // Encabezados
            for (int i = 0; i < modeloTabla.getColumnCount(); i++) {
                pdfTable.addCell(new com.itextpdf.text.Phrase(modeloTabla.getColumnName(i)));
            }

            // Datos
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                for (int j = 0; j < modeloTabla.getColumnCount(); j++) {
                    Object value = modeloTabla.getValueAt(i, j);
                    pdfTable.addCell(new com.itextpdf.text.Phrase(value != null ? value.toString() : ""));
                }
            }

            document.add(new com.itextpdf.text.Paragraph("Avance de Metas"));
            document.add(new com.itextpdf.text.Paragraph("Año: " + spinnerAnio.getValue() + " | Mes: " + comboMes.getSelectedItem()));
            document.add(new com.itextpdf.text.Paragraph(" "));
            document.add(pdfTable);
            document.close();

            JOptionPane.showMessageDialog(this, "PDF exportado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al exportar PDF: " + e.getMessage());
        }
    }


}
