/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package from;

/**
 *
 * @author PEÑA
 */
import ln.Reporte;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import java.awt.*;
import java.awt.print.*;;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class FrmReporteCartera extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmReporteCartera.class.getName());
    
      // Datos del gráfico
    private double montoAprobado   = 0;
    private double montoPendiente  = 0;
    private double montoRechazado  = 0;
    private int    cantAprobado    = 0;
    private int    cantPendiente   = 0;
    private int    cantRechazado   = 0;
    /**
     * Creates new form FrmReporteCartera
     */
    public FrmReporteCartera() {
        initComponents();
      setTitle("Reporte - Cartera de Préstamos");
        setLocationRelativeTo(null);
        setSize(960, 645);
        // Registrar el repintado del panel gráfico
        pnlGrafico.addComponentListener(new java.awt.event.ComponentAdapter() {});
        // Se usa un Timer para repintar cuando carguen los datos
        cargarDatos(); 
        
    }
    
    private void cargarDatos() {
        try {
            Reporte obj = new Reporte();
            ResultSet rs = obj.obtenerResumenCartera();
            montoAprobado = montoPendiente = montoRechazado = 0;
            cantAprobado  = cantPendiente  = cantRechazado  = 0;

            while (rs.next()) {
                String estado = rs.getString("estado");
                double monto  = rs.getDouble("monto_total");
                int    cant   = rs.getInt("cantidad");
                switch (estado) {
                    case "DESEMBOLSADO":  montoAprobado  = monto; cantAprobado  = cant; break;
                    case "PENDIENTE": montoPendiente = monto; cantPendiente = cant; break;
                    case "RECHAZADO": montoRechazado = monto; cantRechazado = cant; break;
                }
            }
            NumberFormat fmt = NumberFormat.getInstance(new Locale("es", "PE"));
            fmt.setMinimumFractionDigits(2);
            fmt.setMaximumFractionDigits(2);

            lblMontoAprobado.setText("S/. " + fmt.format(montoAprobado)
                + "  (" + cantAprobado + " préstamo" + (cantAprobado != 1 ? "s" : "") + ")");
            lblMontoPendiente.setText("S/. " + fmt.format(montoPendiente)
                + "  (" + cantPendiente + " préstamo" + (cantPendiente != 1 ? "s" : "") + ")");
            lblMontoRechazado.setText("S/. " + fmt.format(montoRechazado)
                + "  (" + cantRechazado + " préstamo" + (cantRechazado != 1 ? "s" : "") + ")");

            pnlGrafico.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar datos:\n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void dibujarBarras(Graphics2D g2, int w, int h) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int marginLeft = 80, marginRight = 20, marginTop = 50, marginBottom = 45;
        int areaW = w - marginLeft - marginRight;
        int areaH = h - marginTop - marginBottom;

        double maxMonto = Math.max(montoAprobado, Math.max(montoPendiente, montoRechazado));
        if (maxMonto == 0) maxMonto = 1;

        String[] etiquetas = {"Desembolsados", "Pendientes", "Rechazados"};
        double[] montos    = {montoAprobado, montoPendiente, montoRechazado};
        Color[]  colores   = {new Color(39,174,96), new Color(243,156,18), new Color(231,76,60)};

        int barWidth = areaW / 6;
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
            g2.drawString("S/." + fmt.format(maxMonto * i / 4), 2, y + 4);
        }

        // Ejes
        g2.setColor(Color.DARK_GRAY);
        g2.drawLine(marginLeft, marginTop, marginLeft, marginTop + areaH);
        g2.drawLine(marginLeft, marginTop + areaH, marginLeft + areaW, marginTop + areaH);

        // Barras
        for (int i = 0; i < 3; i++) {
            int barH = (int)(areaH * montos[i] / maxMonto);
            int x    = marginLeft + gap + i * (barWidth + gap * 2);
            int y    = marginTop + areaH - barH;

            g2.setColor(colores[i]);
            g2.fillRoundRect(x, y, barWidth, barH, 6, 6);

            // Valor encima
            g2.setColor(Color.DARK_GRAY);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
            String val = "S/." + fmt.format(montos[i]);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(val, x + (barWidth - fm.stringWidth(val)) / 2, y - 5);

            // Etiqueta debajo
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            FontMetrics fm2 = g2.getFontMetrics();
            g2.drawString(etiquetas[i],
                x + (barWidth - fm2.stringWidth(etiquetas[i])) / 2,
                marginTop + areaH + 18);
        }

        // Título
        g2.setColor(new Color(34, 85, 136));
        g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        g2.drawString("Monto por estado de préstamo (S/.)", marginLeft + 5, marginTop - 15);
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
        lblTitulo = new javax.swing.JLabel();
        pnlGrafico = new javax.swing.JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                dibujarBarras((java.awt.Graphics2D) g, getWidth(), getHeight());
            }
        };
        pnlResumen = new javax.swing.JPanel();
        lblTitAprobado = new javax.swing.JLabel();
        lblMontoAprobado = new javax.swing.JLabel();
        lblTitPendiente = new javax.swing.JLabel();
        lblMontoPendiente = new javax.swing.JLabel();
        lblTitRechazado = new javax.swing.JLabel();
        lblMontoRechazado = new javax.swing.JLabel();
        btnActualizar = new javax.swing.JButton();
        btnExportarPDF = new javax.swing.JButton();
        btnExportarExcel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(22, 85, 136));

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("REPORTE DE CARTERA DE PRÉSTAMOS");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(238, 238, 238))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblTitulo)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pnlGrafico.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pnlGraficoLayout = new javax.swing.GroupLayout(pnlGrafico);
        pnlGrafico.setLayout(pnlGraficoLayout);
        pnlGraficoLayout.setHorizontalGroup(
            pnlGraficoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlGraficoLayout.setVerticalGroup(
            pnlGraficoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 320, Short.MAX_VALUE)
        );

        pnlResumen.setBackground(new java.awt.Color(255, 255, 255));

        lblTitAprobado.setText("Cartera Colocada (Desembolsados):");

        lblMontoAprobado.setText("S/. 0.00");

        lblTitPendiente.setText("Capital en Evaluación (Pendientes):");

        lblMontoPendiente.setText("S/. 0.00");

        lblTitRechazado.setText("Costo de Oportunidad (Rechazados):");

        lblMontoRechazado.setText("S/. 0.00");

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
                .addGap(52, 52, 52)
                .addGroup(pnlResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblTitAprobado)
                    .addComponent(lblTitPendiente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTitRechazado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(25, 25, 25)
                .addGroup(pnlResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblMontoAprobado, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                    .addComponent(lblMontoPendiente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblMontoRechazado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                .addComponent(btnActualizar)
                .addGap(42, 42, 42)
                .addComponent(btnExportarPDF)
                .addGap(36, 36, 36)
                .addComponent(btnExportarExcel)
                .addGap(38, 38, 38))
        );
        pnlResumenLayout.setVerticalGroup(
            pnlResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlResumenLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(pnlResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitAprobado)
                    .addComponent(lblMontoAprobado))
                .addGap(29, 29, 29)
                .addGroup(pnlResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitPendiente)
                    .addComponent(lblMontoPendiente)
                    .addComponent(btnActualizar)
                    .addComponent(btnExportarPDF)
                    .addComponent(btnExportarExcel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addGroup(pnlResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitRechazado)
                    .addComponent(lblMontoRechazado))
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnlResumen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlGrafico, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlGrafico, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlResumen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        // TODO add your handling code here:
           cargarDatos();
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnExportarPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportarPDFActionPerformed
    String marca = new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new java.util.Date());
    JFileChooser fc = new JFileChooser();
    fc.setDialogTitle("Guardar Reporte PDF");
    fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivo PDF (*.pdf)", "pdf"));
    fc.setSelectedFile(new java.io.File("Reporte_Cartera_" + marca + ".pdf"));
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
            "REPORTE DE CARTERA DE PRÉSTAMOS", fTitulo);
        titulo.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
        titulo.setSpacingAfter(10);

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
        doc.add(new com.itextpdf.text.Paragraph(
            "Generado: " + new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm")
                .format(new java.util.Date()), fFecha));
        doc.add(com.itextpdf.text.Chunk.NEWLINE);

        // Gráfico como imagen
        int imgW = 700, imgH = 280;
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

        // Tabla resumen
        NumberFormat fmt = NumberFormat.getInstance(new Locale("es", "PE"));
        fmt.setMinimumFractionDigits(2); fmt.setMaximumFractionDigits(2);

        com.itextpdf.text.pdf.PdfPTable tabla = new com.itextpdf.text.pdf.PdfPTable(3);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{4f, 1.5f, 2f});

        com.itextpdf.text.Font fHead = new com.itextpdf.text.Font(
            com.itextpdf.text.Font.FontFamily.HELVETICA, 11,
            com.itextpdf.text.Font.BOLD, com.itextpdf.text.BaseColor.WHITE);
        for (String h : new String[]{"Concepto", "Cantidad", "Monto Total"}) {
            com.itextpdf.text.pdf.PdfPCell c = new com.itextpdf.text.pdf.PdfPCell(
                new com.itextpdf.text.Phrase(h, fHead));
            c.setBackgroundColor(new com.itextpdf.text.BaseColor(22, 85, 136));
            c.setPadding(6);
            tabla.addCell(c);
        }

        com.itextpdf.text.Font fFila = new com.itextpdf.text.Font(
            com.itextpdf.text.Font.FontFamily.HELVETICA, 10);
        Object[][] filas = {
            {"Cartera Colocada (Desembolsados)",   cantAprobado,  montoAprobado,  new com.itextpdf.text.BaseColor(198,239,206)},
            {"Capital en Evaluación (Pendientes)", cantPendiente, montoPendiente, new com.itextpdf.text.BaseColor(255,235,156)},
            {"Costo de Oportunidad (Rechazados)",  cantRechazado, montoRechazado, new com.itextpdf.text.BaseColor(255,199,206)}
        };
        for (Object[] fila : filas) {
            com.itextpdf.text.BaseColor bg = (com.itextpdf.text.BaseColor) fila[3];
            com.itextpdf.text.pdf.PdfPCell c1 = new com.itextpdf.text.pdf.PdfPCell(
                new com.itextpdf.text.Phrase(fila[0].toString(), fFila));
            com.itextpdf.text.pdf.PdfPCell c2 = new com.itextpdf.text.pdf.PdfPCell(
                new com.itextpdf.text.Phrase(fila[1].toString(), fFila));
            com.itextpdf.text.pdf.PdfPCell c3 = new com.itextpdf.text.pdf.PdfPCell(
                new com.itextpdf.text.Phrase("S/. " + fmt.format((double)fila[2]), fFila));
            c1.setBackgroundColor(bg); c1.setPadding(5);
            c2.setBackgroundColor(bg); c2.setPadding(5);
            c3.setBackgroundColor(bg); c3.setPadding(5);
            tabla.addCell(c1); tabla.addCell(c2); tabla.addCell(c3);
        }

        // Total general
        double total = montoAprobado + montoPendiente + montoRechazado;
        int cantTotal = cantAprobado + cantPendiente + cantRechazado;
        com.itextpdf.text.Font fTotal = new com.itextpdf.text.Font(
            com.itextpdf.text.Font.FontFamily.HELVETICA, 11,
            com.itextpdf.text.Font.BOLD, new com.itextpdf.text.BaseColor(22, 85, 136));
        for (String txt : new String[]{"TOTAL GENERAL", String.valueOf(cantTotal), "S/. " + fmt.format(total)}) {
            com.itextpdf.text.pdf.PdfPCell c = new com.itextpdf.text.pdf.PdfPCell(
                new com.itextpdf.text.Phrase(txt, fTotal));
            c.setBackgroundColor(new com.itextpdf.text.BaseColor(217, 225, 242));
            c.setPadding(6);
            tabla.addCell(c);
        }

        doc.add(tabla);
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
    String rutaArchivo = "Reporte_Cartera_" + marca + ".xls";

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

        pw.println("<tr><td colspan='3' style='background-color:#003366; color:white; font-size:16px; font-weight:bold; text-align:center;'>"
                + "REPORTE DE CARTERA DE PRÉSTAMOS</td></tr>");

        pw.println("<tr><td colspan='3' style='background-color:#D9E1F2;'>"
                + "<b>Fecha generación:</b> "
                + new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date())
                + "</td></tr>");

        pw.println("<tr style='background-color:#336699; color:white; font-weight:bold;'>"
                + "<td>Concepto</td><td>Cantidad</td><td>Monto Total (S/)</td></tr>");

        java.text.NumberFormat fmt = java.text.NumberFormat.getInstance(new java.util.Locale("es", "PE"));
        fmt.setMinimumFractionDigits(2); fmt.setMaximumFractionDigits(2);

        String[][] filas = {
            {"Cartera Colocada (Desembolsados)", String.valueOf(cantAprobado),  fmt.format(montoAprobado),  "#C6EFCE"},
            {"Capital en Evaluación (Pendientes)", String.valueOf(cantPendiente), fmt.format(montoPendiente), "#FFEB9C"},
            {"Costo de Oportunidad (Rechazados)", String.valueOf(cantRechazado), fmt.format(montoRechazado), "#FFC7CE"}
        };
        for (String[] fila : filas) {
            pw.println("<tr style='background-color:" + fila[3] + ";'>"
                    + "<td>" + fila[0] + "</td>"
                    + "<td>" + fila[1] + "</td>"
                    + "<td>S/. " + fila[2] + "</td></tr>");
        }

        double total = montoAprobado + montoPendiente + montoRechazado;
        int cantTotal = cantAprobado + cantPendiente + cantRechazado;
        pw.println("<tr style='background-color:#D9E1F2; font-weight:bold;'>"
                + "<td>TOTAL GENERAL</td>"
                + "<td>" + cantTotal + "</td>"
                + "<td>S/. " + fmt.format(total) + "</td></tr>");

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
        java.awt.EventQueue.invokeLater(() -> new FrmReporteCartera().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnExportarExcel;
    private javax.swing.JButton btnExportarPDF;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblMontoAprobado;
    private javax.swing.JLabel lblMontoPendiente;
    private javax.swing.JLabel lblMontoRechazado;
    private javax.swing.JLabel lblTitAprobado;
    private javax.swing.JLabel lblTitPendiente;
    private javax.swing.JLabel lblTitRechazado;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel pnlGrafico;
    private javax.swing.JPanel pnlResumen;
    // End of variables declaration//GEN-END:variables
}
