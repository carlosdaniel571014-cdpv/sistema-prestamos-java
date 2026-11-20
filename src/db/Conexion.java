package db;

import exs.ADExcepcion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    Connection con;
    // Datos de conexión
    String url = "jdbc:postgresql://localhost:5432/proyecto_DAE";
    String usuario = "postgres";
    String pass = System.getenv().getOrDefault("DB_PASS", "");

    public void conectarBD(){
        try{
            // Cargar driver y establecer conexión
            this.con = DriverManager.getConnection(this.url, this.usuario, 
                                this.pass);
            this.con.setAutoCommit(false);
        } catch (SQLException e) {
            if ("08001".equals(e.getSQLState()))  
                throw new ADExcepcion("No hay driver", e);
            if ("3D000".equals(e.getSQLState()))  
                throw new ADExcepcion("No existe la BD", e);
            if ("28P01".equals(e.getSQLState()))  
                throw new ADExcepcion("La contraseña de la cadena de conexión es incorrecta", e);
            throw new ADExcepcion("Error desconocido");
        }
    }
    
    public Connection obtenerConexion(){
        try{
            return this.con;
        }catch (Exception e) {
            throw e;
        }
    }
    public void asignarConexion(Connection p_con){
        try{
            this.con = p_con;
        }catch (Exception e) {
            throw e;
        }
    }
    public void confirmarCambios() throws Exception{
        try{
            this.con.commit();
        }catch(SQLException e){
            throw e;
        }
    }
    
    public void descartarCambios() throws Exception{
        try{
            this.con.rollback();
        }catch(SQLException e){
            throw e;
        }
    }
}
