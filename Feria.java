/**
 *  Feria con los 4 juegos ya disponibles.
 *  @author Diego Hernandez Vazquez
 *  @version 2.0  
 *  @date 10 dic 2024
 */
import java.io.*;
import java.util.*;

public class Feria {
    // Mapa que almacena a los jugadores registrados, con su nombre como clave y el objeto Jugador como valor
    static Map<String, Jugador> jugadores = new HashMap<>();
    
    // Objeto Scanner para leer entradas desde la consola
    static Scanner scanner = new Scanner(System.in);
    
    // Credito inicial que se asigna a cada jugador al registrarse
    static final int CREDITO_INICIAL = 100;
    
    // Costo fijo para participar en un juego
    static final int COSTO_JUEGO = 15;
    
    // Nombre del archivo donde se almacenan los datos de los jugadores
    static final String ARCHIVO_JUGADORES = "jugadores.dat";
    
    // Tamaño del tablero para el juego "Cuadrado Mágico"
    static final int N = 4; 
    
    public static void main(String[] args) {
        // Carga los datos de los jugadores guardados en el archivo
        cargarDatos(); 
        
        // Bucle principal que mantiene en ejecución el menú de la feria
        while (true) {
            // Muestra el menú con las opciones
            mostrarMenu();
            
            // Lee la opción seleccionada por el usuario
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer
            
            // Ejecuta la opción seleccionada
            switch (opcion) {
                case 1:
                    registrarJugador();  // Opción para registrar un jugador
                    break;
                case 2:
                    iniciarJuego();      // Opción para iniciar un nuevo juego
                    break;
                case 3:
                    verMejoresJugadores(); // Opción para ver los3 jugadores con mas creditos
                    break;
                case 4:
                    verPuntosJugador();    // Opción para ver los creditos del jugador actual
                    break;
                case 5:
                    guardarYSalir();      // Opción para guardar los datos y salir del programa
                    return;
                default:
                    System.out.println("Opción no válida. Intenta nuevamente.");
            }
        }
    }

    /**
     * Muestra el menú principal con las opciones disponibles.
     */
    private static void mostrarMenu() {
        System.out.println("\n--- ¡Bienvenido a la feria! ---");
        System.out.println("1. Registrar jugador");
        System.out.println("2. Iniciar nuevo juego");
        System.out.println("3. Ver top 3 jugadores");
        System.out.println("4. Ver tus creditos actualmente");
        System.out.println("5. Guardar y salir");
        System.out.print("Selecciona una opción: ");
    }

    /**
     * Permite registrar un nuevo jugador o saludar a uno ya registrado.
     */
    private static void registrarJugador() {
        System.out.print("Ingresa tu nombre: ");
        String nombre = scanner.nextLine();
        
        // Verifica si el jugador ya está registrado
        if (jugadores.containsKey(nombre)) {
            System.out.println("¡Bienvenido de vuelta, " + nombre + "!");
            System.out.println("Tienes " + jugadores.get(nombre).getCreditos() + " créditos disponibles");
        } else {
            // Si no está registrado, crea un nuevo jugador con crédito inicial
            Jugador nuevoJugador = new Jugador(nombre, CREDITO_INICIAL);
            jugadores.put(nombre, nuevoJugador);
            System.out.println("¡Registro exitoso! Has sido registrado con " + CREDITO_INICIAL + " créditos");
        }
    }
/**
 * Muestra los puntos y créditos del jugador actual.
 * Pide al jugador que ingrese su nombre y muestra sus créditos si está registrado.
 */
private static void verPuntosJugador() {
    System.out.print("Ingresa tu nombre: ");
    String nombre = scanner.nextLine();

    if (jugadores.containsKey(nombre)) {
        Jugador jugador = jugadores.get(nombre);
        System.out.println("Tienes " + jugador.getCreditos() + " créditos.");
    } else {
        System.out.println("Jugador no registrado.");
    }
}

