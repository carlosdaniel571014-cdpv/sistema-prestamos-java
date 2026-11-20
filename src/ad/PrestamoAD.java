package ad;

import db.Conexion;
import exs.ADExcepcion;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class PrestamoAD extends Conexion {

    public int registrarSolicitudBD(int p_idCliente, int p_idTrabajador,
                                    double p_montoSolicitado, double p_tasaInteres,
                                    int p_plazoCuotas, String p_frecuenciaPago) {
        String sql = "CALL registrar_solicitud_prestamo(?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            CallableStatement cstmt = this.obtenerConexion().prepareCall(sql);
            cstmt.setInt(1, p_idCliente);
            cstmt.setInt(2, p_idTrabajador);
            cstmt.setObject(3, p_montoSolicitado, Types.NUMERIC);
            cstmt.setObject(4, p_tasaInteres, Types.NUMERIC);
            cstmt.setString(5, p_frecuenciaPago);
            cstmt.setInt(6, p_plazoCuotas);
            cstmt.setDate(7, new java.sql.Date(System.currentTimeMillis()));
            cstmt.setNull(8, Types.INTEGER);
            cstmt.registerOutParameter(8, Types.INTEGER);
            cstmt.execute();
            return cstmt.getInt(8);
        } catch (SQLException e) {
            throw new ADExcepcion("Error al registrar solicitud de préstamo", e);
        }
    }

    public void aprobarPrestamoBD(int p_idPrestamo, int p_idTrabajador,
                                  String p_decision, Double p_montoAprobado,
                                  String p_motivoRechazo) {
        String sql = "CALL aprobar_prestamo(?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            CallableStatement cstmt = this.obtenerConexion().prepareCall(sql);
            cstmt.setInt(1, p_idPrestamo);
            cstmt.setInt(2, p_idTrabajador);
            cstmt.setString(3, p_decision);
            if (p_montoAprobado != null) {
                cstmt.setObject(4, p_montoAprobado, Types.NUMERIC);
            } else {
                cstmt.setNull(4, Types.NUMERIC);
            }
            cstmt.setDate(5, new java.sql.Date(System.currentTimeMillis()));
            cstmt.setDate(6, new java.sql.Date(System.currentTimeMillis()));
            cstmt.setNull(7, Types.DATE);
            if (p_motivoRechazo != null) {
                cstmt.setString(8, p_motivoRechazo);
            } else {
                cstmt.setNull(8, Types.VARCHAR);
            }
            cstmt.execute();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al " + (p_decision.equals("APROBADO") ? "aprobar" : "rechazar") + " préstamo", e);
        }
    }

    public ResultSet buscarPrestamosBD(String p_busqueda) {
        String sql = " SELECT p.id_prestamo, p.id_cliente, " +
                     "        c.nombres AS cli_nombres, c.apellidos AS cli_apellidos, c.dni, c.telefono AS celular, " +
                     "        t.nombres AS trab_nombres, t.apellidos AS trab_apellidos, " +
                     "        p.monto_solicitado, p.monto_aprobado, p.tasa_interes, " +
                     "        p.plazo_cuotas, p.cuota_monto, p.frecuencia_pago, p.estado, " +
                     "        p.fecha_solicitud, p.fecha_aprobacion, p.motivo_rechazo " +
                     "   FROM prestamos p " +
                     "   JOIN clientes c ON c.id_cliente = p.id_cliente " +
                     "   JOIN trabajadores t ON t.id_trabajador = p.id_trabajador " +
                     "  WHERE (CAST(p.id_prestamo AS TEXT) ILIKE '%' || ? || '%' " +
                     "     OR CAST(p.id_cliente AS TEXT) ILIKE '%' || ? || '%' " +
                     "     OR c.nombres ILIKE '%' || ? || '%' " +
                     "     OR c.apellidos ILIKE '%' || ? || '%' " +
                     "     OR t.nombres ILIKE '%' || ? || '%' " +
                     "     OR t.apellidos ILIKE '%' || ? || '%') " +
                     "  ORDER BY p.fecha_solicitud DESC ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setString(1, p_busqueda);
            pstmt.setString(2, p_busqueda);
            pstmt.setString(3, p_busqueda);
            pstmt.setString(4, p_busqueda);
            pstmt.setString(5, p_busqueda);
            pstmt.setString(6, p_busqueda);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al buscar préstamos", e);
        }
    }

    public void actualizarPrestamoBD(int p_idPrestamo, double p_montoAprobado,
                                     double p_tasaInteres, int p_plazoCuotas,
                                     String p_frecuenciaPago) {
        String sql = " UPDATE prestamos " +
                     "    SET monto_aprobado = ?, tasa_interes = ?, plazo_cuotas = ?, " +
                     "        frecuencia_pago = ?, fecha_aprobacion = CURRENT_DATE " +
                     "  WHERE id_prestamo = ? ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setDouble(1, p_montoAprobado);
            pstmt.setDouble(2, p_tasaInteres);
            pstmt.setInt(3, p_plazoCuotas);
            pstmt.setString(4, p_frecuenciaPago);
            pstmt.setInt(5, p_idPrestamo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al actualizar préstamo en BD", e);
        }
    }

    public void cancelarPrestamoBD(int p_idPrestamo) {
        String sql = " UPDATE prestamos SET estado = 'CANCELADO', fecha_vencimiento = CURRENT_DATE " +
                     "  WHERE id_prestamo = ? ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setInt(1, p_idPrestamo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al cancelar préstamo en BD", e);
        }
    }
    
    public ResultSet obtenerPrestamoBD(int p_idPrestamo) {
        String sql = " SELECT p.id_prestamo, p.id_cliente, p.id_trabajador, " +
                     "        p.id_trabajador_aprov, " +
                     "        p.monto_solicitado, p.monto_aprobado, p.tasa_interes, " +
                     "        p.plazo_cuotas, p.cuota_monto, p.frecuencia_pago, " +
                     "        p.estado, p.fecha_solicitud, p.fecha_aprobacion, " +
                     "        p.fecha_desembolso, p.fecha_vencimiento, p.motivo_rechazo " +
                     "   FROM prestamos p " +
                     "  WHERE p.id_prestamo = ? ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setInt(1, p_idPrestamo);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al obtener préstamo por ID", e);
        }
    }

    public int obtenerPlazoCuotasBD(int p_idPrestamo) {
        String sql = " SELECT plazo_cuotas FROM prestamos WHERE id_prestamo = ? ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setInt(1, p_idPrestamo);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getInt("plazo_cuotas") : 0;
        } catch (SQLException e) {
            throw new ADExcepcion("Error al obtener plazo de cuotas", e);
        }
    }

    public double obtenerSaldoPendienteTotalBD(int p_idPrestamo) {
        String sql = " SELECT COALESCE(SUM(saldo_pendiente), 0) AS total_pendiente " +
                     "   FROM cuotas " +
                     "  WHERE id_prestamo = ? ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setInt(1, p_idPrestamo);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getDouble("total_pendiente") : 0;
        } catch (SQLException e) {
            throw new ADExcepcion("Error al obtener saldo pendiente total", e);
        }
    }

    public boolean existePrestamoPorIdBD(int p_idPrestamo) {
        String sql = "SELECT 1 " +
                     "  FROM prestamos " +
                     " WHERE id_prestamo = ?";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setInt(1, p_idPrestamo);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al verificar existencia del préstamo", e);
        }
    }
}
