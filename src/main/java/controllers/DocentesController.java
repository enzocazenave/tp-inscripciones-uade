package controllers;
import impl.Curso;
import impl.Docente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class DocentesController {
    private static DocentesController instance = new DocentesController();
    private HashMap<UUID, Docente> docentes = new HashMap<>();

    private DocentesController() {}

    public static DocentesController getInstance() {
        return instance;
    }

    public Docente crearDocente(String nombre, String apellido) {
        Docente nuevoDocente = new Docente(nombre, apellido);
        docentes.put(nuevoDocente.getLegajo(), nuevoDocente);
        return nuevoDocente;
    }

    public Docente getDocentePorLegajo(UUID legajo) {
        if (this.docentes.containsKey(legajo)) {
            return this.docentes.get(legajo);
        }

        throw new IllegalArgumentException("El docente con legajo " + legajo + " que estás intentado obtener no existe.");
    }

    public ArrayList<UUID> getCursosAsignadosPorLegajoDocente(UUID legajoDocente) {
        ArrayList<UUID> cursosAsignados = new ArrayList<>();
        CursosController cursosController = CursosController.getInstance();
        Set<UUID> cursos = cursosController.getCursos();

        for (UUID cursoId : cursos) {
            Curso curso = cursosController.getCursoPorId(cursoId);

            if (curso.getLegajoDocente().equals(legajoDocente)) {
                cursosAsignados.add(cursoId);
            }
        }

        return cursosAsignados;
    }

    public HashMap<String, ArrayList<UUID>> getCronogramaSemanalPorLegajoDocente(UUID legajoDocente) {
        HashMap<String, ArrayList<UUID>> cronograma = new HashMap<>();
        ArrayList<UUID> cursosAsignados = this.getCursosAsignadosPorLegajoDocente(legajoDocente);
        CursosController cursosController = CursosController.getInstance();

        for (UUID cursoId : cursosAsignados) {
            Curso curso = cursosController.getCursoPorId(cursoId);
            cronograma.computeIfAbsent(curso.getDia(), k -> new ArrayList<>()).add(cursoId);
        }

        return cronograma;
    }
}
