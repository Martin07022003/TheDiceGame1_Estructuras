import java.util.Random;

public class Simulador {
    private Estacion[] estaciones;
    private int turno = 0, terminados = 0, totalEntrada = 0;
    private Random rnd = new Random();
    private int estado = 0;

    public Simulador() {
        estaciones = new Estacion[10];
        for(int i = 0; i < 10; i++) estaciones[i] = new Estacion("E" + (i + 1));
        reiniciarSimulacion();
    }

    public void reiniciarSimulacion() {
        turno = 0; terminados = 0; totalEntrada = 0; estado = 0;
        for(Estacion e : estaciones) e.reiniciarEstacion();
    }

    public void ejecutarRoll() {
        if (turno >= 20) return;
        for (Estacion e : estaciones) e.turnoTerminado();
        for (Estacion e : estaciones) e.setUltimoDado(rnd.nextInt(6) + 1);
        estado = 1;
    }

    public void ejecutarMove() {
        turno++;
        // 1. Guardamos cuánto va a meter la E1 según su dado ANTES de mover
        int entradaTurnoActual = estaciones[0].getUltimoDado();

        // 2. Movimiento de E10 a E2
        for (int i = estaciones.length - 1; i >= 1; i--) {
            int dado = estaciones[i].getUltimoDado();
            int salen = estaciones[i].calcularSalida(dado);
            estaciones[i].mover(salen);
            if (i == estaciones.length - 1) terminados += salen;
            else estaciones[i+1].recibir(salen);
        }

        // 3. Movimiento de la E1 (Mueve lo viejo y RECIBE lo del dado de materia prima)
        int salenE1 = estaciones[0].calcularSalida(estaciones[0].getUltimoDado());
        estaciones[0].mover(salenE1);
        estaciones[1].recibir(salenE1);

        // La E1 recibe unidades NUEVAS (azules) igual a lo que sacó su dado
        estaciones[0].recibir(entradaTurnoActual);
        totalEntrada += entradaTurnoActual;

        estado = 0;
    }

    public int getEstado() { return estado; }
    public Estacion[] getEstaciones() { return estaciones; }
    public int getTurno() { return turno; }
    public int getTerminados() { return terminados; }
    public int getTotalEntrada() { return totalEntrada; }
}