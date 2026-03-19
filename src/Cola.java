public class Cola {
    private int[] arreglo;
    private int inicio, fin, MAX;

    public Cola(int tamano) {
        this.MAX = tamano;
        this.arreglo = new int[MAX];
        this.inicio = -1;
        this.fin = -1;
    }

    public void insertarDato(int dato) {
        if (fin < MAX - 1) {
            fin++;
            arreglo[fin] = dato;
            if (fin == 0) inicio = 0;
        }
    }

    public void eliminarDato() {
        if (inicio != -1) {
            if (inicio == fin) {
                inicio = -1;
                fin = -1;
            } else {
                inicio++;
            }
        }
    }

    public int getTamanoActual() {
        return (inicio == -1) ? 0 : (fin - inicio) + 1;
    }

    public int getDatoEn(int i) {
        return arreglo[inicio + i];
    }
}