package from;

import exs.ADExcepcion;
import ln.MenuOpcion;
import ln.Trabajador;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FrmConfiguracion extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmConfiguracion.class.getName());

    public FrmConfiguracion(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        ponerCargos();
        if (cbxCargo.getItemCount() > 0) {
            cargarCheckboxes();
        }
    }

    public void ponerCargos(){
        try{
            cbxCargo.removeAllItems();
            ResultSet rs = new Trabajador().obtenerCargo();
            if (rs != null) {
                while (rs.next()) {
                    cbxCargo.addItem(rs.getString("cargo"));
                }
            }
        }catch(Exception e){
            msjError.setText(e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cbxCargo = new javax.swing.JComboBox<>();
        mnGestion = new javax.swing.JCheckBox();
        smnCliente = new javax.swing.JCheckBox();
        smnTrabajador = new javax.swing.JCheckBox();
        mnAdmin = new javax.swing.JCheckBox();
        smnPrestamo = new javax.swing.JCheckBox();
        smnReporte = new javax.swing.JCheckBox();
        mnComprobantes = new javax.swing.JCheckBox();
        mnOpc = new javax.swing.JCheckBox();
        smnConfig = new javax.swing.JCheckBox();
        smnVerComporbantes = new javax.swing.JCheckBox();
        smnAcercaDe = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        msjError = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("MENÚS SEGÚN CARGO");

        cbxCargo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbxCargo.addItemListener(this::cbxCargoItemStateChanged);

        mnGestion.setText("Gestión");
        mnGestion.addActionListener(this::mnGestionActionPerformed);

        smnCliente.setText("Clientes");

        smnTrabajador.setText("Trabajador");

        mnAdmin.setText("Administración");
        mnAdmin.addChangeListener(this::mnAdminStateChanged);

        smnPrestamo.setText("Préstamo");
        smnPrestamo.addActionListener(this::smnPrestamoActionPerformed);

        smnReporte.setText("Reporte");

        mnComprobantes.setText("Comprobantes");
        mnComprobantes.addChangeListener(this::mnComprobantesStateChanged);

        mnOpc.setText("Opciones");
        mnOpc.addChangeListener(this::mnOpcStateChanged);

        smnConfig.setText("Configuración");

        smnVerComporbantes.setText("Ver Comprobantes");

        smnAcercaDe.setText("Acerca de");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cbxCargo, 0, 129, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(mnGestion)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(21, 21, 21)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(smnTrabajador)
                                            .addComponent(smnCliente))))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(mnAdmin)
                            .addComponent(mnComprobantes)
                            .addComponent(mnOpc)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(smnVerComporbantes)
                                    .addComponent(smnReporte)
                                    .addComponent(smnPrestamo)
                                    .addComponent(smnConfig)
                                    .addComponent(smnAcercaDe))))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cbxCargo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mnGestion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(smnCliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(smnTrabajador)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mnAdmin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(smnPrestamo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(smnReporte)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mnComprobantes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(smnVerComporbantes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mnOpc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(smnConfig)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(smnAcercaDe)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setText("Aceptar");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jButton2.setText("Cancelar");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        msjError.setForeground(java.awt.Color.red);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(msjError, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(msjError, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void cargarCheckboxes() {
        String cargo = (String) cbxCargo.getSelectedItem();
        if (cargo == null) return;
        for (java.awt.Component c : jPanel1.getComponents()) {
            if (c instanceof javax.swing.JCheckBox) {
                ((javax.swing.JCheckBox) c).setSelected(false);
            }
        }
        try {
            ResultSet rs = new MenuOpcion().buscarPorCargo(cargo);
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                try {
                    java.lang.reflect.Field field = getClass().getDeclaredField(nombre);
                    Object obj = field.get(this);
                    if (obj instanceof javax.swing.JCheckBox) {
                        ((javax.swing.JCheckBox) obj).setSelected(true);
                    }
                } catch (NoSuchFieldException | IllegalAccessException ex) {
                    // el nombre no tiene checkbox asociado, se ignora
                }
            }
        } catch (Exception ex) {
            msjError.setText(ex.getMessage());
        }
    }

    private void mnGestionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnGestionActionPerformed
        boolean estado = mnGestion.isSelected();    
        smnCliente.setSelected(estado);
        smnCliente.setEnabled(estado);
        smnTrabajador.setSelected(estado);
        smnTrabajador.setEnabled(estado);
    }//GEN-LAST:event_mnGestionActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private String padreDe(String nombre) {
        switch (nombre) {
            case "smnCliente": case "smnTrabajador": return "mnGestion";
            case "smnPrestamo": case "smnReporte":   return "mnAdmin";
            case "mnVerComporbantes":                 return "mnComprobantes";
            case "smnConfig": case "smnAcercaDe":    return "mnOpc";
            default: return null;
        }
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String cargo = (String) cbxCargo.getSelectedItem();
        if (cargo == null) return;

        MenuOpcion menu = new MenuOpcion();
        try {
            menu.conectarBD();
            for (java.lang.reflect.Field field : getClass().getDeclaredFields()) {
                if (!javax.swing.JCheckBox.class.isAssignableFrom(field.getType())) continue;
                javax.swing.JCheckBox chk = (javax.swing.JCheckBox) field.get(this);
                String nombre = field.getName();

                ResultSet rsBusq = menu.buscarIdPorCargoYNombreBD(cargo, nombre);
                int idExistente = rsBusq.next() ? rsBusq.getInt("id") : 0;

                if (chk.isSelected()) {
                    if (idExistente > 0) continue;

                    String padre = padreDe(nombre);
                    Integer idPadre = null;
                    if (padre != null) {
                        javax.swing.JCheckBox chkPadre = (javax.swing.JCheckBox) getClass().getDeclaredField(padre).get(this);
                        if (!chkPadre.isSelected()) {
                            msjError.setText("Seleccionar '" + chkPadre.getText() + "' antes de '" + chk.getText() + "'");
                            menu.descartarCambios();
                            return;
                        }
                        ResultSet rsPadre = menu.buscarIdPorCargoYNombreBD(cargo, padre);
                        idPadre = rsPadre.next() ? rsPadre.getInt("id") : null;
                    }
                    menu.insertarMenuOpcionBD(cargo, nombre, idPadre);
                } else {
                    if (idExistente > 0) {
                        menu.eliminarMenuOpcionBD(idExistente);
                    }
                }
            }
            menu.confirmarCambios();
            this.dispose();
        } catch (Exception e) {
            try { menu.descartarCambios(); } catch (Exception ex) {}
            msjError.setText(e.getMessage());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cbxCargoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxCargoItemStateChanged
        cargarCheckboxes();
    }//GEN-LAST:event_cbxCargoItemStateChanged

    private void mnAdminStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_mnAdminStateChanged
        boolean estado = mnAdmin.isSelected();
        smnPrestamo.setSelected(estado);
        smnPrestamo.setEnabled(estado);
        smnReporte.setSelected(estado);
        smnReporte.setEnabled(estado);
    }//GEN-LAST:event_mnAdminStateChanged

    private void mnComprobantesStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_mnComprobantesStateChanged
        boolean estado = mnComprobantes.isSelected();
        smnVerComporbantes.setSelected(estado);
        smnVerComporbantes.setEnabled(estado);
    }//GEN-LAST:event_mnComprobantesStateChanged

    private void mnOpcStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_mnOpcStateChanged
        boolean estado = mnOpc.isSelected();
        smnConfig.setSelected(estado);
        smnConfig.setEnabled(estado);
        smnAcercaDe.setSelected(estado);
        smnAcercaDe.setEnabled(estado);
    }//GEN-LAST:event_mnOpcStateChanged

    private void smnPrestamoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smnPrestamoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_smnPrestamoActionPerformed

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

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                FrmConfiguracion dialog = new FrmConfiguracion(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cbxCargo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JCheckBox mnAdmin;
    private javax.swing.JCheckBox mnComprobantes;
    private javax.swing.JCheckBox mnGestion;
    private javax.swing.JCheckBox mnOpc;
    private javax.swing.JLabel msjError;
    private javax.swing.JCheckBox smnAcercaDe;
    private javax.swing.JCheckBox smnCliente;
    private javax.swing.JCheckBox smnConfig;
    private javax.swing.JCheckBox smnPrestamo;
    private javax.swing.JCheckBox smnReporte;
    private javax.swing.JCheckBox smnTrabajador;
    private javax.swing.JCheckBox smnVerComporbantes;
    // End of variables declaration//GEN-END:variables
}
