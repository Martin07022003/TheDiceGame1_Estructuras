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

        // Clics en el panel guinda
        canvas.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                int x = e.getX(); int y = e.getY();
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

        add(canvas, BorderLayout.CENTER);
    }

    // Dibujar estaciones, piezas y panel de control
    private void dibujarTodo(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(235, 235, 225));
        g2.fillRect(0, 0, getWidth(), getHeight());

        int[][] pos = { {50,60}, {230,60}, {410,60}, {590,60}, {770,60}, {770,300}, {590,300}, {410,300}, {230,300}, {50,300} };
        for (int i = 0; i < 10; i++) {
            Estacion e = sim.getEstaciones()[i];
            int x = pos[i][0], y = pos[i][1];
            dibujarDado(g2, x, y, e.getUltimoDado());
            Cola c = e.getCola();
            for (int j = 0; j < c.getTamanoActual(); j++) {
                g2.setColor(c.getDatoEn(j) == 1 ? Color.GRAY : new Color(0, 150, 255));
                g2.fillOval(x + 50 + (j % 4) * 13, y + (j / 4) * 13, 11, 11);
            }
        }

        // Bolitas introducidas
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString("TOTAL INTRODUCIDO AL SISTEMA:", 50, 550);
        for(int i = 0; i < sim.getTotalIntroducido(); i++) {
            g2.setColor(sim.getTipoPiezaSalida(i) == 1 ? Color.GRAY : new Color(0, 150, 255));
            g2.fillOval(50 + (i % 60) * 12, 570 + (i / 60) * 12, 9, 9);
        }

        // Panel guinda
        g2.setColor(new Color(74, 16, 16));
        g2.fillRect(1000, 0, 300, getHeight());

        String[] nombres = {"Activity", "Throughput", "Number in system"};
        for(int i=0; i<3; i++) {
            g2.setColor(new Color(200, 190, 180));
            g2.fillRoundRect(1020, 200 + (i*50), 230, 40, 5, 5);
            g2.setColor(Color.BLACK);
            g2.drawString(nombres[i], 1040, 225 + (i*50));
        }

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString("Total: " + sim.getMateriaPrima(), 1020, 100);

        g2.setFont(new Font("Arial", Font.PLAIN, 18));
        g2.drawString("Turno", 1080, 580);
        g2.setFont(new Font("Arial", Font.BOLD, 45));
        g2.drawString("" + sim.getTurno(), 1090, 630);

        // Botones Roll/Move
        g2.setColor(new Color(200, 190, 180));
        g2.fillRoundRect(1020, 650, 230, 60, 5, 5);
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 30));
        g2.drawString(sim.esEsperandoMove() ? "Move" : "Roll", 1090, 690);

        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.setColor(new Color(200, 190, 180));
        g2.fillRoundRect(1020, 720, 230, 30, 5, 5);
        g2.setColor(Color.BLACK);
        g2.drawString("REINICIAR", 1105, 740);
    }

    private void dibujarDado(Graphics2D g2, int x, int y, int v) {
        g2.setColor(new Color(200, 0, 0));
        g2.fillRoundRect(x, y, 40, 40, 8, 8);
        g2.setColor(Color.WHITE);
        if (v == 0) return;
        if (v % 2 != 0) g2.fillOval(x + 16, y + 16, 8, 8);
        if (v > 1) { g2.fillOval(x + 6, y + 6, 8, 8); g2.fillOval(x + 26, y + 26, 8, 8); }
        if (v > 3) { g2.fillOval(x + 26, y + 6, 8, 8); g2.fillOval(x + 6, y + 26, 8, 8); }
        if (v == 6) { g2.fillOval(x + 6, y + 16, 8, 8); g2.fillOval(x + 26, y + 16, 8, 8); }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaJuego().setVisible(true));
    }
}