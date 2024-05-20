package impl;
import java.time.LocalDate;
import java.util.UUID;

public class Curso {
    private UUID curso;
    private UUID codigoMateria;
    private int numeroAula;
    private Turno turno;
    private UUID legajoDocente;
    private LocalDate fechaInicio;
    private LocalDate fechaFinalRegular;
    private String dia;
    private int capacidad;

    public Curso(UUID codigoMateria, int numeroAula, Turno turno, UUID legajoDocente, LocalDate fechaInicio, LocalDate fechaFinalRegular, String dia, int capacidad) {
        this.curso = UUID.randomUUID();
        this.codigoMateria = codigoMateria;
        this.numeroAula = numeroAula;
        this.turno = turno;
        this.legajoDocente = legajoDocente;
        this.fechaInicio = fechaInicio;
        this.fechaFinalRegular = fechaFinalRegular;
        this.dia = dia;
        this.capacidad = capacidad;
    }

    public UUID getCurso() {
        return this.curso;
    }

    public UUID getCodigoMateria() {
        return this.codigoMateria;
    }

    public int getNumeroAula() {
        return this.numeroAula;
    }

    public Turno getTurno() {
        return this.turno;
    }

    public UUID getLegajoDocente() {
        return this.legajoDocente;
    }

    public LocalDate getFechaInicio() {
        return this.fechaInicio;
    }

    public LocalDate getFechaFinalRegular() {
        return this.fechaFinalRegular;
    }

    public String getDia() {
        return this.dia;
    }

    public int getCapacidad() {
        return this.capacidad;
    }
}
