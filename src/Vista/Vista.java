package Vista;


import Modelo.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Vista extends JFrame {
    private JPanel panelTablero;
    private JButton[][] casillas;
    private JButton btnAnterior, btnSiguiente;
    private JTextArea areaMovimientos;
    private static final Map<String, String> SIMBOLOS_UNICODE = new HashMap<>();



    static {
        // Símbolos Unicode para las piezas
        SIMBOLOS_UNICODE.put("Rey-B", "♔");
        SIMBOLOS_UNICODE.put("Dama-B", "♕");
        SIMBOLOS_UNICODE.put("Torre-B", "♖");
        SIMBOLOS_UNICODE.put("Alfil-B", "♗");
        SIMBOLOS_UNICODE.put("Caballo-B", "♘");
        SIMBOLOS_UNICODE.put("Peón-B", "♙");

        SIMBOLOS_UNICODE.put("Rey-N", "♚");
        SIMBOLOS_UNICODE.put("Dama-N", "♛");
        SIMBOLOS_UNICODE.put("Torre-N", "♜");
        SIMBOLOS_UNICODE.put("Alfil-N", "♝");
        SIMBOLOS_UNICODE.put("Caballo-N", "♞");
        SIMBOLOS_UNICODE.put("Peón-N", "♟");
    }

    public Vista() {
        configurarVentana();
        inicializarComponentes();
        configurarLayout();
    }

    private void configurarVentana() {
        setTitle("Visor de Partidas de Ajedrez");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
    }

    private void inicializarComponentes() {
        panelTablero = new JPanel(new GridLayout(8, 8));
        casillas = new JButton[8][8];

        // Inicializar casillas del tablero
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                casillas[i][j] = new JButton();
                casillas[i][j].setPreferredSize(new Dimension(60, 60));


                // Usar una fuente que soporte símbolos Unicode de ajedrez
                casillas[i][j].setFont(new Font("Arial Unicode MS", Font.PLAIN, 40));
                if ((i + j) % 2 == 0) {
                    casillas[i][j].setBackground(new Color(240, 217, 181));
                } else {
                    casillas[i][j].setBackground(new Color(181, 136, 99));
                }
                casillas[i][j].setFocusPainted(false);
                casillas[i][j].setBorderPainted(false);
                panelTablero.add(casillas[i][j]);
            }

        }

        btnAnterior = new JButton("Anterior");
        btnSiguiente = new JButton("Siguiente");
        areaMovimientos = new JTextArea(10, 30);
        areaMovimientos.setEditable(false);
    }

    private void configurarLayout() {
        setLayout(new BorderLayout());
        add(panelTablero, BorderLayout.CENTER);

        JPanel panelControles = new JPanel();
        panelControles.add(btnAnterior);
        panelControles.add(btnSiguiente);
        add(panelControles, BorderLayout.SOUTH);
        JScrollPane scrollMovimientos = new JScrollPane(areaMovimientos);
        add(scrollMovimientos, BorderLayout.EAST);
    }

    public void actualizarTablero(PiezaAjedrez[][] estadoTablero) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (estadoTablero[i][j] != null) {
                    PiezaAjedrez pieza = estadoTablero[i][j];
                    String clave = pieza.getNombre() + "-" + pieza.getColor();
                    String simbolo = SIMBOLOS_UNICODE.get(clave);

                    if (simbolo != null) {
                        casillas[7-i][j].setText(simbolo);
                        // Cambiar el color de las piezas blancas a un tono más oscuro
                        if (pieza.getColor() == 'B') {
                            casillas[7-i][j].setForeground(new Color(255, 250, 240)); // Un blanco más oscuro
                        } else {
                            casillas[7-i][j].setForeground(new Color(0, 0, 0)); // Negro puro para las piezas negras
                        }
                    } else {
                        System.out.println("No se encontró símbolo para: " + clave);
                        casillas[7-i][j].setText(pieza.getNombre().substring(0, 1));
                    }
                } else {
                    casillas[7-i][j].setText("");
                }
            }
        }
    }

    public void mostrarMovimientos(List<String> movimientos) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < movimientos.size(); i++) {
            if (i % 2 == 0) {
                sb.append((i / 2 + 1)).append(". ");
            }
            sb.append(movimientos.get(i)).append(" ");
            if (i % 2 == 1) {
                sb.append("\n");
            }
        }
        areaMovimientos.setText(sb.toString());
    }

    public void setControlador(ActionListener controlador) {
        btnAnterior.addActionListener(controlador);
        btnAnterior.setActionCommand("ANTERIOR");
        btnSiguiente.addActionListener(controlador);
        btnSiguiente.setActionCommand("SIGUIENTE");
    }
}
