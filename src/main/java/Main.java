import com.mercadopago.resources.preference.Preference;
import controllers.*;
import impl.*;
import services.PDFGeneratorService;
import java.io.FileNotFoundException;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Main {
  public static void main(String[] args) throws FileNotFoundException {
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

    // Creacion de aula
    Aula aula1 = aulasController.crearAula(341, 50);
    Aula aula2 = aulasController.crearAula(742, 50);

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

    // Crea la carrera "Ingenieria Informatica"
    Carrera carrera = carrerasController.crearCarrera("Ingenieria Informatica", materiasDeCarrera, 476);

    // Crea un docente
    Docente docente1 = docentesController.crearDocente("David", "Tua");
    Docente docente2 = docentesController.crearDocente("Ana", "Martinez");

    // Crea un curso
    Curso curso1 = cursosController.crearCurso(
            materia1.getCodigo(),
            aula1.getNumero(),
            turnoM,
            docente1.getLegajo(),
            LocalDate.of(2024, 4, 19),
            LocalDate.of(2024, 7, 19),
            "Lunes",
            10
    );

    Curso curso2 = cursosController.crearCurso(
            materia2.getCodigo(),
            aula2.getNumero(),
            turnoT,
            docente2.getLegajo(),
            LocalDate.of(2024, 3, 19),
            LocalDate.of(2024, 7, 19),
            "Martes",
            10
    );

    Curso curso3 = cursosController.crearCurso(
            materia4.getCodigo(),
            aula2.getNumero(),
            turnoN,
            docente2.getLegajo(),
            LocalDate.of(2024, 3, 19),
            LocalDate.of(2024, 7, 19),
            "Martes",
            10
    );

    // Crea un alumno
    Alumno alumno = alumnosController.crearAlumno("Enzo", "Cazenave", carrera.getNombre());
    alumno.aprobarMateria(materia1.getCodigo());
    alumno.aprobarMateria(materia2.getCodigo());

    // Fecha estipulada de inscripcion
    inscripcionesController.setNuevaFechaDeInscripciones(LocalDate.of(2024, 5, 20), LocalDate.of(2024, 5, 30));
    inscripcionesController.setPrecioMateria(55541.75);

    // Crear inscripcion
    Inscripcion inscripcion1 = inscripcionesController.crearInscripcion(alumno.getLegajo(), curso1.getCurso());
    Inscripcion inscripcion2 = inscripcionesController.crearInscripcion(alumno.getLegajo(), curso3.getCurso());

    // Pagar inscripciones creadas
    String linkDePago = inscripcionesController.pagarInscripcionesPendientesDeAlumno(alumno.getLegajo());
    System.out.println("Link de pago de inscripciones: " + linkDePago);

    // Generacion de pdf de cursos asignados a docente
    PDFGeneratorService pdfGeneratorService = PDFGeneratorService.getInstance();
    pdfGeneratorService.generarInformeDeCursosAsignadosPorDocente(docente1.getLegajo());
    pdfGeneratorService.generarInformeDeCursosAsignadosPorDocente(docente2.getLegajo());

    // Generacion de cronograma
    formatearCronograma(docentesController.getCronogramaSemanalPorLegajoDocente(docente2.getLegajo()));

    // Cursos por materia
    System.out.println("\n\n\n Cursos de la materia con codigo: " + materia1.getCodigo());
    System.out.println(cursosController.getCursosDeMateria(materia1.getCodigo()));
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
}