    /**
     * Inicia un nuevo juego. Permite seleccionar entre diferentes juegos disponibles.
     */
    private static void iniciarJuego() {
        // Muestra las opciones de juegos disponibles
        System.out.println("\nSelecciona un juego para jugar:");
        System.out.println("1. Cuadrado Mágico (Costo: 15 créditos)");
        System.out.println("2. Conecta 4 (Costo: 5 créditos)");
        System.out.println("3. Salvado (Costo: 15 créditos)");
        System.out.println("4. Torres de Hanoi (Costo: 15 créditos)");
       
        int opcionJuego = scanner.nextInt(); // Lee la opción del juego
        scanner.nextLine(); // Limpiar el buffer
        
        System.out.print("Ingresa tu nombre para continuar: ");
        String nombreJugador = scanner.nextLine(); // Lee el nombre del jugador
        
        // Verifica si el jugador está registrado
        if (!jugadores.containsKey(nombreJugador)) {
            System.out.println("Jugador no registrado. Primero debes registrarte.");
            return;  // Sale del método si el jugador no está registrado
        }
        
        Jugador jugador = jugadores.get(nombreJugador); // Obtiene el objeto Jugador
        
        // Verifica si el jugador tiene suficientes créditos para jugar
        if (jugador.getCreditos() < COSTO_JUEGO) {
            System.out.println("¡No tienes suficientes créditos para jugar! Tienes " + jugador.getCreditos() + " créditos.");
            return;  // Sale del método si no tiene suficientes créditos
        }
        
        // Si tiene créditos, se descuentan los créditos para participar en el juego
        jugador.reducirCreditos(COSTO_JUEGO);
        System.out.println("¡Bienvenido al juego!");
        
        // Llama al método correspondiente dependiendo del juego seleccionado
        if (opcionJuego == 1) {
            jugarCuadradoMagico();
        } else if (opcionJuego == 2) {
            jugarConecta4();
        } else if (opcionJuego == 3) {
            jugarSalvado();
        } else if (opcionJuego == 4) { 
            jugarTorresDeHanoi();
        } else {
            System.out.println("Opción de juego no válida.");
        }
    }
// Juego Cuadrado Mágico
/**
 * Inicia el juego "Cuadrado Mágico". El jugador debe completar el tablero
 * de manera que la suma de cada fila, cada columna y ambas diagonales sea
 * igual a la "suma mágica".
 */
private static void jugarCuadradoMagico() {
    int puntuacionJugador = 0;  // Inicializa la puntuación del jugador
    Scanner scanner = new Scanner(System.in);
    
    // Inicializa el tablero y lo muestra
    int[][] tablero = inicializarTablero();
    mostrarTablero(tablero);
    
    System.out.println("Completa el tablero para que sea un cuadrado mágico.");
    int numerosUsados = 0; // Cuenta cuántos números han sido colocados en el tablero
    
    // Bucle que permite al jugador colocar números hasta completar el tablero
    while (numerosUsados < 16) {
        System.out.print("Ingresa la fila (0-3): ");
        int fila = scanner.nextInt(); // Lee la fila donde el jugador quiere colocar un número

        System.out.print("Ingresa la columna (0-3): ");
        int columna = scanner.nextInt(); // Lee la columna donde el jugador quiere colocar un número

        // Verifica si la celda ya tiene un número o está ocupada
        if (tablero[fila][columna] != 0) {
            System.out.println("Esta celda ya tiene un valor fijo o ya has colocado un número aquí.");
            continue;  // Si la celda está ocupada, pide al jugador que intente de nuevo
        }

        System.out.print("Ingresa el número que deseas colocar (1-16): ");
        int numero = scanner.nextInt(); // Lee el número que el jugador desea colocar

        // Verifica si el número ingresado es válido (entre 1 y 16 y no repetido)
        if (numero < 1 || numero > 16 || contiene(tablero, numero)) {
            System.out.println("Número inválido o ya utilizado. Intenta de nuevo.");
            continue;  // Si el número es inválido, pide al jugador que intente de nuevo
        }

        // Coloca el número en el tablero y aumenta el contador de números usados
        tablero[fila][columna] = numero;
        numerosUsados++;
        
        // Muestra el tablero actualizado
        mostrarTablero(tablero);

        // Verifica si es posible completar el cuadrado mágico
        if (!esPosibleCuadradoMagico(tablero)) {
            System.out.println("No es posible completar el cuadrado mágico. ¡Has perdido!");
            return;  // Termina el juego si no es posible completar el cuadrado mágico
        }
    }

    // Si el tablero es un cuadrado mágico, se muestra un mensaje de felicitación
    if (esCuadradoMagico(tablero)) {
        System.out.println("¡Felicitaciones! Has completado el cuadrado mágico.");
        puntuacionJugador += 10; // Suma puntos al jugador por completar el cuadrado mágico
    } else {
        System.out.println("El tablero no es un cuadrado mágico. Mejor suerte la próxima vez.");
    }

    // Muestra la puntuación final del jugador en este juego
    System.out.println("Tu puntuación en este juego es: " + puntuacionJugador);
}

/**
 * Verifica si el tablero cumple con las condiciones de un cuadrado mágico.
 * Un cuadrado mágico es una matriz en la que la suma de cada fila, cada columna
 * y ambas diagonales es igual a un valor constante conocido como la "suma mágica".
 * 
 * @param tablero El tablero del juego.
 * @return `true` si el tablero es un cuadrado mágico, `false` en caso contrario.
 */
private static boolean esCuadradoMagico(int[][] tablero) {
    int sumaMagica = N * (N * N + 1) / 2;  // La suma mágica para un cuadrado de tamaño N x N

    // Verificar las filas
    for (int i = 0; i < N; i++) {
        int sumaFila = 0;
        for (int j = 0; j < N; j++) {
            sumaFila += tablero[i][j];
        }
        if (sumaFila != sumaMagica) {
            return false;  // Si alguna fila no tiene la suma mágica, no es un cuadrado mágico
        }
    }

    // Verificar las columnas
    for (int j = 0; j < N; j++) {
        int sumaColumna = 0;
        for (int i = 0; i < N; i++) {
            sumaColumna += tablero[i][j];
        }
        if (sumaColumna != sumaMagica) {
            return false;  // Si alguna columna no tiene la suma mágica, no es un cuadrado mágico
        }
    }

    // Verificar las diagonales
    int sumaDiagonal1 = 0;
    int sumaDiagonal2 = 0;
    for (int i = 0; i < N; i++) {
        sumaDiagonal1 += tablero[i][i];  // Diagonal principal
        sumaDiagonal2 += tablero[i][N - 1 - i];  // Diagonal secundaria
    }
    if (sumaDiagonal1 != sumaMagica || sumaDiagonal2 != sumaMagica) {
        return false;  // Si alguna de las diagonales no tiene la suma mágica, no es un cuadrado mágico
    }

    return true;  // Si todas las sumas son iguales a la suma mágica, es un cuadrado mágico
}

/**
 * Inicializa el tablero del juego Cuadrado Mágico con algunos valores fijos.
 * Este método asigna los primeros valores de manera predeterminada para facilitar
 * el inicio del juego.
 * 
 * @return Un tablero 4x4 con algunos valores fijos.
 */
private static int[][] inicializarTablero() {
    int[][] tablero = new int[N][N];
    // Inicializa algunas celdas del tablero con valores predeterminados
    tablero[0][0] = 1;
    tablero[1][1] = 6;
    tablero[2][2] = 11;
    tablero[3][3] = 16;
    return tablero;  // Devuelve el tablero inicializado
}

/**
 * Muestra el tablero de juego en la consola.
 * Las celdas vacías se muestran como un guion bajo ("_"), mientras que los números
 * colocados por el jugador se muestran en su valor respectivo.
 * 
 * @param tablero El tablero a mostrar.
 */
private static void mostrarTablero(int[][] tablero) {
    for (int[] fila : tablero) {
        for (int celda : fila) {
            System.out.print((celda == 0 ? "_" : celda) + "\t");  // Muestra "_" para celdas vacías
        }
        System.out.println();  // Salto de línea después de cada fila
    }
}

/**
 * Verifica si un número ya ha sido colocado en el tablero.
 * 
 * @param tablero El tablero en el que se busca el número.
 * @param numero El número a buscar en el tablero.
 * @return `true` si el número ya está en el tablero, `false` en caso contrario.
 */
private static boolean contiene(int[][] tablero, int numero) {
    for (int[] fila : tablero) {
        for (int celda : fila) {
            if (celda == numero) {
                return true;  // Si el número ya está en el tablero, retorna `true`
            }
        }
    }
    return false;  // Si no se encuentra el número, retorna `false`
}

/**
 * Verifica si es posible completar el cuadrado mágico con los números actuales.
 * Realiza una evaluación preliminar de las sumas de filas, columnas y diagonales
 * para determinar si es posible seguir completando el cuadrado mágico.
 * 
 * @param tablero El tablero de juego.
 * @return `true` si es posible completar el cuadrado mágico, `false` en caso contrario.
 */
private static boolean esPosibleCuadradoMagico(int[][] tablero) {
    int sumaMagica = N * (N * N + 1) / 2;  // La suma mágica para un cuadrado de tamaño N x N
    int[] filas = new int[N];
    int[] columnas = new int[N];
    int diagonal1 = 0, diagonal2 = 0;

    // Suma los valores de filas, columnas y diagonales
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            filas[i] += tablero[i][j];
            columnas[j] += tablero[i][j];
            if (i == j) diagonal1 += tablero[i][j];
            if (i + j == N - 1) diagonal2 += tablero[i][j];
        }
    }

    // Verifica si alguna fila o columna excede la suma mágica
    for (int i = 0; i < N; i++) {
        if (filas[i] > sumaMagica || columnas[i] > sumaMagica) {
            return false;  // Si alguna fila o columna es mayor que la suma mágica, no es posible
        }
    }

    // Verifica si las diagonales también cumplen con la suma mágica
    return diagonal1 <= sumaMagica && diagonal2 <= sumaMagica;
}
// Juego Conecta 4
/**
 * Inicia el juego "Conecta 4". Los jugadores se alternan para colocar sus fichas
 * en las columnas de un tablero de 6x7. El objetivo es alinear 4 fichas del mismo
 * jugador en fila, columna o diagonal.
 */
