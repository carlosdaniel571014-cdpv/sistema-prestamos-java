package ln;

import ad.HistorialAD;
import java.sql.ResultSet;

public class HistorialCrediticio extends HistorialAD {
    private int     idHistorial;
    private int     idCliente;
    private int     scoreCrediticio;
    private boolean deudasActivas;
    private double  montoDeuda;
    private String  entidadReporte;
    private String  observaciones;

    public HistorialCrediticio(int obj_idcliente) {
        this.idCliente = obj_idcliente;
    }

    public int     getIdHistorial()     { return idHistorial; }
    public int     getIdCliente()       { return idCliente; }
    public int     getScoreCrediticio() { return scoreCrediticio; }
    public boolean isDeudasActivas()    { return deudasActivas; }
    public double  getMontoDeuda()      { return montoDeuda; }
    public String  getEntidadReporte()  { return entidadReporte; }
    public String  getObservaciones()   { return observaciones; }

    public void setIdHistorial(int v)         { this.idHistorial = v; }
    public void setIdCliente(int v)           { this.idCliente = v; }
    public void setScoreCrediticio(int v)     { this.scoreCrediticio = v; }
    public void setDeudasActivas(boolean v)   { this.deudasActivas = v; }
    public void setMontoDeuda(double v)       { this.montoDeuda = v; }
    public void setEntidadReporte(String v)   { this.entidadReporte = v; }
    public void setObservaciones(String v)    { this.observaciones = v; }

    // Busca el historial del cliente y lo carga en el objeto
    public boolean consultarHistorial()throws Exception {
        try {
            this.conectarBD();
            ResultSet rs = this.buscarHistorialBD(this.idCliente);
            if (rs == null) return false;
            if (rs.next()) {
                this.idHistorial     = rs.getInt("id_historial");
                this.scoreCrediticio = rs.getInt("score_crediticio");
                this.deudasActivas   = rs.getBoolean("deudas_activas");
                this.montoDeuda      = rs.getDouble("monto_deuda");
                this.entidadReporte  = rs.getString("entidad_reporte");
                this.observaciones   = rs.getString("observaciones");
                return true;
            }
            return false;
        } catch (Exception e) {
            throw e;
        }
    }
}
