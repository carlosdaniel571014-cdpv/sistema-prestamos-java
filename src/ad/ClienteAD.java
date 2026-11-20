package ad;

import db.Conexion;
import exs.ADExcepcion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteAD extends Conexion {

    // Buscar por DNI (como tu amigo)
    public ResultSet buscarClienteBD(int p_dni) {
        String sql = " SELECT id_cliente, nombres, apellidos, dni, telefono, email, direccion, activo "
                + "   FROM clientes "
                + "  WHERE dni = ? ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setInt(1, p_dni);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                if (!rs.getBoolean("activo")) {
                    throw new ADExcepcion("Cliente desactivado");
                }
                return rs;
            }
            return null;
        } catch (SQLException e) {
            throw new ADExcepcion("Error al buscar cliente en BD", e);
        }
    }

    // Buscar por ID (para cargar en modo modificar)
    public ResultSet buscarClientePorIdBD(int p_idCliente) {
        String sql = " SELECT id_cliente, nombres, apellidos, dni, telefono, email, direccion, activo "
                + "   FROM clientes "
                + "  WHERE id_cliente = ? ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setInt(1, p_idCliente);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                if (!rs.getBoolean("activo")) {
                    throw new ADExcepcion("Cliente desactivado");
                }
                return rs;
            }
            return null;
        } catch (SQLException e) {
            throw new ADExcepcion("Error al buscar cliente por ID en BD", e);
        }
    }

    // Guardar nuevo cliente
    public void guardarClienteBD(String p_nombres, String p_apellidos, String p_dni,
            String p_telefono, String p_email, String p_direccion) {
        String sql = " INSERT INTO clientes(nombres, apellidos, dni, telefono, email, direccion, activo) "
                + " VALUES(?, ?, ?, ?, ?, ?, TRUE) ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setString(1, p_nombres);
            pstmt.setString(2, p_apellidos);
            pstmt.setString(3, p_dni);
            pstmt.setString(4, p_telefono);
            pstmt.setString(5, p_email);
            pstmt.setString(6, p_direccion);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al guardar cliente en BD", e);
        }
    }

    // Actualizar cliente
    public void actualizarClienteBD(int p_idCliente, String p_nombres, String p_apellidos, String p_dni,
            String p_telefono, String p_email, String p_direccion) {
        String sql = " UPDATE clientes "
                + "    SET nombres = ?, apellidos = ?, dni = ?, telefono = ?, email = ?, direccion = ? "
                + "  WHERE id_cliente = ? ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setString(1, p_nombres);
            pstmt.setString(2, p_apellidos);
            pstmt.setString(3, p_dni);
            pstmt.setString(4, p_telefono);
            pstmt.setString(5, p_email);
            pstmt.setString(6, p_direccion);
            pstmt.setInt(7, p_idCliente);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al actualizar cliente en BD", e);
        }
    }

    // Baja lógica
    public void eliminarClienteBD(int p_idCliente) {
        String sql = " UPDATE clientes SET activo = FALSE WHERE id_cliente = ? ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setInt(1, p_idCliente);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al eliminar cliente en BD", e);
        }
    }

    // Reactivar cliente
    public void reactivarClienteBD(int p_idCliente) {
        String sql = " UPDATE clientes SET activo = TRUE WHERE id_cliente = ? ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setInt(1, p_idCliente);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al reactivar cliente en BD", e);
        }
    }

    // Buscar por nombre o apellido
    public ResultSet buscarClientesBD(String p_busqueda) {
        String sql = " SELECT id_cliente, nombres, apellidos, dni, telefono, email, direccion, activo "
                + "   FROM clientes "
                + "  WHERE (nombres ILIKE '%' || ? || '%' "
                + "     OR apellidos ILIKE '%' || ? || '%') "
                + "  ORDER BY activo DESC, nombres ASC ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setString(1, p_busqueda);
            pstmt.setString(2, p_busqueda);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al buscar clientes", e);
        }
    }

    // Verificar DNI duplicado
    public boolean existeDniBD(String p_dni, int p_idClienteExcluir) {
        String sql = "SELECT 1 FROM clientes WHERE dni = ? AND id_cliente != ?";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setString(1, p_dni);
            pstmt.setInt(2, p_idClienteExcluir);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al verificar DNI duplicado", e);
        }
    }

    public ResultSet verificarElegibilidadBD(int p_idCliente) {
        String sql = " SELECT " +
                     "   (SELECT COUNT(*) FROM prestamos " +
                     "     WHERE id_cliente = ? " +
                     "       AND estado IN ('DESEMBOLSADO','EN_MORA')) AS prestamos_activos, " +
                     "   (SELECT COUNT(*) FROM cuotas c " +
                     "     JOIN prestamos p ON p.id_prestamo = c.id_prestamo " +
                     "    WHERE p.id_cliente = ? " +
                     "      AND c.estado IN ('PENDIENTE','VENCIDO') " +
                     "      AND c.fecha_vencimiento < CURRENT_DATE) AS cuotas_vencidas, " +
                     "   (SELECT COALESCE(SUM(c.saldo_pendiente),0) FROM cuotas c " +
                     "     JOIN prestamos p ON p.id_prestamo = c.id_prestamo " +
                     "    WHERE p.id_cliente = ? " +
                     "      AND p.estado IN ('DESEMBOLSADO','EN_MORA')) AS deuda_total ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setInt(1, p_idCliente);
            pstmt.setInt(2, p_idCliente);
            pstmt.setInt(3, p_idCliente);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al verificar elegibilidad del cliente", e);
        }
    }

    public ResultSet obtenerAlertasVencidasBD() {
        String sql = " SELECT c.numero_cuota, "
                + "        cl.nombres || ' ' || cl.apellidos AS cliente, "
                + "        cl.telefono, "
                + "        p.id_prestamo, "
                + "        c.fecha_vencimiento, "
                + "        c.saldo_pendiente, "
                + "        (CURRENT_DATE - c.fecha_vencimiento) AS dias_atraso "
                + "   FROM cuotas c "
                + "   JOIN prestamos p  ON p.id_prestamo = c.id_prestamo "
                + "   JOIN clientes cl  ON cl.id_cliente = p.id_cliente "
                + "  WHERE c.estado IN ('PENDIENTE','VENCIDO') "
                + "    AND c.fecha_vencimiento < CURRENT_DATE "
                + "    AND cl.activo = TRUE "
                + "  ORDER BY dias_atraso DESC, cl.apellidos ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al obtener alertas de cuotas vencidas", e);
        }
    }
}
