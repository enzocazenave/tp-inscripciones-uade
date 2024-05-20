package impl;
import java.util.ArrayList;
import java.util.UUID;

public class Alumno {
    private UUID legajo;
    private String nombre;
    private String apellido;
    private ArrayList<UUID> materiasAprobadas;
    private String mail;
    private String imageSrc;
    private String carrera;

    public Alumno(String nombre, String apellido, String carrera) {
        this.legajo = UUID.randomUUID();
        this.nombre = nombre;
        this.apellido = apellido;
        this.materiasAprobadas = new ArrayList<>();
        this.carrera = carrera;
    }

    public void aprobarMateria(UUID codigoMateria) {
        if (materiasAprobadas.contains(codigoMateria)) {
            throw new IllegalArgumentException("La materia con el codigo " + codigoMateria + "ya está aprobada");
        }

        materiasAprobadas.add(codigoMateria);
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getApellido() {
        return this.apellido;
    }

    public UUID getLegajo() {
        return this.legajo;
    }

    public ArrayList<UUID> getMateriasAprobadas() {
        return this.materiasAprobadas;
    }

    public String getCarrera() {
        return this.carrera;
    }
}