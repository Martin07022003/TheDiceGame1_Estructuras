import java.util.Random;

/**
 * Clase Controlador que gestiona la lógica global de la simulación.
 * Coordina el flujo de unidades entre las 10 estaciones.
 */
public class Simulador {
    private Estacion[] estaciones;
    private int turno;
    private int terminados;
    private int totalEntrada;
    private Random rnd;
    private int estado; // 0 = Esperando TIRAR DADOS, 1 = Esperando MOVER PIEZAS

    public Simulador() {
        this.rnd = new Random();
        this.estaciones = new Estacion[10];
        for (int i = 0; i < 10; i++) {
            estaciones[i] = new Estacion("E" + (i + 1));
        }
        reiniciarSimulacion();
    }

    /**
     * Restablece todos los valores a cero para una nueva corrida.
     */
    public void reiniciarSimulacion() {
        this.turno = 0;
        this.terminados = 0;
        this.totalEntrada = 0;
        this.estado = 0;
        for (Estacion e : estaciones) {
            e.reiniciarEstacion();
        }
    }

    /**
     * Paso 1: Genera los valores de los dados para cada estación.
     * Prepara el inventario "envejeciendo" las unidades azules del turno anterior.
     */
    public void ejecutarRoll() {
        if (turno >= 20) return;

        // Antes de tirar dados, las unidades nuevas (azules) se vuelven procesables (grises)
        for (Estacion e : estaciones) {
            e.turnoTerminado();
        }

        // Se lanza el dado para cada uno de los 10 puestos
        for (Estacion e : estaciones) {
            e.setUltimoDado(rnd.nextInt(6) + 1);
        }

        this.estado = 1; // Cambia el estado a MOVER
    }

    /**
     * Paso 2: Mueve las unidades físicamente de una estación a la siguiente.
     * Sigue la lógica FIFO (First In, First Out) usando la estructura de Cola.
     */
    public void ejecutarMove() {
        this.turno++;

        // El movimiento se procesa de la última estación a la primera
        // para asegurar que una unidad solo avance un puesto por turno.
        for (int i = estaciones.length - 1; i >= 0; i--) {
            int dadoActual = estaciones[i].getUltimoDado();
            int unidadesASalir = estaciones[i].calcularSalida(dadoActual);

            // Ejecuta la eliminación física en la Cola de la estación actual
            estaciones[i].mover(unidadesASalir);

            if (i == estaciones.length - 1) {
                // Si es la última estación (E10), las unidades salen del sistema
                this.terminados += unidadesASalir;
            } else {
                // Si no, entran como "Nuevas" (Azules) a la cola de la siguiente estación
                estaciones[i + 1].recibir(unidadesASalir);
            }
        }

        // Regla del juego: Entran 4 unidades nuevas de materia prima a la E1 en cada turno
        estaciones[0].recibir(4);
        this.totalEntrada += 4;

        this.estado = 0; // Regresa el estado a TIRAR DADOS
    }

    // --- MÉTODOS GETTER PARA LA INTERFAZ GRÁFICA ---

    public Estacion[] getEstaciones() {
        return estaciones;
    }

    public int getTurno() {
        return turno;
    }

    public int getTerminados() {
        return terminados;
    }

    public int getTotalEntrada() {
        return totalEntrada;
    }

    public int getEstado() {
        return estado;
    }
}