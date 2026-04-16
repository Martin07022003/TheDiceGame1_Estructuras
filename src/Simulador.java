import java.util.Random;

public class Simulador {

    private Estacion[] estaciones;
    private int turno = 0;

    private Random rnd = new Random();
    private boolean esperandoMove = false;

    public int[][] historialDados = new int[21][10];
    public int[][] historialMovido = new int[21][10];
    public int[] historialThroughput = new int[21];
    public int[] historialEnSistema = new int[21];

    public Simulador() {
        estaciones = new Estacion[10];
        for (int i = 0; i < 10; i++) estaciones[i] = new Estacion();
        reiniciarSimulacion();
    }

    public void reiniciarSimulacion() {
        turno = 0;
        esperandoMove = false;

        // 36 piezas: 9 buffers con 4 piezas.
        for (int i = 0; i < 10; i++) {
            if (i < 9) {
                estaciones[i].reiniciar(4);
            } else {
                estaciones[i].reiniciar(0);
            }
        }

        for (int i = 0; i < 21; i++) {
            historialThroughput[i] = 0;
            historialEnSistema[i] = 0;
            for (int j = 0; j < 10; j++) {
                historialDados[i][j] = 0;
                historialMovido[i][j] = 0;
            }
        }

        historialEnSistema[0] = 36;

        historialThroughput[0] = 0;
    }

    public void ejecutarRoll() {
        if (turno >= 20) return;

        for (int i = 0; i < 10; i++) {
            int d = rnd.nextInt(6) + 1;
            estaciones[i].setUltimoDado(d);
            historialDados[turno + 1][i] = d;
        }

        esperandoMove = true;
    }

    public void ejecutarMove() {
        if (!esperandoMove) return;

        turno++;

        int[] cantidadAExtraer = new int[10]; // estaciones 0..9

        // Cada estacion extrae de la estación anterior
        // E10 toma de E9 segun el dado de E10, pero se queda acumulado en E10.
        for (int i = 9; i >= 1; i--) {
            int dadoReceptor = estaciones[i].getUltimoDado();
            int disponibleEnCestaAnterior = estaciones[i - 1].getCola().getTamanoActual();
            cantidadAExtraer[i] = Math.min(dadoReceptor, disponibleEnCestaAnterior);
        }

        // Movimiento entre estaciones
        for (int i = 9; i >= 1; i--) {
            int totalAMover = cantidadAExtraer[i];

            for (int j = 0; j < totalAMover; j++) {
                int pieza = estaciones[i - 1].getCola().getDatoEn(0);
                estaciones[i - 1].getCola().eliminarDato();
                estaciones[i].getCola().insertarDato(pieza);
            }

            historialMovido[turno][i - 1] = totalAMover;
            estaciones[i - 1].setUltimoMovido(totalAMover);
        }

        // Entrada de bolitas nuevas a E1
        int dadoE1 = estaciones[0].getUltimoDado();
        for (int j = 0; j < dadoE1; j++) {
            estaciones[0].getCola().insertarDato(2); // Azul
        }

        estaciones[0].setUltimoMovido(dadoE1);
        historialMovido[turno][0] = dadoE1;

        // Throughput = total acumulado en E10
        historialThroughput[turno] = estaciones[9].getCola().getTamanoActual();

        // Number in system = de E1 a E9
        int suma = 0;
        for (int i = 0; i < 9; i++) {
            suma += estaciones[i].getCola().getTamanoActual();
        }
        historialEnSistema[turno] = suma;

        esperandoMove = false;
    }

    public Estacion[] getEstaciones() {
        return estaciones;
    }

    public int getTurno() {
        return turno;
    }

    public int getTotalJuego() {
        int suma = 0;
        for (Estacion e : estaciones) {
            suma += e.getCola().getTamanoActual();
        }
        return suma;
    }

    public int getTotalEnE10() {
        return estaciones[9].getCola().getTamanoActual();
    }

    public int getNumberInSystemActual() {
        int suma = 0;
        for (int i = 0; i < 9; i++) {
            suma += estaciones[i].getCola().getTamanoActual();
        }
        return suma;
    }

    public boolean esEsperandoMove() {
        return esperandoMove;
    }
}