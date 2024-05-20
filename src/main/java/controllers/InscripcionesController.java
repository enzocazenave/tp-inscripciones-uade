package controllers;
import api.InscripcionesControllerInterface;
import com.mercadopago.resources.preference.Preference;
import impl.*;
import services.MercadoPagoService;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.*;

public class InscripcionesController implements InscripcionesControllerInterface {
    private static InscripcionesController instance = new InscripcionesController();

    private HashMap<UUID, HashMap<UUID, Inscripcion>> inscripciones = new HashMap<>();
    private HashMap<UUID, ArrayList<UUID>> inscripcionesPorAlumno = new HashMap<>();

    private LocalDate inicioDeInscripciones;
    private LocalDate finDeInscripciones;
    private double precioMateria;

    private InscripcionesController() {}

    public static InscripcionesController getInstance() {
        return instance;
    }

    public Inscripcion crearInscripcion(UUID legajoAlumno, UUID cursoId) {
        AlumnosController alumnosController = AlumnosController.getInstance();
        CursosController cursosController = CursosController.getInstance();
        MateriasController materiasController = MateriasController.getInstance();
        CarrerasController carrerasController = CarrerasController.getInstance();

        LocalDate nowTime = LocalDate.now();
        boolean noEsFechaDeInscripciones = !nowTime.isEqual(inicioDeInscripciones) && !nowTime.isEqual(finDeInscripciones) && !nowTime.isAfter(inicioDeInscripciones) && !nowTime.isBefore(finDeInscripciones);

        if (noEsFechaDeInscripciones) {
            throw new IllegalStateException("No se puede realizar una inscripción en el día de la fecha.");
        }

        Alumno alumno = alumnosController.getAlumnoPorLegajo(legajoAlumno);
        Carrera carrera = carrerasController.getCarreraPorNombre(alumno.getCarrera());
        Curso curso = cursosController.getCursoPorId(cursoId);
        Materia materia = materiasController.getMateriaPorCodigo(curso.getCodigoMateria());

        if (alumno.getMateriasAprobadas().contains(curso.getCodigoMateria())) {
            throw new IllegalArgumentException("El alumno ya posee aprobada esta materia.");
        }

        int cantidadAlumnosInscriptos = this.getCantidadAlumnosEnCurso(cursoId);

        if (cantidadAlumnosInscriptos >= curso.getCapacidad()) {
            throw new IllegalStateException("El curso con id " + cursoId + " ha alcanzado su capacidad máxima.");
        }

        boolean correlativasAnterioresEstanAprobadas = this.tieneCorrelativasAprobadas(alumno.getMateriasAprobadas(), materia.getCorrelativasAnteriores());
        boolean correlativasPosterioresEstanAprobadas = this.tieneCorrelativasAprobadas(alumno.getMateriasAprobadas(), materia.getCorrelativasAnteriores());

        if (!correlativasPosterioresEstanAprobadas || !correlativasAnterioresEstanAprobadas) {
            throw new IllegalStateException("El alumno con legajo " + legajoAlumno + " no ha aprobado las materias correlativas necesarias para inscribirse en el curso con id " + cursoId + ".");
        }

        int cargaHorariaDeCursadaDelAlumno = this.getCargaHorariaDeCursadaPorLegajo(legajoAlumno);

        if (cargaHorariaDeCursadaDelAlumno + materia.getCargaHoraria() >= carrera.getCargaHorariaMaximaDeCursada()) {
            throw new IllegalStateException("La carga horaria total del alumno con legajo " + legajoAlumno + " excede la carga horaria permitida por cuatrimestre.");
        }

        Inscripcion inscripcion = new Inscripcion(legajoAlumno);

        this.inscripciones.computeIfAbsent(cursoId, k -> new HashMap<>()).put(inscripcion.getId(), inscripcion);
        this.inscripcionesPorAlumno.computeIfAbsent(legajoAlumno, k -> new ArrayList<>()).add(inscripcion.getId());

        return inscripcion;
    }

