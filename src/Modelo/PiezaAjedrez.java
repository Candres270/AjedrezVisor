package Modelo;

import java.util.ArrayList;
import java.util.List;

// Clase para representar una pieza de ajedrez
public class PiezaAjedrez {
    private String nombre;
    private char color; // 'B' para blancas, 'N' para negras
    private String posicion;

    public PiezaAjedrez(String nombre, char color, String posicion) {
        this.nombre = nombre;
        this.color = color;
        this.posicion = posicion;
    }

    public String getNombre() {
        return nombre;
    }

    public char getColor() {
        return color;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }
}
