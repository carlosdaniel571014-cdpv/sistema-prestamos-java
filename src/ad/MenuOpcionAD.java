package ad;

import db.Conexion;
import exs.ADExcepcion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MenuOpcionAD extends Conexion {

    public void insertarMenuOpcionBD(String p_cargo, String p_nombre, Integer p_idPadre) {
        String sql = " INSERT INTO menu_opcion(cargo, nombre, id_padre) VALUES(?, ?, ?) ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setString(1, p_cargo);
            pstmt.setString(2, p_nombre);
            if (p_idPadre != null) {
                pstmt.setInt(3, p_idPadre);
            } else {
                pstmt.setNull(3, java.sql.Types.INTEGER);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al insertar opción de menú", e);
        }
    }

    public void eliminarMenuOpcionBD(int p_id) {
        String sql = " DELETE FROM menu_opcion WHERE id = ? ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setInt(1, p_id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al eliminar opción de menú", e);
        }
    }

    public ResultSet buscarPorCargoBD(String p_cargo) {
        String sql = " SELECT nombre FROM menu_opcion WHERE cargo = ? ORDER BY id ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setString(1, p_cargo);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al buscar opciones por cargo", e);
        }
    }

    public ResultSet buscarIdPorCargoYNombreBD(String p_cargo, String p_nombre) {
        String sql = " SELECT id FROM menu_opcion WHERE cargo = ? AND nombre = ? ";
        try {
            PreparedStatement pstmt = this.obtenerConexion().prepareStatement(sql);
            pstmt.setString(1, p_cargo);
            pstmt.setString(2, p_nombre);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            throw new ADExcepcion("Error al buscar id de opción de menú", e);
        }
    }
}
