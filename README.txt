Visor de Partidas de Ajedrez 
================================

Esta aplicación permite visualizar partidas de ajedrez en formato PGN (Portable Game Notation).

Requisitos
----------
- Java JDK 11 o superior
- IDE compatible con Java (IntelliJ IDEA recomendado)

Instalación
-----------
1. Clonar el repositorio o descargar los archivos fuente
2. Abrir el proyecto en el IDE
3. Asegurarse que todas las dependencias están correctamente configuradas

Ejecución
---------
1. Ejecutar la clase Main.java
2. La interfaz gráfica se abrirá automáticamente
3. El archivo partida.pgn debe estar en la carpeta raíz del proyecto

Funcionalidades
--------------
- Visualización del tablero de ajedrez con piezas
- Carga de partidas desde archivos PGN
- Navegación de movimientos con botones "Anterior" y "Siguiente"
- Visualización del historial de movimientos
- Representación gráfica de las piezas en el tablero

Controles
---------
- Botón "Anterior": Retrocede un movimiento en la partida
- Botón "Siguiente": Avanza un movimiento en la partida

Estructura del Proyecto
----------------------
- src/
  - Controlador/: Manejo de la lógica del juego
  - Modelo/: Clases para la representación del tablero y piezas
  - Vista/: Interfaz gráfica
- partida.pgn: Archivo con la partida de ejemplo

Notas
-----
- La aplicación utiliza el patrón MVC (Modelo-Vista-Controlador)
- Los movimientos PGN deben estar en formato estándar
- Se recomienda usar partidas completas y correctamente formateadas
