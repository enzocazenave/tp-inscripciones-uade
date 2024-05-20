package controllers;
import impl.Curso;
import impl.Docente;
import impl.Materia;
import impl.Turno;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

public class CursosController {
    private static CursosController instance = new CursosController();
    private HashMap<UUID, Curso> cursos = new HashMap<>();

    private CursosController() {}

    public static CursosController getInstance() {
        return instance;
    }

    public Curso crearCurso(UUID codigoMateria, int numeroAula, Turno turno, UUID legajoDocente, LocalDate fechaInicio, LocalDate fechaFinalRegular, String dia, int capacidad) {
        DocentesController docentesController = DocentesController.getInstance();
        Docente docente = docentesController.getDocentePorLegajo(legajoDocente);

        if (!docente.getDisponibilidad().get(dia).contains(turno)) {
            throw new IllegalArgumentException("El docente no se encuentra disponible en ese día y franja horaria.");
        }

        if (!validarDisponibilidadDeProfesor(legajoDocente, turno, dia, fechaInicio)) {
            throw new IllegalArgumentException("El docente no se encuentra disponible en ese día y franja horaria, ya que dicta otro curso.");
        }

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

    private boolean validarDisponibilidadDeProfesor(UUID legajoDocente, Turno turno, String dia, LocalDate fechaInicio) {
        DocentesController docentesController = DocentesController.getInstance();
        CursosController cursosController = CursosController.getInstance();

        ArrayList<UUID> cursosAsignados = docentesController.getCursosAsignadosPorLegajoDocente(legajoDocente);
        HashMap<String, ArrayList<Turno>> disponibilidadDocente = docentesController.getDocentePorLegajo(legajoDocente).getDisponibilidad();

        boolean estaDisponible = true;

        for (UUID cursoId : cursosAsignados) {
            Curso curso = cursosController.getCursoPorId(cursoId);
            ArrayList<Turno> disponibilidadDeTurnosEnElDia = disponibilidadDocente.get(dia);

            if (!disponibilidadDeTurnosEnElDia.contains(turno)) {
                estaDisponible = false;
                break;
            }

            if (curso.getDia().equals(dia) && curso.getTurno().equals(turno)) {
                Month mes = curso.getFechaInicio().getMonth();
                Month newMes = fechaInicio.getMonth();

                boolean mismoCuatri = (mes.getValue() <= 6 && newMes.getValue() <= 6) || (mes.getValue() > 6 && newMes.getValue() > 6);

                if (mismoCuatri) {
                    estaDisponible = false;
                    break;
                }
            }
        }

        return estaDisponible;
    }
}
