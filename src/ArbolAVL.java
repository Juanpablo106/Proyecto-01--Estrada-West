import java.io.*;
import java.util.*;

public class ArbolAVL {
    private NodoAVL raiz;
    private static final String RUTA_TXT = "C:\\Users\\juanp\\OneDrive\\Documentos\\Nueva carpeta\\";

    // Método para insertar un contacto en el árbol AVL
    // Llama al método recursivo para insertar el nodo y balancea el árbol
    public void insertar(String clave, int id) {
        raiz = insertarRec(raiz, clave, id);
    }

    // Método recursivo para insertar un nodo en el árbol AVL
    // Inserta la clave de forma similar a un árbol binario de búsqueda
    // Luego, actualiza la altura del nodo y balancea el árbol si es necesario
    private NodoAVL insertarRec(NodoAVL nodo, String clave, int id) {
        if (nodo == null) return new NodoAVL(clave, id);  // Si el nodo es nulo, se crea uno nuevo

        int comparacion = clave.compareToIgnoreCase(nodo.clave);  // Comparación alfabética de la clave
        if (comparacion < 0) {
            nodo.izquierda = insertarRec(nodo.izquierda, clave, id);  // Inserta en la rama izquierda
        } else if (comparacion > 0) {
            nodo.derecha = insertarRec(nodo.derecha, clave, id);  // Inserta en la rama derecha
        } else {
            if (!nodo.ids.contains(id)) nodo.ids.add(id);  // Si la clave ya existe, solo agrega el ID si no está duplicado
            return nodo;
        }

        // Actualiza la altura del nodo después de la inserción
        actualizarAltura(nodo);
        return balancear(nodo);  // Balancea el árbol si es necesario
    }

    // Método para actualizar la altura de un nodo basado en la altura de sus hijos
    private void actualizarAltura(NodoAVL nodo) {
        nodo.altura = 1 + Math.max(altura(nodo.izquierda), altura(nodo.derecha));  // La altura es 1 más la máxima altura de los hijos
    }

    // Método para obtener la altura de un nodo
    private int altura(NodoAVL nodo) {
        return nodo == null ? 0 : nodo.altura;  // Si el nodo es nulo, la altura es 0
    }

    // Método para obtener el balance de un nodo
    private int obtenerBalance(NodoAVL nodo) {
        return nodo == null ? 0 : altura(nodo.izquierda) - altura(nodo.derecha);  // Devuelve la diferencia de alturas
    }

    // Método para realizar una rotación a la derecha para balancear el árbol
    private NodoAVL rotarDerecha(NodoAVL y) {
        NodoAVL x = y.izquierda;
        NodoAVL T2 = x.derecha;

        x.derecha = y;
        y.izquierda = T2;

        actualizarAltura(y);  // Actualiza las alturas después de la rotación
        actualizarAltura(x);

        return x;  // Retorna el nuevo nodo raíz después de la rotación
    }

    // Método para realizar una rotación a la izquierda para balancear el árbol
    private NodoAVL rotarIzquierda(NodoAVL x) {
        NodoAVL y = x.derecha;
        NodoAVL T2 = y.izquierda;

        y.izquierda = x;
        x.derecha = T2;

        actualizarAltura(x);  // Actualiza las alturas después de la rotación
        actualizarAltura(y);

        return y;  // Retorna el nuevo nodo raíz después de la rotación
    }

    // Método para balancear el árbol, aplicando las rotaciones necesarias según el balance del nodo
    private NodoAVL balancear(NodoAVL nodo) {
        int balance = obtenerBalance(nodo);  // Obtiene el balance del nodo

        // Caso de rotación derecha
        if (balance > 1 && obtenerBalance(nodo.izquierda) >= 0)
            return rotarDerecha(nodo);

        // Caso de rotación izquierda-derecha
        if (balance > 1 && obtenerBalance(nodo.izquierda) < 0) {
            nodo.izquierda = rotarIzquierda(nodo.izquierda);
            return rotarDerecha(nodo);
        }

        // Caso de rotación izquierda
        if (balance < -1 && obtenerBalance(nodo.derecha) <= 0)
            return rotarIzquierda(nodo);

        // Caso de rotación derecha-izquierda
        if (balance < -1 && obtenerBalance(nodo.derecha) > 0) {
            nodo.derecha = rotarDerecha(nodo.derecha);
            return rotarIzquierda(nodo);
        }

        return nodo;  // Si el árbol ya está balanceado, no hace ninguna rotación
    }

    // Método para exportar el recorrido por niveles del árbol AVL (BFS)
    // Exporta solo el primer ID de cada nodo y omite los nodos nulos al final
    public void guardarRecorridoNivel(String nombreArchivo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_TXT + nombreArchivo))) {
            if (raiz == null) {
                bw.write("[]");  // Si el árbol está vacío, escribe "[]"
                return;
            }

            Queue<NodoAVL> cola = new LinkedList<>();
            cola.add(raiz);
            List<String> resultado = new ArrayList<>();

            while (!cola.isEmpty()) {
                NodoAVL actual = cola.poll();

                if (actual == null) {
                    resultado.add("null");  // Añade "null" para los nodos nulos
                } else {
                    // Solo se exporta el primer ID del nodo
                    resultado.add(String.valueOf(actual.ids.get(0)));
                    cola.add(actual.izquierda);  // Añade el hijo izquierdo
                    cola.add(actual.derecha);  // Añade el hijo derecho
                }
            }

            // Elimina los nulls al final de la lista para limpiar la salida
            while (!resultado.isEmpty() && resultado.get(resultado.size() - 1).equals("null")) {
                resultado.remove(resultado.size() - 1);
            }

            bw.write(String.join(",", resultado));  // Escribe el resultado como una línea separada por comas
            System.out.println("Árbol AVL exportado correctamente a " + nombreArchivo);
        } catch (IOException e) {
            System.out.println("Error al guardar el recorrido del AVL: " + e.getMessage());
        }
    }
}