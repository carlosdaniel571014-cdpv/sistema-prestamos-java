/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ln;

import ad.NotificacionCuota_AD;
import java.sql.Timestamp;
import java.util.ArrayList;

public class NotificacionCuota extends NotificacionCuota_AD {

    // ── Atributos ────────────────────────────────────────────
    private int       idNotificacion;
    private int       idCuota;
    private int       idTrabajador;
    private String    canal;
    private String    tipoAlerta;
    private String    mensaje;
    private boolean   enviado;
    private Timestamp fechaEnvio;
    private Timestamp createdAt;

    // Campos extra que vienen del JOIN (para mostrar en la tabla)
    private String    nombreCliente;
    private int       numeroCuota;
    private String    estadoCuota;
    private String    fechaVencimiento;  // se guarda como String formateado

    // ── Constructores ────────────────────────────────────────
    public NotificacionCuota() {
    }

    public NotificacionCuota(int idCuota, int idTrabajador,
                              String tipoAlerta, String mensaje) {
        this.idCuota      = idCuota;
        this.idTrabajador = idTrabajador;
        this.tipoAlerta   = tipoAlerta;
        this.mensaje      = mensaje;
        this.canal        = "SISTEMA";
        this.enviado      = false;
    }

    
    public int       getIdNotificacion()  { return idNotificacion; }
    public int       getIdCuota()         { return idCuota; }
    public int       getIdTrabajador()    { return idTrabajador; }
    public String    getCanal()           { return canal; }
    public String    getTipoAlerta()      { return tipoAlerta; }
    public String    getMensaje()         { return mensaje; }
    public boolean   isEnviado()          { return enviado; }
    public Timestamp getFechaEnvio()      { return fechaEnvio; }
    public Timestamp getCreatedAt()       { return createdAt; }
    public String    getNombreCliente()   { return nombreCliente; }
    public int       getNumeroCuota()     { return numeroCuota; }
    public String    getEstadoCuota()     { return estadoCuota; }
    public String    getFechaVencimiento(){ return fechaVencimiento; }

    
    public void setIdNotificacion(int v)    { this.idNotificacion  = v; }
    public void setIdCuota(int v)           { this.idCuota         = v; }
    public void setIdTrabajador(int v)      { this.idTrabajador    = v; }
    public void setCanal(String v)          { this.canal           = v; }
    public void setTipoAlerta(String v)     { this.tipoAlerta      = v; }
    public void setMensaje(String v)        { this.mensaje         = v; }
    public void setEnviado(boolean v)       { this.enviado         = v; }
    public void setFechaEnvio(Timestamp v)  { this.fechaEnvio      = v; }
    public void setCreatedAt(Timestamp v)   { this.createdAt       = v; }
    public void setNombreCliente(String v)  { this.nombreCliente   = v; }
    public void setNumeroCuota(int v)       { this.numeroCuota     = v; }
    public void setEstadoCuota(String v)    { this.estadoCuota     = v; }
    public void setFechaVencimiento(String v){ this.fechaVencimiento = v; }

    // ── Métodos de negocio (delegan a la capa AD) ────────────

    /**
     * Escanea la BD y genera las notificaciones nuevas que aún no existan.
     * Llama a la capa AD que hace el INSERT de cuotas VENCIDAS y PENDIENTES
     * que vencen en exactamente 3 días.
     */
    public void escanearYGenerarNotificaciones() throws Exception {
        try {
            this.conectarBD();
            this.escanearYGenerarBD();
            this.confirmarCambios();
        } catch (Exception e) {
            this.descartarCambios();
            throw e;
        }
    }

    /**
     * Retorna la lista de notificaciones activas (enviado = FALSE)
     * filtradas según el cargo del trabajador que inició sesión.
     *
     * @param cargo         "admin" o "trabajador"
     * @param idTrabajador  ID del trabajador logueado
     */
    public ArrayList<NotificacionCuota> listarActivas(String cargo,
                                                       int idTrabajador) {
        this.conectarBD();
        return this.listarActivasBD(cargo, idTrabajador);
    }

    /**
     * Marca una notificación como revisada (enviado = TRUE, fecha_envio = NOW).
     *
     * @param idNotificacion ID de la notificación a marcar
     */
    public void marcarComoRevisada(int idNotificacion) throws Exception {
        try {
            this.conectarBD();
            this.marcarComoRevisadaBD(idNotificacion);
            this.confirmarCambios();
        } catch (Exception e) {
            this.descartarCambios();
            throw e;
        }
    }
}