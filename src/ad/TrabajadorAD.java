package ad;

import db.Conexion;
import exs.ADExcepcion;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class TrabajadorAD extends Conexion {

    // ─── BUSCAR POR ID ────────────────────────────────────────────────────────
    public ResultSet buscarTrabajadorBD(int p_idTrabajador) {
        String sql = " SELECT id_trabajador, nombres, apellidos, dni, cargo, email, activo "
                + "   FROM trabajadores "
                + "  WHERE id_trabajador = ? ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setInt(1, p_idTrabajador);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                if (!rs.getBoolean("activo")) {
                    throw new ADExcepcion("Trabajador desactivado");
                }
                return rs;
            }
            return null;
        } catch (SQLException e) {
            throw new ADExcepcion("Error al buscar trabajador en BD", e);
        }
    }

    // ─── BUSCAR POR DNI ───────────────────────────────────────────────────────
    public ResultSet buscarTrabajadorPorDniBD(String p_dni) {
        String sql = " SELECT id_trabajador, nombres, apellidos, dni, cargo, email, activo "
                + "   FROM trabajadores "
                + "  WHERE dni = ? ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setString(1, p_dni);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                if (!rs.getBoolean("activo")) {
                    throw new ADExcepcion("Trabajador desactivado");
                }
                return rs;
            }
            return null;
        } catch (SQLException e) {
            throw new ADExcepcion("Error al buscar trabajador por DNI en BD", e);
        }
    }

    // ─── LISTAR TODOS ─────────────────────────────────────────────────────────
    public ResultSet listarTrabajadoresBD() {
        String sql = " SELECT id_trabajador, nombres, apellidos, dni, cargo, email, activo "
                + "   FROM trabajadores "
                + "  ORDER BY apellidos, nombres ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al listar trabajadores en BD", e);
        }
    }

    // ─── BUSCAR POR ID, NOMBRE O DNI (ILIKE) ──────────────────────────────────
    public ResultSet buscarTrabajadoresBD(String p_busqueda) {
        String sql = " SELECT id_trabajador, nombres, apellidos, dni, cargo, email, activo "
                + "   FROM trabajadores "
                + "  WHERE (CAST(id_trabajador AS TEXT) ILIKE '%' || ? || '%' "
                + "     OR nombres  ILIKE '%' || ? || '%' "
                + "     OR apellidos ILIKE '%' || ? || '%' "
                + "     OR dni       ILIKE '%' || ? || '%') "
                + "  ORDER BY apellidos, nombres ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setString(1, p_busqueda);
            pstmt.setString(2, p_busqueda);
            pstmt.setString(3, p_busqueda);
            pstmt.setString(4, p_busqueda);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al buscar trabajadores en BD", e);
        }
    }

    // ─── LISTAR SOLO ACTIVOS ──────────────────────────────────────────────────
    public ResultSet listarTrabajadoresActivosBD() {
        String sql = " SELECT id_trabajador, nombres, apellidos, dni, cargo, email, activo "
                + "   FROM trabajadores "
                + "  WHERE activo = TRUE "
                + "  ORDER BY apellidos, nombres ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al listar trabajadores activos en BD", e);
        }
    }

    // ─── AUTENTICAR (LOGIN) ───────────────────────────────────────────────────
    // NOTA: p_contrasena ya debe llegar hasheada con SHA-256 desde la capa LN
    public ResultSet autenticarTrabajadorBD(String p_email, String p_contrasena) {
        String sql = " SELECT id_trabajador, nombres, apellidos, dni, cargo, email, activo "
                + "   FROM trabajadores "
                + "  WHERE (email = ? OR dni = ?) "
                + "    AND contrasena = ? ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setString(1, p_email);
            pstmt.setString(2, p_email);
            pstmt.setString(3, p_contrasena);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                if (!rs.getBoolean("activo")) {
                    throw new ADExcepcion("Cuenta desactivada. Contacte al administrador.");
                }
                return rs;
            }
            return null;
        } catch (SQLException e) {
            throw new ADExcepcion("Error al autenticar trabajador en BD", e);
        }
    }

    // ─── INSERTAR ─────────────────────────────────────────────────────────────
    // NOTA: p_contrasena ya debe llegar hasheada con SHA-256 desde la capa LN
    public int insertarTrabajadorBD(String p_nombres, String p_apellidos, String p_dni,
            String p_cargo, String p_email, String p_contrasena) {
        String sql = " INSERT INTO trabajadores (nombres, apellidos, dni, cargo, email, contrasena) "
                + " VALUES (?, ?, ?, ?, ?, ?) "
                + " RETURNING id_trabajador ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setString(1, p_nombres);
            pstmt.setString(2, p_apellidos);
            pstmt.setString(3, p_dni);
            pstmt.setString(4, p_cargo);
            pstmt.setString(5, p_email);
            pstmt.setString(6, p_contrasena);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new ADExcepcion("No se pudo obtener el ID del trabajador insertado");
        } catch (SQLException e) {
            throw new ADExcepcion("Error al insertar trabajador en BD", e);
        }
    }

    // ─── ACTUALIZAR DATOS ─────────────────────────────────────────────────────
    public boolean actualizarTrabajadorBD(int p_idTrabajador, String p_nombres, String p_apellidos,
            String p_dni, String p_cargo, String p_email) {
        String sql = " UPDATE trabajadores "
                + "    SET nombres   = ?, "
                + "        apellidos = ?, "
                + "        dni       = ?, "
                + "        cargo     = ?, "
                + "        email     = ? "
                + "  WHERE id_trabajador = ? ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setString(1, p_nombres);
            pstmt.setString(2, p_apellidos);
            pstmt.setString(3, p_dni);
            pstmt.setString(4, p_cargo);
            pstmt.setString(5, p_email);
            pstmt.setInt(6, p_idTrabajador);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ADExcepcion("Error al actualizar trabajador en BD", e);
        }
    }

    // ─── CAMBIAR CONTRASEÑA ───────────────────────────────────────────────────
    // NOTA: ambas contraseñas ya deben llegar hasheadas con SHA-256 desde la capa LN
    public boolean cambiarContrasenaBD(int p_idTrabajador, String p_contrasenaActual,
            String p_contrasenaNueva) {
        String sql = " UPDATE trabajadores "
                + "    SET contrasena = ? "
                + "  WHERE id_trabajador = ? "
                + "    AND contrasena    = ? ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setString(1, p_contrasenaNueva);
            pstmt.setInt(2, p_idTrabajador);
            pstmt.setString(3, p_contrasenaActual);
            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                throw new ADExcepcion("La contraseña actual es incorrecta");
            }
            return true;
        } catch (ADExcepcion e) {
            throw e;
        } catch (SQLException e) {
            throw new ADExcepcion("Error al cambiar contraseña en BD", e);
        }
    }

    // ─── CAMBIAR CONTRASEÑA SIN VERIFICAR ACTUAL (solo admin) ─────────────────
    // NOTA: p_contrasenaNueva ya debe llegar hasheada con SHA-256 desde la capa LN
    public boolean cambiarContrasenaAdminBD(int p_idTrabajador, String p_contrasenaNueva) {
        String sql = " UPDATE trabajadores "
                + "    SET contrasena = ? "
                + "  WHERE id_trabajador = ? ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setString(1, p_contrasenaNueva);
            pstmt.setInt(2, p_idTrabajador);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ADExcepcion("Error al cambiar contraseña (admin) en BD", e);
        }
    }

    // ─── DESACTIVAR (baja lógica) ─────────────────────────────────────────────
    public boolean desactivarTrabajadorBD(int p_idTrabajador) {
        String sql = " UPDATE trabajadores "
                + "    SET activo = FALSE "
                + "  WHERE id_trabajador = ? ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setInt(1, p_idTrabajador);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ADExcepcion("Error al desactivar trabajador en BD", e);
        }
    }

    // ─── REACTIVAR ────────────────────────────────────────────────────────────
    public boolean reactivarTrabajadorBD(int p_idTrabajador) {
        String sql = " UPDATE trabajadores "
                + "    SET activo = TRUE "
                + "  WHERE id_trabajador = ? ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setInt(1, p_idTrabajador);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ADExcepcion("Error al reactivar trabajador en BD", e);
        }
    }

    // ─── VERIFICAR DNI DUPLICADO ──────────────────────────────────────────────
    public boolean existeDniBD(String p_dni, int p_idExcluir) {
        String sql = " SELECT 1 FROM trabajadores "
                + "  WHERE dni = ? "
                + "    AND id_trabajador <> ? ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setString(1, p_dni);
            pstmt.setInt(2, p_idExcluir);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al verificar DNI en BD", e);
        }
    }

    // ─── VERIFICAR EMAIL DUPLICADO ────────────────────────────────────────────
    public boolean existeEmailBD(String p_email, int p_idExcluir) {
        String sql = " SELECT 1 FROM trabajadores "
                + "  WHERE email = ? "
                + "    AND id_trabajador <> ? ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setString(1, p_email);
            pstmt.setInt(2, p_idExcluir);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al verificar email en BD", e);
        }
    }

    // ─── OBTENER CARGOS ───────────────────────────────────────────────────────
    public ResultSet obtenerCargoBD() {
        String sql = " SELECT DISTINCT cargo FROM trabajadores ORDER BY cargo ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al obtener cargos en BD", e);
        }
    }
}
