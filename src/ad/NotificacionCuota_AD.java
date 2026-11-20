/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ad;

import db.Conexion;
import exs.ADExcepcion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import ln.NotificacionCuota;

public class NotificacionCuota_AD extends Conexion {

    // ────────────────────────────────────────────────────────────────────────
    //  MÉTODO 1 – ESCANEAR E INSERTAR NOTIFICACIONES NUEVAS
    // ────────────────────────────────────────────────────────────────────────
    /**
     * Recorre todas las cuotas VENCIDAS y PENDIENTES (que vencen en 3 días)
     * e inserta en notificaciones_cuota solo las que todavía NO existan
     * (gracias al índice UNIQUE uq_notif_cuota_tipo).
     *
     * Este método se debe llamar cada vez que el usuario abre el formulario
     * de notificaciones para que la lista esté siempre al día.
     */
    public void escanearYGenerarBD() {

        // ── 1. Cuotas VENCIDAS → alerta de COBRANZA ─────────────────────
        String sqlVencidas =
              " INSERT INTO notificaciones_cuota "
            + "     (id_cuota, id_trabajador, canal, tipo_alerta, mensaje) "
            + " SELECT c.id_cuota, "
            + "        p.id_trabajador, "
            + "        'SISTEMA', "
            + "        'COBRANZA', "
            + "        'Cliente presenta mora activa' "
            + "   FROM cuotas c "
            + "   JOIN prestamos p ON p.id_prestamo = c.id_prestamo "
            + "  WHERE c.estado = 'VENCIDO' "
            + "    AND NOT EXISTS ( "
            + "          SELECT 1 FROM notificaciones_cuota n "
            + "           WHERE n.id_cuota    = c.id_cuota "
            + "             AND n.tipo_alerta = 'COBRANZA' "
            + "        ) ";

        // ── 2. Cuotas PENDIENTES que vencen EXACTAMENTE en 3 días → PREVENTIVA
        String sqlPreventivas =
              " INSERT INTO notificaciones_cuota "
            + "     (id_cuota, id_trabajador, canal, tipo_alerta, mensaje) "
            + " SELECT c.id_cuota, "
            + "        p.id_trabajador, "
            + "        'SISTEMA', "
            + "        'PREVENTIVA', "
            + "        'Cuota próxima a vencer (vence en 3 días)' "
            + "   FROM cuotas c "
            + "   JOIN prestamos p ON p.id_prestamo = c.id_prestamo "
            + "  WHERE c.estado = 'PENDIENTE' "
            + "    AND c.fecha_vencimiento = CURRENT_DATE + INTERVAL '3 days' "
            + "    AND NOT EXISTS ( "
            + "          SELECT 1 FROM notificaciones_cuota n "
            + "           WHERE n.id_cuota    = c.id_cuota "
            + "             AND n.tipo_alerta = 'PREVENTIVA' "
            + "        ) ";

        try {
            PreparedStatement ps1 = this.obtenerConexion().prepareStatement(sqlVencidas);
            ps1.executeUpdate();

            PreparedStatement ps2 = this.obtenerConexion().prepareStatement(sqlPreventivas);
            ps2.executeUpdate();

        } catch (SQLException e) {
            throw new ADExcepcion("Error al escanear y generar notificaciones", e);
        }
    }

    // ────────────────────────────────────────────────────────────────────────
    //  MÉTODO 2 – LISTAR NOTIFICACIONES ACTIVAS SEGÚN ROL
    // ────────────────────────────────────────────────────────────────────────
    /**
     * Devuelve un ArrayList con las notificaciones activas (enviado = FALSE).
     *
     * - Si cargo = "admin"      → ve TODAS las notificaciones de la empresa.
     * - Si cargo = "trabajador" → solo ve las de los préstamos que él registró.
     *
     * El orden es: más nuevas primero (DESC por created_at).
     *
     * @param cargo        "admin" o "trabajador"
     * @param idTrabajador ID del trabajador logueado
     */
    public ArrayList<NotificacionCuota> listarActivasBD(String cargo,
                                                         int idTrabajador) {
        ArrayList<NotificacionCuota> lista = new ArrayList<>();

        // La cláusula WHERE cambia según el rol:
        //   admin     → sin filtro de trabajador
        //   trabajador → n.id_trabajador = ?
        String filtroRol = cargo.equalsIgnoreCase("admin")
                ? " "
                : " AND n.id_trabajador = ? ";

        String sql =
              " SELECT n.id_notificacion, "
            + "        n.id_cuota, "
            + "        n.id_trabajador, "
            + "        n.tipo_alerta, "
            + "        n.mensaje, "
            + "        n.created_at, "
            + "        c.numero_cuota, "
            + "        c.estado                            AS estado_cuota, "
            + "        TO_CHAR(c.fecha_vencimiento,'DD/MM/YYYY') AS fecha_venc, "
            + "        cl.nombres || ' ' || cl.apellidos   AS nombre_cliente "
            + "   FROM notificaciones_cuota n "
            + "   JOIN cuotas   c  ON c.id_cuota    = n.id_cuota "
            + "   JOIN prestamos p  ON p.id_prestamo = c.id_prestamo "
            + "   JOIN clientes  cl ON cl.id_cliente = p.id_cliente "
            + "  WHERE n.enviado  = FALSE "
            + "    AND n.canal    = 'SISTEMA' "
            + filtroRol
            + "  ORDER BY n.created_at DESC ";

        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);

            // Si es trabajador, hay que asignar el parámetro del id
            if (!cargo.equalsIgnoreCase("admin")) {
                pstmt.setInt(1, idTrabajador);
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                NotificacionCuota obj = new NotificacionCuota();
                obj.setIdNotificacion (rs.getInt("id_notificacion"));
                obj.setIdCuota        (rs.getInt("id_cuota"));
                obj.setIdTrabajador   (rs.getInt("id_trabajador"));
                obj.setTipoAlerta     (rs.getString("tipo_alerta"));
                obj.setMensaje        (rs.getString("mensaje"));
                obj.setCreatedAt      (rs.getTimestamp("created_at"));
                obj.setNumeroCuota    (rs.getInt("numero_cuota"));
                obj.setEstadoCuota    (rs.getString("estado_cuota"));
                obj.setFechaVencimiento(rs.getString("fecha_venc"));
                obj.setNombreCliente  (rs.getString("nombre_cliente"));
                lista.add(obj);
            }

        } catch (SQLException e) {
            throw new ADExcepcion("Error al listar notificaciones activas", e);
        }

        return lista;
    }

    // ────────────────────────────────────────────────────────────────────────
    //  MÉTODO 3 – MARCAR COMO REVISADA (enviado = TRUE)
    // ────────────────────────────────────────────────────────────────────────
    /**
     * Actualiza la notificación indicada:
     *   enviado    = TRUE
     *   fecha_envio = NOW()
     *
     * @param idNotificacion ID de la fila a actualizar
     */
    public void marcarComoRevisadaBD(int idNotificacion) {
        String sql =
              " UPDATE notificaciones_cuota "
            + "    SET enviado     = TRUE, "
            + "        fecha_envio = NOW() "
            + "  WHERE id_notificacion = ? ";

        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setInt(1, idNotificacion);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al marcar notificación como revisada", e);
        }
    }
}