private static void jugarConecta4() {
    int[][] tablero = new int[6][7];  // 6 filas y 7 columnas
    int jugadorActual = 1;  // 1 para el jugador 1 (X), 2 para el jugador 2 (O)
    boolean juegoTerminado = false;  // Estado del juego

    // Bucle principal del juego que se repite hasta que termine
    while (!juegoTerminado) {
        // Mostrar el tablero actual
        mostrarTableroConecta4(tablero);

        // Solicitar al jugador que seleccione una columna
        System.out.print("Jugador " + jugadorActual + ", selecciona una columna (0-6): ");
        int columna = scanner.nextInt();

        // Verificar si la columna es válida (dentro del rango y no llena)
        if (columna < 0 || columna >= 7 || tablero[0][columna] != 0) {
            System.out.println("Columna inválida o llena. Intenta de nuevo.");
            continue;
        }

        // Obtener la fila disponible en la columna seleccionada
        int fila = obtenerFilaDisponible(tablero, columna);
        tablero[fila][columna] = jugadorActual;  // Colocar la ficha en el tablero

        // Verificar si el jugador actual ha ganado
        if (verificarVictoria(tablero, fila, columna, jugadorActual)) {
            mostrarTableroConecta4(tablero);
            System.out.println("¡Jugador " + jugadorActual + " gana!");  // Mensaje de victoria
            juegoTerminado = true;  // Termina el juego
        } else {
            // Cambiar de jugador
            jugadorActual = (jugadorActual == 1) ? 2 : 1;
        }
    }
}

