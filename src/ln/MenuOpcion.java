package ln;

import ad.MenuOpcionAD;
import java.sql.ResultSet;

public class MenuOpcion extends MenuOpcionAD {
    private int id;
    private String cargo;
    private String nombre;
    private Integer idPadre;

    public MenuOpcion(String p_cargo, String p_nombre, Integer p_idPadre) {
        this.cargo = p_cargo;
        this.nombre = p_nombre;
        this.idPadre = p_idPadre;
    }

    public MenuOpcion(String p_cargo, String p_nombre) {
        this.cargo = p_cargo;
        this.nombre = p_nombre;
    }

    public MenuOpcion() {
    }

    public int getId() { return id; }
    public String getCargo() { return cargo; }
    public String getNombre() { return nombre; }
    public Integer getIdPadre() { return idPadre; }

    public void setId(int v) { this.id = v; }
    public void setCargo(String v) { this.cargo = v; }
    public void setNombre(String v) { this.nombre = v; }
    public void setIdPadre(Integer v) { this.idPadre = v; }

    public void insertarMenuOpcion() throws Exception {
        try {
            this.conectarBD();
            this.insertarMenuOpcionBD(this.cargo, this.nombre, this.idPadre);
            this.confirmarCambios();
        } catch (Exception e) {
            this.descartarCambios();
            throw e;
        }
    }

    public void eliminarMenuOpcion(int p_id) throws Exception {
        try {
            this.conectarBD();
            this.eliminarMenuOpcionBD(p_id);
            this.confirmarCambios();
        } catch (Exception e) {
            this.descartarCambios();
            throw e;
        }
    }

    public ResultSet buscarPorCargo(String p_cargo) throws Exception {
        this.conectarBD();
        return this.buscarPorCargoBD(p_cargo);
    }

    public int buscarIdPorCargoYNombre() throws Exception {
        this.conectarBD();
        ResultSet rs = this.buscarIdPorCargoYNombreBD(this.cargo, this.nombre);
        int id = 0;
        if (rs.next()) {
            id = rs.getInt("id");
        }
        rs.close();
        return id;
    }
}
