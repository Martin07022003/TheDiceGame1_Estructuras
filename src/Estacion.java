public class Estacion {
    private Cola cola;
    private int ultimoDado = 0;

    public Estacion() {
        this.cola = new Cola(200);
        reiniciar();
    }

    public void reiniciar() {
        this.cola = new Cola(200);
        this.ultimoDado = 0;
        // 4 piezas iniciales grises
        for(int i = 0; i < 4; i++) {
            cola.insertarDato(1);
        }
    }

    public int calcularSalida(int dado) {
        return Math.min(dado, cola.getTamanoActual());
    }

    public void setUltimoDado(int d) { this.ultimoDado = d; }
    public int getUltimoDado() { return ultimoDado; }
    public Cola getCola() { return cola; }
}