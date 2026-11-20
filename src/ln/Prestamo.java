package ln;

import ad.PrestamoAD;
import java.sql.Date;
import java.sql.ResultSet;
import ln.Cuota;

public class Prestamo extends PrestamoAD {
    private int    idPrestamo;
    private int    idCliente;
    private int    idTrabajador;
    private double montoSolicitado;
    private double montoAprobado;
    private double tasaInteres;
    private int    plazoCuotas;
    private double cuotaMonto;
    private String frecuenciaPago;
    private String estado;
    private String motivoRechazo;

    public Prestamo(int idCliente, int idTrabajador, double montoSolicitado, double tasaInteres, int plazoCuotas, String frecuenciaPago) {
        this.idCliente = idCliente;
        this.idTrabajador = idTrabajador;
        this.montoSolicitado = montoSolicitado;
        this.tasaInteres = tasaInteres;
        this.plazoCuotas = plazoCuotas;
        this.frecuenciaPago = frecuenciaPago;
    }

    public Prestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public Prestamo() {
    }
    
    public int    getIdPrestamo()      { return idPrestamo; }
    public int    getIdCliente()       { return idCliente; }
    public int    getIdTrabajador()    { return idTrabajador; }
    public double getMontoSolicitado() { return montoSolicitado; }
    public double getMontoAprobado()   { return montoAprobado; }
    public double getTasaInteres()     { return tasaInteres; }
    public int    getPlazoCuotas()     { return plazoCuotas; }
    public double getCuotaMonto()      { return cuotaMonto; }
    public String getFrecuenciaPago()  { return frecuenciaPago; }
    public String getEstado()          { return estado; }
    public String getMotivoRechazo()   { return motivoRechazo; }

    public void setIdPrestamo(int v)        { this.idPrestamo = v; }
    public void setIdCliente(int v)         { this.idCliente = v; }
    public void setIdTrabajador(int v)      { this.idTrabajador = v; }
    public void setMontoSolicitado(double v){ this.montoSolicitado = v; }
    public void setMontoAprobado(double v)  { this.montoAprobado = v; }
    public void setTasaInteres(double v)    { this.tasaInteres = v; }
    public void setPlazoCuotas(int v)       { this.plazoCuotas = v; }
    public void setCuotaMonto(double v)     { this.cuotaMonto = v; }
    public void setFrecuenciaPago(String v) { this.frecuenciaPago = v; }
    public void setEstado(String v)         { this.estado = v; }
    public void setMotivoRechazo(String v)  { this.motivoRechazo = v; }

    public void registrarPrestamo() throws Exception {
        try {
            this.conectarBD();
            this.idPrestamo = this.registrarSolicitudBD(
                this.idCliente, this.idTrabajador,
                this.montoSolicitado, this.tasaInteres,
                this.plazoCuotas, this.frecuenciaPago);
            this.confirmarCambios();
        } catch (Exception e) {
            this.descartarCambios();
            throw e;
        }
    }

    public void aprobarPrestamo(double p_montoAprobado) throws Exception {
        try {
            this.conectarBD();
            this.aprobarPrestamoBD(this.idPrestamo, this.idTrabajador, "APROBADO", p_montoAprobado, null);
            this.confirmarCambios();
        } catch (Exception e) {
            this.descartarCambios();
            throw e;
        }
    }

    public void rechazarPrestamo(String p_motivoRechazo) throws Exception {
        try {
            this.conectarBD();
            this.aprobarPrestamoBD(this.idPrestamo, this.idTrabajador, "RECHAZADO", null, p_motivoRechazo);
            this.confirmarCambios();
        } catch (Exception e) {
            this.descartarCambios();
            throw e;
        }
    }

    public ResultSet buscarPrestamos(String p_busqueda){
        this.conectarBD();
        return this.buscarPrestamosBD(p_busqueda);
    }
    
