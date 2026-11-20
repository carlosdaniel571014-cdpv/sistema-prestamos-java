package ad;

import db.Conexion;
import exs.ADExcepcion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HistorialAD extends Conexion {

    public ResultSet buscarHistorialBD(int p_idCliente) {
        String sql = " SELECT id_historial, score_crediticio, deudas_activas, " +
                     "        monto_deuda, entidad_reporte, fecha_evaluacion, observaciones " +
                     "   FROM historial_crediticio " +
                     "  WHERE id_cliente = ? " +
                     "  ORDER BY fecha_evaluacion DESC " +
                     "  LIMIT 1 ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setInt(1, p_idCliente);
            ResultSet rs = pstmt.executeQuery();
            return rs.isBeforeFirst() ? rs : null;
        } catch (SQLException e) {
            throw new ADExcepcion("Error al buscar historial crediticio en BD", e);
        }
    }
}
