/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ln;

/**
 *
 * @author PEÑA
 */
import ad.ReporteAD;
import java.sql.ResultSet;

public class Reporte extends ReporteAD {
    // REPORTE CARTERA
    // Devuelve ResultSet con columnas: estado, cantidad, monto_total
    public ResultSet obtenerResumenCartera() {
        this.conectarBD();
        return this.obtenerResumenCarteraBD();
    }
    
    // REPORTE MOROSIDAD
    public ResultSet obtenerCabeceraReporteCuota(int idPrestamo) {
    this.conectarBD();
    return this.obtenerCabeceraReporteCuotaBD(idPrestamo);
 }
    
 public ResultSet obtenerReporteMorosidad() {
    this.conectarBD();
    return this.obtenerReporteMorosidadBD();
}

public ResultSet obtenerMorosidadPorCliente() {
    this.conectarBD();
    return this.obtenerMorosidadPorClienteBD();
}

// REPORTE HISTORIAL CLIENTE:

public ResultSet obtenerListaClientes() {
    this.conectarBD();
    return this.obtenerListaClientesBD();
}

public ResultSet obtenerHistorialPrestamos(int idCliente) {
    this.conectarBD();
    return this.obtenerHistorialPrestamosBD(idCliente);
}

//REPORTE DE RENDIMIENTO POR TRABAJADOR
public ResultSet obtenerRendimientoTrabajadores() {
    this.conectarBD();
    return this.obtenerRendimientoTrabajadoresBD();
}
// REPORTE METODO PAGO
public ResultSet obtenerMetodosPago() {
    this.conectarBD();
    return this.obtenerMetodosPagoBD();
}
}
