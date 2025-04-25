import java.io.*;
import java.util.*;

public class GestorContactos {
    private Map<Integer, Contacto> contactos = new HashMap<>();
    private Map<String, ArbolBST> indicesBST = new HashMap<>();
    private Map<String, ArbolAVL> indicesAVL = new HashMap<>();
    private int siguienteId = 1;
    private static final String RUTA_CSV = "C:\\Users\\juanp\\OneDrive\\Documentos\\Nueva carpeta\\contacts.csv";

    // Constructor: Inicializa el gestor de contactos y carga los contactos desde un archivo CSV si existe.
    public GestorContactos() {
        cargarContactosDesdeArchivo();
    }

    // Agrega un nuevo contacto al sistema y lo guarda en el archivo CSV.
    public void agregarContacto(String nombre, String apellido, String apodo,
                                String correo, String telefono, String direccion, String fechaNacimiento) {
        Contacto nuevo = new Contacto(siguienteId++, nombre, apellido, apodo, telefono, correo, direccion, fechaNacimiento);
        contactos.put(nuevo.getId(), nuevo);
        guardarContactosEnArchivo();
        System.out.println("Contacto agregado con ID: " + nuevo.getId());
    }

    // Elimina un contacto por ID y reorganiza los IDs de los demás contactos.
    public void eliminarContacto(int id) {
        if (!contactos.containsKey(id)) {
            System.out.println("Contacto no encontrado.");
            return;
        }

        contactos.remove(id);

        // Reorganizar IDs
        Map<Integer, Contacto> nuevosContactos = new HashMap<>();
        int nuevoId = 1;
        List<Contacto> listaOrdenada = new ArrayList<>(contactos.values());
        listaOrdenada.sort(Comparator.comparingInt(Contacto::getId));

        for (Contacto c : listaOrdenada) {
            c.setId(nuevoId);
            nuevosContactos.put(nuevoId, c);
            nuevoId++;
        }

        contactos = nuevosContactos;
        siguienteId = nuevoId;

        // Guardar cambios
        guardarContactosEnArchivo();

        // Regenerar todos los índices
        indicesBST.clear();
        indicesAVL.clear();
        System.out.println("Contacto eliminado e IDs reorganizados.");

        for (String campo : List.of("nombre", "apellido", "apodo", "telefono", "correo", "direccion", "fecha_nacimiento")) {
            if (indicesBST.containsKey(campo)) {
                crearIndiceBST(campo);
            }
            if (indicesAVL.containsKey(campo)) {
                crearIndiceAVL(campo, "AVL");
            }
        }
    }

    // Elimina todos los contactos y vacía los índices.
    public void eliminarTodosLosContactos() {
        contactos.clear(); // Elimina todos los contactos en memoria
        siguienteId = 1; // Reinicia el contador de IDs
        guardarContactosEnArchivo(); // Actualiza el archivo CSV vacío
        indicesBST.clear(); // Borra todos los índices
        indicesAVL.clear();
        System.out.println("Todos los contactos han sido eliminados.");
    }

    // Actualiza un contacto existente por su ID.
    public void actualizarContacto(int id, Contacto actualizado) {
        if (contactos.containsKey(id)) {
            contactos.put(id, actualizado);
            guardarContactosEnArchivo();
            System.out.println("Contacto actualizado.");
        } else {
            System.out.println("ID no encontrado.");
        }
    }

    // Lista todos los contactos en el sistema.
    public void listarContactos() {
        if (contactos.isEmpty()) {
            System.out.println("No hay contactos registrados.");
            return;
        }
        for (Contacto contacto : contactos.values()) {
            System.out.println(contacto);
        }
    }

