package api;

import impl.Inscripcion;
import impl.Turno;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

public interface InscripcionesControllerInterface {
    Inscripcion crearInscripcion(UUID legajoAlumno, UUID cursoId);
    String pagarInscripcionesPendientesDeAlumno(UUID legajoAlumno);
    void setPrecioMateria(double precio);
    void setNuevaFechaDeInscripciones(LocalDate inicio, LocalDate fin);
    int getCantidadAlumnosEnCurso(UUID cursoId);
    ArrayList<UUID> getCursosPorMateriaYTurno(UUID codigoMateriaBuscada, Turno turnoBuscado);
}
