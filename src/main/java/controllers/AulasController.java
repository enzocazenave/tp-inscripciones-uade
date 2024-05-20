package controllers;
import impl.Aula;
import java.util.HashMap;

public class AulasController {
    private static AulasController instance = new AulasController();
    private HashMap<Integer, Aula> aulas = new HashMap();

    private AulasController() {}

    public static AulasController getInstance() {
        return instance;
    }

    public Aula crearAula(int numero, int capacidad) {
        Aula nuevaAula = new Aula(numero, capacidad);
        aulas.put(numero, nuevaAula);
        return nuevaAula;
    }
}