/**
 * Muestra el tablero del juego Conecta 4 en la consola.
 * Utiliza "X" para el jugador 1, "O" para el jugador 2 y "." para las celdas vacías.
 * 
 * @param tablero El tablero de juego a mostrar.
 */
private static void mostrarTableroConecta4(int[][] tablero) {
    for (int i = 0; i < 6; i++) {  // Recorrer las filas del tablero
        for (int j = 0; j < 7; j++) {  // Recorrer las columnas
            // Mostrar "X" para el jugador 1, "O" para el jugador 2, y "." para las celdas vacías
            System.out.print((tablero[i][j] == 0 ? "." : (tablero[i][j] == 1 ? "X" : "O")) + " ");
        }
        System.out.println();  // Salto de línea al final de cada fila
    }
}

/**
 * Obtiene la fila disponible en una columna específica.
 * La fila es la que está más cerca del fondo del tablero (fila 5 es la más baja).
 * 
 * @param tablero El tablero de juego.
 * @param columna La columna donde el jugador desea colocar su ficha.
 * @return El número de la fila donde la ficha será colocada.
 */
private static int obtenerFilaDisponible(int[][] tablero, int columna) {
    for (int i = 5; i >= 0; i--) {  // Comienza desde la fila más baja (5) y sube
        if (tablero[i][columna] == 0) {  // Si la celda está vacía
            return i;  // Retorna la fila disponible
        }
    }
    return -1;  // Columna llena (aunque no debe ocurrir debido a las validaciones previas)
}

