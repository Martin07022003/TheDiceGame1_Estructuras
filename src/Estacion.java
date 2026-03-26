public class Estacion {
    private Cola cola;
    private int ultimoDado = 0;
    private int ultimoMovido = 0;

    public Estacion() {
        this.cola = new Cola(200);
    }

    public void reiniciar(boolean conPiezas) {
        this.cola = new Cola(200);
        this.ultimoDado = 0;
        this.ultimoMovido = 0; // Contador de movimiento
        if (conPiezas) {
            for(int i = 0; i < 4; i++) {
                cola.insertarDato(1); // Gris
            }
        }
    }

    public int calcularSalida(int dado) {
        return Math.min(dado, cola.getTamanoActual());
    }

    // Getters y Setters
    public void setUltimoDado(int d) { this.ultimoDado = d; }
    public int getUltimoDado() { return ultimoDado; }
    public void setUltimoMovido(int m) { this.ultimoMovido = m; }
    public int getUltimoMovido() { return ultimoMovido; }

    public Cola getCola() { return cola; }
}