    // Carga los contactos desde un archivo CSV y actualiza el ID máximo.
    private void cargarContactosDesdeArchivo() {
        File archivo = new File(RUTA_CSV);
        if (!archivo.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            br.readLine(); // Saltar encabezado
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",", -1);
                if (partes.length < 8) continue;
                int id = Integer.parseInt(partes[0]);
                Contacto contacto = new Contacto(id, partes[1], partes[2], partes[3],
                        partes[4], partes[5], partes[6], partes[7]);
                contactos.put(id, contacto);
                if (id >= siguienteId) siguienteId = id + 1;
            }
        } catch (IOException e) {
            System.out.println("Error al cargar contactos: " + e.getMessage());
        }
    }

    // Guarda todos los contactos en un archivo CSV.
    public void guardarContactosEnArchivo() {
        try {
            File archivo = new File(RUTA_CSV);
            if (!archivo.exists()) {
                // Si el archivo no existe, se crea
                archivo.createNewFile();
            }

            try (PrintWriter writerCsv = new PrintWriter(new FileWriter(archivo, false))) {
                writerCsv.println("ID,Nombre,Apellido,Apodo,Telefono,Correo,Direccion,FechaNacimiento");
                for (Contacto contacto : contactos.values()) {
                    writerCsv.println(contacto.getId() + "," +
                            contacto.getNombre() + "," +
                            contacto.getApellido() + "," +
                            contacto.getApodo() + "," +
                            contacto.getTelefono() + "," +
                            contacto.getCorreo() + "," +
                            contacto.getDireccion() + "," +
                            contacto.getFechaNacimiento());
                }
            }
        } catch (IOException e) {
            System.out.println("Error al guardar los contactos en los archivos: " + e.getMessage());
        }
    }

    // Obtiene el valor de un campo específico de un contacto.
    private String obtenerValorCampo(Contacto c, String campo) {
        return switch (campo.toLowerCase()) {
            case "nombre" -> c.getNombre();
            case "apellido" -> c.getApellido();
            case "apodo" -> c.getApodo();
            case "telefono" -> c.getTelefono();
            case "correo" -> c.getCorreo();
            case "direccion" -> c.getDireccion();
            case "fecha_nacimiento" -> c.getFechaNacimiento();
            default -> "";
        };
    }

    // Crea un índice BST para un campo específico y lo exporta.
    public void crearIndiceBST(String campo) {
        ArbolBST arbol = new ArbolBST();
        for (Contacto c : contactos.values()) {
            arbol.insertar(obtenerValorCampo(c, campo), c.getId());
        }
        indicesBST.put(campo, arbol);
        arbol.exportarPorNiveles(campo + "-bst.txt");
    }

    // Crea un índice AVL o BST para un campo específico y lo exporta.
    public void crearIndiceAVL(String campo, String tipo) {
        if (tipo.equalsIgnoreCase("BST")) {
            ArbolBST arbol = new ArbolBST();
            for (Contacto c : contactos.values()) {
                arbol.insertar(obtenerValorCampo(c, campo), c.getId());
            }
            indicesBST.put(campo, arbol);
            arbol.exportarPorNiveles(campo + "-bst.txt");
        } else if (tipo.equalsIgnoreCase("AVL")) {
            ArbolAVL arbol = new ArbolAVL();
            for (Contacto c : contactos.values()) {
                arbol.insertar(obtenerValorCampo(c, campo), c.getId());
            }
            indicesAVL.put(campo, arbol);
            arbol.guardarRecorridoNivel(campo + "-avl.txt");
        } else {
            System.out.println("Tipo de árbol no reconocido. Use BST o AVL.");
        }
    }

    // Obtiene un contacto por su ID.
    public Contacto obtenerContactoPorId(int id) {
        return contactos.get(id);
    }

    // Obtiene todos los contactos registrados.
    public Collection<Contacto> obtenerTodosLosContactos() {
        return contactos.values();
    }

    //Importa los contactos a un CSV y actualiza los ids
    // Importa contactos desde un archivo CSV.
    public void importarContactosDesdeCSV(String rutaArchivo) {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            System.out.println("El archivo especificado no existe.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            boolean primeraLinea = true;

            while ((linea = br.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false; // saltar encabezado
                    continue;
                }

                String[] partes = linea.split(",", -1);
                if (partes.length < 8) continue;

                int id;
                try {
                    id = Integer.parseInt(partes[0]);
                } catch (NumberFormatException e) {
                    System.out.println("ID inválido en línea: " + linea);
                    continue;
                }

                String nombre = partes[1];
                String apellido = partes[2];
                String apodo = partes[3];
                String correo = partes[4];
                String telefono = partes[5];
                String direccion = partes[6];
                String fechaNacimiento = partes[7];

                // Buscar contacto existente por correo o teléfono
                Integer idExistente = null;
                for (Map.Entry<Integer, Contacto> entry : contactos.entrySet()) {
                    Contacto existente = entry.getValue();
                    if (existente.getCorreo().equalsIgnoreCase(correo) || existente.getTelefono().equals(telefono)) {
                        idExistente = entry.getKey();
                        break;
                    }
                }

                if (idExistente != null) {
                    // Actualizar el contacto existente
                    Contacto actualizado = new Contacto(idExistente, nombre, apellido, apodo, correo, telefono, direccion, fechaNacimiento);
                    contactos.put(idExistente, actualizado);
                } else {
                    // Agregar nuevo contacto
                    Contacto nuevo = new Contacto(id, nombre, apellido, apodo, correo, telefono, direccion, fechaNacimiento);
                    contactos.put(id, nuevo);

                    if (id >= siguienteId) {
                        siguienteId = id + 1;
                    }
                }
            }

            System.out.println("✔ Importación completada.");
            // guardarContactosEnCSV(rutaArchivo); // Opcional

        } catch (IOException e) {
            System.out.println("Error al importar contactos: " + e.getMessage());
        }
    }
    // Guarda los contactos en un archivo CSV específico.
    public void guardarContactosEnCSV(String rutaArchivo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            // Escribir encabezado
            bw.write("ID,Nombre,Apellido,Apodo,Correo,Telefono,Direccion,FechaNacimiento\n");

            for (Contacto contacto : contactos.values()) {
                // Escribir los contactos en el archivo CSV
                bw.write(contacto.getId() + "," + contacto.getNombre() + "," + contacto.getApellido() + ","
                        + contacto.getApodo() + "," + contacto.getCorreo() + "," + contacto.getTelefono() + ","
                        + contacto.getDireccion() + "," + contacto.getFechaNacimiento() + "\n");
            }
            System.out.println("Datos guardados correctamente en el archivo CSV.");
        } catch (IOException e) {
            System.out.println("Error al guardar los contactos en el archivo CSV: " + e.getMessage());
        }
    }

    //Exporta los contactos a un archivo CSV
    // Exporta los contactos al archivo CSV predeterminado.
    public void Exportar() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_CSV))) {
            // Escribir encabezado
            bw.write("ID,Nombre,Apellido,Apodo,Correo,Telefono,Direccion,FechaNacimiento\n");
            for (int i = 1; i <= contactos.size(); i++) {
                // Escribir los contactos en el archivo CSV
                bw.write(contactos.get(i).getId() + "," + contactos.get(i).getNombre() + "," +
                        contactos.get(i).getApellido() + "," + contactos.get(i).getApodo() + "," +
                        contactos.get(i).getTelefono() + "," + contactos.get(i).getCorreo() + "," +
                        contactos.get(i).getDireccion() + "," + contactos.get(i).getFechaNacimiento() + "\n");
            }
            System.out.println("Datos guardados correctamente en el archivo CSV.");
        } catch (IOException e) {
            System.out.println("Error al guardar los contactos en el archivo CSV: " + e.getMessage());
        }
    }
}