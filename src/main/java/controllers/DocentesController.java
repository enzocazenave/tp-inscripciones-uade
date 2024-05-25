package controllers;
import api.DocentesControllerInterface;
import impl.Curso;
import impl.Docente;
import impl.Materia;
import impl.Turno;
import services.ExcelGeneratorService;
import services.PDFGeneratorService;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class DocentesController implements DocentesControllerInterface {
    private static DocentesController instance = new DocentesController();
    private HashMap<UUID, Docente> docentes = new HashMap<>();

    private DocentesController() {}

    public static DocentesController getInstance() {
        return instance;
    }

    public Docente crearDocente(String nombre, String apellido, HashMap<String, ArrayList<Turno>> disponibilidad) {
        Docente nuevoDocente = new Docente(nombre, apellido, disponibilidad);
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

    public double getCargaHorariaMensualPorLegajoDocente(UUID legajoDocente) {
        CursosController cursosController = CursosController.getInstance();
        MateriasController materiasController = MateriasController.getInstance();
        ArrayList<UUID> cursosAsignados = this.getCursosAsignadosPorLegajoDocente(legajoDocente);

        int cargaHorariaMensual = 0;

        for (UUID cursoId : cursosAsignados) {
            Curso curso = cursosController.getCursoPorId(cursoId);
            Materia materia = materiasController.getMateriaPorCodigo(curso.getCodigoMateria());

            cargaHorariaMensual += materia.getCargaHoraria();
        }

        return (double) cargaHorariaMensual / 4; // divido 4 meses
    }

    public void generarInformeDeCursosAsignadosPorDocentePDF(UUID legajoDocente) throws FileNotFoundException {
        PDFGeneratorService pdfGeneratorService = PDFGeneratorService.getInstance();
        pdfGeneratorService.generarInformeDeCursosAsignadosPorDocente(legajoDocente);
    }

    public void generarInformeDeCursosAsignadosPorDocenteExcel(UUID legajoDocente) throws FileNotFoundException {
        ExcelGeneratorService excelGeneratorService = ExcelGeneratorService.getInstance();
        excelGeneratorService.generarInformeDeCursosAsignadosPorDocente(legajoDocente);
    }
}