    public String pagarInscripcionesPendientesDeAlumno(UUID legajoAlumno) {
        AlumnosController alumnosController = AlumnosController.getInstance();
        CursosController cursosController = CursosController.getInstance();
        MateriasController materiasController = MateriasController.getInstance();

        alumnosController.getAlumnoPorLegajo(legajoAlumno); // Para validar que el alumno existe

        ArrayList<UUID> inscripcionesDeAlumno = this.getInscripcionesPorLegajo(legajoAlumno);
        Set<UUID> cursos = inscripciones.keySet();
        double unitPrice = 0.0;

        StringBuilder descripcion = new StringBuilder();

        for (UUID inscripcionId : inscripcionesDeAlumno) {
            for (UUID cursoId : cursos) {
                if (!this.inscripciones.get(cursoId).containsKey(inscripcionId)) {
                    continue;
                }

                Inscripcion inscripcion = this.inscripciones.get(cursoId).get(inscripcionId);

                if (inscripcion.getEstado().equals("pendiente")) {
                    unitPrice += this.precioMateria;
                    Curso curso = cursosController.getCursoPorId(cursoId);
                    Materia materia = materiasController.getMateriaPorCodigo(curso.getCodigoMateria());
                    descripcion.append(materia.getNombre()).append(" ");
                }

                break;
            }
        }

        MercadoPagoService mercadoPagoService = MercadoPagoService.getInstance();

        String title = "Inscripciones a materias";
        return mercadoPagoService.crearLinkDePago(title, "Inscripcion a " + descripcion, unitPrice);
    }

    public void setPrecioMateria(double precio) {
        this.precioMateria = precio;
    }

    public void setNuevaFechaDeInscripciones(LocalDate inicio, LocalDate fin) {
        if (inicio.isAfter(fin)) {
            throw new IllegalArgumentException("La fecha de finalizacion no puede ser anterior a la de inicio de inscripciones.");
        }

        this.inicioDeInscripciones = inicio;
        this.finDeInscripciones = fin;
    }

    public int getCantidadAlumnosEnCurso(UUID cursoId) {
        if (this.inscripciones.containsKey(cursoId)) {
            return this.inscripciones.get(cursoId).size();
        }

        return 0;
    }

    public ArrayList<UUID> getCursosPorMateriaYTurno(UUID codigoMateriaBuscada, Turno turnoBuscado) {
        if (codigoMateriaBuscada == null && turnoBuscado == null) {
            throw new IllegalArgumentException("Se debe completar al menos un parámetro de busqueda");
        }

        CursosController cursosController = CursosController.getInstance();

        Set<UUID> cursos = cursosController.getCursos();
        ArrayList<UUID> cursosFiltrados = new ArrayList<>();

        if (codigoMateriaBuscada == null && turnoBuscado != null) {
            for (UUID cursoId : cursos) {
                Turno turno = cursosController.getCursoPorId(cursoId).getTurno();
                if (turno.equals(turnoBuscado)) {
                    cursosFiltrados.add(cursoId);
                }
            }
        } else if (codigoMateriaBuscada != null && turnoBuscado == null) {
            for (UUID cursoId : cursos) {
                UUID codigoMateria = cursosController.getCursoPorId(cursoId).getCodigoMateria();

                if (codigoMateria.equals(codigoMateriaBuscada)) {
                    cursosFiltrados.add(cursoId);
                }
            }
        } else {
            ArrayList<UUID> cursosDeMateria = cursosController.getCursosDeMateria(codigoMateriaBuscada);

            for (UUID cursoId : cursosDeMateria) {
                Turno turno = cursosController.getCursoPorId(cursoId).getTurno();

                if (turno.equals(turnoBuscado)) {
                    cursosFiltrados.add(cursoId);
                }
            }
        }

        return cursosFiltrados;
    }

    private boolean tieneCorrelativasAprobadas(ArrayList<UUID> aprobadas, ArrayList<UUID> correlativasNecesarias) {
        boolean estanAprobadas = true;

        for (UUID correlativaNecesaria : correlativasNecesarias) {
            if (!aprobadas.contains(correlativaNecesaria)) {
                estanAprobadas = false;
                break;
            }
        }

        return estanAprobadas;
    }

    private int getCargaHorariaDeCursadaPorLegajo(UUID legajoAlumno) {
        CursosController cursosController = CursosController.getInstance();
        MateriasController materiasController = MateriasController.getInstance();

        ArrayList<UUID> inscripcionesDeAlumno = this.getInscripcionesPorLegajo(legajoAlumno);
        Set<UUID> cursos = inscripciones.keySet();

        int cargaHoraria = 0;

        for (UUID inscripcionId : inscripcionesDeAlumno) {
            for (UUID cursoId : cursos) {
                if (!this.inscripciones.get(cursoId).containsKey(inscripcionId)) {
                    continue;
                }

                Curso curso = cursosController.getCursoPorId(cursoId);
                Materia materia = materiasController.getMateriaPorCodigo(curso.getCodigoMateria());

                cargaHoraria += materia.getCargaHoraria();
                break;
            }
        }

        return cargaHoraria;
    }

    private ArrayList<UUID> getInscripcionesPorLegajo(UUID legajoAlumno) {
        if (this.inscripcionesPorAlumno.containsKey(legajoAlumno)) {
            return this.inscripcionesPorAlumno.get(legajoAlumno);
        }

        return new ArrayList<>();
    }
}
