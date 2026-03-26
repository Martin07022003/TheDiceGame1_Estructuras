import java.util.ArrayList;
import java.util.Random;

public class Simulador {
    private Estacion[] estaciones;
    private int turno = 0;
    private int materiaPrimaTotal = 36;
    private ArrayList<Integer> listaSalida = new ArrayList<>();
    private Random rnd = new Random();
    private boolean esperandoMove = false;

    // Listas para almacenar datos de las graficas
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
        turno = 0; materiaPrimaTotal = 36; esperandoMove = false;
        listaSalida.clear();
        for (int i = 0; i < 10; i++) {
            estaciones[i].reiniciar(i < 9);
        }
        for(int i=0; i<21; i++) {
            historialThroughput[i] = 0;
            historialEnSistema[i] = 0;
            for(int j=0; j<10; j++) {
                historialDados[i][j] = 0;
                historialMovido[i][j] = 0;
            }
        }
        historialEnSistema[0] = 36; // Inventario inicial
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

    // Piezas entre estaciones
    public void ejecutarMove() {
        turno++;
        int[] cantAMover = new int[10];
        int[][] datosAMover = new int[10][6];

        for (int i = 0; i < 10; i++) {
            cantAMover[i] = estaciones[i].calcularSalida(estaciones[i].getUltimoDado());
            estaciones[i].setUltimoMovido(cantAMover[i]);
            historialMovido[turno][i] = cantAMover[i];
            for (int j = 0; j < cantAMover[i]; j++) {
                datosAMover[i][j] = estaciones[i].getCola().getDatoEn(0);
                estaciones[i].getCola().eliminarDato();
            }
        }

        for (int i = 0; i < 10; i++) {
            if (i == 9) {
                for (int j = 0; j < cantAMover[i]; j++) listaSalida.add(datosAMover[i][j]);
            } else {
                for (int j = 0; j < cantAMover[i]; j++) {
                    estaciones[i + 1].getCola().insertarDato(datosAMover[i][j]);
                }
            }
        }

        // Ingreso de bolitas segun dado de E1
        int dadoE1 = estaciones[0].getUltimoDado();
        for (int i = 0; i < dadoE1; i++) {
            estaciones[0].getCola().insertarDato(2);
            materiaPrimaTotal++;
        }

        historialThroughput[turno] = listaSalida.size();
        int sumaSistema = 0;
        for (Estacion e : estaciones) sumaSistema += e.getCola().getTamanoActual();
        historialEnSistema[turno] = sumaSistema;
        esperandoMove = false;
    }

    public int getTotalIntroducido() { return listaSalida.size(); }
    public int getTipoPiezaSalida(int index) { return listaSalida.get(index); }
    public Estacion[] getEstaciones() { return estaciones; }
    public int getTurno() { return turno; }
    public int getMateriaPrima() { return materiaPrimaTotal; }
    public boolean esEsperandoMove() { return esperandoMove; }
}