    public boolean cargarDatos() throws Exception {
        try {
            this.conectarBD();
            ResultSet rs = this.obtenerPrestamoBD(this.idPrestamo);
            if (rs.next()) {
                this.idCliente      = rs.getInt("id_cliente");
                this.idTrabajador   = rs.getInt("id_trabajador");
                this.montoSolicitado = rs.getDouble("monto_solicitado");
                this.montoAprobado   = rs.getDouble("monto_aprobado");
                this.tasaInteres     = rs.getDouble("tasa_interes");
                this.plazoCuotas     = rs.getInt("plazo_cuotas");
                this.cuotaMonto      = rs.getDouble("cuota_monto");
                this.frecuenciaPago  = rs.getString("frecuencia_pago");
                this.estado          = rs.getString("estado");
                this.motivoRechazo   = rs.getString("motivo_rechazo");
                return true;
            }
            return false;
        } catch (Exception e) {
            throw e;
        }
    }

    public int obtenerPlazoCuotas(){
        this.conectarBD();
        return this.obtenerPlazoCuotasBD(this.idPrestamo);
    }

    public double obtenerSaldoPendienteTotal(){
        this.conectarBD();
        return this.obtenerSaldoPendienteTotalBD(this.idPrestamo);
    }

    public void consolidarCuotas(int[] p_idsCuotas, int[] p_nrosCuotas,
                                  double[] p_montosCuota, double[] p_montosCapital,
                                  double[] p_montosInteres, double[] p_saldosPendientes,
                                  Date[] p_fechasVenc) throws Exception {
        try {
            this.conectarBD();
            Cuota objCuota = new Cuota();
            objCuota.asignarConexion(this.obtenerConexion());

            int n = p_idsCuotas.length;
            int nroConsolidado = p_nrosCuotas[0];
            Date primeraFecha = p_fechasVenc[0];

            double sumaMonto = 0, sumaCapital = 0, sumaInteres = 0, sumaSaldo = 0;
            for (int i = 0; i < n; i++) {
                sumaMonto   += p_montosCuota[i];
                sumaCapital += p_montosCapital[i];
                sumaInteres += p_montosInteres[i];
                sumaSaldo   += p_saldosPendientes[i];
            }

            objCuota.eliminarCuotasBD(this.idPrestamo, p_idsCuotas);
            objCuota.insertarCuotaBD(this.idPrestamo, nroConsolidado, primeraFecha,
                                     sumaMonto, sumaCapital, sumaInteres, sumaSaldo);

            int desplazamiento = n - 1;
            int diasPorPeriodo;
            switch (this.frecuenciaPago) {
                case "DIARIO":    diasPorPeriodo = 1;  break;
                case "QUINCENAL": diasPorPeriodo = 15; break;
                case "SEMANAL":   diasPorPeriodo = 7;  break;
                default:          diasPorPeriodo = 30;
            }

            objCuota.renumerarCuotasBD(this.idPrestamo, nroConsolidado + n, desplazamiento);
            objCuota.actualizarFechasCuotasBD(this.idPrestamo, nroConsolidado + n,
                                              desplazamiento * diasPorPeriodo);
        } catch (Exception e) {
            this.descartarCambios();
            throw e;
        }
    }

    public boolean existePrestamoPorId(){
        this.conectarBD();
        return this.existePrestamoPorIdBD(this.idPrestamo);
    }

    public void actualizarPrestamo(double p_montoAprobado, double p_tasaInteres,
                                   int p_plazoCuotas, String p_frecuenciaPago) throws Exception {
        try {
            this.conectarBD();
            this.actualizarPrestamoBD(this.idPrestamo, p_montoAprobado,
                                      p_tasaInteres, p_plazoCuotas, p_frecuenciaPago);
            this.confirmarCambios();
        } catch (Exception e) {
            this.descartarCambios();
            throw e;
        }
    }

    public void cancelarPrestamo() throws Exception {
        try {
            this.conectarBD();
            this.cancelarPrestamoBD(this.idPrestamo);
            this.confirmarCambios();
        } catch (Exception e) {
            this.descartarCambios();
            throw e;
        }
    }
}
