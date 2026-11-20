/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ln;

import ad.ComprobanteAD;
import java.sql.ResultSet;

/**
 *
 * @author ASUS
 */
public class Comprobante extends ComprobanteAD {
 
    public static final double TASA_ITF = 0.00005;
 
    private int    idComprobante;
    private int    idPago;
    private int    idPrestamo;
    private int    idCuota;
    private int    idMora;
    private String numeroSerie;
    private String tipo;
    private String cliente;
    private String dni;
    private String trabajador;
    private String fechaPago;
    private double montoTotal;
    private double itf;
    private double totalConITF;
    private String observaciones;
 
    public Comprobante() {}
    public Comprobante(int idPago) { this.idPago = idPago; }
 
    public int    getIdComprobante()  { return idComprobante; }
    public int    getIdPago()         { return idPago; }
    public int    getIdPrestamo()     { return idPrestamo; }
    public int    getIdCuota()        { return idCuota; }
    public int    getIdMora()         { return idMora; }
    public String getNumeroSerie()    { return numeroSerie; }
    public String getTipo()           { return tipo; }
    public String getCliente()        { return cliente; }
    public String getDni()            { return dni; }
    public String getTrabajador()     { return trabajador; }
    public String getFechaPago()      { return fechaPago; }
    public double getMontoTotal()     { return montoTotal; }
    public double getItf()            { return itf; }
    public double getTotalConITF()    { return totalConITF; }
    public String getObservaciones()  { return observaciones; }
 
    public void setIdComprobante(int idComprobante)    { this.idComprobante = idComprobante; }
    public void setIdPago(int idPago)                  { this.idPago = idPago; }
    public void setIdPrestamo(int idPrestamo)          { this.idPrestamo = idPrestamo; }
    public void setIdCuota(int idCuota)                { this.idCuota = idCuota; }
    public void setIdMora(int idMora)                  { this.idMora = idMora; }
    public void setNumeroSerie(String numeroSerie)     { this.numeroSerie = numeroSerie; }
    public void setTipo(String tipo)                   { this.tipo = tipo; }
    public void setCliente(String cliente)             { this.cliente = cliente; }
    public void setDni(String dni)                     { this.dni = dni; }
    public void setTrabajador(String trabajador)       { this.trabajador = trabajador; }
    public void setFechaPago(String fechaPago)         { this.fechaPago = fechaPago; }
    public void setMontoTotal(double montoTotal)       { this.montoTotal = montoTotal; }
    public void setItf(double itf)                     { this.itf = itf; }
    public void setTotalConITF(double totalConITF)     { this.totalConITF = totalConITF; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
 
    public double calcularITF(double monto) {
        return Math.round(monto * TASA_ITF * 100.0) / 100.0;
    }
 
    /**
     * Genera e inserta un comprobante en BD.
     * El número de serie se genera dentro de la misma conexión
     * para evitar que conectarBD() sea llamado dos veces y pise this.con.
     */
    public void registrarComprobante(int idPrestamo, int idCuota,
            Integer idMora, String tipo,
            double monto, String observaciones) throws Exception {
        try {
            this.conectarBD();
            int nro  = this.obtenerUltimoNumeroComprobanteBD();
            int anio = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
            String numeroSerie = String.format("BOL-%d-%04d", anio, nro + 1);
            this.registrarComprobanteBD(idPrestamo, idCuota, idMora,
                    tipo, numeroSerie, monto, observaciones);
            this.confirmarCambios();
        } catch (Exception e) {
            this.descartarCambios();
            throw e;
        }
    }
 
    public boolean tieneComprobantes(int idPago) {
        this.conectarBD();
        return this.tieneComprobantesBD(idPago);
    }
 
    public ResultSet listarPagos() {
        this.conectarBD();
        return this.listarPagosBD();
    }
 
    public ResultSet obtenerCabecera(int idPago) {
        this.conectarBD();
        return this.obtenerCabeceraPagoBD(idPago);
    }
 
    public ResultSet obtenerCuotasPago(int idPago) {
        this.conectarBD();
        return this.obtenerCuotasPagoBD(idPago);
    }
 
    public ResultSet obtenerMediosPago(int idPago) {
        this.conectarBD();
        return this.obtenerMediosPagoBD(idPago);
    }
 
    public ResultSet listarComprobantes(int idPago) {
        this.conectarBD();
        return this.listarComprobantesBD(idPago);
    }
}
