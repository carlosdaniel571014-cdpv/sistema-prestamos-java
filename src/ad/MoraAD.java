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

/**
 *
 * @author ASUS
 */
public class MoraAD extends Conexion {

    public void calcularMoraCuotaBD(int p_idCuota) {
        String sql = "CALL calcular_mora_cuota(?)";
        try {
            CallableStatement cstmt = this.obtenerConexion().prepareCall(sql);
            cstmt.setInt(1, p_idCuota);
            cstmt.execute();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al calcular mora de la cuota", e);
        }
    }

    public void calcularMorasPrestamosBD(int p_idPrestamo) {
        String sql = "CALL calcular_moras_prestamo(?)";
        try {
            CallableStatement cstmt = this.obtenerConexion().prepareCall(sql);
            cstmt.setInt(1, p_idPrestamo);
            cstmt.execute();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al calcular moras del préstamo", e);
        }
    }

    public void regularizarMoraBD(int p_idCuota) {
        String sql = "CALL regularizar_mora(?)";
        try {
            CallableStatement cstmt = this.obtenerConexion().prepareCall(sql);
            cstmt.setInt(1, p_idCuota);
            cstmt.execute();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al regularizar mora", e);
        }
    }

    public ResultSet obtenerDetalleMoraBD(int p_idCuota) {
        String sql = " SELECT c.monto_capital AS kv, "
                + "        c.monto_interes AS ic, "
                + "        p.tasa_interes  AS tea, "
                + "        (CURRENT_DATE - c.fecha_vencimiento) AS dias_calc, "
                + "        COALESCE(m.monto_mora, 0) AS monto_mora "
                + "   FROM cuotas c "
                + "   JOIN prestamos p ON p.id_prestamo = c.id_prestamo "
                + "   LEFT JOIN moras m ON m.id_cuota = c.id_cuota "
                + "                    AND m.estado = 'ACTIVA' "
                + "  WHERE c.id_cuota = ? ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setInt(1, p_idCuota);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al obtener detalle de mora", e);
        }
    }

    public ResultSet obtenerMoraActivaBD(int p_idCuota) {
        String sql = " SELECT monto_mora FROM moras "
                + "  WHERE id_cuota = ? AND estado = 'ACTIVA' ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setInt(1, p_idCuota);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al obtener mora activa", e);
        }
    }

    public ResultSet listarCuotasVencidasBD(int p_idPrestamo) {
        String sql = " SELECT c.id_cuota, c.numero_cuota, c.fecha_vencimiento, "
                + "        c.monto_cuota, c.monto_capital, c.monto_interes, "
                + "        c.estado AS estado_cuota, "
                + "        COALESCE(m.dias_mora,  0) AS dias_mora, "
                + "        COALESCE(m.monto_mora, 0) AS monto_mora, "
                + "        COALESCE(m.tasa_mora,  0) AS tasa_mora, "
                + "        COALESCE(m.estado, '---')  AS estado_mora " // ACTIVA o REGULARIZADA
                + "   FROM cuotas c "
                + "   JOIN moras m ON m.id_cuota = c.id_cuota " // INNER JOIN — solo cuotas que tuvieron mora
                + "  WHERE c.id_prestamo = ? "
                + "  ORDER BY c.numero_cuota ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setInt(1, p_idPrestamo);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al listar moras del préstamo", e);
        }
    }
}
