/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package from;

/**
 *
 * @author PEÑA
 */
import exs.ADExcepcion;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import ln.NotificacionCuota;
import ln.Trabajador;

public class FrmNotificaciones extends javax.swing.JDialog {
   private Trabajador trabajadorR;
 
    // ── Modelo de la tabla (se accede desde el listener) ─────
    private DefaultTableModel modeloTabla;
 
    // ── Columnas de la tabla ─────────────────────────────────
    private static final String[] COLUMNAS = {
        "Cliente", "Cuota N°", "Estado", "Fecha Venc.", "Mensaje", "Acción"
    };
 
    // ── Índice de la columna Acción (checkbox) ───────────────
    private static final int COL_ACCION = 5;
 
    // ── Índice oculto: guardamos idNotificacion en col 0 del
    //    modelo extendido (se mapea internamente, no se muestra)
    //    En lugar de columna oculta usamos un ArrayList paralelo.
    private ArrayList<Integer> listaIds = new ArrayList<>();
    
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmNotificaciones.class.getName());

    public FrmNotificaciones(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
    }
    
    public FrmNotificaciones(java.awt.Frame parent, boolean modal,
                              Trabajador obj) {
        super(parent, modal);
        this.trabajadorR = obj;
        initComponents();       // 1ro: construye los componentes Swing
        configurarTabla();      // 2do: asigna el modelo correcto a la tabla
        cargarNotificaciones(); // 3ro: carga datos de BD
        setLocationRelativeTo(null);
    }
   
   //  CONFIGURACIÓN DE LA TABLA
    // ────────────────────────────────────────────────────────
    /**
     * Define el modelo de la tabla con las 6 columnas fijas.
     * La columna "Acción" usa Boolean para que Swing
     * pinte automáticamente un JCheckBox sin código extra.
     */
    private void configurarTabla() {
 
        modeloTabla = new DefaultTableModel(null, COLUMNAS) {
 
            /**
             * getColumnClass: le dice a Swing el tipo de dato de cada columna.
             * Cuando devuelve Boolean.class, Swing renderiza un JCheckBox
             * automáticamente en TODA la columna, sin crear renderers manuales.
             */
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == COL_ACCION) return Boolean.class;
                return String.class;
            }
 
            /**
             * isCellEditable: solo la columna Acción es editable
             * para que el usuario pueda marcar el checkbox.
             */
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == COL_ACCION;
            }
        };
 
        tblNotificaciones.setModel(modeloTabla);
 
        // ── Ancho de columnas ────────────────────────────────
        tblNotificaciones.getColumnModel().getColumn(0).setPreferredWidth(180); // Cliente
        tblNotificaciones.getColumnModel().getColumn(1).setPreferredWidth(70);  // Cuota N°
        tblNotificaciones.getColumnModel().getColumn(2).setPreferredWidth(80);  // Estado
        tblNotificaciones.getColumnModel().getColumn(3).setPreferredWidth(90);  // Fecha Venc.
        tblNotificaciones.getColumnModel().getColumn(4).setPreferredWidth(260); // Mensaje
        tblNotificaciones.getColumnModel().getColumn(5).setPreferredWidth(60);  // Acción
 
        tblNotificaciones.setRowHeight(24);
        tblNotificaciones.getTableHeader().setReorderingAllowed(false);
 
        // ── TableModelListener: captura el evento del Checkbox ─
        modeloTabla.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
 
                // Solo nos interesa cuando cambia la columna Acción
                // y es un UPDATE (el usuario marcó o desmarcó)
                if (e.getType()   != TableModelEvent.UPDATE) return;
                if (e.getColumn() != COL_ACCION)             return;
 
                int fila = e.getFirstRow();
                if (fila < 0 || fila >= modeloTabla.getRowCount()) return;
 
                Boolean marcado = (Boolean) modeloTabla.getValueAt(fila, COL_ACCION);
 
                if (marcado != null && marcado) {
                    // Obtener el ID real de la notificación desde el ArrayList paralelo
                    int idNotificacion = listaIds.get(fila);
 
                    try {
                        // UPDATE en BD: enviado = TRUE, fecha_envio = NOW()
                        NotificacionCuota obj = new NotificacionCuota();
                        obj.marcarComoRevisada(idNotificacion);
 
                        // Quitar la fila visualmente (del más nuevo al menos)
                        modeloTabla.removeRow(fila);
                        listaIds.remove(fila);
 
                        // Actualizar contador
                        actualizarContador();
 
                    } catch (ADExcepcion ex) {
                        JOptionPane.showMessageDialog(
                            FrmNotificaciones.this,
                            "Error al actualizar la notificación:\n" + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                        // Desmarcar el checkbox para no dejar estado inconsistente
                        modeloTabla.setValueAt(false, fila, COL_ACCION);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(
                            FrmNotificaciones.this,
                            "Error inesperado:\n" + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                        modeloTabla.setValueAt(false, fila, COL_ACCION);
                    }
                }
            }
        });
    }
 
    // ────────────────────────────────────────────────────────
    //  CARGA DE DATOS
    // ────────────────────────────────────────────────────────
    private void cargarNotificaciones() {
        // Limpiar tabla y lista de IDs antes de recargar
        modeloTabla.setRowCount(0);
        listaIds.clear();
 
        try {
            NotificacionCuota obj = new NotificacionCuota();
 
            // 1. Escanear y generar las notificaciones nuevas en BD
            obj.escanearYGenerarNotificaciones();
 
            // 2. Listar las activas aplicando filtro de rol
            String cargo        = trabajadorR.getCargo();
            int    idTrabajador = trabajadorR.getIdTrabajador();
 
            ArrayList<NotificacionCuota> lista =
                    obj.listarActivas(cargo, idTrabajador);
 
            // 3. Agregar cada notificación a la tabla
            //    El orden ya viene correcto desde BD (DESC created_at)
            //    → las más nuevas se insertan primero = aparecen arriba
            for (NotificacionCuota n : lista) {
                modeloTabla.addRow(new Object[]{
                    n.getNombreCliente(),
                    n.getNumeroCuota(),
                    n.getEstadoCuota(),
                    n.getFechaVencimiento(),
                    n.getMensaje(),
                    Boolean.FALSE           // checkbox desmarcado por defecto
                });
                // Guardar el ID en el ArrayList paralelo (misma posición que la fila)
                listaIds.add(n.getIdNotificacion());
            }
 
            actualizarContador();
 
        } catch (ADExcepcion ex) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar notificaciones:\n" + ex.getMessage(),
                "Error de base de datos",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error inesperado:\n" + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
 
    private void actualizarContador() {
        int total = modeloTabla.getRowCount();
        lblContador.setText(total == 0
                ? "No hay notificaciones pendientes."
                : total + " notificación(es) activa(s)");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlTitulo = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblContador = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblNotificaciones = new javax.swing.JTable();
        pnlBotones = new javax.swing.JPanel();
        btnRefrescar = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(false);

        pnlTitulo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText(" Notificaciones del Sistema");

        lblContador.setText("jLabel2");

        javax.swing.GroupLayout pnlTituloLayout = new javax.swing.GroupLayout(pnlTitulo);
        pnlTitulo.setLayout(pnlTituloLayout);
        pnlTituloLayout.setHorizontalGroup(
            pnlTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTituloLayout.createSequentialGroup()
                .addGap(166, 166, 166)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblContador)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlTituloLayout.setVerticalGroup(
            pnlTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTituloLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(pnlTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblContador))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        tblNotificaciones.setModel(new javax.swing.table.DefaultTableModel(
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
        tblNotificaciones.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblNotificaciones.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tblNotificaciones);

        pnlBotones.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnRefrescar.setText("Actualizar");
        btnRefrescar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefrescarActionPerformed(evt);
            }
        });

        btnCerrar.setText("Cerrar");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlBotonesLayout = new javax.swing.GroupLayout(pnlBotones);
        pnlBotones.setLayout(pnlBotonesLayout);
        pnlBotonesLayout.setHorizontalGroup(
            pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBotonesLayout.createSequentialGroup()
                .addGap(140, 140, 140)
                .addComponent(btnRefrescar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCerrar)
                .addGap(150, 150, 150))
        );
        pnlBotonesLayout.setVerticalGroup(
            pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBotonesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRefrescar)
                    .addComponent(btnCerrar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 624, Short.MAX_VALUE)
                    .addComponent(pnlBotones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRefrescarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefrescarActionPerformed
        // TODO add your handling code here:
        cargarNotificaciones(); 
    }//GEN-LAST:event_btnRefrescarActionPerformed

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

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
                FrmNotificaciones dialog = new FrmNotificaciones(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnRefrescar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblContador;
    private javax.swing.JPanel pnlBotones;
    private javax.swing.JPanel pnlTitulo;
    private javax.swing.JTable tblNotificaciones;
    // End of variables declaration//GEN-END:variables
}
