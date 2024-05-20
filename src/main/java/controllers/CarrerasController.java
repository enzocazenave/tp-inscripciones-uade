package controllers;
import impl.Carrera;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class CarrerasController {
    private static CarrerasController instance = new CarrerasController();
    private HashMap<String, Carrera> carreras = new HashMap<>();

    private CarrerasController() {}

    public static CarrerasController getInstance() {
        return instance;
    }

    public Carrera crearCarrera(String nombre, ArrayList<UUID> materias, int cargaHorariaMaximaDeCursada) {
        Carrera nuevaCarrera = new Carrera(nombre, materias, cargaHorariaMaximaDeCursada);
        carreras.put(nombre, nuevaCarrera);
        return nuevaCarrera;
    }

    public Carrera getCarreraPorNombre(String nombre) {
        if (this.carreras.containsKey(nombre)) {
            return this.carreras.get(nombre);
        }

        throw new IllegalArgumentException("La carrera " + nombre + " que estás intentado obtener no existe.");
    }
}
