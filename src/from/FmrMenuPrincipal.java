package from;

import ln.MenuOpcion;
import ln.Trabajador;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

public class FmrMenuPrincipal extends javax.swing.JFrame {

    Trabajador trabajadorR;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FmrMenuPrincipal.class.getName());

    public FmrMenuPrincipal() {
        initComponents();
    }

    public FmrMenuPrincipal(Trabajador obj) {
        initComponents();
        this.trabajadorR = obj;
        trabajadorIngresado();
        cargarMenu();
    }

    public void trabajadorIngresado() {
        h1Ingresado.setText(trabajadorR.getApellidos() + ", " + trabajadorR.getNombres());
    }

    public void cargarMenu() {
        java.awt.Component[] menus = mnGeneral.getComponents();
        for (java.awt.Component m : menus) {
            if (m instanceof javax.swing.JMenu) {
                ocultarMenu((javax.swing.JMenu) m);
            }
        }
        try {
            ResultSet rs = new MenuOpcion().buscarPorCargo(trabajadorR.getCargo());
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                try {
                    java.lang.reflect.Field field = getClass().getDeclaredField(nombre);
                    Object obj = field.get(this);
                    if (obj instanceof javax.swing.JMenuItem) {
                        ((javax.swing.JMenuItem) obj).setVisible(true);
                    }
                } catch (NoSuchFieldException | IllegalAccessException ex) {
                    // sin componente asociado, se ignora
                }
            }
        } catch (Exception ex) {
        }
    }

    private void ocultarMenu(javax.swing.JMenu menu) {
        menu.setVisible(false);
        for (int i = 0; i < menu.getItemCount(); i++) {
            javax.swing.JMenuItem item = menu.getItem(i);
            if (item != null) {
                item.setVisible(false);
                if (item instanceof javax.swing.JMenu) {
                    ocultarMenu((javax.swing.JMenu) item);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlDatos = new javax.swing.JPanel();
        h1Bienvenido = new javax.swing.JLabel();
        h1Ingresado = new javax.swing.JLabel();
        btnCerrarSesion = new javax.swing.JButton();
        btnCampana = new javax.swing.JButton();
        mnGeneral = new javax.swing.JMenuBar();
        mnGestion = new javax.swing.JMenu();
        smnCliente = new javax.swing.JMenuItem();
        smnTrabajador = new javax.swing.JMenuItem();
        mnAdmin = new javax.swing.JMenu();
        smnPrestamo = new javax.swing.JMenuItem();
        spr1 = new javax.swing.JPopupMenu.Separator();
        smnReporte = new javax.swing.JMenu();
        smnReporteCartera = new javax.swing.JMenuItem();
        smnReporteMorosidad = new javax.swing.JMenuItem();
        smnReporteHistorial = new javax.swing.JMenuItem();
        smnReporteRendimiento = new javax.swing.JMenuItem();
        smnReporteMetodos = new javax.swing.JMenuItem();
        mnComprobantes = new javax.swing.JMenu();
        smnVerComporbantes = new javax.swing.JMenuItem();
        mnOpc = new javax.swing.JMenu();
        smnConfig = new javax.swing.JMenuItem();
        smnAcercaDe = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlDatos.setBackground(new java.awt.Color(255, 255, 255));

        h1Bienvenido.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        h1Bienvenido.setText("BIENVENIDO");

        h1Ingresado.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        btnCerrarSesion.setText("Cerrar Sesión");
        btnCerrarSesion.addActionListener(this::btnCerrarSesionActionPerformed);

        btnCampana.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/campana.png"))); // NOI18N
        btnCampana.setContentAreaFilled(false);
        btnCampana.addActionListener(this::btnCampanaActionPerformed);

        javax.swing.GroupLayout pnlDatosLayout = new javax.swing.GroupLayout(pnlDatos);
        pnlDatos.setLayout(pnlDatosLayout);
        pnlDatosLayout.setHorizontalGroup(
            pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDatosLayout.createSequentialGroup()
                .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlDatosLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(h1Bienvenido)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(h1Ingresado, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE))
                    .addGroup(pnlDatosLayout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(btnCampana)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCerrarSesion)))
                .addContainerGap())
        );
        pnlDatosLayout.setVerticalGroup(
            pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDatosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(h1Bienvenido)
                    .addComponent(h1Ingresado, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCerrarSesion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCampana, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        mnGestion.setText("Gestión");

        smnCliente.setText("Clientes");
        smnCliente.addActionListener(this::smnClienteActionPerformed);
        mnGestion.add(smnCliente);

        smnTrabajador.setText("Trabajador");
        smnTrabajador.addActionListener(this::smnTrabajadorActionPerformed);
        mnGestion.add(smnTrabajador);

        mnGeneral.add(mnGestion);

        mnAdmin.setText("Administración");

        smnPrestamo.setText("Préstamos");
        smnPrestamo.addActionListener(this::smnPrestamoActionPerformed);
        mnAdmin.add(smnPrestamo);
        mnAdmin.add(spr1);

        smnReporte.setText("Reportes");

        smnReporteCartera.setText("Cartera de Préstamos");
        smnReporteCartera.addActionListener(this::smnReporteCarteraActionPerformed);
        smnReporte.add(smnReporteCartera);

        smnReporteMorosidad.setText("Morosidad por cliente");
        smnReporteMorosidad.addActionListener(this::smnReporteMorosidadActionPerformed);
        smnReporte.add(smnReporteMorosidad);

        smnReporteHistorial.setText("Historial por Cliente");
        smnReporteHistorial.addActionListener(this::smnReporteHistorialActionPerformed);
        smnReporte.add(smnReporteHistorial);

        smnReporteRendimiento.setText("Rendimiento por trabajador");
        smnReporteRendimiento.addActionListener(this::smnReporteRendimientoActionPerformed);
        smnReporte.add(smnReporteRendimiento);

        smnReporteMetodos.setText("Métodos de pago");
        smnReporteMetodos.addActionListener(this::smnReporteMetodosActionPerformed);
        smnReporte.add(smnReporteMetodos);

        mnAdmin.add(smnReporte);

        mnGeneral.add(mnAdmin);

        mnComprobantes.setText("Comprobantes");

        smnVerComporbantes.setText("Ver Comprobantes");
        smnVerComporbantes.addActionListener(this::smnVerComporbantesActionPerformed);
        mnComprobantes.add(smnVerComporbantes);

        mnGeneral.add(mnComprobantes);

        mnOpc.setText("Opciones");

        smnConfig.setText("Configuración");
        smnConfig.addActionListener(this::smnConfigActionPerformed);
        mnOpc.add(smnConfig);

        smnAcercaDe.setText("Acerca de");
        smnAcercaDe.addActionListener(this::smnAcercaDeActionPerformed);
        mnOpc.add(smnAcercaDe);

        mnGeneral.add(mnOpc);

        setJMenuBar(mnGeneral);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(348, Short.MAX_VALUE)
                .addComponent(pnlDatos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlDatos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(299, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void smnPrestamoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smnPrestamoActionPerformed
        FrmCRUDPrestamo objfrm = new FrmCRUDPrestamo(trabajadorR);
        objfrm.setVisible(true);
        objfrm.setLocationRelativeTo(null);
    }//GEN-LAST:event_smnPrestamoActionPerformed

    private void smnVerComporbantesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smnVerComporbantesActionPerformed
        FrmComprobantes frm = new FrmComprobantes();
        frm.setVisible(true);
        frm.setLocationRelativeTo(null);
    }//GEN-LAST:event_smnVerComporbantesActionPerformed

    private void smnConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smnConfigActionPerformed
        FrmConfiguracion objfrm = new FrmConfiguracion(this, true);
        objfrm.setLocationRelativeTo(null);
        objfrm.setVisible(true);
        cargarMenu();
    }//GEN-LAST:event_smnConfigActionPerformed

    private void btnCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarSesionActionPerformed
        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Desea cerrar sesión?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            FrmLogin objlogin = new FrmLogin();
            objlogin.setVisible(true);
            objlogin.setLocationRelativeTo(null);
            this.dispose();
        }
    }//GEN-LAST:event_btnCerrarSesionActionPerformed

    private void smnTrabajadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smnTrabajadorActionPerformed
        FrmGestionTrabajadores frm = new FrmGestionTrabajadores(null);
        frm.setLocationRelativeTo(null);

        frm.setVisible(true);
    }//GEN-LAST:event_smnTrabajadorActionPerformed

    private void smnClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smnClienteActionPerformed
        FrmCrudCliente frm = new FrmCrudCliente();
        frm.setVisible(true);
        frm.setLocationRelativeTo(null);
    }//GEN-LAST:event_smnClienteActionPerformed

    private void btnCampanaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCampanaActionPerformed
        // TODO add your handling code here:
        FrmNotificaciones frm = new FrmNotificaciones(this, true, trabajadorR); //  Correcto
        frm.setVisible(true);
    }//GEN-LAST:event_btnCampanaActionPerformed

    private void smnReporteCarteraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smnReporteCarteraActionPerformed
       FrmReporteCartera objfrm = new FrmReporteCartera();
       objfrm.setVisible(true);
    }//GEN-LAST:event_smnReporteCarteraActionPerformed

    private void smnReporteMorosidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smnReporteMorosidadActionPerformed
        // TODO add your handling code here:
         FrmReporteMorosidad objfrm = new FrmReporteMorosidad();
         objfrm.setVisible(true);
    }//GEN-LAST:event_smnReporteMorosidadActionPerformed

    private void smnReporteHistorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smnReporteHistorialActionPerformed
        // TODO add your handling code here:
         FrmReporteHistorialCliente objfrm = new FrmReporteHistorialCliente();
         objfrm.setVisible(true);
    }//GEN-LAST:event_smnReporteHistorialActionPerformed

    private void smnReporteRendimientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smnReporteRendimientoActionPerformed
        // TODO add your handling code here:
        FrmReporteRendimientoTrabajador objfrm = new FrmReporteRendimientoTrabajador();
        objfrm.setVisible(true);
    }//GEN-LAST:event_smnReporteRendimientoActionPerformed

    private void smnReporteMetodosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smnReporteMetodosActionPerformed
        // TODO add your handling code here:
        FrmReporteMetodosPago objfrm = new FrmReporteMetodosPago();
        objfrm.setVisible(true);
        
    }//GEN-LAST:event_smnReporteMetodosActionPerformed

    private void smnAcercaDeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smnAcercaDeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_smnAcercaDeActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new FmrMenuPrincipal().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCampana;
    private javax.swing.JButton btnCerrarSesion;
    private javax.swing.JLabel h1Bienvenido;
    private javax.swing.JLabel h1Ingresado;
    private javax.swing.JMenu mnAdmin;
    private javax.swing.JMenu mnComprobantes;
    private javax.swing.JMenuBar mnGeneral;
    private javax.swing.JMenu mnGestion;
    private javax.swing.JMenu mnOpc;
    private javax.swing.JPanel pnlDatos;
    private javax.swing.JMenuItem smnAcercaDe;
    private javax.swing.JMenuItem smnCliente;
    private javax.swing.JMenuItem smnConfig;
    private javax.swing.JMenuItem smnPrestamo;
    private javax.swing.JMenu smnReporte;
    private javax.swing.JMenuItem smnReporteCartera;
    private javax.swing.JMenuItem smnReporteHistorial;
    private javax.swing.JMenuItem smnReporteMetodos;
    private javax.swing.JMenuItem smnReporteMorosidad;
    private javax.swing.JMenuItem smnReporteRendimiento;
    private javax.swing.JMenuItem smnTrabajador;
    private javax.swing.JMenuItem smnVerComporbantes;
    private javax.swing.JPopupMenu.Separator spr1;
    // End of variables declaration//GEN-END:variables
}
