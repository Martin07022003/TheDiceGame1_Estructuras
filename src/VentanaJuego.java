import javax.swing.*;
import java.awt.*;

public class VentanaJuego extends JFrame {

    private Simulador sim = new Simulador();

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

        canvas.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                if (x > 1020 && x < 1250) {
                    if (y > 200 && y < 240) new VentanaGrafica(VentanaJuego.this, "Activity", 1, sim).setVisible(true);
                    if (y > 250 && y < 290) new VentanaGrafica(VentanaJuego.this, "Throughput", 2, sim).setVisible(true);
                    if (y > 300 && y < 340) new VentanaGrafica(VentanaJuego.this, "Number in system", 3, sim).setVisible(true);

                    if (y > 650 && y < 710 && sim.getTurno() < 20) {
                        if (!sim.esEsperandoMove()) sim.ejecutarRoll();
                        else sim.ejecutarMove();
                        repaint();
                    }

                    if (y > 720 && y < 760) {
                        sim.reiniciarSimulacion();
                        repaint();
                    }
                }
            }
        });

        add(canvas);
    }

    private void dibujarTodo(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(235, 235, 225));
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Coordenadas en U
        int[][] pos = {
                {120, 110}, {300, 110}, {480, 110}, {660, 110},
                {820, 230}, {820, 430},
                {660, 600}, {480, 600}, {300, 600}, {120, 600}
        };

        for (int i = 0; i < 10; i++) {
            Estacion e = sim.getEstaciones()[i];
            int x = pos[i][0], y = pos[i][1];

            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            g2.drawString("E" + (i + 1), x, y - 10);

            dibujarDado(g2, x, y, e.getUltimoDado());

            Cola c = e.getCola();
            dibujarCola(g2, c, i, x, y);
        }

        // Panel Guinda
        g2.setColor(new Color(74, 16, 16));
        g2.fillRect(1000, 0, 300, getHeight());

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 14));

        g2.drawString("Total del juego: " + sim.getTotalJuego(), 1020, 70);
        g2.drawString("Total ultima estacion (E10): " + sim.getTotalEnE10(), 1020, 100);

        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.drawString("Turno: " + sim.getTurno(), 1080, 600);

        String[] nombres = {"Activity", "Throughput", "Number in system"};

        for (int i = 0; i < 3; i++) {
            g2.setColor(new Color(200, 190, 180));
            g2.fillRoundRect(1020, 200 + (i * 50), 230, 40, 5, 5);
            g2.setColor(Color.BLACK);
            g2.drawString(nombres[i], 1040, 225 + (i * 50));
        }

        g2.setColor(new Color(200, 190, 180));
        g2.fillRoundRect(1020, 650, 230, 60, 5, 5);
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 25));
        g2.drawString(sim.esEsperandoMove() ? "Move" : "Roll", 1100, 690);

        g2.setColor(new Color(200, 190, 180));
        g2.fillRoundRect(1020, 720, 230, 40, 5, 5);
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.drawString("REINICIAR", 1100, 745);
    }

    private void dibujarCola(Graphics2D g2, Cola c, int estacion, int x, int y) {
        for (int j = 0; j < c.getTamanoActual(); j++) {
            g2.setColor(c.getDatoEn(j) == 1 ? Color.GRAY : new Color(0, 150, 255));

            int dx = 0;
            int dy = 0;

            int a = j % 4;
            int b = j / 4;

            switch (estacion) {
                case 0: // E1
                case 1: // E2
                case 2: // E3
                case 3: // E4
                    dx = 50 + a * 13;
                    dy = b * 13;
                    break;

                case 4: // E5
                case 5: // E6
                    dx = 50;
                    dy = 5 + a * 13 + b * 52;
                    break;

                case 6: // E7
                case 7: // E8
                case 8: // E9
                    dx = -15 - a * 13 - b * 52;
                    dy = 15;
                    break;

                case 9: // E10
                    dx = -60 + (a * 13);
                    dy = 28 - (b * 13);
                    break;
            }

            g2.fillOval(x + dx, y + dy, 11, 11);
        }
    }

    private void dibujarDado(Graphics2D g2, int x, int y, int v) {
        g2.setColor(new Color(200, 0, 0));
        g2.fillRoundRect(x, y, 40, 40, 8, 8);

        g2.setColor(Color.WHITE);

        if (v == 0) return;
        if (v % 2 != 0) g2.fillOval(x + 16, y + 16, 8, 8);
        if (v > 1) {
            g2.fillOval(x + 6, y + 6, 8, 8);
            g2.fillOval(x + 26, y + 26, 8, 8);
        }
        if (v > 3) {
            g2.fillOval(x + 26, y + 6, 8, 8);
            g2.fillOval(x + 6, y + 26, 8, 8);
        }
        if (v == 6) {
            g2.fillOval(x + 6, y + 16, 8, 8);
            g2.fillOval(x + 26, y + 16, 8, 8);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaJuego().setVisible(true));
    }
}