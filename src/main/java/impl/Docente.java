package impl;
import java.util.UUID;

public class Docente {
    private UUID legajo;
    private String nombre;
    private String apellido;
    private String mail;
    private String imageSrc;

    public Docente(String nombre, String apellido) {
        this.legajo = UUID.randomUUID();
        this.nombre = nombre;
        this.apellido = apellido;
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
}
