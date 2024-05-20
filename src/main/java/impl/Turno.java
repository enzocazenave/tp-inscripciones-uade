package impl;

public class Turno {
    private String nombre;
    private String horarioInicio;
    private String horarioFinalizacion;

    public Turno(String nombre, String horarioInicio, String horarioFinalizacion) {
        this.nombre = nombre;
        this.horarioInicio = horarioInicio;
        this.horarioFinalizacion = horarioFinalizacion;
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getHorarioInicio() {
        return this.horarioInicio;
    }

    public String getHorarioFinalizacion() {
        return this.horarioFinalizacion;
    }
}
