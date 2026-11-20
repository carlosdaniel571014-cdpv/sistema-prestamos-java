package ad;

import db.Conexion;
import exs.ADExcepcion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ComprobanteAD extends Conexion {

    public ResultSet listarPagosBD() {
        String sql = " SELECT p.id_pago, p.monto_total, p.fecha_pago, "
                + "        cl.nombres || ' ' || cl.apellidos AS cliente, "
                + "        cl.dni, pr.id_prestamo "
                + "   FROM pagos p "
                + "   JOIN prestamos  pr ON pr.id_prestamo = p.id_prestamo "
                + "   JOIN clientes   cl ON cl.id_cliente  = pr.id_cliente "
                + "  ORDER BY p.fecha_pago DESC ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al listar pagos", e);
        }
    }
 
    public ResultSet obtenerCabeceraPagoBD(int idPago) {
        String sql = " SELECT p.id_pago, p.fecha_pago, p.monto_total, "
                + "        p.tiene_adelanto, pr.id_prestamo, "
                + "        cl.nombres || ' ' || cl.apellidos AS cliente, "
                + "        cl.dni, tr.nombres || ' ' || tr.apellidos AS trabajador "
                + "   FROM pagos p "
                + "   JOIN prestamos   pr ON pr.id_prestamo  = p.id_prestamo "
                + "   JOIN clientes    cl ON cl.id_cliente   = pr.id_cliente "
                + "   JOIN trabajadores tr ON tr.id_trabajador = p.id_trabajador "
                + "  WHERE p.id_pago = ? ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setInt(1, idPago);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al obtener cabecera del pago", e);
        }
    }
 
    /**
     * Retorna las cuotas asociadas a un pago, incluyendo id_cuota, id_mora
     * y monto_mora para poder generar comprobantes correctamente.
     */
    public ResultSet obtenerCuotasPagoBD(int idPago) {
        String sql = " SELECT c.id_cuota, c.numero_cuota, c.monto_cuota, "
                + "        pc.monto_abonado, pc.es_adelanto, "
                + "        m.id_mora, m.monto_mora "
                + "   FROM pago_cuota pc "
                + "   JOIN cuotas c ON c.id_cuota = pc.id_cuota "
                + "   LEFT JOIN moras m ON m.id_cuota = c.id_cuota "
                + "                    AND m.estado IN ('ACTIVA','REGULARIZADA') "
                + "  WHERE pc.id_pago = ? "
                + "  ORDER BY c.numero_cuota ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setInt(1, idPago);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al obtener cuotas del pago", e);
        }
    }
 
    public ResultSet obtenerMediosPagoBD(int idPago) {
        String sql = " SELECT mp.nombre AS medio, pd.monto, pd.referencia "
                + "   FROM pago_detalle pd "
                + "   JOIN metodo_pago mp ON mp.id_metodo_pago = pd.id_metodo_pago "
                + "  WHERE pd.id_pago = ? "
                + "  ORDER BY mp.nombre ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setInt(1, idPago);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al obtener medios de pago", e);
        }
    }
 
    public void registrarComprobanteBD(int idPrestamo, int idCuota,
            Integer idMora, String tipo,
            String numeroSerie, double monto, String observaciones) {
        String sql = " INSERT INTO comprobantes(id_prestamo, id_cuota, id_mora, "
                + "             tipo, numero_serie, monto, observaciones) "
                + " VALUES(?, ?, ?, ?, ?, ?, ?) ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setInt(1, idPrestamo);
            pstmt.setInt(2, idCuota);
            if (idMora != null) {
                pstmt.setInt(3, idMora);
            } else {
                pstmt.setNull(3, java.sql.Types.INTEGER);
            }
            pstmt.setString(4, tipo);
            pstmt.setString(5, numeroSerie);
            pstmt.setDouble(6, monto);
            pstmt.setString(7, observaciones);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al registrar comprobante", e);
        }
    }
 
    public int obtenerUltimoNumeroComprobanteBD() {
        String sql = " SELECT COUNT(*) AS total FROM comprobantes ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("total");
            return 0;
        } catch (SQLException e) {
            throw new ADExcepcion("Error al obtener número de comprobante", e);
        }
    }
 
    /**
     * Verifica si un pago ya tiene comprobantes generados en BD.
     */
    public boolean tieneComprobantesBD(int idPago) {
        String sql = " SELECT COUNT(*) AS total "
                + "   FROM comprobantes co "
                + "   JOIN pago_cuota pc ON pc.id_cuota = co.id_cuota "
                + "  WHERE pc.id_pago = ? ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setInt(1, idPago);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt("total") > 0;
        } catch (SQLException e) {
            throw new ADExcepcion("Error al verificar comprobantes", e);
        }
    }
 
    public ResultSet listarComprobantesBD(int idPago) {
        String sql = " SELECT co.numero_serie, co.tipo, co.monto, "
                + "        co.fecha_emision, co.observaciones, "
                + "        c.numero_cuota "
                + "   FROM comprobantes co "
                + "   JOIN cuotas c ON c.id_cuota = co.id_cuota "
                + "  WHERE co.id_cuota IN ( "
                + "        SELECT id_cuota FROM pago_cuota WHERE id_pago = ?) "
                + "    AND co.id_prestamo = ( "
                + "        SELECT id_prestamo FROM pagos WHERE id_pago = ?) "
                + "  ORDER BY co.tipo, c.numero_cuota ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setInt(1, idPago);
            pstmt.setInt(2, idPago);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al listar comprobantes", e);
        }
    }
}
