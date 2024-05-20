package impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Docente {
    private UUID legajo;
    private String nombre;
    private String apellido;
    private String mail;
    private String imageSrc;
    private HashMap<String, ArrayList<Turno>> disponibilidad;


    public Docente(String nombre, String apellido, HashMap<String, ArrayList<Turno>> disponibilidad) {
        this.legajo = UUID.randomUUID();
        this.nombre = nombre;
        this.apellido = apellido;
        this.disponibilidad = disponibilidad;
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

    public HashMap<String, ArrayList<Turno>> getDisponibilidad() {
        return this.disponibilidad;
    }
}