/**
 * Verifica si un jugador ha ganado. Un jugador gana si tiene 4 fichas alineadas
 * en alguna de las direcciones: horizontal, vertical o diagonal.
 * 
 * @param tablero El tablero de juego.
 * @param fila La fila donde se ha colocado la última ficha.
 * @param columna La columna donde se ha colocado la última ficha.
 * @param jugador El jugador actual (1 o 2).
 * @return `true` si el jugador ha ganado, `false` en caso contrario.
 */
private static boolean verificarVictoria(int[][] tablero, int fila, int columna, int jugador) {
    // Comprobar en las 4 direcciones: horizontal, vertical, diagonal 1 y diagonal 2
    return verificarDireccion(tablero, fila, columna, jugador, 1, 0) ||  // Horizontal
           verificarDireccion(tablero, fila, columna, jugador, 0, 1) ||  // Vertical
           verificarDireccion(tablero, fila, columna, jugador, 1, 1) ||  // Diagonal principal
           verificarDireccion(tablero, fila, columna, jugador, 1, -1);   // Diagonal inversa
}

/**
 * Verifica si hay una secuencia de 4 fichas consecutivas en una dirección específica.
 * 
 * @param tablero El tablero de juego.
 * @param fila La fila de la última ficha colocada.
 * @param columna La columna de la última ficha colocada.
 * @param jugador El jugador actual (1 o 2).
 * @param dirX Dirección en el eje X (horizontal o diagonal).
 * @param dirY Dirección en el eje Y (vertical o diagonal).
 * @return `true` si se ha encontrado una secuencia de 4 fichas consecutivas en la dirección especificada.
 */
