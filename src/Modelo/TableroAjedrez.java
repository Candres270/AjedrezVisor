package Modelo;

import java.util.ArrayList;
import java.util.List;

public class TableroAjedrez {
    private PiezaAjedrez[][] tablero;
    private List<String> historialMovimientos;
    private boolean turnoBlancas;

    public TableroAjedrez() {
        tablero = new PiezaAjedrez[8][8];
        historialMovimientos = new ArrayList<>();
        turnoBlancas = true;
        inicializarTablero();
    }

    private void inicializarTablero() {
        // Inicializar piezas blancas
        tablero[0][0] = new PiezaAjedrez("Torre", 'B', "a1");
        tablero[0][1] = new PiezaAjedrez("Caballo", 'B', "b1");
        tablero[0][2] = new PiezaAjedrez("Alfil", 'B', "c1");
        tablero[0][3] = new PiezaAjedrez("Dama", 'B', "d1");
        tablero[0][4] = new PiezaAjedrez("Rey", 'B', "e1");
        tablero[0][5] = new PiezaAjedrez("Alfil", 'B', "f1");
        tablero[0][6] = new PiezaAjedrez("Caballo", 'B', "g1");
        tablero[0][7] = new PiezaAjedrez("Torre", 'B', "h1");

        for (int i = 0; i < 8; i++) {
            tablero[1][i] = new PiezaAjedrez("Peón", 'B', (char)('a' + i) + "2");
        }

        // Inicializar piezas negras
        tablero[7][0] = new PiezaAjedrez("Torre", 'N', "a8");
        tablero[7][1] = new PiezaAjedrez("Caballo", 'N', "b8");
        tablero[7][2] = new PiezaAjedrez("Alfil", 'N', "c8");
        tablero[7][3] = new PiezaAjedrez("Dama", 'N', "d8");
        tablero[7][4] = new PiezaAjedrez("Rey", 'N', "e8");
        tablero[7][5] = new PiezaAjedrez("Alfil", 'N', "f8");
        tablero[7][6] = new PiezaAjedrez("Caballo", 'N', "g8");
        tablero[7][7] = new PiezaAjedrez("Torre", 'N', "h8");

        for (int i = 0; i < 8; i++) {
            tablero[6][i] = new PiezaAjedrez("Peón", 'N', (char)('a' + i) + "7");
        }
    }

    public void realizarMovimiento(String movimientoPGN) {
        if (movimientoPGN == null || movimientoPGN.isEmpty()) {
            return;
        }

        movimientoPGN = movimientoPGN.replaceAll("[+#]", "").trim();

        if (movimientoPGN.equals("O-O") || movimientoPGN.equals("O-O-O")) {
            realizarEnroque(movimientoPGN);
            historialMovimientos.add(movimientoPGN);
            turnoBlancas = !turnoBlancas;
            return;
        }

        try {
            char tipoPieza = 'P';
            int startIndex = 0;
            if (Character.isUpperCase(movimientoPGN.charAt(0)) && movimientoPGN.charAt(0) != 'O') {
                tipoPieza = movimientoPGN.charAt(0);
                startIndex = 1;
            }

            boolean hayCaptura = movimientoPGN.contains("x");
            movimientoPGN = movimientoPGN.replace("x", "");

            String destino = movimientoPGN.substring(movimientoPGN.length() - 2);
            int columnaDestino = destino.charAt(0) - 'a';
            int filaDestino = 8 - Character.getNumericValue(destino.charAt(1));

            String especificador = movimientoPGN.substring(startIndex, movimientoPGN.length() - 2);
            PiezaAjedrez piezaAMover = encontrarPiezaParaMovimiento(tipoPieza, columnaDestino, filaDestino,
                    especificador, turnoBlancas ? 'B' : 'N');

            if (piezaAMover != null) {
                int[] posicionActual = encontrarPosicionPieza(piezaAMover);
                if (posicionActual != null) {
                    tablero[filaDestino][columnaDestino] = piezaAMover;
                    tablero[posicionActual[0]][posicionActual[1]] = null;
                    piezaAMover.setPosicion(String.format("%c%d", (char)('a' + columnaDestino), 8 - filaDestino));
                    historialMovimientos.add(movimientoPGN);
                    turnoBlancas = !turnoBlancas;
                }
            }
        } catch (Exception e) {
            System.err.println("Error al procesar movimiento: " + movimientoPGN);
            e.printStackTrace();
        }
    }

