package impl;
import java.util.ArrayList;
import java.util.UUID;

public class Materia {
    private UUID codigo;
    private String nombre;
    private int cargaHoraria;
    private ArrayList<UUID> correlativasAnteriores;
    private ArrayList<UUID> correlativasPosteriores;

    public Materia(String nombre, int cargaHoraria, ArrayList<UUID> correlativasAnteriores, ArrayList<UUID> correlativasPosteriores) {
        this.codigo = UUID.randomUUID();
        this.nombre = nombre;
        this.cargaHoraria = cargaHoraria;
        this.correlativasAnteriores = correlativasAnteriores;
        this.correlativasPosteriores = correlativasPosteriores;
    }

    public UUID getCodigo() {
        return this.codigo;
    }

    public String getNombre() {
        return this.nombre;
    }

    public int getCargaHoraria() {
        return this.cargaHoraria;
    }

    public ArrayList<UUID> getCorrelativasAnteriores() {
        return correlativasAnteriores;
    }

    public ArrayList<UUID> getCorrelativasPosteriores() {
        return correlativasPosteriores;
    }
}
