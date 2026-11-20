/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package from;

import ln.Reporte;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
/**
 *
 * @author PEÑA
 */
public class FrmReporteRendimientoTrabajador extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmReporteRendimientoTrabajador.class.getName());

    /**
     * Creates new form FrmReporteRendimientoTrabajador
     */
    private String[] topTrabajadores = {};
    private double[] topMontos = {};
    private int      totalPrestamos  = 0;
    private double   montoTotal      = 0;

    public FrmReporteRendimientoTrabajador() {
    initComponents();
    setTitle("Reporte - Rendimiento por Trabajador");
    setSize(900, 770);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    tblRendimiento.setModel(new DefaultTableModel(
        new String[]{"Trabajador", "Cargo", "Préstamos Registrados",
                     "Préstamos Aprobados", "Monto Gestionado (S/)"}, 0));
    pnlGrafico.setLayout(new java.awt.BorderLayout());
    pnlGrafico.add(new javax.swing.JPanel() {
        @Override
        protected void paintComponent(java.awt.Graphics g) {
            super.paintComponent(g);
            dibujarBarras((java.awt.Graphics2D) g, getWidth(), getHeight());
        }
    }, java.awt.BorderLayout.CENTER);
    pnlGrafico.setPreferredSize(new java.awt.Dimension(860, 220));
    cargarDatos();
    }
    
   private void cargarDatos() {
    try {
        Reporte obj = new Reporte();
        ResultSet rs = obj.obtenerRendimientoTrabajadores();

        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Trabajador", "Cargo", "Préstamos Registrados",
                         "Préstamos Aprobados", "Monto Gestionado (S/)"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        totalPrestamos = 0;
        montoTotal     = 0;

        java.util.List<String> nombres = new java.util.ArrayList<>();
        java.util.List<Double> montos  = new java.util.ArrayList<>();

        NumberFormat fmt = NumberFormat.getInstance(new Locale("es", "PE"));
        fmt.setMinimumFractionDigits(2);
        fmt.setMaximumFractionDigits(2);

        while (rs.next()) {
            String trabajador  = rs.getString("trabajador");
            String cargo       = rs.getString("cargo");
            int    registrados = rs.getInt("prestamos_registrados");
            int    aprobados   = rs.getInt("prestamos_aprobados");
            double monto       = rs.getDouble("monto_gestionado");

            model.addRow(new Object[]{
                trabajador, cargo, registrados, aprobados,
                "S/. " + fmt.format(monto)
            });

            totalPrestamos += aprobados;
            montoTotal     += monto;

            if (nombres.size() < 5) {
                String nombreCorto = trabajador.length() > 15
                    ? trabajador.substring(0, 15) + "." : trabajador;
                nombres.add(nombreCorto);
                montos.add(monto);
            }
        }

        tblRendimiento.setModel(model);
        topTrabajadores = nombres.toArray(new String[0]);
        topMontos       = montos.stream().mapToDouble(Double::doubleValue).toArray();

        lblTotalPrest.setText(String.valueOf(totalPrestamos));
        lblMontoTotal.setText("S/. " + fmt.format(montoTotal));

        pnlGrafico.repaint();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error al cargar datos:\n" + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
 private void dibujarBarras(Graphics2D g2, int w, int h) {
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    if (topTrabajadores.length == 0) {
        g2.setColor(Color.GRAY);
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        g2.drawString("Sin datos disponibles", w / 2 - 90, h / 2);
        return;
    }

    int marginLeft = 90, marginRight = 20, marginTop = 50, marginBottom = 45;
    int areaW = w - marginLeft - marginRight;
    int areaH = h - marginTop - marginBottom;

    double maxVal = 0;
    for (double v : topMontos) if (v > maxVal) maxVal = v;
    if (maxVal == 0) maxVal = 1;

    Color[] colores = {
        new Color(52,152,219), new Color(39,174,96),
        new Color(243,156,18), new Color(155,89,182), new Color(231,76,60)
    };

    int n        = topTrabajadores.length;
    int barWidth = areaW / (n * 2 + 1);
    int gap      = barWidth / 2;

    NumberFormat fmt = NumberFormat.getInstance(new Locale("es", "PE"));
    fmt.setMinimumFractionDigits(0);
    fmt.setMaximumFractionDigits(0);

    // Líneas guía
    g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
    for (int i = 1; i <= 4; i++) {
        int y = marginTop + areaH - (areaH * i / 4);
        g2.setColor(new Color(220, 220, 220));
        g2.drawLine(marginLeft, y, marginLeft + areaW, y);
        g2.setColor(Color.DARK_GRAY);
        g2.drawString("S/." + fmt.format(maxVal * i / 4), 2, y + 4);
    }

    // Ejes
    g2.setColor(Color.DARK_GRAY);
    g2.drawLine(marginLeft, marginTop, marginLeft, marginTop + areaH);
    g2.drawLine(marginLeft, marginTop + areaH, marginLeft + areaW, marginTop + areaH);

    // Barras
    for (int i = 0; i < n; i++) {
        int barH = (int)(areaH * topMontos[i] / maxVal);
        int x    = marginLeft + gap + i * (barWidth + gap * 2);
        int y    = marginTop + areaH - barH;

        g2.setColor(colores[i % colores.length]);
        g2.fillRoundRect(x, y, barWidth, barH, 6, 6);

        // Valor encima
        g2.setColor(Color.DARK_GRAY);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
        String val = "S/." + fmt.format(topMontos[i]);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(val, x + (barWidth - fm.stringWidth(val)) / 2, y - 4);

        // Nombre debajo
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        FontMetrics fm2 = g2.getFontMetrics();
        g2.drawString(topTrabajadores[i],
            x + (barWidth - fm2.stringWidth(topTrabajadores[i])) / 2,
            marginTop + areaH + 16);
    }

    // Título
    g2.setColor(new Color(34, 85, 136));
    g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
    g2.drawString("Monto aprobado por trabajador (S/.)", marginLeft + 5, marginTop - 15);
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        pnlGrafico = new javax.swing.JPanel();
        pnlTabla = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblRendimiento = new javax.swing.JTable();
        pnlResumen = new javax.swing.JPanel();
        lblTitTotal = new javax.swing.JLabel();
        lblTotalPrest = new javax.swing.JLabel();
        lblTitMonto = new javax.swing.JLabel();
        lblMontoTotal = new javax.swing.JLabel();
        btnActualizar = new javax.swing.JButton();
        btnExportarPDF = new javax.swing.JButton();
        btnExportarExcel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(22, 85, 136));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("REPORTE DE RENDIMIENTO POR TRABAJADOR");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(266, 266, 266)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        pnlGrafico.setBackground(new java.awt.Color(255, 255, 255));
        pnlGrafico.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout pnlGraficoLayout = new javax.swing.GroupLayout(pnlGrafico);
        pnlGrafico.setLayout(pnlGraficoLayout);
        pnlGraficoLayout.setHorizontalGroup(
            pnlGraficoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlGraficoLayout.setVerticalGroup(
            pnlGraficoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 223, Short.MAX_VALUE)
        );

        pnlTabla.setBackground(new java.awt.Color(255, 255, 255));
        pnlTabla.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        tblRendimiento.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tblRendimiento);

        javax.swing.GroupLayout pnlTablaLayout = new javax.swing.GroupLayout(pnlTabla);
        pnlTabla.setLayout(pnlTablaLayout);
        pnlTablaLayout.setHorizontalGroup(
            pnlTablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTablaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        pnlTablaLayout.setVerticalGroup(
            pnlTablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTablaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlResumen.setBackground(new java.awt.Color(255, 255, 255));
        pnlResumen.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblTitTotal.setForeground(new java.awt.Color(0, 0, 0));
        lblTitTotal.setText("Total préstamos aprobados:");

        lblTotalPrest.setForeground(new java.awt.Color(0, 0, 0));
        lblTotalPrest.setText("0");

        lblTitMonto.setForeground(new java.awt.Color(0, 0, 0));
        lblTitMonto.setText("Monto gestionado:");

        lblMontoTotal.setForeground(new java.awt.Color(0, 0, 0));
        lblMontoTotal.setText("S/.0.00");

        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        btnExportarPDF.setText("Exportar PDF");
        btnExportarPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportarPDFActionPerformed(evt);
            }
        });

        btnExportarExcel.setText("Exportar Excel");
        btnExportarExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportarExcelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlResumenLayout = new javax.swing.GroupLayout(pnlResumen);
        pnlResumen.setLayout(pnlResumenLayout);
        pnlResumenLayout.setHorizontalGroup(
            pnlResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlResumenLayout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addGroup(pnlResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitMonto, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTitTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(82, 82, 82)
                .addGroup(pnlResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblMontoTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                    .addComponent(lblTotalPrest, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(382, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlResumenLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnActualizar)
                .addGap(30, 30, 30)
                .addComponent(btnExportarPDF)
                .addGap(32, 32, 32)
                .addComponent(btnExportarExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );
        pnlResumenLayout.setVerticalGroup(
            pnlResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlResumenLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(pnlResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitTotal)
                    .addComponent(lblTotalPrest))
                .addGap(36, 36, 36)
                .addGroup(pnlResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitMonto)
                    .addComponent(lblMontoTotal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addGroup(pnlResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnActualizar)
                    .addComponent(btnExportarPDF)
                    .addComponent(btnExportarExcel))
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlGrafico, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlTabla, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlResumen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlGrafico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlTabla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlResumen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        // TODO add your handling code here:
        cargarDatos();
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnExportarPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportarPDFActionPerformed
        // TODO add your handling code here:
String marca = new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new java.util.Date());
JFileChooser fc = new JFileChooser();
fc.setDialogTitle("Guardar Reporte PDF");
fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivo PDF (*.pdf)", "pdf"));
fc.setSelectedFile(new java.io.File("Reporte_Rendimiento_" + marca + ".pdf"));
if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

java.io.File destino = fc.getSelectedFile();
if (!destino.getName().toLowerCase().endsWith(".pdf"))
    destino = new java.io.File(destino.getAbsolutePath() + ".pdf");

try {
    com.itextpdf.text.Document doc = new com.itextpdf.text.Document();
    com.itextpdf.text.pdf.PdfWriter.getInstance(doc, new java.io.FileOutputStream(destino));
    doc.open();

    // Título
    com.itextpdf.text.Font fTitulo = new com.itextpdf.text.Font(
        com.itextpdf.text.Font.FontFamily.HELVETICA, 16,
        com.itextpdf.text.Font.BOLD, com.itextpdf.text.BaseColor.WHITE);
    com.itextpdf.text.Paragraph titulo = new com.itextpdf.text.Paragraph(
        "REPORTE DE RENDIMIENTO POR TRABAJADOR", fTitulo);
    titulo.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
    com.itextpdf.text.pdf.PdfPTable tblTitulo = new com.itextpdf.text.pdf.PdfPTable(1);
    tblTitulo.setWidthPercentage(100);
    com.itextpdf.text.pdf.PdfPCell celdaTitulo = new com.itextpdf.text.pdf.PdfPCell(titulo);
    celdaTitulo.setBackgroundColor(new com.itextpdf.text.BaseColor(22, 85, 136));
    celdaTitulo.setPadding(10);
    celdaTitulo.setBorder(com.itextpdf.text.pdf.PdfPCell.NO_BORDER);
    tblTitulo.addCell(celdaTitulo);
    doc.add(tblTitulo);

    // Fecha
    com.itextpdf.text.Font fFecha = new com.itextpdf.text.Font(
        com.itextpdf.text.Font.FontFamily.HELVETICA, 9,
        com.itextpdf.text.Font.NORMAL, com.itextpdf.text.BaseColor.GRAY);
    doc.add(new com.itextpdf.text.Paragraph("Generado: " +
        new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date()), fFecha));
    doc.add(com.itextpdf.text.Chunk.NEWLINE);

    // Gráfico
    int imgW = 700, imgH = 220;
    java.awt.image.BufferedImage buffImg = new java.awt.image.BufferedImage(
        imgW, imgH, java.awt.image.BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = buffImg.createGraphics();
    g2.setColor(Color.WHITE);
    g2.fillRect(0, 0, imgW, imgH);
    dibujarBarras(g2, imgW, imgH);
    g2.dispose();
    java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
    javax.imageio.ImageIO.write(buffImg, "png", baos);
    com.itextpdf.text.Image grafico = com.itextpdf.text.Image.getInstance(baos.toByteArray());
    grafico.scaleToFit(500, 200);
    grafico.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
    doc.add(grafico);
    doc.add(com.itextpdf.text.Chunk.NEWLINE);

    // Tabla
    com.itextpdf.text.pdf.PdfPTable tabla = new com.itextpdf.text.pdf.PdfPTable(5);
    tabla.setWidthPercentage(100);
    tabla.setWidths(new float[]{2.5f, 1.5f, 1.5f, 1.5f, 2f});
    com.itextpdf.text.Font fHead = new com.itextpdf.text.Font(
        com.itextpdf.text.Font.FontFamily.HELVETICA, 9,
        com.itextpdf.text.Font.BOLD, com.itextpdf.text.BaseColor.WHITE);
    for (String h : new String[]{"Trabajador","Cargo","Prest. Registrados",
                                  "Prest. Aprobados","Monto Gestionado (S/)"}) {
        com.itextpdf.text.pdf.PdfPCell c = new com.itextpdf.text.pdf.PdfPCell(
            new com.itextpdf.text.Phrase(h, fHead));
        c.setBackgroundColor(new com.itextpdf.text.BaseColor(22, 85, 136));
        c.setPadding(5);
        tabla.addCell(c);
    }
    com.itextpdf.text.Font fFila = new com.itextpdf.text.Font(
        com.itextpdf.text.Font.FontFamily.HELVETICA, 9);
    DefaultTableModel m = (DefaultTableModel) tblRendimiento.getModel();
    for (int i = 0; i < m.getRowCount(); i++) {
        com.itextpdf.text.BaseColor bg = i % 2 == 0
            ? com.itextpdf.text.BaseColor.WHITE
            : new com.itextpdf.text.BaseColor(240, 245, 255);
        for (int j = 0; j < 5; j++) {
            com.itextpdf.text.pdf.PdfPCell c = new com.itextpdf.text.pdf.PdfPCell(
                new com.itextpdf.text.Phrase(m.getValueAt(i, j).toString(), fFila));
            c.setBackgroundColor(bg);
            c.setPadding(4);
            tabla.addCell(c);
        }
    }
    doc.add(tabla);
    doc.add(com.itextpdf.text.Chunk.NEWLINE);

    // Resumen
    com.itextpdf.text.Font fRes = new com.itextpdf.text.Font(
        com.itextpdf.text.Font.FontFamily.HELVETICA, 11, com.itextpdf.text.Font.BOLD);
    doc.add(new com.itextpdf.text.Paragraph(
        "Total préstamos registrados: " + lblTotalPrest.getText(), fRes));
    doc.add(new com.itextpdf.text.Paragraph(
        "Monto total gestionado: " + lblMontoTotal.getText(), fRes));

    doc.close();

    JOptionPane.showMessageDialog(this, "PDF guardado en:\n" + destino.getAbsolutePath(),
        "Éxito", JOptionPane.INFORMATION_MESSAGE);
    if (java.awt.Desktop.isDesktopSupported())
        java.awt.Desktop.getDesktop().open(destino);

} catch (Exception e) {
    JOptionPane.showMessageDialog(this, "Error al generar PDF:\n" + e.getMessage(),
        "Error", JOptionPane.ERROR_MESSAGE);
}
    }//GEN-LAST:event_btnExportarPDFActionPerformed

    private void btnExportarExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportarExcelActionPerformed
        // TODO add your handling code here:
    String marca = new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new java.util.Date());
    String rutaArchivo = "Reporte_Rendimiento_Trabajador_" + marca + ".xls";

    javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
    fileChooser.setDialogTitle("Guardar reporte");
    fileChooser.setSelectedFile(new java.io.File(rutaArchivo));
    int seleccion = fileChooser.showSaveDialog(this);
    if (seleccion != javax.swing.JFileChooser.APPROVE_OPTION) return;
    rutaArchivo = fileChooser.getSelectedFile().getAbsolutePath();
    if (!rutaArchivo.endsWith(".xls")) rutaArchivo = rutaArchivo + ".xls";

    try (java.io.FileWriter fw = new java.io.FileWriter(rutaArchivo, java.nio.charset.StandardCharsets.UTF_8);
         java.io.PrintWriter pw = new java.io.PrintWriter(fw)) {

        pw.println("<html><head><meta charset='UTF-8'></head><body>");
        pw.println("<table border='1' cellpadding='5' cellspacing='0'>");

        pw.println("<tr><td colspan='5' style='background-color:#003366; color:white; font-size:16px; font-weight:bold; text-align:center;'>"
                + "REPORTE DE RENDIMIENTO POR TRABAJADOR</td></tr>");

        pw.println("<tr><td colspan='5' style='background-color:#D9E1F2;'>"
                + "<b>Fecha:</b> " + new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date())
                + " &nbsp;&nbsp; <b>Total préstamos aprobados:</b> " + lblTotalPrest.getText()
                + " &nbsp;&nbsp; <b>Monto gestionado:</b> " + lblMontoTotal.getText()
                + "</td></tr>");

        pw.println("<tr style='background-color:#336699; color:white; font-weight:bold;'>"
                + "<td>Trabajador</td><td>Cargo</td>"
                + "<td>Préstamos Registrados</td>"
                + "<td>Préstamos Aprobados</td>"
                + "<td>Monto Gestionado (S/)</td></tr>");

        javax.swing.table.DefaultTableModel m = (javax.swing.table.DefaultTableModel) tblRendimiento.getModel();
        for (int i = 0; i < m.getRowCount(); i++) {
            String bg = (i % 2 == 0) ? "#FFFFFF" : "#DCE6F1";
            pw.println("<tr style='background-color:" + bg + ";'>"
                    + "<td>" + m.getValueAt(i, 0) + "</td>"
                    + "<td>" + m.getValueAt(i, 1) + "</td>"
                    + "<td>" + m.getValueAt(i, 2) + "</td>"
                    + "<td>" + m.getValueAt(i, 3) + "</td>"
                    + "<td>" + m.getValueAt(i, 4) + "</td></tr>");
        }

        pw.println("</table></body></html>");

        javax.swing.JOptionPane.showMessageDialog(this, "Reporte generado correctamente:\n" + rutaArchivo, "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        java.awt.Desktop.getDesktop().open(new java.io.File(rutaArchivo));

    } catch (java.io.IOException e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Error al generar reporte:\n" + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnExportarExcelActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new FrmReporteRendimientoTrabajador().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnExportarExcel;
    private javax.swing.JButton btnExportarPDF;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblMontoTotal;
    private javax.swing.JLabel lblTitMonto;
    private javax.swing.JLabel lblTitTotal;
    private javax.swing.JLabel lblTotalPrest;
    private javax.swing.JPanel pnlGrafico;
    private javax.swing.JPanel pnlResumen;
    private javax.swing.JPanel pnlTabla;
    private javax.swing.JTable tblRendimiento;
    // End of variables declaration//GEN-END:variables
}
