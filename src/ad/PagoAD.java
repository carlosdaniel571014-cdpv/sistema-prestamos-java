/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ad;

import db.Conexion;
import exs.ADExcepcion;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 *
 * @author ASUS
 */
public class PagoAD extends Conexion {

    public boolean verificarOrdenCuotasBD(int p_idPrestamo, int p_numeroCuotaMin) {
        String sql = "CALL verificar_orden_cuotas(?, ?, ?)";
        try {
            CallableStatement cstmt = this.obtenerConexion().prepareCall(sql);
            cstmt.setInt(1, p_idPrestamo);
            cstmt.setInt(2, p_numeroCuotaMin);
            cstmt.setBoolean(3, false);
            cstmt.registerOutParameter(3, Types.BOOLEAN);
            cstmt.execute();
            return cstmt.getBoolean(3);
        } catch (SQLException e) {
            throw new ADExcepcion("Error al verificar orden de cuotas", e);
        }
    }

    public int insertarPagoBD(int p_idPrestamo, int p_idTrabajador,
            double p_montoTotal, boolean p_tieneAdelanto,
            String p_observaciones) {
        String sql = "INSERT INTO pagos(id_prestamo, id_trabajador, monto_total, "
                + "tiene_adelanto, fecha_pago, observaciones) "
                + "VALUES(?, ?, ?, ?, CURRENT_TIMESTAMP, ?) RETURNING id_pago";
        try {
            java.sql.PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setInt(1, p_idPrestamo);
            pstmt.setInt(2, p_idTrabajador);
            pstmt.setDouble(3, p_montoTotal);
            pstmt.setBoolean(4, p_tieneAdelanto);
            pstmt.setString(5, p_observaciones);
            java.sql.ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_pago");
            }
            throw new ADExcepcion("No se pudo obtener el id del pago");
        } catch (SQLException e) {
            throw new ADExcepcion("Error al registrar cabecera de pago", e);
        }
    }

    public void insertarPagoCuotaBD(int p_idPago, int p_idCuota,
            double p_montoAbonado, boolean p_esAdelanto) {
        String sql = "INSERT INTO pago_cuota(id_pago, id_cuota, monto_abonado, es_adelanto) "
                + "VALUES(?, ?, ?, ?)";
        try {
            java.sql.PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setInt(1, p_idPago);
            pstmt.setInt(2, p_idCuota);
            pstmt.setDouble(3, p_montoAbonado);
            pstmt.setBoolean(4, p_esAdelanto);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al registrar cuota en el pago", e);
        }
    }

    public void insertarPagoDetalleBD(int p_idPago, int p_idMetodo,
            double p_monto, String p_referencia) {
        String sql = "INSERT INTO pago_detalle(id_pago, id_metodo_pago, monto, referencia) "
                + "VALUES(?, ?, ?, ?)";
        try {
            java.sql.PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setInt(1, p_idPago);
            pstmt.setInt(2, p_idMetodo);
            pstmt.setDouble(3, p_monto);
            pstmt.setString(4, p_referencia);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al registrar medio de pago", e);
        }
    }

    public void actualizarCuotaAbonoBD(int p_idCuota, double p_montoAbonado) {
        String sql = "UPDATE cuotas "
                + "SET monto_pagado    = COALESCE(monto_pagado, 0) + ?, "
                + "    saldo_pendiente = GREATEST(saldo_pendiente - ?, 0), "
                + "    fecha_pago      = CURRENT_DATE, "
                + "    estado = CASE "
                + "               WHEN (saldo_pendiente - ?) <= 0 THEN 'PAGADO' "
                + "               ELSE 'PARCIAL' "
                + "             END "
                + "WHERE id_cuota = ?";
        try {
            java.sql.PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setDouble(1, p_montoAbonado);
            pstmt.setDouble(2, p_montoAbonado);
            pstmt.setDouble(3, p_montoAbonado);
            pstmt.setInt(4, p_idCuota);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al actualizar cuota", e);
        }
    }

    public ResultSet listarMetodosPagoBD() {
        String sql = " SELECT id_metodo_pago, nombre "
                + "   FROM metodo_pago "
                + "  ORDER BY id_metodo_pago ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al listar métodos de pago", e);
        }
    }

    public ResultSet listarCuotasBD(int p_idPrestamo) {
        String sql = " SELECT c.id_cuota, c.numero_cuota, c.monto_cuota, "
                + "        CASE WHEN c.estado = 'PAGADO' THEN 0 "
                + "             ELSE c.saldo_pendiente + COALESCE("
                + "                  CASE WHEN m.estado = 'ACTIVA' THEN m.monto_mora ELSE 0 END, 0) "
                + "        END AS saldo_pendiente, "
                + "        c.estado, c.fecha_vencimiento, "
                + "        COALESCE(m.monto_mora, 0) AS monto_mora "
                + "   FROM cuotas c "
                + "   LEFT JOIN moras m ON m.id_cuota = c.id_cuota "
                + "                    AND m.estado IN ('ACTIVA', 'REGULARIZADA') "
                + "  WHERE c.id_prestamo = ? "
                + "  ORDER BY c.numero_cuota ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setInt(1, p_idPrestamo);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al listar cuotas", e);
        }
    }

    public ResultSet obtenerSiguienteCuotaBD(int p_idPrestamo, int p_nroCuotaMax) {
        String sql = " SELECT c.id_cuota, c.numero_cuota, c.saldo_pendiente "
                + "   FROM cuotas c "
                + "  WHERE c.id_prestamo  = ? "
                + "    AND c.numero_cuota = ? + 1 "
                + "    AND c.estado      <> 'PAGADO' ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setInt(1, p_idPrestamo);
            pstmt.setInt(2, p_nroCuotaMax);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al obtener cuota siguiente", e);
        }
    }

    public void regularizarMoraBD(int p_idCuota) {
        String sql = "UPDATE moras SET estado = 'REGULARIZADA', "
                + "fecha_regularizacion = CURRENT_DATE, "
                + "updated_at = CURRENT_TIMESTAMP "
                + "WHERE id_cuota = ? AND estado = 'ACTIVA'";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setInt(1, p_idCuota);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al regularizar mora", e);
        }
    }
}
