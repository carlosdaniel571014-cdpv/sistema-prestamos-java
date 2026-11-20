package from;

import java.util.List;
import javax.swing.table.DefaultTableModel;
import ln.Trabajador;
/**
 *
 * @author Richard
 */
public class FrmGestionTrabajadores extends javax.swing.JFrame {
    
    private Trabajador objUsuario;

    public FrmGestionTrabajadores(Trabajador objUsuario) {
        initComponents();
        this.objUsuario = objUsuario;
        cargarTabla();
        configurarSeleccionTabla();
        btnNuevo.addActionListener(evt -> btnNuevoActionPerformed(evt));
        btnActivarDesactivar.addActionListener(evt -> btnActivarDesactivarActionPerformed(evt)); 

    }

    private void btnDesactivarActionPerformed(java.awt.event.ActionEvent evt) {
        if (idTrabajadorSeleccionado == -1) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Selecciona un trabajador de la tabla.",
                    "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        String accion = btnActivarDesactivar.getText(); // "Desactivar" o "Reactivar"
        int confirmar = javax.swing.JOptionPane.showConfirmDialog(this,
                "¿Deseas " + accion.toLowerCase() + " a este trabajador?",
                "Confirmar", javax.swing.JOptionPane.YES_NO_OPTION);

        if (confirmar != javax.swing.JOptionPane.YES_OPTION) return;

        try {
            Trabajador obj = new Trabajador(idTrabajadorSeleccionado);

            if (accion.equals("Desactivar")) {
                obj.desactivar();
            } else {
                obj.reactivar();
            }

            obj.confirmarCambios();

            javax.swing.JOptionPane.showMessageDialog(this,
                    "Trabajador " + accion.toLowerCase() + "do correctamente.",
                    "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);

            cargarTabla(); // refresca la tabla

        } catch (Exception e) {
            msjError.setText(e.getMessage());
        }
    }

    // ─── VARIABLE PARA FILA SELECCIONADA ─────────────────────────────────────
    private int idTrabajadorSeleccionado = -1;

    // ─── CARGAR TABLA ─────────────────────────────────────────────────────────
    public void cargarTabla() {
        try {
            jTable1.getColumnModel().getColumn(4).setHeaderValue("Email");
            jTable1.getTableHeader().repaint();

            DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
            modelo.setRowCount(0);

            Trabajador obj = new Trabajador();
            List<Trabajador> lista = obj.listarTrabajadores();

            for (Trabajador t : lista) {
                modelo.addRow(new Object[]{
                    t.getIdTrabajador(),
                    t.getApellidos() + ", " + t.getNombres(),
                    t.getDni(),
                    t.getCargo(),
                    t.getEmail(),
                    t.isActivo() ? "Activo" : "Inactivo"
                });
            }

            idTrabajadorSeleccionado = -1;
            btnActivarDesactivar.setText("Desactivar");

        } catch (Exception e) {
            msjError.setText(e.getMessage());
        }
    }

    public void configurarSeleccionTabla() {
        jTable1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = jTable1.getSelectedRow();
                if (fila >= 0) {
                    idTrabajadorSeleccionado = (int) jTable1.getValueAt(fila, 0);
                    String estado = jTable1.getValueAt(fila, 5).toString();
                    btnActivarDesactivar.setText(estado.equals("Activo") ? "Desactivar" : "Reactivar");
                }
            }
        });
    }


    private void smnTrabajadorActionPerformed(java.awt.event.ActionEvent evt) {
        FrmGestionTrabajadores frm = new FrmGestionTrabajadores(this.objUsuario);
        frm.setVisible(true);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtNombreTrabajador = new javax.swing.JTextField();
        btnNuevo = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnActivarDesactivar = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnCerrar = new javax.swing.JToggleButton();
        msjError = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Gestión de trabajadores");

        jLabel2.setText("Buscar por nombre / dni:");

        txtNombreTrabajador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreTrabajadorActionPerformed(evt);
            }
        });

        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnEditar.setText("Editar");
        btnEditar.setToolTipText("");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnActivarDesactivar.setText("Cambiar Estado");
        btnActivarDesactivar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActivarDesactivarActionPerformed(evt);
            }
        });

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        jSeparator1.setForeground(new java.awt.Color(102, 102, 102));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNombreTrabajador, javax.swing.GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscar)))
                .addGap(12, 12, 12)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(btnActivarDesactivar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnEditar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnNuevo)
                            .addComponent(btnActivarDesactivar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtNombreTrabajador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEditar)
                            .addComponent(btnBuscar))
                        .addGap(0, 12, Short.MAX_VALUE))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Apellidos y nombres", "DNI", "Cargo", "Email", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setMaxWidth(60);
            jTable1.getColumnModel().getColumn(2).setMaxWidth(100);
            jTable1.getColumnModel().getColumn(5).setMaxWidth(100);
        }

        btnCerrar.setText("Cerrar");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });

        msjError.setForeground(java.awt.Color.red);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(msjError, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCerrar)
                    .addComponent(msjError, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void btnActivarDesactivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActivarDesactivarActionPerformed
        if (idTrabajadorSeleccionado == -1) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Selecciona un trabajador de la tabla.",
                "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        String accion = btnActivarDesactivar.getText(); // "Desactivar" o "Reactivar"
        int confirmar = javax.swing.JOptionPane.showConfirmDialog(this,
            "¿Deseas " + accion.toLowerCase() + " a este trabajador?",
            "Confirmar", javax.swing.JOptionPane.YES_NO_OPTION);

        if (confirmar != javax.swing.JOptionPane.YES_OPTION) return;

        try {
            Trabajador obj = new Trabajador(idTrabajadorSeleccionado);

            if (accion.equals("Desactivar")) {
                obj.desactivar();
            } else {
                obj.reactivar();
            }

            obj.confirmarCambios();

            javax.swing.JOptionPane.showMessageDialog(this,
                "Trabajador " + accion.toLowerCase() + "do correctamente.",
                "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);

            cargarTabla();

        } catch (Exception e) {
            msjError.setText(e.getMessage());
        }
    }//GEN-LAST:event_btnActivarDesactivarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        if (idTrabajadorSeleccionado == -1) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Selecciona un trabajador de la tabla.",
                "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        FrmEditarTrabajador frm = new FrmEditarTrabajador(this, idTrabajadorSeleccionado);
        frm.setLocationRelativeTo(this);
        frm.setVisible(true);
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        FrmNuevoTrabajador frm = new FrmNuevoTrabajador(this);
        frm.setLocationRelativeTo(this);
        frm.setVisible(true);
        cargarTabla(); // se ejecuta cuando el form se cierra
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        try {
            String texto = txtNombreTrabajador.getText().trim();
            DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
            modelo.setRowCount(0);

            Trabajador obj = new Trabajador();
            List<Trabajador> lista;
            if (texto.isEmpty()) {
                lista = obj.listarTrabajadores();
            } else {
                lista = obj.buscarTrabajadores(texto);
            }

            for (Trabajador t : lista) {
                modelo.addRow(new Object[]{
                    t.getIdTrabajador(),
                    t.getApellidos() + ", " + t.getNombres(),
                    t.getDni(),
                    t.getCargo(),
                    t.getEmail(),
                    t.isActivo() ? "Activo" : "Inactivo"
                });
            }

            idTrabajadorSeleccionado = -1;
            btnActivarDesactivar.setText("Desactivar");
            msjError.setText("");
        } catch (Exception e) {
            msjError.setText(e.getMessage());
        }
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void txtNombreTrabajadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreTrabajadorActionPerformed
        btnBuscar.doClick();
    }//GEN-LAST:event_txtNombreTrabajadorActionPerformed

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmGestionTrabajadores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmGestionTrabajadores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmGestionTrabajadores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmGestionTrabajadores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmGestionTrabajadores(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActivarDesactivar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JToggleButton btnCerrar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel msjError;
    private javax.swing.JTextField txtNombreTrabajador;
    // End of variables declaration//GEN-END:variables
}
