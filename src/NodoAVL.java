import java.util.*;

public class NodoAVL {
    String clave;               // Valor utilizado para ordenar el nodo (por ejemplo, nombre o correo)
    List<Integer> ids;          // Lista de IDs asociados a esta clave (puede haber duplicados con distintos IDs)
    NodoAVL izquierda, derecha; // Referencias a los subárboles izquierdo y derecho
    int altura;                 // Altura del nodo dentro del árbol AVL

    // Constructor del nodo
    public NodoAVL(String clave, int id) {
        this.clave = clave;
        this.ids = new ArrayList<>();
        this.ids.add(id);
        this.altura = 1;
    }
}