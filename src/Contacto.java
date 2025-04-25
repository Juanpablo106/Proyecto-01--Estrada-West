public class Contacto {
    private int id;
    private String nombre;
    private String apellido;
    private String apodo;
    private String telefono;
    private String correo;
    private String direccion;
    private String fechaNacimiento;

    public Contacto(int id, String nombre, String apellido, String apodo,
                    String telefono, String correo, String direccion, String fechaNacimiento) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.apodo = apodo;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.fechaNacimiento = fechaNacimiento;
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getApodo() { return apodo; }
    public String getTelefono() { return telefono; }
    public String getCorreo() { return correo; }
    public String getDireccion() { return direccion; }
    public String getFechaNacimiento() { return fechaNacimiento; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setApodo(String apodo) { this.apodo = apodo; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    // Devolver el valor de un campo por nombre (útil para los índices)
    public String getCampo(String campo) {
        return switch (campo.toLowerCase()) {
            case "nombre" -> nombre;
            case "apellido" -> apellido;
            case "apodo" -> apodo;
            case "telefono" -> telefono;
            case "correo" -> correo;
            case "direccion" -> direccion;
            case "fecha_nacimiento" -> fechaNacimiento;
            default -> "";
        };
    }

    @Override
    public String toString() {
        return id + "," + nombre + "," + apellido + "," + apodo + "," +
                telefono + "," + correo + "," + direccion + "," + fechaNacimiento;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Contacto)) return false;
        Contacto otro = (Contacto) obj;
        return this.id == otro.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    // Crear contacto desde una línea CSV
    public static Contacto fromCSV(String linea) {
        String[] datos = linea.split(",");
        return new Contacto(
                Integer.parseInt(datos[0]),
                datos[1],
                datos[2],
                datos[3],
                datos[4],
                datos[5],
                datos[6],
                datos[7]
        );
    }
}