private static boolean verificarDireccion(int[][] tablero, int fila, int columna, int jugador, int dirX, int dirY) {
    int count = 1;  // Contador de fichas consecutivas (comienza con la última ficha colocada)

    // Comprobar en una dirección (dirX, dirY)
    for (int i = 1; i < 4; i++) {  // Verificar hasta 3 posiciones en la dirección indicada
        int x = fila + i * dirX;
        int y = columna + i * dirY;

        // Verificar si la posición está fuera de los límites o si no tiene la ficha del jugador
        if (x < 0 || x >= 6 || y < 0 || y >= 7 || tablero[x][y] != jugador) {
            break;  // Si no es válida, salir del bucle
        }
        count++;  // Incrementar el contador
    }

    // Comprobar en la dirección opuesta (-dirX, -dirY)
    for (int i = 1; i < 4; i++) {  // Verificar hasta 3 posiciones en la dirección opuesta
        int x = fila - i * dirX;
        int y = columna - i * dirY;

        // Verificar si la posición está fuera de los límites o si no tiene la ficha del jugador
        if (x < 0 || x >= 6 || y < 0 || y >= 7 || tablero[x][y] != jugador) {
            break;  // Si no es válida, salir del bucle
        }
        count++;  // Incrementar el contador
    }

    return count >= 4;  // Si el contador es 4 o más, el jugador ha ganado
}

/**
 * Inicia el juego de "Salvado". En este juego, el jugador debe adivinar cuál es la última silla
 * que quedará ocupada en un círculo de 100 personas. A medida que se eliminan personas,
 * el jugador debe adivinar correctamente la última persona que queda.
 */
private static void jugarSalvado() {
    System.out.println("Bienvenido al juego de Salvado: ¡adivina cuál es la última silla ocupada!");

    // Configuración inicial
    Random rand = new Random();
    int n = 100;  // Total de personas
    int[] personas = new int[n];  // Arreglo para simular las personas en el círculo
    for (int i = 0; i < n; i++) {
        personas[i] = i + 1;  // Personas numeradas del 1 al 100
    }

    // Elegir un número aleatorio i entre 1 y 100 (cantidad de pasos a avanzar)
    int i = rand.nextInt(100) + 1;  // La cantidad de pasos a avanzar
    System.out.println("Número de pasos a avanzar para eliminar: " + i);

    // Pedir al jugador que adivine la última silla ocupada
    System.out.print("Adivina la última silla ocupada (entre 1 y 100): ");
    Scanner scanner = new Scanner(System.in);
    int respuestaJugador = scanner.nextInt();

    // Eliminar personas hasta que quede una
    int indiceEliminado = 0;  // Comenzamos desde la primera persona
    List<Integer> listaPersonas = new ArrayList<>();
    for (int j = 0; j < n; j++) {
        listaPersonas.add(personas[j]);  // Llenar la lista con las personas
    }

    // Proceso de eliminación de personas
    while (listaPersonas.size() > 1) {
        // Calcular el índice de la persona a eliminar
        indiceEliminado = (indiceEliminado + i - 1) % listaPersonas.size();
        listaPersonas.remove(indiceEliminado);  // Eliminar la persona en el índice calculado
    }

    // La última persona es la que se salva
    int ultimaSilla = listaPersonas.get(0);

    // Verificar si la respuesta del jugador es correcta
    if (respuestaJugador == ultimaSilla) {
        System.out.println("¡Correcto! La silla que se salva es la número " + ultimaSilla + ".");
    } else {
        System.out.println("La silla que se salva es la número " + ultimaSilla + ".");
    }
}
/**
 * Inicia el juego de Torres de Hanoi. En este juego, el objetivo es mover todos los discos
 * de la torre A (izquierda) a la torre C (derecha) siguiendo unas reglas:
 * 1. Solo se puede mover un disco a la vez.
 * 2. Un disco más grande no puede colocarse sobre un disco más pequeño.
 * 3. El número mínimo de movimientos es 63 si se hacen las jugadas correctas.
 */
