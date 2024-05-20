package controllers;
import impl.Alumno;
import java.util.HashMap;
import java.util.UUID;

public class AlumnosController {
    private static AlumnosController instance = new AlumnosController();
    private HashMap<UUID, Alumno> alumnos = new HashMap<>();

    private AlumnosController() {}

    public static AlumnosController getInstance() {
        return instance;
    }

    public Alumno crearAlumno(String nombre, String apellido, String carrera) {
        Alumno nuevoAlumno = new Alumno(nombre, apellido, carrera);
        alumnos.put(nuevoAlumno.getLegajo(), nuevoAlumno);
        return nuevoAlumno;
    }

    public Alumno getAlumnoPorLegajo(UUID legajo) {
        if (alumnos.containsKey(legajo)) {
            return this.alumnos.get(legajo);
        }

        throw new IllegalArgumentException("El alumno con legajo " + legajo + " que estás intentado obtener no existe.");
    }
}
