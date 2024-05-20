package impl;
import java.util.ArrayList;
import java.util.UUID;

public class Carrera {
    private String nombre;
    private ArrayList<UUID> materias;
    private int cargaHorariaMaximaDeCursada;

    public Carrera(String nombre, ArrayList<UUID> materias, int cargaHorariaMaximaDeCursada) {
        this.nombre = nombre;
        this.materias = materias;
        this.cargaHorariaMaximaDeCursada = cargaHorariaMaximaDeCursada;
    }

    public String getNombre() {
        return this.nombre;
    }

    public ArrayList<UUID> getMaterias() {
        return this.materias;
    }

    public int getCargaHorariaMaximaDeCursada() {
        return cargaHorariaMaximaDeCursada;
    }
}
