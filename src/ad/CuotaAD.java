package ad;

import db.Conexion;
import exs.ADExcepcion;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class CuotaAD extends Conexion {

    public ResultSet listarCuotasBD(int p_idPrestamo) {
        String sql = " SELECT c.id_cuota, c.numero_cuota, c.monto_cuota, c.monto_pagado, "
                + "        c.fecha_pago, c.fecha_vencimiento, c.estado, "
                + "        CASE WHEN c.estado = 'PAGADO' THEN 0 "
                + "             ELSE c.saldo_pendiente + COALESCE("
                + "                  CASE WHEN m.estado = 'ACTIVA' THEN m.monto_mora ELSE 0 END, 0) "
                + "        END AS saldo_pendiente, "
                + "        c.monto_capital, c.monto_interes, "
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
            throw new ADExcepcion("Error al listar cuotas del préstamo", e);
        }
    }

    public void eliminarCuotasBD(int p_idPrestamo, int[] p_idsCuotas) {
        if (p_idsCuotas == null || p_idsCuotas.length == 0) {
            return;
        }
        StringBuilder sql = new StringBuilder("DELETE FROM cuotas WHERE id_prestamo = ? AND id_cuota IN (");
        for (int i = 0; i < p_idsCuotas.length; i++) {
            sql.append(i == 0 ? "?" : ",?");
        }
        sql.append(")");
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql.toString());
            pstmt.setInt(1, p_idPrestamo);
            for (int i = 0; i < p_idsCuotas.length; i++) {
                pstmt.setInt(2 + i, p_idsCuotas[i]);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al eliminar cuotas", e);
        }
    }

    public int insertarCuotaBD(int p_idPrestamo, int p_numeroCuota, Date p_fechaVenc,
            double p_montoCuota, double p_montoCapital,
            double p_montoInteres, double p_saldoPendiente) {
        String sql = " INSERT INTO cuotas(id_prestamo, numero_cuota, fecha_vencimiento, "
                + "                   monto_cuota, monto_capital, monto_interes, saldo_pendiente, estado) "
                + " VALUES(?, ?, ?, ?, ?, ?, ?, 'PENDIENTE') RETURNING id_cuota ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setInt(1, p_idPrestamo);
            pstmt.setInt(2, p_numeroCuota);
            pstmt.setDate(3, p_fechaVenc);
            pstmt.setDouble(4, p_montoCuota);
            pstmt.setDouble(5, p_montoCapital);
            pstmt.setDouble(6, p_montoInteres);
            pstmt.setDouble(7, p_saldoPendiente);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new ADExcepcion("Error al insertar cuota consolidada", e);
        }
    }

    public void renumerarCuotasBD(int p_idPrestamo, int p_desdeNumero, int p_desplazamiento) {
        String sql = " UPDATE cuotas "
                + "    SET numero_cuota = numero_cuota - ? "
                + "  WHERE id_prestamo = ? AND numero_cuota >= ? ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setInt(1, p_desplazamiento);
            pstmt.setInt(2, p_idPrestamo);
            pstmt.setInt(3, p_desdeNumero);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al renumerar cuotas", e);
        }
    }

    public void actualizarFechasCuotasBD(int p_idPrestamo, int p_desdeNumero,
            int p_diasShift) {
        if (p_diasShift == 0) {
            return;
        }
        String sql = " UPDATE cuotas "
                + "    SET fecha_vencimiento = fecha_vencimiento - (? || ' days')::INTERVAL "
                + "  WHERE id_prestamo = ? AND numero_cuota >= ? ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setInt(1, p_diasShift);
            pstmt.setInt(2, p_idPrestamo);
            pstmt.setInt(3, p_desdeNumero);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al actualizar fechas de cuotas", e);
        }
    }
}
