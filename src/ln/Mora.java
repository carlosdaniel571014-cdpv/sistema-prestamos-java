/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ln;

import ad.MoraAD;
import exs.ADExcepcion;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author ASUS
 */
public class Mora extends MoraAD {

    public static final double TASA_MORA = 15.34753351;

    private int idPrestamo;
    private int idCuota;
    private int dias;
    private double kv;
    private double ic;
    private double tea;
    private double iMora;
    private double iCv;
    private double total;

    public Mora() {
    }

    public Mora(int p_idPrestamo) {
        this.idPrestamo = p_idPrestamo;
    }

    public int getIdPrestamo() {
        return idPrestamo;
    }

    public int getIdCuota() {
        return idCuota;
    }

    public int getDias() {
        return dias;
    }

    public double getKv() {
        return kv;
    }

    public double getIc() {
        return ic;
    }

    public double getTea() {
        return tea;
    }

    public double getIMora() {
        return iMora;
    }

    public double getICv() {
        return iCv;
    }

    public double getTotal() {
        return total;
    }

    public void setIdPrestamo(int v) {
        this.idPrestamo = v;
    }

    public void calcularMorasPrestamo() throws Exception {
        try {
            this.conectarBD();
            this.calcularMorasPrestamosBD(this.idPrestamo);
            this.confirmarCambios();
        } catch (Exception e) {
            this.descartarCambios();
            throw e;
        }
    }

    public void regularizarMora(int p_idCuota) throws Exception {
        try {
            this.conectarBD();
            this.regularizarMoraBD(p_idCuota);
            this.confirmarCambios();
        } catch (Exception e) {
            this.descartarCambios();
            throw e;
        }
    }

    public void cargarDetalleMora(int p_idCuota) {
        this.conectarBD();
        ResultSet rs = this.obtenerDetalleMoraBD(p_idCuota);
        try {
            if (rs.next()) {
                this.idCuota = p_idCuota;
                this.kv = rs.getDouble("kv");
                this.ic = rs.getDouble("ic");
                this.tea = rs.getDouble("tea");
                this.dias = rs.getInt("dias_calc");
                if (this.dias > 0) {
                    double ted = Math.pow(1 + this.tea / 100.0, 1.0 / 360.0) - 1;
                    this.iMora = Math.round(kv * ((TASA_MORA / 100.0 / 360.0) * dias) * 100.0) / 100.0;
                    this.iCv = Math.round((kv + ic) * (Math.pow(1 + ted, dias) - 1) * 100.0) / 100.0;
                    this.total = this.iMora + this.iCv;
                } else {
                    this.iMora = 0;
                    this.iCv = 0;
                    this.total = 0;
                }
            }
        } catch (SQLException e) {
            throw new ADExcepcion("Error al cargar detalle de mora", e);
        }
    }

    public ResultSet obtenerMoraActiva(int p_idCuota) {
        this.conectarBD();
        return this.obtenerMoraActivaBD(p_idCuota);
    }

    public ResultSet listarCuotasVencidas() {
        this.conectarBD();
        return this.listarCuotasVencidasBD(this.idPrestamo);
    }
}