    private void realizarEnroque(String movimiento) {
        int fila = turnoBlancas ? 0 : 7;
        if (movimiento.equals("O-O")) {
            // Enroque corto
            PiezaAjedrez rey = tablero[fila][4];
            PiezaAjedrez torre = tablero[fila][7];
            if (rey != null && torre != null) {
                tablero[fila][6] = rey;
                tablero[fila][5] = torre;
                tablero[fila][4] = null;
                tablero[fila][7] = null;
                rey.setPosicion(String.format("%c%d", 'g', fila + 1));
                torre.setPosicion(String.format("%c%d", 'f', fila + 1));
            }
        } else {
            // Enroque largo
            PiezaAjedrez rey = tablero[fila][4];
            PiezaAjedrez torre = tablero[fila][0];
            if (rey != null && torre != null) {
                tablero[fila][2] = rey;
                tablero[fila][3] = torre;
                tablero[fila][4] = null;
                tablero[fila][0] = null;
                rey.setPosicion(String.format("%c%d", 'c', fila + 1));
                torre.setPosicion(String.format("%c%d", 'd', fila + 1));
            }
        }
    }

    private PiezaAjedrez encontrarPiezaParaMovimiento(char tipoPieza, int columnaDestino, int filaDestino,
                                                      String especificador, char color) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                PiezaAjedrez pieza = tablero[i][j];
                if (pieza != null && pieza.getColor() == color && esPiezaCorrecta(pieza, tipoPieza)) {
                    if (puedeMoverse(i, j, filaDestino, columnaDestino) &&
                            coincideEspecificador(pieza, i, j, especificador)) {
                        return pieza;
                    }
                }
            }
        }
        return null;
    }

    private boolean esPiezaCorrecta(PiezaAjedrez pieza, char tipoPieza) {
        switch (tipoPieza) {
            case 'P': return pieza.getNombre().equals("Peón");
            case 'N': return pieza.getNombre().equals("Caballo");
            case 'B': return pieza.getNombre().equals("Alfil");
            case 'R': return pieza.getNombre().equals("Torre");
            case 'Q': return pieza.getNombre().equals("Dama");
            case 'K': return pieza.getNombre().equals("Rey");
            default: return false;
        }
    }

    private boolean puedeMoverse(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) {
        // Implementación básica - se puede mejorar con reglas específicas de movimiento
        return true;
    }

    private boolean coincideEspecificador(PiezaAjedrez pieza, int fila, int columna, String especificador) {
        if (especificador.isEmpty()) {
            return true;
        }

        for (char c : especificador.toCharArray()) {
            if (Character.isLetter(c)) {
                if (columna != (c - 'a')) {
                    return false;
                }
            } else if (Character.isDigit(c)) {
                if (fila != (8 - Character.getNumericValue(c))) {
                    return false;
                }
            }
        }
        return true;
    }

    private int[] encontrarPosicionPieza(PiezaAjedrez pieza) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (tablero[i][j] == pieza) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    public PiezaAjedrez[][] getTablero() {
        return tablero;
    }

    public List<String> getHistorialMovimientos() {
        return new ArrayList<>(historialMovimientos);
    }

    public void setTablero(PiezaAjedrez[][] nuevoTablero) {
        if (nuevoTablero != null && nuevoTablero.length == 8 && nuevoTablero[0].length == 8) {
            for (int i = 0; i < 8; i++) {
                System.arraycopy(nuevoTablero[i], 0, tablero[i], 0, 8);
            }
        }
    }

    public boolean getTurnoBlancas() {
        return turnoBlancas;
    }

    public void reiniciarTablero() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tablero[i][j] = null;
            }
        }
        inicializarTablero();
        turnoBlancas = true;
        historialMovimientos.clear();
    }
}