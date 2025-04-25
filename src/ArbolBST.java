import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ArbolBST {
    private NodoBST raiz;
    private static final String RUTA_TXT = "C:\\Users\\juanp\\OneDrive\\Documentos\\Nueva carpeta\\";

    // Método para insertar un contacto en el árbol
    public void insertar(String clave, int id) {
        raiz = insertarRec(raiz, clave, id);
    }

    // Inserción recursiva
    private NodoBST insertarRec(NodoBST nodo, String clave, int id) {
        if (nodo == null) return new NodoBST(clave, id);

        int comparacion = clave.compareToIgnoreCase(nodo.clave);
        if (comparacion < 0) {
            nodo.izquierda = insertarRec(nodo.izquierda, clave, id);
        } else if (comparacion > 0) {
            nodo.derecha = insertarRec(nodo.derecha, clave, id);
        } else {
            if (!nodo.ids.contains(id)) nodo.ids.add(id);
        }
        return nodo;
    }

    // Exporta el recorrido por niveles (BFS), incluyendo nulls solo para hijos faltantes
    public void exportarPorNiveles(String nombreArchivo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_TXT + nombreArchivo))) {
            if (raiz == null) {
                bw.write("null");
                return;
            }

            Queue<NodoBST> cola = new LinkedList<>();
            List<String> resultado = new ArrayList<>();
            cola.add(raiz);

            while (!cola.isEmpty()) {
                NodoBST actual = cola.poll();

                if (actual == null) {
                    resultado.add("null");
                    // No agregamos hijos de null
                } else {
                    resultado.add(String.valueOf(actual.ids.get(0)));
                    cola.add(actual.izquierda);
                    cola.add(actual.derecha);
                }
            }

            // Elimina nulls innecesarios del final
            int i = resultado.size() - 1;
            while (i >= 0 && resultado.get(i).equals("null")) {
                resultado.remove(i);
                i--;
            }

            bw.write(String.join(",", resultado));
            System.out.println("Árbol BST exportado (recorrido por niveles) a " + nombreArchivo);
        } catch (IOException e) {
            System.out.println("Error al guardar el recorrido del BST: " + e.getMessage());
        }
    }

    // Buscar por clave
    public List<Integer> buscar(String clave) {
        NodoBST nodo = buscarRec(raiz, clave);
        return nodo != null ? nodo.ids : new ArrayList<>();
    }

    // Búsqueda recursiva
    private NodoBST buscarRec(NodoBST nodo, String clave) {
        if (nodo == null) return null;

        int comparacion = clave.compareToIgnoreCase(nodo.clave);
        if (comparacion < 0) return buscarRec(nodo.izquierda, clave);
        else if (comparacion > 0) return buscarRec(nodo.derecha, clave);
        else return nodo;
    }
}