package Controlador;

import Modelo.*;
import Vista.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controlador implements ActionListener {
    private Modelo modelo;
    private Vista vista;
    private int movimientoActual;
    private List<TableroEstado> historicoTableros;

    public Controlador(Modelo modelo, Vista vista) {
        this.modelo = modelo;
        this.vista = vista;
        this.movimientoActual = 0;
        this.historicoTableros = new ArrayList<>();
        vista.setControlador(this);

        try {
            cargarPartidaPGN("partida.pgn");
            inicializarHistoricoTableros();
            actualizarVista();
        } catch (Exception e) {
            System.err.println("Error al inicializar el controlador: " + e.getMessage());
        }
    }

    private void inicializarHistoricoTableros() {
        historicoTableros.clear();

        // Guardar estado inicial
        TableroAjedrez tableroInicial = new TableroAjedrez();
        historicoTableros.add(new TableroEstado(tableroInicial.getTablero()));

        // Crear tablero temporal para simular movimientos
        TableroAjedrez tableroTemp = new TableroAjedrez();

        // Simular movimientos
        for (String movimiento : modelo.getMovimientosPGN()) {
            if (movimiento != null && !movimiento.isEmpty()) {
                tableroTemp.realizarMovimiento(movimiento);
                historicoTableros.add(new TableroEstado(tableroTemp.getTablero()));
            }
        }

        movimientoActual = 0;
        modelo.reiniciarTablero();
    }

    private void cargarPartidaPGN(String archivo) {
        List<String> movimientos = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(archivo))) {
            StringBuilder contenidoMovimientos = new StringBuilder();
            boolean enMetadatos = true;

            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine().trim();

                // Detectar fin de metadatos
                if (enMetadatos && linea.isEmpty()) {
                    enMetadatos = false;
                    continue;
                }

                // Saltar metadatos y líneas vacías
                if (enMetadatos || linea.isEmpty() || linea.startsWith("[")) {
                    continue;
                }

                contenidoMovimientos.append(linea).append(" ");
            }

            // Procesar el contenido
            String contenido = contenidoMovimientos.toString()
                    .replaceAll("\\{.*?\\}", "") // Eliminar comentarios
                    .replaceAll("1-0|0-1|1/2-1/2", "") // Eliminar resultado
                    .trim();

            // Patrón para capturar pares de movimientos (blancas y negras)
            Pattern pattern = Pattern.compile("\\d+\\.\\s*([^\\s]+)\\s+([^\\s]+)");
            Matcher matcher = pattern.matcher(contenido);

            while (matcher.find()) {
                // Añadir movimiento de blancas
                String movimientoBlancas = matcher.group(1);
                movimientos.add(movimientoBlancas);

                // Añadir movimiento de negras
                String movimientoNegras = matcher.group(2);
                movimientos.add(movimientoNegras);
            }

            // Verificar si hay un último movimiento de blancas sin respuesta
            if (contenido.matches(".*\\d+\\.\\s*([^\\s]+)\\s*$")) {
                Pattern lastPattern = Pattern.compile("\\d+\\.\\s*([^\\s]+)\\s*$");
                Matcher lastMatcher = lastPattern.matcher(contenido);
                if (lastMatcher.find()) {
                    movimientos.add(lastMatcher.group(1));
                }
            }

            // Debug: imprimir movimientos procesados
            System.out.println("Movimientos procesados:");
            for (int i = 0; i < movimientos.size(); i++) {
                System.out.println((i % 2 == 0 ? "Blancas: " : "Negras: ") + movimientos.get(i));
            }

        } catch (FileNotFoundException e) {
            System.err.println("No se pudo encontrar el archivo PGN: " + e.getMessage());
            e.printStackTrace();
        }

        modelo.cargarMovimientosPGN(movimientos);
    }

    private void actualizarVista() {
        if (movimientoActual >= 0 && movimientoActual < historicoTableros.size()) {
            TableroEstado estado = historicoTableros.get(movimientoActual);
            if (estado != null) {
                modelo.setTablero(estado.getEstado());
                vista.actualizarTablero(estado.getEstado());
                vista.mostrarMovimientos(modelo.getMovimientosPGN());
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "ANTERIOR":
                if (movimientoActual > 0) {
                    movimientoActual--;
                    actualizarVista();
                }
                break;
            case "SIGUIENTE":
                if (movimientoActual < historicoTableros.size() - 1) {
                    movimientoActual++;
                    actualizarVista();
                }
                break;
        }
    }
}