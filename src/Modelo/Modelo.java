package Modelo;

import java.util.ArrayList;
import java.util.List;

// Clase principal del modelo
public class Modelo {
    private TableroAjedrez tablero;
    private List<String> movimientosPGN;

    public Modelo() {
        tablero = new TableroAjedrez();
        movimientosPGN = new ArrayList<>();
    }

    public void cargarMovimientosPGN(List<String> movimientos) {
        this.movimientosPGN = movimientos;
        // No realizamos los movimientos aquí, lo haremos en el Controlador
    }

    public TableroAjedrez getTablero() {
        return tablero;
    }

    public List<String> getMovimientosPGN() {
        return movimientosPGN;
    }

    public void reiniciarTablero() {
        tablero = new TableroAjedrez();
    }

    public void setTablero(PiezaAjedrez[][] estado) {
        tablero.setTablero(estado);
    }
}