private static void jugarTorresDeHanoi() {
    System.out.println("Bienvenido al juego de Torres de Hanoi.");
    System.out.println("El objetivo es mover los discos de la torre A (izquierda) a la torre C (derecha).");
    System.out.println("Reglas:");
    System.out.println("- Puedes mover solo un disco a la vez.");
    System.out.println("- Un disco más grande no puede colocarse sobre uno más pequeño.");
    System.out.println("- El número mínimo de movimientos es 63 si haces las jugadas correctas.");
    
    System.out.print("Ingresa tu nombre: ");
    String nombreJugador = scanner.nextLine();

    // Verificar si el jugador está registrado
    if (!jugadores.containsKey(nombreJugador)) {
        System.out.println("Jugador no registrado. Primero debes registrarte.");
        return;
    }

    Jugador jugador = jugadores.get(nombreJugador);
    int movimientosRealizados = 0;

    // Inicializamos las tres torres
    Stack<Integer> torreA = new Stack<>();
    Stack<Integer> torreB = new Stack<>();
    Stack<Integer> torreC = new Stack<>();

    // Colocamos los discos en la torre A (de mayor a menor)
    for (int i = 6; i > 0; i--) {
        torreA.push(i);
    }

    // Jugamos hasta que el juego se complete
    while (torreC.size() < 6) {
        mostrarEstado(torreA, torreB, torreC);

        // El jugador hace un movimiento
        moverDisco(torreA, torreB, torreC);
        movimientosRealizados++;

        // Verificamos si el juego ha terminado
        if (torreC.size() == 6) {
            System.out.println("¡Felicidades, has completado el juego de Torres de Hanoi!");
            break;
        }
    }

    // Determinamos la puntuación
    int puntos = calcularPuntos(movimientosRealizados);
    jugador.agregarPuntos(puntos);
    System.out.println("¡Juego completado! Tu puntuación es: " + puntos);
}

/**
 * Solicita al jugador un movimiento, pidiendo elegir las torres de origen y destino.
 * Verifica que el movimiento sea válido antes de realizarlo.
 * 
 * @param torreA La torre A (izquierda).
 * @param torreB La torre B (centro).
 * @param torreC La torre C (derecha).
 */
private static void moverDisco(Stack<Integer> torreA, Stack<Integer> torreB, Stack<Integer> torreC) {
    String origen, destino;

    // Pedimos al jugador que seleccione un movimiento
    System.out.print("Selecciona el poste de origen (A, B, C): ");
    origen = scanner.nextLine().toUpperCase();
    System.out.print("Selecciona el poste de destino (A, B, C): ");
    destino = scanner.nextLine().toUpperCase();

    // Determinamos las pilas correspondientes
    Stack<Integer> torreOrigen = obtenerTorre(origen, torreA, torreB, torreC);
    Stack<Integer> torreDestino = obtenerTorre(destino, torreA, torreB, torreC);

    // Validamos que el movimiento sea posible
    if (torreOrigen.isEmpty()) {
        System.out.println("La torre de origen está vacía. Elige otra torre.");
    } else if (!torreDestino.isEmpty() && torreDestino.peek() < torreOrigen.peek()) {
        System.out.println("No puedes colocar un disco más grande sobre uno más pequeño.");
    } else {
        // Realizamos el movimiento
        torreDestino.push(torreOrigen.pop());
        System.out.println("Movimiento exitoso: Disco movido de " + origen + " a " + destino);
    }
}

/**
 * Obtiene la torre correspondiente a un nombre (A, B o C).
 * 
 * @param nombreTorre El nombre de la torre (A, B o C).
 * @param torreA La torre A.
 * @param torreB La torre B.
 * @param torreC La torre C.
 * @return La torre correspondiente.
 */
private static Stack<Integer> obtenerTorre(String nombreTorre, Stack<Integer> torreA, Stack<Integer> torreB, Stack<Integer> torreC) {
    switch (nombreTorre) {
        case "A": return torreA;
        case "B": return torreB;
        case "C": return torreC;
        default: return null;
    }
}

/**
 * Muestra el estado actual de las tres torres en la consola.
 * 
 * @param torreA La torre A.
 * @param torreB La torre B.
 * @param torreC La torre C.
 */
