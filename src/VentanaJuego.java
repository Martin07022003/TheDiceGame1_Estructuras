import javax.swing.*;
import java.awt.*;

/**
 * Interfaz Gráfica para la simulación "The Dice Game 1".
 * Implementa la visualización de 10 estaciones, dados y flujo de inventario.
 */
public class VentanaJuego extends JFrame {
    private Simulador sim = new Simulador();
    private JButton btnAccion;

    public VentanaJuego() {
        // Configuración básica de la ventana
        setTitle("The Dice Game 1 - Simulación de Producción FIM UABC");
        setSize(1300, 850);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel de dibujo principal
        JPanel canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarTodo((Graphics2D) g);
            }
        };

        // Botón principal de acción (ROLL / MOVE)
        btnAccion = new JButton("TIRAR DADOS (ROLL)");
        btnAccion.setFont(new Font("Arial", Font.BOLD, 18));
        btnAccion.addActionListener(e -> {
            if (sim.getTurno() < 20) {
                if (sim.getEstado() == 0) {
                    sim.ejecutarRoll();
                    btnAccion.setText("MOVER PIEZAS (MOVE)");
                } else {
                    sim.ejecutarMove();
                    btnAccion.setText("TIRAR DADOS (ROLL)");
                }
                repaint();
            } else {
                btnAccion.setEnabled(false);
                JOptionPane.showMessageDialog(null, "Simulación terminada: 20 rondas completadas.");
            }
        });

        // Botón para reiniciar la práctica
        JButton btnReset = new JButton("REINICIAR");
        btnReset.setFont(new Font("Arial", Font.PLAIN, 14));
        btnReset.addActionListener(e -> {
            sim.reiniciarSimulacion();
            btnAccion.setText("TIRAR DADOS (ROLL)");
            btnAccion.setEnabled(true);
            repaint();
        });

        // Panel inferior para los controles
        JPanel pnlBotones = new JPanel();
        pnlBotones.add(btnAccion);
        pnlBotones.add(btnReset);

        add(canvas, BorderLayout.CENTER);
        add(pnlBotones, BorderLayout.SOUTH);
    }

    private void dibujarTodo(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Color de fondo del tablero
        g2.setColor(new Color(235, 235, 225));
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Coordenadas para las 10 estaciones en forma de "U"
        int[][] pos = {
                {50,60}, {230,60}, {410,60}, {590,60}, {770,60},     // Fila superior (E1-E5)
                {770,300}, {590,300}, {410,300}, {230,300}, {50,300} // Fila inferior (E6-E10)
        };

        for (int i = 0; i < sim.getEstaciones().length; i++) {
            Estacion e = sim.getEstaciones()[i];
            int x = pos[i][0], y = pos[i][1];

            // Dibujar el dado de la estación
            dibujarDado(g2, x, y, e.getUltimoDado());

            // Dibujar las bolitas de la cola (Gris = Viejas, Azul = Nuevas)
            Cola c = e.getCola();
            for (int j = 0; j < c.getTamanoActual(); j++) {
                g2.setColor(c.getDatoEn(j) == 1 ? Color.GRAY : new Color(0, 150, 255));
                g2.fillOval(x + 50 + (j % 5) * 13, y + (j / 5) * 13, 11, 11);
            }

            // Etiqueta de la estación
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.drawString("E" + (i + 1), x, y - 10);
        }

        // --- SECCIÓN DE PRODUCTO INTRODUCIDO (Reserva abajo) ---
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString("PRODUCTO TOTAL INTRODUCIDO AL SISTEMA:", 50, 550);

        int totalPiezas = sim.getTotalEntrada();
        for(int i = 0; i < totalPiezas; i++) {
            // Las piezas recién introducidas en el último MOVE se ven azules si estamos en ROLL
            if (i >= totalPiezas - sim.getEstaciones()[0].getUltimoDado() && sim.getEstado() == 0) {
                g2.setColor(new Color(0, 150, 255));
            } else {
                g2.setColor(Color.GRAY);
            }
            g2.fillOval(50 + (i % 60) * 12, 570 + (i / 60) * 12, 9, 9);
        }

        // --- BARRA LATERAL ESTILO GUINDA ---
        g2.setColor(new Color(74, 16, 16)); // Guinda institucional
        g2.fillRect(1000, 0, 300, getHeight());

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 22));
        g2.drawString("Turns", 1080, 450);
        g2.setFont(new Font("Arial", Font.BOLD, 60));
        g2.drawString("" + sim.getTurno(), 1090, 520);

        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString("Introducido: " + sim.getTotalEntrada(), 1020, 100);
        g2.drawString("Salida Total: " + sim.getTerminados(), 1020, 140);

        // Indicador de estado visual discreto
        g2.setFont(new Font("Arial", Font.ITALIC, 14));
        String txtEstado = (sim.getEstado() == 0) ? "Esperando Roll..." : "Esperando Move...";
        g2.drawString(txtEstado, 1020, 180);
    }

    /**
     * Dibuja un dado rojo con puntos blancos dinámicos.
     */
    private void dibujarDado(Graphics2D g2, int x, int y, int v) {
        g2.setColor(Color.RED);
        g2.fillRoundRect(x, y, 40, 40, 8, 8);
        g2.setColor(Color.WHITE);
        if (v == 0) return; // No dibujar puntos si no se ha tirado

        // Lógica de posición de los puntos (pips)
        if (v == 1 || v == 3 || v == 5) g2.fillOval(x + 16, y + 16, 8, 8); // Punto central
        if (v > 1) {
            g2.fillOval(x + 6, y + 6, 8, 8);   // Top-Left
            g2.fillOval(x + 26, y + 26, 8, 8); // Bottom-Right
        }
        if (v > 3) {
            g2.fillOval(x + 26, y + 6, 8, 8);  // Top-Right
            g2.fillOval(x + 6, y + 26, 8, 8);  // Bottom-Left
        }
        if (v == 6) {
            g2.fillOval(x + 6, y + 16, 8, 8);  // Middle-Left
            g2.fillOval(x + 26, y + 16, 8, 8); // Middle-Right
        }
    }

    public static void main(String[] args) {
        // Hilo de despacho de eventos para la GUI
        SwingUtilities.invokeLater(() -> {
            VentanaJuego frame = new VentanaJuego();
            frame.setVisible(true);
        });
    }
}