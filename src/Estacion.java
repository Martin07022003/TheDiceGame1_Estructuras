public class Estacion {
    private String nombre;
    private Cola cola;
    private int ultimoDado = 0;

    public Estacion(String nombre) {
        this.nombre = nombre;
        this.cola = new Cola(100);
        // Quitamos el for que insertaba datos iniciales
    }

    public void reiniciarEstacion() {
        this.cola = new Cola(100);
        this.ultimoDado = 0;
        // El sistema inicia vacío como el original
    }

    public void setUltimoDado(int dado) { this.ultimoDado = dado; }
    public int getUltimoDado() { return ultimoDado; }

    public int calcularSalida(int dado) {
        int inicial = cola.getTamanoActual();
        int procesables = 0;
        for(int i = 0; i < inicial; i++) {
            // Solo procesamos las que ya eran GRISES (valor 1)
            if(cola.getDatoEn(i) == 1) procesables++;
        }
        return Math.min(dado, procesables);
    }

    public void mover(int cant) {
        for(int i = 0; i < cant; i++) cola.eliminarDato();
    }

    public void recibir(int cant) {
        for(int i = 0; i < cant; i++) cola.insertarDato(2); // 2 = Azul (Nueva)
    }

    public void turnoTerminado() {
        cola.envejecer(); // Aquí las azules pasan a ser grises para el siguiente ROLL
    }

    public Cola getCola() { return cola; }
    public String getNombre() { return nombre; }
}