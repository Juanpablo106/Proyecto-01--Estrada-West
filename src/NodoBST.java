import java.util.ArrayList;
import java.util.List;

public class NodoBST {
    String clave;               // Valor usado para ordenar el nodo en el árbol (por ejemplo, nombre o correo)
    List<Integer> ids;          // Lista de IDs asociados a esta clave (pueden existir múltiples contactos con la misma clave)
    NodoBST izquierda;          // Referencia al subárbol izquierdo
    NodoBST derecha;            // Referencia al subárbol derecho

    // Constructor del nodo
    public NodoBST(String clave, int id) {
        this.clave = clave;
        this.ids = new ArrayList<>();
        this.ids.add(id);
    }
}