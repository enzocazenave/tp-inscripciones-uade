import controllers.*;
import impl.*;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class Main {
  public static void main(String[] args) throws FileNotFoundException, IllegalAccessException {
    // Instancias de controladores
    AlumnosController alumnosController = AlumnosController.getInstance();
    DocentesController docentesController = DocentesController.getInstance();
    CursosController cursosController = CursosController.getInstance();
    MateriasController materiasController = MateriasController.getInstance();
    InscripcionesController inscripcionesController = InscripcionesController.getInstance();
    CarrerasController carrerasController = CarrerasController.getInstance();
    AulasController aulasController = AulasController.getInstance();

    // Creacion de turnos
    Turno turnoM = new Turno("Mañana", "7.45", "11.45");
    Turno turnoT = new Turno("Tarde", "14.00", "18.00");
    Turno turnoN = new Turno("Noche", "18.30", "22.00");

    // Randomizar Aula
    Random random = new Random();

    // Creacion de aula
    Aula aula1 = aulasController.crearAula(random.nextInt(100, 900), 50);
    Aula aula2 = aulasController.crearAula(random.nextInt(100, 900), 50);
    Aula aula3 = aulasController.crearAula(random.nextInt(100, 900), 50);

    // Creacion de materias
    Materia materia1 = materiasController.crearMateria("Programacion 3", 68, new ArrayList<>(), new ArrayList<>());
    Materia materia4 = materiasController.crearMateria("Proceso de desarrollo de software", 68, new ArrayList<>(), new ArrayList<>());
    Materia materia3 = materiasController.crearMateria("Programacion 5", 68, new ArrayList<>(), new ArrayList<>());

    ArrayList<UUID> correlativasAnteriores = new ArrayList<>();
    correlativasAnteriores.add(materia1.getCodigo());
    ArrayList<UUID> correlativasPosteriores = new ArrayList<>();
    correlativasPosteriores.add(materia3.getCodigo());

    Materia materia2 = materiasController.crearMateria("Programacion 4.5", 68, correlativasAnteriores, correlativasPosteriores);

    // Agrega materias a un array para asignar las materias de la carrera "Ingenieria Informatica"
    ArrayList<UUID> materiasDeCarrera = new ArrayList<>();
    materiasDeCarrera.add(materia1.getCodigo());
    materiasDeCarrera.add(materia2.getCodigo());
    materiasDeCarrera.add(materia3.getCodigo());
    materiasDeCarrera.add(materia4.getCodigo());


    // Crea la carrera "Ingenieria Informatica"
    Carrera carrera = carrerasController.crearCarrera("Ingenieria Informatica", materiasDeCarrera, 476);

    // Crea un docente

    Docente docente1 = docentesController.crearDocente("David", "Tua", crearHashmapDisponibilidad(turnoM, turnoN, turnoT));
    Docente docente2 = docentesController.crearDocente("Ana", "Martinez", crearHashmapDisponibilidad(turnoM, turnoN, turnoT));

    // Crea un curso
    Curso curso1 = cursosController.crearCurso(materia1.getCodigo(), aula1.getNumero(), turnoM, docente1.getLegajo(), LocalDate.of(2024, 4, 19), LocalDate.of(2024, 7, 19), "Lunes", 10);
    Curso curso2 = cursosController.crearCurso(materia2.getCodigo(), aula2.getNumero(), turnoT, docente2.getLegajo(), LocalDate.of(2024, 3, 19), LocalDate.of(2024, 7, 19), "Martes", 10);
    Curso curso3 = cursosController.crearCurso(materia4.getCodigo(), aula3.getNumero(), turnoN, docente2.getLegajo(), LocalDate.of(2024, 3, 19), LocalDate.of(2024, 7, 19), "Martes", 10);

    // Crea un alumno
    Alumno alumno1 = alumnosController.crearAlumno("Enzo", "Cazenave", carrera.getNombre());
    Alumno alumno2 = alumnosController.crearAlumno("Matias", "Romero", carrera.getNombre());

    alumno1.aprobarMateria(materia1.getCodigo());
    alumno2.aprobarMateria(materia1.getCodigo());

    // Fecha estipulada de inscripcion
    inscripcionesController.setNuevaFechaDeInscripciones(LocalDate.of(2024, 5, 20), LocalDate.of(2024, 5, 30));
    inscripcionesController.setPrecioMateria(55541.75);

    // Crear inscripcion
    Inscripcion inscripcion1 = inscripcionesController.crearInscripcion(alumno1.getLegajo(), curso2.getCurso());
    Inscripcion inscripcion2 = inscripcionesController.crearInscripcion(alumno1.getLegajo(), curso3.getCurso());
    Inscripcion inscripcion3 = inscripcionesController.crearInscripcion(alumno2.getLegajo(), curso3.getCurso());

    // Pagar inscripciones creadas
    String linkDePago1 = inscripcionesController.pagarInscripcionesPendientesDeAlumno(alumno1.getLegajo());
    String linkDePago2 = inscripcionesController.pagarInscripcionesPendientesDeAlumno(alumno2.getLegajo());

    System.out.println("Link de pago " + alumno1.getNombre() + " " + alumno1.getApellido() + ": " + linkDePago1);
    System.out.println("Link de pago " + alumno2.getNombre() + " " + alumno2.getApellido() + ": " + linkDePago2);

    // Generacion de pdf de cursos asignados a docente
    docentesController.generarInformeDeCursosAsignadosPorDocentePDF(docente1.getLegajo());
    docentesController.generarInformeDeCursosAsignadosPorDocentePDF(docente2.getLegajo());
    docentesController.generarInformeDeCursosAsignadosPorDocenteExcel(docente1.getLegajo());
    docentesController.generarInformeDeCursosAsignadosPorDocenteExcel(docente2.getLegajo());

    // Generacion de cronograma
    formatearCronograma(docentesController.getCronogramaSemanalPorLegajoDocente(docente2.getLegajo()));

    // Cursos por materia
    System.out.println("\n\n\n Cursos de la materia con codigo: " + materia1.getCodigo());
    System.out.println(cursosController.getCursosDeMateria(materia1.getCodigo()));
    System.out.println();

    // Buscar cursos por turno y/o materia
    System.out.println("Cursos del turno mañana");
    System.out.println(inscripcionesController.getCursosPorMateriaYTurno(null, turnoM));
    System.out.println();

    System.out.println("Cursos del turno mañana y materia1");
    System.out.println(inscripcionesController.getCursosPorMateriaYTurno(materia1.getCodigo(), turnoM));
    System.out.println();

    // Carga horaria mensual de docente
    double cargaHorariaMensual = docentesController.getCargaHorariaMensualPorLegajoDocente(docente1.getLegajo());
    System.out.println("La carga horaria de " + docente1.getNombre() + " " + docente1.getApellido() + " es: " + cargaHorariaMensual + "hs");
    System.out.println();

    // Cantidad de inscriptos en un curso
    int cantidadInscriptos = inscripcionesController.getCantidadAlumnosEnCurso(curso1.getCurso());

    System.out.println("Cantidad de alumnos inscriptos en el curso " + curso1.getCurso() + ": " + cantidadInscriptos);
  }

  public static void formatearCronograma(HashMap<String, ArrayList<UUID>> cronograma) {
    CursosController cursosController = CursosController.getInstance();
    MateriasController materiasController = MateriasController.getInstance();

    System.out.println("==================================");
    System.out.println("||          CRONOGRAMA          ||");
    System.out.println("==================================");
    System.out.println();

    for (String dia : cronograma.keySet()) {
      System.out.print(dia + ": ");

      ArrayList<UUID> cursos = cronograma.get(dia);

      for (UUID cursoId : cursos) {
        Curso curso = cursosController.getCursoPorId(cursoId);
        Materia materia = materiasController.getMateriaPorCodigo(curso.getCodigoMateria());
        System.out.print(materia.getNombre() + " ");
      }

      System.out.println("\n==================================");
    }
  }

  public static HashMap<String, ArrayList<Turno>> crearHashmapDisponibilidad(Turno turno1, Turno turno2, Turno turno3) {
    HashMap<String, ArrayList<Turno>> disponibilidad = new HashMap<>();
    String[] dias = {"Lunes", "Martes", "Miercoles", "Jueves", "Viernes"};

    for (String dia : dias) {
      ArrayList<Turno> disponibilidadDeTurnos = new ArrayList<>();
      Random random = new Random();

      if (random.nextInt(10) >= 1) {
        disponibilidadDeTurnos.add(turno1);
        disponibilidadDeTurnos.add(turno2);
        disponibilidadDeTurnos.add(turno3);
      } else {
        disponibilidadDeTurnos.add(turno3);
        disponibilidadDeTurnos.add(turno2);
      }

      disponibilidad.put(dia, disponibilidadDeTurnos);
    }

    return disponibilidad;
  }
}