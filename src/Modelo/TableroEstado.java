package Modelo;

public class TableroEstado {
    private PiezaAjedrez[][] estado;

    public TableroEstado(PiezaAjedrez[][] tablero) {
        this.estado = new PiezaAjedrez[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (tablero[i][j] != null) {
                    this.estado[i][j] = new PiezaAjedrez(
                            tablero[i][j].getNombre(),
                            tablero[i][j].getColor(),
                            tablero[i][j].getPosicion()
                    );
                }
            }
        }
    }

    public PiezaAjedrez[][] getEstado() {
        return estado;
    }
}
