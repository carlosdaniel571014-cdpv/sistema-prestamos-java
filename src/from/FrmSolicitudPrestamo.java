package from;

import java.awt.Color;
import javax.swing.JOptionPane;
import ln.Cliente;
import ln.Prestamo;
import ln.Trabajador;


public class FrmSolicitudPrestamo extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmSolicitudPrestamo.class.getName());
    private boolean aprovado = false;
    private Cliente cliente;
    private int id_Trabajador = 1;
    
    public FrmSolicitudPrestamo() {
        initComponents();
        btnAprobar.setEnabled(false);
        btnAprobar.addActionListener(this::btnAprobarActionPerformed);
    }
    
    public FrmSolicitudPrestamo(Trabajador obj) {
        initComponents();
        this.id_Trabajador = obj.getIdTrabajador();
        btnAprobar.setEnabled(false);
        btnAprobar.addActionListener(this::btnAprobarActionPerformed);
    }
    
    public void limpiarMsj(){
        msjCliente.setForeground(Color.RED);
        msjCliente.setText("");
        msjHistorial.setText("");
        msjPrestamo.setText("");
    }
    public void limpiarDato(){
        txtCliente.setText("");
        txtMontoSol.setText("");
        txtTInteres.setText("");
        txtNCuotas.setText("");
        cbxFrecuencia.setSelectedIndex(-1);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlCliente = new javax.swing.JPanel();
        h1Cliente = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JButton();
        btnHistorial = new javax.swing.JButton();
        txtCliente = new javax.swing.JFormattedTextField();
        msjCliente = new javax.swing.JLabel();
        msjHistorial = new javax.swing.JLabel();
        pnlPrestamo = new javax.swing.JPanel();
        h1Prestamo = new javax.swing.JLabel();
        txtMontoSol = new javax.swing.JFormattedTextField();
        lblMontoSol = new javax.swing.JLabel();
        lblNroCuota = new javax.swing.JLabel();
        lblTasaInt = new javax.swing.JLabel();
        btnAgregar = new javax.swing.JButton();
        msjPrestamo = new javax.swing.JLabel();
        txtTInteres = new javax.swing.JFormattedTextField();
        btnCancelar = new javax.swing.JButton();
        lblTipoFrecuencia = new javax.swing.JLabel();
        cbxFrecuencia = new javax.swing.JComboBox<>();
        btnAprobar = new javax.swing.JButton();
        txtNCuotas = new javax.swing.JFormattedTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        pnlCliente.setBackground(new java.awt.Color(255, 255, 255));
        pnlCliente.setForeground(new java.awt.Color(255, 255, 255));

        h1Cliente.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        h1Cliente.setText("CLIENTE");

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(this::btnBuscarActionPerformed);

        btnHistorial.setText("Historial");
        btnHistorial.setEnabled(false);
        btnHistorial.addActionListener(this::btnHistorialActionPerformed);

        txtCliente.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtCliente.addActionListener(this::txtClienteActionPerformed);
        txtCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtClienteKeyPressed(evt);
            }
        });

        msjCliente.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        msjCliente.setForeground(java.awt.Color.red);

        msjHistorial.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        msjHistorial.setForeground(java.awt.Color.red);

        javax.swing.GroupLayout pnlClienteLayout = new javax.swing.GroupLayout(pnlCliente);
        pnlCliente.setLayout(pnlClienteLayout);
        pnlClienteLayout.setHorizontalGroup(
            pnlClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlClienteLayout.createSequentialGroup()
                        .addComponent(h1Cliente)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnlClienteLayout.createSequentialGroup()
                        .addGroup(pnlClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(msjCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtCliente))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlClienteLayout.createSequentialGroup()
                                .addComponent(btnBuscar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnHistorial))
                            .addComponent(msjHistorial, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        pnlClienteLayout.setVerticalGroup(
            pnlClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(h1Cliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuscar)
                    .addComponent(btnHistorial)
                    .addComponent(txtCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(msjHistorial, javax.swing.GroupLayout.DEFAULT_SIZE, 18, Short.MAX_VALUE)
                    .addComponent(msjCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pnlPrestamo.setBackground(new java.awt.Color(255, 255, 255));

        h1Prestamo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        h1Prestamo.setText("PRÉSTAMO");

        txtMontoSol.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));

        lblMontoSol.setText("Monto solicitado:");

        lblNroCuota.setText("Nro Cuotas:");

        lblTasaInt.setText("Tasa de interés:");

        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(this::btnAgregarActionPerformed);

        txtTInteres.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(this::btnCancelarActionPerformed);

        lblTipoFrecuencia.setText("Tipo de frecuencia:");

        cbxFrecuencia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "DIARIO", "MENSUAL", "QUINCENAL", "SEMANAL" }));

        btnAprobar.setText("Agregar y Aprobar");
        btnAprobar.addActionListener(this::btnAprobarActionPerformed);

        txtNCuotas.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtNCuotas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNCuotasKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout pnlPrestamoLayout = new javax.swing.GroupLayout(pnlPrestamo);
        pnlPrestamo.setLayout(pnlPrestamoLayout);
        pnlPrestamoLayout.setHorizontalGroup(
            pnlPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPrestamoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPrestamoLayout.createSequentialGroup()
                        .addComponent(h1Prestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnlPrestamoLayout.createSequentialGroup()
                        .addGroup(pnlPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblMontoSol, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblTipoFrecuencia, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtMontoSol, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                            .addComponent(cbxFrecuencia, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(44, 44, 44)
                        .addGroup(pnlPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblTasaInt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblNroCuota, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTInteres)
                            .addComponent(txtNCuotas)))
                    .addGroup(pnlPrestamoLayout.createSequentialGroup()
                        .addComponent(msjPrestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAprobar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCancelar)))
                .addContainerGap())
        );
        pnlPrestamoLayout.setVerticalGroup(
            pnlPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPrestamoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(h1Prestamo)
                .addGap(18, 18, 18)
                .addGroup(pnlPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNroCuota)
                    .addComponent(lblTipoFrecuencia)
                    .addComponent(cbxFrecuencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNCuotas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblMontoSol)
                        .addComponent(txtMontoSol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblTasaInt)
                        .addComponent(txtTInteres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPrestamoLayout.createSequentialGroup()
                        .addGroup(pnlPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAgregar)
                            .addGroup(pnlPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnCancelar)
                                .addComponent(btnAprobar)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(msjPrestamo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pnlPrestamo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(9, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlPrestamo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
    Cliente obj = new Cliente();
    limpiarMsj();
    try {
        int id = Integer.parseInt(txtCliente.getText());
        if (obj.buscarClientePorId(id)) {
            this.cliente = obj;
            String bloqueo = obj.verificarElegibilidad();
            if (bloqueo != null) {
                msjCliente.setForeground(Color.RED);
                msjCliente.setText("NO ELEGIBLE");
                btnHistorial.setEnabled(false);
                btnAgregar.setEnabled(false);
                btnAprobar.setEnabled(false);
                JOptionPane.showMessageDialog(this, bloqueo,
                    "Cliente no elegible", JOptionPane.WARNING_MESSAGE);
            } else {
                btnHistorial.setEnabled(true);
                btnAgregar.setEnabled(true);
                msjCliente.setForeground(Color.GREEN);
                msjCliente.setText("Aceptado");
            }
        } else {
            msjCliente.setText("No existe ID cliente");
            btnHistorial.setEnabled(false);
            btnAgregar.setEnabled(false);
            btnAprobar.setEnabled(false);
        }
    } catch (Exception ex) {
        msjCliente.setText(ex.getMessage());
    }     
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnHistorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistorialActionPerformed
        if(this.cliente == null){
            msjHistorial.setText("No hay cliente");
        }
        FrmHistorialCliente frm = new FrmHistorialCliente(this, true, this.cliente);
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
        this.aprovado =frm.respuesta;
        if(aprovado){
            msjHistorial.setText("Aceptado");
            msjHistorial.setForeground(Color.GREEN);
            btnAprobar.setEnabled(true);
        }else{
            msjHistorial.setText("Denegado");
            msjHistorial.setForeground(Color.RED);
            btnAprobar.setEnabled(false);
        }
    }//GEN-LAST:event_btnHistorialActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        try {
            String frecuencia = String.valueOf(this.cbxFrecuencia.getSelectedItem());
            int nroCuotas = Integer.parseInt(txtNCuotas.getText().trim());
            if (nroCuotas <= 1) {
                msjPrestamo.setText("El número de cuotas debe ser mayor a 1");
                return;
            }
            Prestamo obj = new Prestamo(Integer.parseInt(txtCliente.getText()),id_Trabajador,
                    Double.parseDouble(txtMontoSol.getText().replace(",", ".")),Double.parseDouble(txtTInteres.getText().replace(",", ".")),nroCuotas, frecuencia);
            if (aprovado) {
                obj.registrarPrestamo();
                JOptionPane.showMessageDialog(null,"Solicitud registrada correctamente","Éxito",JOptionPane.INFORMATION_MESSAGE);
            } else {
                String motivo = JOptionPane.showInputDialog(null,"Ingrese Motivo de rechazo:");
                if (motivo == null || motivo.trim().isEmpty()) return;
                obj.registrarPrestamo();
                obj.rechazarPrestamo(motivo);
                JOptionPane.showMessageDialog(null,"Préstamo rechazado","Éxito",JOptionPane.INFORMATION_MESSAGE);
            }
            limpiarMsj();
            limpiarDato();
        } catch (Exception ex) {
            msjPrestamo.setText(ex.getMessage());
        }
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void txtClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtClienteKeyPressed

    }//GEN-LAST:event_txtClienteKeyPressed

    private void txtClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtClienteActionPerformed
        btnBuscar.doClick();
    }//GEN-LAST:event_txtClienteActionPerformed

    private void btnAprobarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAprobarActionPerformed
        try {
            String frecuencia = String.valueOf(this.cbxFrecuencia.getSelectedItem());
            int nroCuotas = Integer.parseInt(txtNCuotas.getText().trim());
            if (nroCuotas <= 1) {
                msjPrestamo.setText("El número de cuotas debe ser mayor a 1");
                return;
            }
            Prestamo obj = new Prestamo(Integer.parseInt(txtCliente.getText()),id_Trabajador,
                    Double.parseDouble(txtMontoSol.getText().replace(",", ".")),Double.parseDouble(txtTInteres.getText().replace(",", ".")),nroCuotas, frecuencia);
            obj.registrarPrestamo();
            obj.aprobarPrestamo(obj.getMontoSolicitado());
            JOptionPane.showMessageDialog(null,"Préstamo aprobado correctamente","Éxito",JOptionPane.INFORMATION_MESSAGE);
            limpiarMsj();
            limpiarDato();
        } catch (Exception ex) {
            msjPrestamo.setText(ex.getMessage());
        }
    }//GEN-LAST:event_btnAprobarActionPerformed

    private void txtNCuotasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNCuotasKeyReleased
        
    }//GEN-LAST:event_txtNCuotasKeyReleased

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
        java.awt.EventQueue.invokeLater(() -> new FrmSolicitudPrestamo().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnAprobar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnHistorial;
    private javax.swing.JComboBox<String> cbxFrecuencia;
    private javax.swing.JLabel h1Cliente;
    private javax.swing.JLabel h1Prestamo;
    private javax.swing.JLabel lblMontoSol;
    private javax.swing.JLabel lblNroCuota;
    private javax.swing.JLabel lblTasaInt;
    private javax.swing.JLabel lblTipoFrecuencia;
    private javax.swing.JLabel msjCliente;
    private javax.swing.JLabel msjHistorial;
    private javax.swing.JLabel msjPrestamo;
    private javax.swing.JPanel pnlCliente;
    private javax.swing.JPanel pnlPrestamo;
    private javax.swing.JFormattedTextField txtCliente;
    private javax.swing.JFormattedTextField txtMontoSol;
    private javax.swing.JFormattedTextField txtNCuotas;
    private javax.swing.JFormattedTextField txtTInteres;
    // End of variables declaration//GEN-END:variables
}
