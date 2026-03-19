public class Estacion {
    private String nombre;
    private Cola cola;
    private int ultimoDado = 0;

    public Estacion(String nombre) {
        this.nombre = nombre;
        this.cola = new Cola(100);
        reiniciarEstacion();
    }

    public void reiniciarEstacion() {
        this.cola = new Cola(100);
        this.ultimoDado = 0;
        // Inventario inicial de la práctica
        for(int i=0; i<4; i++) cola.insertarDato(1);
    }

    public void setUltimoDado(int d) { this.ultimoDado = d; }
    public int getUltimoDado() { return ultimoDado; }

    public int calcularSalida(int dado) {
        int inicial = cola.getTamanoActual();
        int procesables = 0;
        for(int i = 0; i < inicial; i++) {
            if(cola.getDatoEn(i) == 1) procesables++;
        }
        return Math.min(dado, procesables);
    }

    public void mover(int cant) { for(int i=0; i<cant; i++) cola.eliminarDato(); }
    public void recibir(int cant) { for(int i=0; i<cant; i++) cola.insertarDato(2); }
    public void turnoTerminado() { cola.envejecer(); }
    public Cola getCola() { return cola; }
    public String getNombre() { return nombre; }
}