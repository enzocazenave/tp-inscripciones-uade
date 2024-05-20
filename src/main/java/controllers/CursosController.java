package controllers;
import impl.Curso;
import impl.Materia;
import impl.Turno;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class CursosController {
    private static CursosController instance = new CursosController();
    private HashMap<UUID, Curso> cursos = new HashMap<>();

    private CursosController() {}

    public static CursosController getInstance() {
        return instance;
    }

    public Curso crearCurso(UUID codigoMateria, int numeroAula, Turno turno, UUID legajoDocente, LocalDate fechaInicio, LocalDate fechaFinalRegular, String dia, int capacidad) {
        Curso nuevoCurso = new Curso(codigoMateria, numeroAula, turno, legajoDocente, fechaInicio, fechaFinalRegular, dia, capacidad);
        cursos.put(nuevoCurso.getCurso(), nuevoCurso);
        return nuevoCurso;
    }

    public Curso getCursoPorId(UUID curso) {
        if (cursos.containsKey(curso)) {
            return this.cursos.get(curso);
        }

        throw new IllegalArgumentException("El curso con id " + curso + " que estás intentado obtener no existe.");
    }

    public ArrayList<UUID> getCursosDeMateria(UUID codigoMateria) {
        ArrayList<UUID> cursosMateria = new ArrayList<>();
        Set<UUID> cursos = this.cursos.keySet();

        for (UUID cursoId : cursos) {
            Curso curso = this.getCursoPorId(cursoId);

            if (curso.getCodigoMateria().equals(codigoMateria)) {
                cursosMateria.add(cursoId);
            }
        }

        return cursosMateria;
    }

    public Set<UUID> getCursos() {
        return this.cursos.keySet();
    }
}
