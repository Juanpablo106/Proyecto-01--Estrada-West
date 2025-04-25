import java.util.Scanner;
import java.util.ArrayList;
import java.time.Year;

public class Main {
    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        GestorContactos gestor = new GestorContactos(); // Instancia del gestor de contactos
        ArrayList<String> telefonos = new ArrayList<>(); // Para validar teléfonos únicos
        ArrayList<String> correos = new ArrayList<>(); // Para validar correos únicos

        int opcion;
        do {
            System.out.println("\n----- MENÚ DE CONTACTOS -----");
            System.out.println("1. Agregar contacto");
            System.out.println("2. Eliminar contactos");
            System.out.println("3. Actualizar contacto");
            System.out.println("4. Ver todos los contactos");
            System.out.println("5. Crear índice por campo");
            System.out.println("6. Exportar en CSV");
            System.out.println("7. Importar contactos desde CSV");
            System.out.println("8. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = entrada.nextInt();
            entrada.nextLine(); // Limpiar buffer

            switch (opcion) {
                case 1: // Opcion para rellenar los datos del contacto
                    System.out.println("---- Agregar nuevo contacto ----");

                    String nombre = leerNombre(entrada, "nombre");
                    String apellido = leerNombre(entrada, "apellido");
                    String apodo = leerCampoLibre(entrada, "apodo");
                    String telefono = Integer.toString(leerTelefono(entrada, telefonos));
                    String correo = leerCorreo(entrada, correos);
                    String direccion = leerCampoLibre(entrada, "dirección");
                    String fechaNacimiento = leerFechaNacimiento(entrada);

                    gestor.agregarContacto(nombre, apellido, apodo, telefono, correo, direccion, fechaNacimiento);
                    break;

                case 2: // Opcion para eliminar contactos
                    System.out.println("¿Qué desea hacer?");
                    System.out.println("1. Eliminar un contacto");
                    System.out.println("2. Eliminar todos los contactos");
                    System.out.print("Seleccione una opción: ");
                    int opcionEliminar = entrada.nextInt();
                    entrada.nextLine();

                    if (opcionEliminar == 1) {
                        System.out.print("Ingrese el ID del contacto a eliminar: ");
                        int idEliminar = entrada.nextInt();
                        entrada.nextLine();
                        gestor.eliminarContacto(idEliminar);
                    } else if (opcionEliminar == 2) {
                        System.out.print("¿Está seguro de que desea eliminar todos los contactos? (si/no): ");
                        String confirmacion = entrada.nextLine();
                        if (confirmacion.equalsIgnoreCase("si")) {
                            gestor.eliminarTodosLosContactos();
                        } else {
                            System.out.println("Operación cancelada.");
                        }
                    } else {
                        System.out.println("Opción no válida.");
                    }
                    break;

                case 3: // Opcion para actualizar los contactos
                    System.out.print("Ingrese el ID del contacto a actualizar: ");
                    int idActualizar = entrada.nextInt();
                    entrada.nextLine();

                    Contacto existente = gestor.obtenerContactoPorId(idActualizar);
                    if (existente == null) {
                        System.out.println("Contacto no encontrado.");
                        break;
                    }

                    System.out.println("---- Actualizar contacto ----");
                    String nuevoNombre = leerNombre(entrada, "nombre");
                    String nuevoApellido = leerNombre(entrada, "apellido");
                    String nuevoApodo = leerCampoLibre(entrada, "apodo");
                    String nuevoTelefono = Integer.toString(leerTelefono(entrada, telefonos));
                    String nuevoCorreo = leerCorreo(entrada, correos);
                    String nuevaDireccion = leerCampoLibre(entrada, "dirección");
                    String nuevaFechaNacimiento = leerFechaNacimiento(entrada);

                    Contacto actualizado = new Contacto(
                            idActualizar,
                            nuevoNombre,
                            nuevoApellido,
                            nuevoApodo,
                            nuevoTelefono,
                            nuevoCorreo,
                            nuevaDireccion,
                            nuevaFechaNacimiento
                    );

                    gestor.actualizarContacto(idActualizar, actualizado);
                    break;

                case 4: // Opcion para listar los contactos que ya existen
                    System.out.println("---- Contactos existentes ----");
                    gestor.listarContactos();
                    break;

                case 5: // Opcion para crear el índice al campo elegido
                    System.out.println("Campos disponibles:");
                    System.out.println("1. nombre");
                    System.out.println("2. apellido");
                    System.out.println("3. apodo");
                    System.out.println("4. teléfono");
                    System.out.println("5. correo");
                    System.out.println("6. dirección");
                    System.out.println("7. fecha de nacimiento");
                    System.out.print("Seleccione un campo para indexar: ");
                    int campo = entrada.nextInt();
                    entrada.nextLine(); // Limpiar buffer

                    String campoTexto = null;
                    switch (campo) {
                        case 1:
                            campoTexto = "nombre";
                            break;
                        case 2:
                            campoTexto = "apellido";
                            break;
                        case 3:
                            campoTexto = "apodo";
                            break;
                        case 4:
                            campoTexto = "telefono";
                            break;
                        case 5:
                            campoTexto = "correo";
                            break;
                        case 6:
                            campoTexto = "direccion";
                            break;
                        case 7:
                            campoTexto = "fecha_nacimiento";
                            break;
                        default:
                            System.out.println("Campo inválido.");
                            break;
                    }

                    if (campoTexto != null) {
                        System.out.print("¿Qué tipo de árbol desea usar para indexar? (1 = BST, 2 = AVL): ");
                        int tipoArbol = entrada.nextInt();
                        entrada.nextLine(); // Limpiar buffer

                        String tipo = (tipoArbol == 1) ? "BST" : (tipoArbol == 2) ? "AVL" : "";

                        if (tipo.isEmpty()) {
                            System.out.println("Opción de árbol inválida.");
                        } else {
                            gestor.crearIndiceAVL(campoTexto, tipo);
                            System.out.println("Índice " + tipo + " creado y exportado a " + campoTexto + "-" + tipo.toLowerCase() + ".txt");
                        }
                    }
                    break;

                case 6: // Opcion para exportar los contactos median CSV
                    gestor.Exportar();
                    System.out.println("Exportación completada. Vuelva a seleccionar una opción.");
                    break;

                case 7:
                    System.out.print("Ingrese la ruta del archivo CSV a importar: ");
                    String rutaCSV = entrada.nextLine();
                    System.out.println("Importando contactos desde: " + rutaCSV);
                    gestor.importarContactosDesdeCSV(rutaCSV);
                    System.out.println("Importación completada. Vuelva a seleccionar una opción.");
                    break;

                case 8:
                    System.out.println("¡Gracias por usar la agenda de contactos!");
                    break;

                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }

        } while (opcion != 8);

        entrada.close();
    }

