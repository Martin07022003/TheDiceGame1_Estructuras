import java.util.ArrayList;
import java.util.Random;

public class Simulador {
    private Estacion[] estaciones;
    private int turno = 0;
    private int materiaPrimaTotal = 40;
    private ArrayList<Integer> listaSalida = new ArrayList<>(); // (Salida de azul o gris)
    private Random rnd = new Random();
    private boolean esperandoMove = false;

    public Simulador() {
        estaciones = new Estacion[10];
        for(int i = 0; i < 10; i++) estaciones[i] = new Estacion();
    }

    public void reiniciarSimulacion() {
        turno = 0; materiaPrimaTotal = 40; esperandoMove = false;
        listaSalida.clear();
        for(Estacion e : estaciones) e.reiniciar();
    }

    public void ejecutarRoll() {
        if (turno >= 20) return;
        for (Estacion e : estaciones) e.setUltimoDado(rnd.nextInt(6) + 1);
        esperandoMove = true;
    }

    public void ejecutarMove() {
        turno++;
        int[] cantAMover = new int[10];
        int[][] datosAMover = new int[10][6];

        // Extraer de las estaciones
        for (int i = 0; i < 10; i++) {
            cantAMover[i] = estaciones[i].calcularSalida(estaciones[i].getUltimoDado());
            for (int j = 0; j < cantAMover[i]; j++) {
                datosAMover[i][j] = estaciones[i].getCola().getDatoEn(0);
                estaciones[i].getCola().eliminarDato();
            }
        }

        // Mover a la siguiente o FINAL
        for (int i = 0; i < 10; i++) {
            if (i == 9) {
                for (int j = 0; j < cantAMover[i]; j++) {
                    listaSalida.add(datosAMover[i][j]);
                }
            } else {
                for (int j = 0; j < cantAMover[i]; j++) {
                    estaciones[i+1].getCola().insertarDato(datosAMover[i][j]);
                }
            }
        }

        // E1 recibe piezas nuevas (azules)
        int dadoE1 = estaciones[0].getUltimoDado();
        for (int i = 0; i < dadoE1; i++) {
            estaciones[0].getCola().insertarDato(2);
            materiaPrimaTotal++;
        }
        esperandoMove = false;
    }

    // Getters
    public int getTotalIntroducido() { return listaSalida.size(); }
    public int getTipoPiezaSalida(int index) { return listaSalida.get(index); }
    public Estacion[] getEstaciones() { return estaciones; }
    public int getTurno() { return turno; }
    public int getMateriaPrima() { return materiaPrimaTotal; }
    public boolean esEsperandoMove() { return esperandoMove; }
}