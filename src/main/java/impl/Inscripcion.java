package impl;
import java.util.UUID;

public class Inscripcion {
    private UUID id;
    private UUID legajoAlumno;
    private String estado; // pendiente - confirmado

    public Inscripcion(UUID legajoAlumno) {
        this.id = UUID.randomUUID();
        this.legajoAlumno = legajoAlumno;
        this.estado = "pendiente";
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return this.estado;
    }

    public UUID getId() {
        return this.id;
    }

    public UUID getLegajoAlumno() {
        return this.legajoAlumno;
    }
}
