/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ln;

import ad.PagoAD;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

/**
 *
 * @author ASUS
 */
public class Pago extends PagoAD {

    private int idPrestamo;
    private int idTrabajador;
    // Cuotas seleccionadas para pagar
    // Cada Object[]: { idCuota(int), numeroCuota(int), montoAbonar(double) }
    private final List<Object[]> cuotasAPagar = new ArrayList<>();
    // Adelanto opcional a la cuota siguiente
    // Object[]: { idCuota(int), numeroCuota(int), montoAdelanto(double) }
    private Object[] adelanto = null;

    // Medios de pago ingresados
    // Cada Object[]: { idMetodoPago(int), monto(double), referencia(String) }
    private final List<Object[]> medios = new ArrayList<>();
    private double totalMedios = 0;

    public Pago(int p_idPrestamo, int p_idTrabajador) {
        this.idPrestamo = p_idPrestamo;
        this.idTrabajador = p_idTrabajador;
    }

    public int getIdPrestamo() {
        return idPrestamo;
    }

    public int getIdTrabajador() {
        return idTrabajador;
    }

    public Object[] getAdelanto() {
        return adelanto;
    }

    public void setAdelanto(Object[] adelanto) {
        this.adelanto = adelanto;
    }

    public void agregarCuota(int p_idCuota, int p_numeroCuota, double p_montoAbonar, double p_mora) {
        cuotasAPagar.add(new Object[]{p_idCuota, p_numeroCuota, p_montoAbonar, p_mora});
    }

    public void quitarCuota(int p_idCuota) {
        cuotasAPagar.removeIf(c -> (int) c[0] == p_idCuota);
    }

    public void limpiarCuotas() {
        cuotasAPagar.clear();
    }

    public List<Object[]> getCuotasAPagar() {
        return cuotasAPagar;
    }

    public double totalCuotas() {
        double total = 0;
        for (Object[] c : cuotasAPagar) {
            total += (double) c[2] + (double) c[3];
        }
        return round2(total);
    }

    public int numeroCuotaMinima() {
        int min = Integer.MAX_VALUE;
        for (Object[] c : cuotasAPagar) {
            int nro = (int) c[1];
            if (nro < min) {
                min = nro;
            }
        }
        return min == Integer.MAX_VALUE ? 0 : min;
    }

    public int numeroCuotaMaxima() {
        int max = 0;
        for (Object[] c : cuotasAPagar) {
            int nro = (int) c[1];
            if (nro > max) {
                max = nro;
            }
        }
        return max;
    }

    public void setAdelanto(int p_idCuota, int p_numeroCuota, double p_monto) throws Exception {
        if (p_monto <= 0) {
            throw new Exception("El monto del adelanto debe ser mayor a 0.");
        }
        this.adelanto = new Object[]{p_idCuota, p_numeroCuota, p_monto};
    }

    public void quitarAdelanto() {
        this.adelanto = null;
    }

    public boolean tieneAdelanto() {
        return adelanto != null;
    }

    public double montoAdelanto() {
        return adelanto != null ? (double) adelanto[2] : 0;
    }

    public double montoTotalOperacion() {
        return round2(totalCuotas() + montoAdelanto());
    }

    public void agregarMedio(int p_idMetodoPago, double p_monto,
            String p_referencia) throws Exception {
        if (p_monto <= 0) {
            throw new Exception("El monto del medio de pago debe ser mayor a 0.");
        }
        double faltante = round2(montoTotalOperacion() - totalMedios);
        if (p_monto > faltante + 0.001) {
            throw new Exception("El monto (S/ " + fmt(p_monto)
                    + ") supera lo faltante por cubrir (S/ " + fmt(faltante) + ").");
        }
        medios.add(new Object[]{p_idMetodoPago, p_monto, p_referencia});
        totalMedios = round2(totalMedios + p_monto);
    }

    public void quitarUltimoMedio() {
        if (medios.isEmpty()) {
            return;
        }
        Object[] ultimo = medios.remove(medios.size() - 1);
        totalMedios = round2(totalMedios - (double) ultimo[1]);
    }

    public void limpiarMedios() {
        medios.clear();
        totalMedios = 0;
    }

    public double getTotalMedios() {
        return totalMedios;
    }

    public double montoFaltante() {
        return round2(montoTotalOperacion() - totalMedios);
    }

    public boolean mediosCompletos() {
        return montoFaltante() <= 0.001;
    }

    public ResultSet listarCuotas() {
        this.conectarBD();
        return this.listarCuotasBD(idPrestamo);
    }

    public ResultSet listarMetodosPago() {
        this.conectarBD();
        return this.listarMetodosPagoBD();
    }

    public ResultSet obtenerSiguienteCuota() {
        this.conectarBD();
        return this.obtenerSiguienteCuotaBD(idPrestamo, numeroCuotaMaxima());
    }

    public void ejecutarPago() throws Exception {
        if (cuotasAPagar.isEmpty()) {
            throw new Exception("Debe seleccionar al menos una cuota a pagar.");
        }
        if (medios.isEmpty()) {
            throw new Exception("Debe agregar al menos un medio de pago.");
        }
        if (!mediosCompletos()) {
            throw new Exception("Los medios de pago no cubren el total. "
                    + "Falta: S/ " + fmt(montoFaltante()));
        }
        try {
            this.conectarBD();

            if (!this.verificarOrdenCuotasBD(idPrestamo, numeroCuotaMinima())) {
                throw new Exception("Debe pagar las cuotas anteriores antes de continuar.");
            }

            int idPago = this.insertarPagoBD(
                    idPrestamo, idTrabajador,
                    montoTotalOperacion(), tieneAdelanto(), null);

            for (Object[] c : cuotasAPagar) {
                this.insertarPagoCuotaBD(idPago, (int) c[0], (double) c[2], false);
            }

            if (tieneAdelanto()) {
                this.insertarPagoCuotaBD(
                        idPago, (int) adelanto[0], (double) adelanto[2], true);
            }

            for (Object[] m : medios) {
                this.insertarPagoDetalleBD(
                        idPago, (int) m[0], (double) m[1], (String) m[2]);
            }

            for (Object[] c : cuotasAPagar) {
                this.actualizarCuotaAbonoBD((int) c[0], (double) c[2]);
                this.regularizarMoraBD((int) c[0]);
            }

            if (tieneAdelanto()) {
                this.actualizarCuotaAbonoBD((int) adelanto[0], (double) adelanto[2]);
            }
            this.confirmarCambios();
            limpiarCuotas();
            limpiarMedios();
            quitarAdelanto();
        } catch (Exception e) {
            this.descartarCambios();
            throw e;
        }
    }

    public void quitarMedioPorIndice(int index) {
        if (index < 0 || index >= medios.size()) {
            return;
        }
        Object[] medio = medios.remove(index);
        totalMedios = round2(totalMedios - (double) medio[1]);
    }

    public void regularizarMora(int idCuota) {
        this.regularizarMoraBD(idCuota);
    }

    private double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    private String fmt(double v) {
        return String.format("%.2f", v);
    }
}
