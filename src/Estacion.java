public class Estacion {

    private Cola cola;
    private int ultimoDado = 0;
    private int ultimoMovido = 0;

    public Estacion() {
        this.cola = new Cola(500);
    }

    public void reiniciar(int piezasIniciales) {
        this.cola = new Cola(500);
        this.ultimoDado = 0;
        this.ultimoMovido = 0; // Reiniciar contador

        for (int i = 0; i < piezasIniciales; i++) {
            cola.insertarDato(1); // Gris
        }
    }

    // Getters y Setters
    public void setUltimoDado(int d) { this.ultimoDado = d; }
    public int getUltimoDado() { return ultimoDado; }
    public void setUltimoMovido(int m) { this.ultimoMovido = m; }
    public int getUltimoMovido() { return ultimoMovido; }
    public Cola getCola() { return cola; }
}