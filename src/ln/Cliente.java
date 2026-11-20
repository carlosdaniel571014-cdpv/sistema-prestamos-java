package ln;

import ad.ClienteAD;
import java.sql.ResultSet;

public class Cliente extends ClienteAD {
    private int    idCliente;
    private String nombres;
    private String apellidos;
    private String dni;
    private String telefono;
    private String email;
    private String direccion;

    public Cliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public Cliente() {
    }

    // Constructor para nuevo cliente (sin ID)
    public Cliente(String p_nombres, String p_apellidos, String p_dni,
                   String p_telefono, String p_email, String p_direccion) {
        this.nombres   = p_nombres;
        this.apellidos = p_apellidos;
        this.dni       = p_dni;
        this.telefono  = p_telefono;
        this.email     = p_email;
        this.direccion = p_direccion;
    }

    // Constructor para modificar (con ID)
    public Cliente(int p_idCliente, String p_nombres, String p_apellidos, String p_dni,
                   String p_telefono, String p_email, String p_direccion) {
        this.idCliente = p_idCliente;
        this.nombres   = p_nombres;
        this.apellidos = p_apellidos;
        this.dni       = p_dni;
        this.telefono  = p_telefono;
        this.email     = p_email;
        this.direccion = p_direccion;
    }

    public int    getIdCliente() { return idCliente; }
    public String getNombres()   { return nombres; }
    public String getApellidos() { return apellidos; }
    public String getDni()       { return dni; }
    public String getTelefono()  { return telefono; }
    public String getEmail()     { return email; }
    public String getDireccion() { return direccion; }

    public void setIdCliente(int v)    { this.idCliente = v; }
    public void setNombres(String v)   { this.nombres = v; }
    public void setApellidos(String v) { this.apellidos = v; }
    public void setDni(String v)       { this.dni = v; }
    public void setTelefono(String v)  { this.telefono = v; }
    public void setEmail(String v)     { this.email = v; }
    public void setDireccion(String v) { this.direccion = v; }

    // Carga los datos del cliente usando el idCliente ya almacenado
    public boolean cargarDatos() throws Exception {
        try {
            this.conectarBD();
            ResultSet rs = this.buscarClientePorIdBD(this.idCliente);
            if (rs == null) return false;
            this.nombres   = rs.getString("nombres");
            this.apellidos = rs.getString("apellidos");
            this.dni       = rs.getString("dni");
            this.telefono  = rs.getString("telefono");
            this.email     = rs.getString("email");
            this.direccion = rs.getString("direccion");
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    // Busca el cliente por DNI 
    public boolean buscarCliente(int p_dni) throws Exception {
        try {
            this.conectarBD();
            ResultSet rs = this.buscarClienteBD(p_dni);
            if (rs == null) return false;
            this.idCliente = rs.getInt("id_cliente");
            this.nombres   = rs.getString("nombres");
            this.apellidos = rs.getString("apellidos");
            this.dni       = rs.getString("dni");
            this.telefono  = rs.getString("telefono");
            this.email     = rs.getString("email");
            this.direccion = rs.getString("direccion");
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    // Busca el cliente por ID (para modo modificar)
    public boolean buscarClientePorId(int p_idCliente) throws Exception {
        try {
            this.conectarBD();
            ResultSet rs = this.buscarClientePorIdBD(p_idCliente);
            if (rs == null) return false;
            this.idCliente = rs.getInt("id_cliente");
            this.nombres   = rs.getString("nombres");
            this.apellidos = rs.getString("apellidos");
            this.dni       = rs.getString("dni");
            this.telefono  = rs.getString("telefono");
            this.email     = rs.getString("email");
            this.direccion = rs.getString("direccion");
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    // Lista todos los clientes
    public ResultSet listarClientes() throws Exception {
        try {
            this.conectarBD();
            return this.buscarClientesBD("");
        } catch (Exception e) {
            throw e;
        }
    }

    // Busca clientes por nombre o apellido
    public ResultSet buscarClientes(String p_busqueda) throws Exception {
        try {
            this.conectarBD();
            return this.buscarClientesBD(p_busqueda);
        } catch (Exception e) {
            throw e;
        }
    }

    // Registra un nuevo cliente
    public void registrarCliente() throws Exception {
        try {
            this.conectarBD();
            this.guardarClienteBD(this.nombres, this.apellidos, this.dni,
                                  this.telefono, this.email, this.direccion);
            this.confirmarCambios();
        } catch (Exception e) {
            this.descartarCambios();
            throw e;
        }
    }

    // Actualiza los datos de un cliente
    public void actualizarCliente() throws Exception {
        try {
            this.conectarBD();
            this.actualizarClienteBD(this.idCliente, this.nombres, this.apellidos, this.dni,
                                     this.telefono, this.email, this.direccion);
            this.confirmarCambios();
        } catch (Exception e) {
            this.descartarCambios();
            throw e;
        }
    }

    // Baja lógica
    public void darBajaCliente() throws Exception {
        try {
            this.conectarBD();
            this.eliminarClienteBD(this.idCliente);
            this.confirmarCambios();
        } catch (Exception e) {
            this.descartarCambios();
            throw e;
        }
    }

    // Reactivar cliente
    public void reactivarCliente() throws Exception {
        try {
            this.conectarBD();
            this.reactivarClienteBD(this.idCliente);
            this.confirmarCambios();
        } catch (Exception e) {
            this.descartarCambios();
            throw e;
        }
    }

    // Verifica si el DNI ya existe en otro cliente
    public boolean existeDni(String p_dni, int p_idClienteExcluir) throws Exception {
        try {
            this.conectarBD();
            return this.existeDniBD(p_dni, p_idClienteExcluir);
        } catch (Exception e) {
            throw e;
        }
    }

    // Evalúa si el cliente califica según su historial crediticio
    public boolean evaluarCalificacion(int p_scoreCrediticio, boolean p_deudasActivas,
                                       double p_montoDeuda, double p_montoSolicitado) {
        if (p_scoreCrediticio < 500) return false;
        if (p_deudasActivas && p_montoDeuda > p_montoSolicitado * 0.5) return false;
        return true;
    }
    
    public String verificarElegibilidad() throws Exception {
        try {
            this.conectarBD();
            ResultSet rs = this.verificarElegibilidadBD(this.idCliente);
            if (!rs.next()) return null;
 
            int    prestamosActivos = rs.getInt("prestamos_activos");
            int    cuotasVencidas   = rs.getInt("cuotas_vencidas");
            double deudaTotal       = rs.getDouble("deuda_total");
 
            if (prestamosActivos == 0) return null; // sin bloqueo
 
            StringBuilder msj = new StringBuilder();
            msj.append("⚠ Cliente NO puede solicitar otro préstamo.\n");
            if (prestamosActivos > 0)
                msj.append("• Tiene ").append(prestamosActivos)
                   .append(" préstamo(s) activo(s). Deuda total: S/ ")
                   .append(String.format("%.2f", deudaTotal)).append("\n");
            if (cuotasVencidas > 0)
                msj.append("• Tiene ").append(cuotasVencidas)
                   .append(" cuota(s) vencida(s) sin pagar.\n");
            return msj.toString();
        } catch (Exception e) {
            throw e;
        }
    }
 
    // ─── NUEVO: Lista de clientes con cuotas vencidas (para alerta al abrir) ──
    // Llama a esto desde FmrMenuPrincipal al iniciar sesión.
    public ResultSet obtenerAlertasVencidas() throws Exception {
        try {
            this.conectarBD();
            return this.obtenerAlertasVencidasBD();
        } catch (Exception e) {
            throw e;
        }
    }
}
