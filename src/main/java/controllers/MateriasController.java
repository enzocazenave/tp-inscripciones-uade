package controllers;
import impl.Materia;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MateriasController {
    private static MateriasController instance = new MateriasController();
    private HashMap<UUID, Materia> materias = new HashMap<>();

    private MateriasController() {}

    public static MateriasController getInstance() {
        return instance;
    }

    public Materia crearMateria(String nombre, int cargaHoraria, ArrayList<UUID> correlativasAnteriores, ArrayList<UUID> correlativasPosteriores) {
        Materia nuevaMateria = new Materia(nombre, cargaHoraria, correlativasAnteriores, correlativasPosteriores);
        materias.put(nuevaMateria.getCodigo(), nuevaMateria);
        return nuevaMateria;
    }

    public Materia getMateriaPorCodigo(UUID codigoMateria) {
        if (this.materias.containsKey(codigoMateria)) {
            return this.materias.get(codigoMateria);
        }

        throw new IllegalArgumentException("La materia con codigo " + codigoMateria + " que estás intentado obtener no existe.");
    }
}
