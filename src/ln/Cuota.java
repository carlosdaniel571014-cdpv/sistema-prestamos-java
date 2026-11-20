package ln;

import ad.CuotaAD;
import java.sql.ResultSet;

public class Cuota extends CuotaAD {
    private int    idPrestamo;
    private int    numeroCuota;
    private double montoCuota;
    private double montoCapital;
    private double montoInteres;
    private double saldoPendiente;
    private String estado;

    public Cuota(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public Cuota() {
    }

    public int    getIdPrestamo()    { return idPrestamo; }
    public int    getNumeroCuota()   { return numeroCuota; }
    public double getMontoCuota()    { return montoCuota; }
    public double getMontoCapital()  { return montoCapital; }
    public double getMontoInteres()  { return montoInteres; }
    public double getSaldoPendiente(){ return saldoPendiente; }
    public String getEstado()        { return estado; }

    public void setIdPrestamo(int v)      { this.idPrestamo = v; }
    public void setNumeroCuota(int v)     { this.numeroCuota = v; }
    public void setMontoCuota(double v)   { this.montoCuota = v; }
    public void setMontoCapital(double v) { this.montoCapital = v; }
    public void setMontoInteres(double v) { this.montoInteres = v; }
    public void setSaldoPendiente(double v){ this.saldoPendiente = v; }
    public void setEstado(String v)       { this.estado = v; }

    public ResultSet listarCuotas(){
        this.conectarBD();
        return this.listarCuotasBD(this.idPrestamo);
    }
}
