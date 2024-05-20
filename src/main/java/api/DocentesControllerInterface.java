package api;

import impl.Docente;
import impl.Turno;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public interface DocentesControllerInterface {
    Docente crearDocente(String nombre, String apellido, HashMap<String, ArrayList<Turno>> disponibilidad);
    Docente getDocentePorLegajo(UUID legajo);
    ArrayList<UUID> getCursosAsignadosPorLegajoDocente(UUID legajoDocente);
    HashMap<String, ArrayList<UUID>> getCronogramaSemanalPorLegajoDocente(UUID legajoDocente);
    double getCargaHorariaMensualPorLegajoDocente(UUID legajoDocente);
}
