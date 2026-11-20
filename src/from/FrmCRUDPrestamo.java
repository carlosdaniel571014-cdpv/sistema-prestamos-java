package from;

import exs.ADExcepcion;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
import ln.Cuota;
import ln.Prestamo;
import ln.Trabajador;


public class FrmCRUDPrestamo extends javax.swing.JFrame {
    DefaultTableModel model;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmCRUDPrestamo.class.getName());
    Trabajador trabajador;

    public FrmCRUDPrestamo(){
        initComponents();
        model = new DefaultTableModel(new String[]{"ID", "DNI", "Cliente", "Trabajador", "Monto Solicitado", "Monto Aprobado", "Estado", "Fecha Solicitud", "Fecha Aprobación"}, 0);
        tblPrestamo.setModel(model);
    }

    public FrmCRUDPrestamo(Trabajador tra){
        initComponents();
        model = new DefaultTableModel(new String[]{"ID", "DNI", "Cliente", "Trabajador", "Monto Solicitado", "Monto Aprobado", "Estado", "Fecha Solicitud", "Fecha Aprobación"}, 0);
        this.trabajador = tra;
        Prestamo obj = new Prestamo();
        ResultSet rs;
        try{
            tblPrestamo.setModel(model);
            tblPrestamo.getColumnModel().getColumn(0).setPreferredWidth(40);
            tblPrestamo.getColumnModel().getColumn(1).setPreferredWidth(80);
            tblPrestamo.getColumnModel().getColumn(2).setPreferredWidth(200);
            tblPrestamo.getColumnModel().getColumn(3).setPreferredWidth(200);
            tblPrestamo.getColumnModel().getColumn(4).setPreferredWidth(120);
            tblPrestamo.getColumnModel().getColumn(5).setPreferredWidth(120);
            tblPrestamo.getColumnModel().getColumn(6).setPreferredWidth(150);
            tblPrestamo.getColumnModel().getColumn(7).setPreferredWidth(120);
            tblPrestamo.getColumnModel().getColumn(8).setPreferredWidth(120);
            rs = obj.buscarPrestamos("");
            llenarTabla(rs);
        }catch(ADExcepcion e){
            msjTabla.setText(e.getMessage());
        }
    }
    
    public void llenarTabla(ResultSet rs) {
        try {
            model.setRowCount(0);
            while (rs.next()) {
                int    id         = rs.getInt("id_prestamo");
                String dni        = nulo(rs.getString("dni"));
                String cliente    = rs.getString("cli_nombres") + " " + rs.getString("cli_apellidos");
                String trabajador = rs.getString("trab_nombres") + " " + rs.getString("trab_apellidos");
                double montoSolic = rs.getDouble("monto_solicitado");
                double mAprob = rs.getDouble("monto_aprobado");
                String montoAprob = rs.wasNull() ? "--" : String.valueOf(mAprob);
                String estado     = rs.getString("estado");
                String fechaSolic = rs.getString("fecha_solicitud");
                String fechaAprob = nulo(rs.getString("fecha_aprobacion"));
                model.addRow(new Object[]{id, dni, cliente, trabajador,
                                          montoSolic, montoAprob, estado,
                                          fechaSolic, fechaAprob});
            }
        } catch (SQLException e) {
            msjTabla.setText(e.getMessage());
        }
    }

    private String nulo(String v) {
        return v == null ? "--" : v;
    }
    
    public void limpiarMsj(){
        msjBusqueda.setText("");
        msjTabla.setText("");
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlPrestamo = new javax.swing.JPanel();
        h1Titulo = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        msjBusqueda = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JButton();
        btnPagar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        spr1 = new javax.swing.JSeparator();
        btnVerMoras = new javax.swing.JButton();
        btnAprobar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPrestamo = new javax.swing.JTable();
        btnCerrar = new javax.swing.JButton();
        msjTabla = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        pnlPrestamo.setBackground(new java.awt.Color(255, 255, 255));

        h1Titulo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        h1Titulo.setText("PRÉSTAMO");

        txtBuscar.addActionListener(this::txtBuscarActionPerformed);
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarKeyPressed(evt);
            }
        });

        msjBusqueda.setForeground(java.awt.Color.red);

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(this::btnBuscarActionPerformed);

        btnPagar.setText("Pagar");
        btnPagar.addActionListener(this::btnPagarActionPerformed);

        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(this::btnNuevoActionPerformed);

        spr1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        btnVerMoras.setText("Mora");
        btnVerMoras.addActionListener(this::btnVerMorasActionPerformed);

        btnAprobar.setText("Aprobar");
        btnAprobar.addActionListener(this::btnAprobarActionPerformed);

        javax.swing.GroupLayout pnlPrestamoLayout = new javax.swing.GroupLayout(pnlPrestamo);
        pnlPrestamo.setLayout(pnlPrestamoLayout);
        pnlPrestamoLayout.setHorizontalGroup(
            pnlPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPrestamoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPrestamoLayout.createSequentialGroup()
                        .addComponent(msjBusqueda, javax.swing.GroupLayout.DEFAULT_SIZE, 622, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscar))
                    .addGroup(pnlPrestamoLayout.createSequentialGroup()
                        .addComponent(h1Titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txtBuscar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spr1, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                    .addComponent(btnAprobar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnVerMoras, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPagar, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12))
        );
        pnlPrestamoLayout.setVerticalGroup(
            pnlPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPrestamoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPrestamoLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(pnlPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnNuevo)
                            .addComponent(btnPagar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnVerMoras)
                            .addComponent(btnAprobar)))
                    .addComponent(spr1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlPrestamoLayout.createSequentialGroup()
                        .addComponent(h1Titulo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(msjBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBuscar, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblPrestamo.setModel(new javax.swing.table.DefaultTableModel(
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
        tblPrestamo.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(tblPrestamo);

        btnCerrar.setText("Cerrar");
        btnCerrar.addActionListener(this::btnCerrarActionPerformed);

        msjTabla.setForeground(java.awt.Color.red);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 898, Short.MAX_VALUE)
                    .addComponent(pnlPrestamo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(msjTabla, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCerrar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlPrestamo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCerrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(msjTabla, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(8, 8, 8))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        FrmSolicitudPrestamo objfrm = new FrmSolicitudPrestamo(this.trabajador);
        objfrm.setVisible(true);
        objfrm.setLocationRelativeTo(null);
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        try{
            limpiarMsj();
            ResultSet rs;
            Prestamo obj = new Prestamo();
            if ("".equals(this.txtBuscar.getText())){
                rs = obj.buscarPrestamos("");
            }else{
                rs = obj.buscarPrestamos(this.txtBuscar.getText());
            }
            llenarTabla(rs);
        }catch(ADExcepcion e){
            msjBusqueda.setText(e.getMessage());
        }
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnPagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPagarActionPerformed
        int fila = tblPrestamo.getSelectedRow();
        if (fila != -1) {
            String estado = tblPrestamo.getValueAt(fila, 6).toString();
            if (!"DESEMBOLSADO".equals(estado)) {
                msjBusqueda.setText("Solo préstamos DESEMBOLSADO pueden pagarse");
                return;
            }
            int idPrestamo = Integer.parseInt(
                    tblPrestamo.getValueAt(fila, 0).toString()
            );
            FrmPago frm = new FrmPago(idPrestamo, this.trabajador.getIdTrabajador());
            frm.setVisible(true);
            btnBuscar.doClick();
        }else{
            msjBusqueda.setText("Seleccione un Préstamo");
        }
        
    }//GEN-LAST:event_btnPagarActionPerformed

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        btnBuscar.doClick();
    }//GEN-LAST:event_txtBuscarActionPerformed

    private void txtBuscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyPressed

    }//GEN-LAST:event_txtBuscarKeyPressed

    private void btnVerMorasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerMorasActionPerformed
    int fila = tblPrestamo.getSelectedRow();
    if (fila != -1) {
        int idPrestamo = Integer.parseInt(
                tblPrestamo.getValueAt(fila, 0).toString()
        );
        FrmMora frm = new FrmMora(idPrestamo);
        frm.setVisible(true);
        frm.setLocationRelativeTo(null);
    } else if (!txtBuscar.getText().trim().isEmpty()) {
        try {
            int idPrestamo = Integer.parseInt(txtBuscar.getText().trim());
            FrmMora frm = new FrmMora(idPrestamo);
            frm.setVisible(true);
            frm.setLocationRelativeTo(null);
        } catch (NumberFormatException e) {
            msjBusqueda.setText("ID de préstamo inválido.");
        }
    } else {
        msjBusqueda.setText("Seleccione un Préstamo o ingrese un ID.");
    }
    }//GEN-LAST:event_btnVerMorasActionPerformed

    private void btnAprobarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAprobarActionPerformed
        int fila = tblPrestamo.getSelectedRow();
        if (fila != -1) {
            String estado = tblPrestamo.getValueAt(fila, 6).toString();
            if (!"PENDIENTE".equals(estado)) {
                msjBusqueda.setText("Solo préstamos PENDIENTE pueden aprobarse");
                return;
            }
            int idPrestamo = Integer.parseInt(
                    tblPrestamo.getValueAt(fila, 0).toString()
            );
            FrmGestionPrestamo frm = new FrmGestionPrestamo(idPrestamo, this.trabajador);
            frm.setVisible(true);
            frm.setLocationRelativeTo(null);
        }else{
            msjBusqueda.setText("Seleccione un Préstamo");
        }
    }//GEN-LAST:event_btnAprobarActionPerformed


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
        java.awt.EventQueue.invokeLater(() -> new FrmCRUDPrestamo().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAprobar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnPagar;
    private javax.swing.JButton btnVerMoras;
    private javax.swing.JLabel h1Titulo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel msjBusqueda;
    private javax.swing.JLabel msjTabla;
    private javax.swing.JPanel pnlPrestamo;
    private javax.swing.JSeparator spr1;
    private javax.swing.JTable tblPrestamo;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