    //Valida que el nombre no contenga números ni símbolos
    public static String leerNombre(Scanner scanner, String campo) {
        String input;
        while (true) {
            System.out.println("Ingrese el " + campo + " del contacto: ");
            input = scanner.nextLine();
            if (!input.matches(".*\\d.*") && input.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
                return input;
            } else {
                System.out.println("El " + campo + " no puede contener números ni símbolos. Intenta de nuevo.");
            }
        }
    }

    //Valida que el teléfono tenga 8 dígitos y no se repita con otros contactos
    public static int leerTelefono(Scanner scanner, ArrayList<String> telefonos) {
        while (true) {
            System.out.println("Ingrese el teléfono del contacto (8 dígitos): ");
            String input = scanner.nextLine();

            if (input.matches("\\d{8}")) {
                if (telefonos.contains(input)) {
                    System.out.println("El teléfono ya existe. Ingresa otro.");
                } else {
                    telefonos.add(input);
                    return Integer.parseInt(input);
                }
            } else {
                System.out.println("Número inválido. Debe tener 8 dígitos numéricos.");
            }
        }
    }

    //Validacion de correo para que no se pueda repetir con otros contactos
    //Valida que el correo contenga un '@'
    public static String leerCorreo(Scanner scanner, ArrayList<String> correos) {
        while (true) {
            System.out.println("Ingrese el correo del contacto:");
            String correo = scanner.nextLine();

            if (correo.matches(".+@.+\\..+")) {
                boolean existe = correos.stream()
                        .anyMatch(c -> c.equalsIgnoreCase(correo));

                if (existe) {
                    System.out.println("El correo ya está en uso. Ingresa otro.");
                } else {
                    correos.add(correo.toLowerCase()); // Agregarlo normalizado en minúsculas
                    return correo;
                }
            } else {
                System.out.println("Correo inválido. Debe contener un '@'. Intenta de nuevo, por ejemplo: 'ejemplo@gmail.com'.");
            }
        }
    }
    //Omite las validaciones para los campos que no lo necesitan
    public static String leerCampoLibre(Scanner scanner, String campo) {
        System.out.println("Ingrese el " + campo + " del contacto:");
        return scanner.nextLine();
    }

    //Validar que tenga la estructura correcta de una fecha
    public static String leerFechaNacimiento(Scanner scanner) {
        while (true) {
            System.out.println("Ingrese la fecha de nacimiento del contacto (DD/MM/AAAA):");
            String fecha = scanner.nextLine();

            if (!fecha.matches("\\d{2}/\\d{2}/\\d{4}")) {
                System.out.println("Formato inválido. Usa DD/MM/AAAA.");
                continue;
            }

            String[] partes = fecha.split("/");
            int dia = Integer.parseInt(partes[0]);
            int mes = Integer.parseInt(partes[1]);
            int anio = Integer.parseInt(partes[2]);
            int anioActual = Year.now().getValue();
            boolean valido = true;

            if (dia < 1 || dia > 31) {
                System.out.println("El día debe estar entre 1 y 31.");
                valido = false;
            }
            if (mes < 1 || mes > 12) {
                System.out.println("El mes debe estar entre 1 y 12.");
                valido = false;
            }
            if (anio < 1900 || anio > anioActual) {
                System.out.println("El año debe estar entre 1900 y " + anioActual + ".");
                valido = false;
            }

            if (valido) {
                return fecha;
            }
        }
    }
}