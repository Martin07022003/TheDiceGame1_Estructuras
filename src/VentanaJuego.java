import javax.swing.*;
import java.awt.*;

public class VentanaJuego extends JFrame {
    private Simulador sim = new Simulador();
    private JButton btnAccion;

    public VentanaJuego() {
        setTitle("The Dice Game 1");
        setSize(1300, 850);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarTodo((Graphics2D) g);
            }
        };

        // Botón ROLL / MOVE
        btnAccion = new JButton("ROLL");
        btnAccion.setFont(new Font("Arial", Font.BOLD, 18));
        btnAccion.addActionListener(e -> {
            if (sim.getTurno() < 20) {
                if (!sim.esEsperandoMove()) {
                    sim.ejecutarRoll();
                    btnAccion.setText("MOVE");
                } else {
                    sim.ejecutarMove();
                    btnAccion.setText("ROLL");
                }
                repaint();
            } else {
                btnAccion.setEnabled(false);
                JOptionPane.showMessageDialog(null, "Simulación terminada: 20 rondas completadas.");
            }
        });

        // Botón reiniciar
        JButton btnReset = new JButton("REINICIAR");
        btnReset.setFont(new Font("Arial", Font.PLAIN, 14));
        btnReset.addActionListener(e -> {
            sim.reiniciarSimulacion();
            btnAccion.setText("ROLL");
            btnAccion.setEnabled(true);
            repaint();
        });

        JPanel pnlBotones = new JPanel();
        pnlBotones.add(btnAccion);
        pnlBotones.add(btnReset);

        add(canvas, BorderLayout.CENTER);
        add(pnlBotones, BorderLayout.SOUTH);
    }

    private void dibujarTodo(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Tablero
        g2.setColor(new Color(235, 235, 225));
        g2.fillRect(0, 0, getWidth(), getHeight());

        // 10 Estaciones
        int[][] pos = {
                {50,60}, {230,60}, {410,60}, {590,60}, {770,60},     // E1-E5
                {770,300}, {590,300}, {410,300}, {230,300}, {50,300} // E6-E10
        };

        // --- DIBUJAR CADA ESTACIÓN ---
        for (int i = 0; i < sim.getEstaciones().length; i++) {
            Estacion e = sim.getEstaciones()[i];
            int x = pos[i][0], y = pos[i][1];

            // Dado con puntos
            dibujarDado(g2, x, y, e.getUltimoDado());

            // Bolitas de la cola (Gris = 1, Azul = 2)
            Cola c = e.getCola();
            for (int j = 0; j < c.getTamanoActual(); j++) {
                int tipo = c.getDatoEn(j);
                g2.setColor(tipo == 1 ? Color.GRAY : new Color(0, 150, 255));
                g2.fillOval(x + 50 + (j % 5) * 13, y + (j / 5) * 13, 11, 11);
            }

            // Nombre de la estación
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.drawString("Estación " + (i + 1), x, y - 10);
        }

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString("TOTAL INTRODUCIDO AL SISTEMA:", 50, 550);

        int totalSalidas = sim.getTotalIntroducido();
        for(int i = 0; i < totalSalidas; i++) {

            int tipo = sim.getTipoPiezaSalida(i);
            g2.setColor(tipo == 1 ? Color.GRAY : new Color(0, 150, 255));
            g2.fillOval(50 + (i % 60) * 12, 570 + (i / 60) * 12, 9, 9);
        }

        // BARRA LATERAL
        g2.setColor(new Color(74, 16, 16));
        g2.fillRect(1000, 0, 300, getHeight());

        g2.setColor(Color.WHITE);
        // Turnos
        g2.setFont(new Font("Arial", Font.BOLD, 22));
        g2.drawString("Turno", 1080, 450);
        g2.setFont(new Font("Arial", Font.BOLD, 60));
        g2.drawString("" + sim.getTurno(), 1090, 520);

        // Estadísticas
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString("Introducido: " + sim.getMateriaPrima(), 1020, 100);
        g2.drawString("Salida Sistema: " + sim.getTotalIntroducido(), 1020, 140);

    }

    private void dibujarDado(Graphics2D g2, int x, int y, int v) {
        g2.setColor(new Color(200, 0, 0)); // Rojo oscuro
        g2.fillRoundRect(x, y, 40, 40, 8, 8);
        g2.setColor(Color.WHITE);
        if (v == 0) return;

        // Puntos del dado
        if (v == 1 || v == 3 || v == 5) g2.fillOval(x + 16, y + 16, 8, 8); // Centro
        if (v > 1) {
            g2.fillOval(x + 6, y + 6, 8, 8);   // TL
            g2.fillOval(x + 26, y + 26, 8, 8); // BR
        }
        if (v > 3) {
            g2.fillOval(x + 26, y + 6, 8, 8);  // TR
            g2.fillOval(x + 6, y + 26, 8, 8);  // BL
        }
        if (v == 6) {
            g2.fillOval(x + 6, y + 16, 8, 8);  // ML
            g2.fillOval(x + 26, y + 16, 8, 8); // MR
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaJuego().setVisible(true);
        });
    }
}