private static void mostrarEstado(Stack<Integer> torreA, Stack<Integer> torreB, Stack<Integer> torreC) {
    System.out.println("Estado actual:");
    System.out.println("Torre A: " + torreA);
    System.out.println("Torre B: " + torreB);
    System.out.println("Torre C: " + torreC);
    System.out.println();
}

/**
 * Calcula la puntuación del jugador según el número de movimientos realizados.
 * 
 * @param movimientosRealizados El número de movimientos realizados por el jugador.
 * @return La puntuación del jugador.
 */
private static int calcularPuntos(int movimientosRealizados) {
    if (movimientosRealizados == 63) {
        return 10;  // Puntuación perfecta
    } else if (movimientosRealizados <= 73) {
        return 5;   // Puntuación decente
    } else {
        return 2;   // Puntuación mínima
    }
}
/**
 * Clase que representa a un jugador en el juego. Un jugador tiene un nombre, un puntaje
 * y una cantidad de créditos. Los créditos pueden ser reducidos y los puntos se suman
 * a medida que el jugador participa en los juegos.
 */
static class Jugador {
    private String nombre;  // Nombre del jugador
    private int puntos;     // Puntos acumulados del jugador
    private int creditos;   // Créditos del jugador

    /**
     * Constructor de la clase Jugador.
     * 
     * @param nombre El nombre del jugador.
     * @param creditos Los créditos iniciales del jugador.
     */
    public Jugador(String nombre, int creditos) {
        this.nombre = nombre;
        this.creditos = creditos;
        this.puntos = 0;  // Inicialmente el jugador tiene 0 puntos
    }

    // Getter para obtener el nombre del jugador
    public String getNombre() {
        return nombre;
    }

    // Getter para obtener los créditos del jugador
    public int getCreditos() {
        return creditos;
    }

    /**
     * Reduce los créditos del jugador en la cantidad especificada.
     * 
     * @param cantidad La cantidad de créditos a reducir.
     */
    public void reducirCreditos(int cantidad) {
        this.creditos -= cantidad;
    }

    /**
     * Agrega los puntos al puntaje del jugador.
     * 
     * @param puntos La cantidad de puntos a agregar.
     */
    public void agregarPuntos(int puntos) {
        this.puntos += puntos;
    }

    // Getter para obtener los puntos acumulados por el jugador
    public int getPuntos() {
        return puntos;
    }
}
/**
 * Muestra los tres jugadores con más créditos en orden descendente.
 */
private static void verMejoresJugadores() {
    // Se obtiene la lista de jugadores y se ordena por los créditos en orden descendente
    List<Jugador> listaJugadores = new ArrayList<>(jugadores.values());
    listaJugadores.sort(Comparator.comparingInt(Jugador::getCreditos).reversed());

    System.out.println("\nLos 3 mejores jugadores son:");
    for (int i = 0; i < 3 && i < listaJugadores.size(); i++) {
        Jugador jugador = listaJugadores.get(i);
        System.out.println((i + 1) + ". " + jugador.getNombre() + " - " + jugador.getCreditos() + " créditos");
    }
}

/**
 * Guarda los datos de los jugadores en un archivo utilizando serialización.
 * Guarda el mapa de jugadores en un archivo llamado `ARCHIVO_JUGADORES`.
 */
private static void guardarYSalir() {
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ARCHIVO_JUGADORES))) {
        out.writeObject(jugadores);  // Serializa el mapa de jugadores
        System.out.println("Datos guardados correctamente.");
    } catch (IOException e) {
        System.out.println("Error al guardar los datos.");
    }
}
/**
 * Carga los datos de los jugadores desde un archivo utilizando deserialización.
 * Si no se puede cargar el archivo, muestra un mensaje de error.
 */
@SuppressWarnings("unchecked")
private static void cargarDatos() {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ARCHIVO_JUGADORES))) {
        jugadores = (Map<String, Jugador>) in.readObject();  // Deserializa el mapa de jugadores
    } catch (IOException | ClassNotFoundException e) {
        System.out.println("No se pudo cargar los datos.");
    }
}

}

