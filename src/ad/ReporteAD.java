package ad;

/**
 *
 * @author PEÑA
 */
import db.Conexion;
import exs.ADExcepcion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReporteAD extends Conexion {
  //REPORTE CARTERA
    public ResultSet obtenerResumenCarteraBD() {
        String sql = " SELECT estado, " +
                     "        COUNT(*) AS cantidad, " +
                     "        COALESCE(SUM(monto_solicitado), 0) AS monto_total " +
                     "   FROM prestamos " +
                     "  WHERE estado IN ('DESEMBOLSADO','RECHAZADO','PENDIENTE') " +
                     "  GROUP BY estado ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al obtener resumen de cartera", e);
        }
    }
  // REPORTE MOROSIDAD
    public ResultSet obtenerCabeceraReporteCuotaBD(int idPrestamo) {
    String sql = " SELECT c.nombres || ' ' || c.apellidos AS cliente, "
               + "        t.nombres || ' ' || t.apellidos AS trabajador, "
               + "        p.monto_aprobado, "
               + "        p.tasa_interes, "
               + "        p.frecuencia_pago, "
               + "        p.plazo_cuotas "
               + "   FROM prestamos p "
               + "   JOIN clientes    c ON c.id_cliente    = p.id_cliente "
               + "   JOIN trabajadores t ON t.id_trabajador = p.id_trabajador "
               + "  WHERE p.id_prestamo = ? ";
    try {
        PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
        pstmt.setInt(1, idPrestamo);
        return pstmt.executeQuery();
    } catch (SQLException e) {
        throw new ADExcepcion("Error al obtener cabecera del reporte de cuotas", e);
    }
  }
    
// Para la TABLA - detalle por cuota
public ResultSet obtenerReporteMorosidadBD() {
    String sql = " SELECT c.nombres || ' ' || c.apellidos AS cliente, "
               + "        c.dni, "
               + "        p.id_prestamo, "
               + "        cu.numero_cuota, "
               + "        m.dias_mora, "
               + "        m.monto_mora, "
               + "        cu.fecha_vencimiento "
               + "   FROM moras m "
               + "   JOIN cuotas      cu ON cu.id_cuota    = m.id_cuota "
               + "   JOIN prestamos    p  ON p.id_prestamo  = cu.id_prestamo "
               + "   JOIN clientes     c  ON c.id_cliente   = p.id_cliente "
               + "  WHERE m.estado = 'ACTIVA' "
               + "  ORDER BY m.dias_mora DESC ";
    try {
        PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
        return pstmt.executeQuery();
    } catch (SQLException e) {
        throw new ADExcepcion("Error al obtener reporte de morosidad", e);
    }
}

// Para el GRAFICO - agrupado por cliente
public ResultSet obtenerMorosidadPorClienteBD() {
    String sql = " SELECT c.nombres || ' ' || c.apellidos AS cliente, "
               + "        COUNT(m.id_mora) AS cuotas_vencidas, "
               + "        SUM(m.monto_mora) AS total_mora "
               + "   FROM moras m "
               + "   JOIN cuotas   cu ON cu.id_cuota   = m.id_cuota "
               + "   JOIN prestamos p  ON p.id_prestamo = cu.id_prestamo "
               + "   JOIN clientes  c  ON c.id_cliente  = p.id_cliente "
               + "  WHERE m.estado = 'ACTIVA' "
               + "  GROUP BY c.id_cliente, c.nombres, c.apellidos "
               + "  ORDER BY total_mora DESC "
               + "  LIMIT 5 ";
    try {
        PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
        return pstmt.executeQuery();
    } catch (SQLException e) {
        throw new ADExcepcion("Error al obtener morosidad por cliente", e);
    }
}

// REPORTE HISTORIAL CLIENTE:

public ResultSet obtenerListaClientesBD() {
    String sql = " SELECT id_cliente, nombres || ' ' || apellidos AS cliente "
               + "   FROM clientes "
               + "  ORDER BY id_cliente ";
    try {
        PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
        return pstmt.executeQuery();
    } catch (SQLException e) {
        throw new ADExcepcion("Error al obtener lista de clientes", e);
    }
}

public ResultSet obtenerHistorialPrestamosBD(int idCliente) {
    String sql = " SELECT p.id_prestamo, "
               + "        p.monto_aprobado, "
               + "        p.tasa_interes, "
               + "        p.estado, "
               + "        p.frecuencia_pago, "
               + "        p.plazo_cuotas, "
               + "        COUNT(CASE WHEN cu.estado = 'PAGADO' THEN 1 END) AS cuotas_pagadas, "
               + "        COUNT(CASE WHEN cu.estado != 'PAGADO' THEN 1 END) AS cuotas_pendientes, "
               + "        p.fecha_desembolso "
               + "   FROM prestamos p "
               + "   LEFT JOIN cuotas cu ON cu.id_prestamo = p.id_prestamo "
               + "  WHERE p.id_cliente = ? "
               + "  GROUP BY p.id_prestamo, p.monto_aprobado, p.tasa_interes, "
               + "           p.estado, p.frecuencia_pago, p.plazo_cuotas, p.fecha_desembolso "
               + "  ORDER BY p.id_prestamo DESC ";
    try {
        PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
        pstmt.setInt(1, idCliente);
        return pstmt.executeQuery();
    } catch (SQLException e) {
        throw new ADExcepcion("Error al obtener historial de préstamos", e);
    }
}
 
//REPORTE DE RENDIMIENTO POR TRABAJADOR
public ResultSet obtenerRendimientoTrabajadoresBD() {
    String sql = " SELECT t.nombres || ' ' || t.apellidos AS trabajador, "
               + "        t.cargo, "
               + "        COUNT(p.id_prestamo) AS prestamos_registrados, "
               + "        COUNT(CASE WHEN p.estado = 'DESEMBOLSADO' THEN 1 END) AS prestamos_aprobados, "
               + "        COALESCE(SUM(CASE WHEN p.estado = 'DESEMBOLSADO' THEN p.monto_aprobado END), 0) AS monto_gestionado "
               + "   FROM trabajadores t "
               + "   LEFT JOIN prestamos p ON p.id_trabajador = t.id_trabajador "
               + "  GROUP BY t.id_trabajador, t.nombres, t.apellidos, t.cargo "
               + "  ORDER BY prestamos_registrados DESC ";
    try {
        PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
        return pstmt.executeQuery();
    } catch (SQLException e) {
        throw new ADExcepcion("Error al obtener rendimiento de trabajadores", e);
    }
}

//REPORTE METODO PAGO
public ResultSet obtenerMetodosPagoBD() {
    String sql = " SELECT mp.nombre AS metodo, "
               + "        COUNT(pd.id_pago) AS veces_usado, "
               + "        SUM(pd.monto) AS monto_total "
               + "   FROM pago_detalle pd "
               + "   JOIN metodo_pago mp ON mp.id_metodo_pago = pd.id_metodo_pago "
               + "  GROUP BY mp.id_metodo_pago, mp.nombre "
               + "  ORDER BY monto_total DESC ";
    try {
        PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
        return pstmt.executeQuery();
    } catch (SQLException e) {
        throw new ADExcepcion("Error al obtener métodos de pago", e);
    }
}
}