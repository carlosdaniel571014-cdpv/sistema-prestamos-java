package ln;

import ad.TrabajadorAD;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import sec.SeguridadApp;

public class Trabajador extends TrabajadorAD {

    private int idTrabajador;
    private String nombres;
    private String apellidos;
    private String dni;
    private String cargo;
    private String email;
    private String contrasena;
    private boolean activo;

    public static final String CARGO_ADMIN = "admin";
    public static final String CARGO_ANALISTA = "analista";
    public static final String CARGO_TRABAJADOR = "trabajador";

    public Trabajador() {
    }

    public Trabajador(int idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public int getIdTrabajador() {
        return idTrabajador;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getDni() {
        return dni;
    }

    public String getCargo() {
        return cargo;
    }

    public String getEmail() {
        return email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public boolean isActivo() {
        return activo;
    }

    public String getNombreCompleto() {
        return apellidos + ", " + nombres;
    }

    public void setIdTrabajador(int v) {
        this.idTrabajador = v;
    }

    public void setNombres(String v) {
        this.nombres = v;
    }

    public void setApellidos(String v) {
        this.apellidos = v;
    }

    public void setDni(String v) {
        this.dni = v;
    }

    public void setCargo(String v) {
        this.cargo = v;
    }

    public void setEmail(String v) {
        this.email = v;
    }

    public void setContrasena(String v) {
        this.contrasena = v;
    }

    public void setActivo(boolean v) {
        this.activo = v;
    }

    public boolean cargarDatos() throws Exception {
        try {
            this.conectarBD();
            ResultSet rs = this.buscarTrabajadorBD(this.idTrabajador);
            if (rs == null) {
                return false;
            }
            this.nombres = rs.getString("nombres");
            this.apellidos = rs.getString("apellidos");
            this.dni = rs.getString("dni");
            this.cargo = rs.getString("cargo");
            this.email = rs.getString("email");
            this.activo = rs.getBoolean("activo");
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean buscarTrabajador(String p_dni) throws Exception {
        try {
            this.conectarBD();
            ResultSet rs = this.buscarTrabajadorPorDniBD(p_dni);
            if (rs == null) {
                return false;
            }
            this.idTrabajador = rs.getInt("id_trabajador");
            this.nombres = rs.getString("nombres");
            this.apellidos = rs.getString("apellidos");
            this.dni = rs.getString("dni");
            this.cargo = rs.getString("cargo");
            this.email = rs.getString("email");
            this.activo = rs.getBoolean("activo");
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    // ─── AUTENTICAR ───────────────────────────────────────────────────────────
    // Hashea la contraseña con SHA-256 antes de enviarla a la BD
    public boolean autenticar(String p_email, String p_contrasena) throws Exception {
        try {
            this.conectarBD();
            String hashContrasena = SeguridadApp.sha256(p_contrasena);
            ResultSet rs = this.autenticarTrabajadorBD(p_email, hashContrasena);
            if (rs == null) {
                return false;
            }
            this.idTrabajador = rs.getInt("id_trabajador");
            this.nombres = rs.getString("nombres");
            this.apellidos = rs.getString("apellidos");
            this.dni = rs.getString("dni");
            this.cargo = rs.getString("cargo");
            this.email = rs.getString("email");
            this.activo = rs.getBoolean("activo");
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Trabajador> listarTrabajadores() throws Exception {
        List<Trabajador> lista = new ArrayList<>();
        try {
            this.conectarBD();
            ResultSet rs = this.listarTrabajadoresBD();
            while (rs.next()) {
                Trabajador t = new Trabajador();
                t.setIdTrabajador(rs.getInt("id_trabajador"));
                t.setNombres(rs.getString("nombres"));
                t.setApellidos(rs.getString("apellidos"));
                t.setDni(rs.getString("dni"));
                t.setCargo(rs.getString("cargo"));
                t.setEmail(rs.getString("email"));
                t.setActivo(rs.getBoolean("activo"));
                lista.add(t);
            }
            return lista;
        } catch (Exception e) {
            throw e;
        }
    }

    // ─── BUSCAR POR ID, NOMBRE O DNI ─────────────────────────────────────────
    public List<Trabajador> buscarTrabajadores(String p_busqueda) throws Exception {
        List<Trabajador> lista = new ArrayList<>();
        try {
            this.conectarBD();
            ResultSet rs = this.buscarTrabajadoresBD(p_busqueda);
            while (rs.next()) {
                Trabajador t = new Trabajador();
                t.setIdTrabajador(rs.getInt("id_trabajador"));
                t.setNombres(rs.getString("nombres"));
                t.setApellidos(rs.getString("apellidos"));
                t.setDni(rs.getString("dni"));
                t.setCargo(rs.getString("cargo"));
                t.setEmail(rs.getString("email"));
                t.setActivo(rs.getBoolean("activo"));
                lista.add(t);
            }
            return lista;
        } catch (Exception e) {
            throw e;
        }
    }

    // ─── LISTAR ACTIVOS ───────────────────────────────────────────────────────
    public List<Trabajador> listarTrabajadoresActivos() throws Exception {
        List<Trabajador> lista = new ArrayList<>();
        try {
            this.conectarBD();
            ResultSet rs = this.listarTrabajadoresActivosBD();
            while (rs.next()) {
                Trabajador t = new Trabajador();
                t.setIdTrabajador(rs.getInt("id_trabajador"));
                t.setNombres(rs.getString("nombres"));
                t.setApellidos(rs.getString("apellidos"));
                t.setDni(rs.getString("dni"));
                t.setCargo(rs.getString("cargo"));
                t.setEmail(rs.getString("email"));
                t.setActivo(true);
                lista.add(t);
            }
            return lista;
        } catch (Exception e) {
            throw e;
        }
    }

    // ─── REGISTRAR ────────────────────────────────────────────────────────────
    // Hashea la contraseña con SHA-256 antes de insertarla en la BD
    public void registrar(String p_nombres, String p_apellidos, String p_dni,
            String p_cargo, String p_email, String p_contrasena) throws Exception {
        validarNombre(p_nombres, "nombres");
        validarNombre(p_apellidos, "apellidos");
        validarDni(p_dni);
        validarCargo(p_cargo);
        validarEmail(p_email);
        validarContrasena(p_contrasena);

        try {
            this.conectarBD();
            if (this.existeDniBD(p_dni, 0)) {
                throw new Exception("Ya existe un trabajador con el DNI " + p_dni);
            }
            if (this.existeEmailBD(p_email, 0)) {
                throw new Exception("Ya existe un trabajador con el email " + p_email);
            }

            String hashContrasena = SeguridadApp.sha256(p_contrasena);

            int nuevoId = this.insertarTrabajadorBD(p_nombres, p_apellidos, p_dni,
                    p_cargo, p_email, hashContrasena);
            this.idTrabajador = nuevoId;
            this.nombres = p_nombres;
            this.apellidos = p_apellidos;
            this.dni = p_dni;
            this.cargo = p_cargo;
            this.email = p_email;
            this.activo = true;
        } catch (Exception e) {
            throw e;
        }
    }

    // ─── ACTUALIZAR ───────────────────────────────────────────────────────────
    public boolean actualizar(String p_nombres, String p_apellidos, String p_dni,
            String p_cargo, String p_email) throws Exception {
        validarNombre(p_nombres, "nombres");
        validarNombre(p_apellidos, "apellidos");
        validarDni(p_dni);
        validarCargo(p_cargo);
        validarEmail(p_email);

        try {
            this.conectarBD();
            if (this.existeDniBD(p_dni, this.idTrabajador)) {
                throw new Exception("El DNI " + p_dni + " ya pertenece a otro trabajador");
            }
            if (this.existeEmailBD(p_email, this.idTrabajador)) {
                throw new Exception("El email " + p_email + " ya pertenece a otro trabajador");
            }

            boolean ok = this.actualizarTrabajadorBD(this.idTrabajador,
                    p_nombres, p_apellidos,
                    p_dni, p_cargo, p_email);
            if (ok) {
                this.nombres = p_nombres;
                this.apellidos = p_apellidos;
                this.dni = p_dni;
                this.cargo = p_cargo;
                this.email = p_email;
            }
            return ok;
        } catch (Exception e) {
            throw e;
        }
    }

    // ─── CAMBIAR CONTRASEÑA (usuario cambia la suya verificando la actual) ────
    // Hashea ambas contraseñas con SHA-256 antes de enviarlas a la BD
    public boolean cambiarContrasena(String p_actual, String p_nueva,
            String p_confirmacion) throws Exception {
        if (p_nueva == null || p_nueva.trim().isEmpty()) {
            throw new Exception("La nueva contraseña no puede estar vacía");
        }
        if (p_nueva.length() < 6) {
            throw new Exception("La nueva contraseña debe tener al menos 6 caracteres");
        }
        if (!p_nueva.equals(p_confirmacion)) {
            throw new Exception("La confirmación no coincide con la nueva contraseña");
        }

        try {
            this.conectarBD();
            String hashActual = SeguridadApp.sha256(p_actual);
            String hashNueva = SeguridadApp.sha256(p_nueva);
            return this.cambiarContrasenaBD(this.idTrabajador, hashActual, hashNueva);
        } catch (Exception e) {
            throw e;
        }
    }

    // ─── CAMBIAR CONTRASEÑA SIN VERIFICAR ACTUAL (admin resetea la de otro) ───
    // Usado desde FrmEditarTrabajador cuando el admin deja campo de contraseña lleno
    public boolean cambiarContrasenaAdmin(String p_nueva, String p_confirmacion) throws Exception {
        if (p_nueva == null || p_nueva.trim().isEmpty()) {
            throw new Exception("La nueva contraseña no puede estar vacía");
        }
        if (p_nueva.length() < 6) {
            throw new Exception("La nueva contraseña debe tener al menos 6 caracteres");
        }
        if (!p_nueva.equals(p_confirmacion)) {
            throw new Exception("La confirmación no coincide con la nueva contraseña");
        }

        try {
            this.conectarBD();
            String hashNueva = SeguridadApp.sha256(p_nueva);
            return this.cambiarContrasenaAdminBD(this.idTrabajador, hashNueva);
        } catch (Exception e) {
            throw e;
        }
    }

    // ─── DESACTIVAR ───────────────────────────────────────────────────────────
    public boolean desactivar() throws Exception {
        try {
            this.conectarBD();
            boolean ok = this.desactivarTrabajadorBD(this.idTrabajador);
            if (ok) {
                this.activo = false;
            }
            return ok;
        } catch (Exception e) {
            throw e;
        }
    }

    // ─── REACTIVAR ────────────────────────────────────────────────────────────
    public boolean reactivar() throws Exception {
        try {
            this.conectarBD();
            boolean ok = this.reactivarTrabajadorBD(this.idTrabajador);
            if (ok) {
                this.activo = true;
            }
            return ok;
        } catch (Exception e) {
            throw e;
        }
    }

    // ─── PERMISOS ─────────────────────────────────────────────────────────────
    public boolean esAdmin() {
        return CARGO_ADMIN.equalsIgnoreCase(this.cargo);
    }

    public boolean esAnalista() {
        return CARGO_ANALISTA.equalsIgnoreCase(this.cargo);
    }

    public boolean esTrabajador() {
        return CARGO_TRABAJADOR.equalsIgnoreCase(this.cargo);
    }

    public boolean puedeGestionarTrabajadores() {
        return esAdmin();
    }

    // ─── OBTENER CARGO ────────────────────────────────────────────────────────
    public ResultSet obtenerCargo() throws Exception {
        try {
            this.conectarBD();
            return this.obtenerCargoBD();
        } catch (Exception e) {
            throw e;
        }
    }

    // ─── VALIDACIONES INTERNAS ────────────────────────────────────────────────
    private void validarNombre(String valor, String campo) throws Exception {
        if (valor == null || valor.trim().isEmpty()) {
            throw new Exception("El campo " + campo + " no puede estar vacío");
        }
        if (valor.trim().length() > 100) {
            throw new Exception("El campo " + campo + " excede los 100 caracteres");
        }
    }

    private void validarDni(String dni) throws Exception {
        if (dni == null || dni.trim().isEmpty()) {
            throw new Exception("El DNI no puede estar vacío");
        }
        if (!dni.matches("\\d{8}")) {
            throw new Exception("El DNI debe tener exactamente 8 dígitos numéricos");
        }
    }

    private void validarCargo(String cargo) throws Exception {
        if (cargo == null || cargo.trim().isEmpty()) {
            throw new Exception("El cargo no puede estar vacío");
        }
        if (!cargo.equals(CARGO_ADMIN)
                && !cargo.equals(CARGO_ANALISTA)
                && !cargo.equals(CARGO_TRABAJADOR)) {
            throw new Exception("Cargo no válido: " + cargo
                    + ". Use: admin, analista o trabajador");
        }
    }

    private void validarEmail(String email) throws Exception {
        if (email == null || email.trim().isEmpty()) {
            throw new Exception("El email no puede estar vacío");
        }
        if (!email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")) {
            throw new Exception("El formato del email no es válido: " + email);
        }
        if (email.length() > 120) {
            throw new Exception("El email excede los 120 caracteres");
        }
    }

    private void validarContrasena(String contrasena) throws Exception {
        if (contrasena == null || contrasena.trim().isEmpty()) {
            throw new Exception("La contraseña no puede estar vacía");
        }
        if (contrasena.length() < 6) {
            throw new Exception("La contraseña debe tener al menos 6 caracteres");
        }
    }

}
