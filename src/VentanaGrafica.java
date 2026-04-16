import javax.swing.*;
import java.awt.*;

public class VentanaGrafica extends JDialog {

    private int tipo;
    private Simulador sim;
    private int playerSel = 0;

    public VentanaGrafica(JFrame padre, String titulo, int tipo, Simulador sim) {
        super(padre, titulo, true);
        this.tipo = tipo;
        this.sim = sim;

        setSize(850, 550);
        setLocationRelativeTo(padre);

        JPanel panelControles = new JPanel();

        if (tipo == 1) {
            panelControles.add(new JLabel("Estacion: "));
            for (int i = 0; i < 10; i++) {
                int index = i;
                JButton b = new JButton("E" + (index + 1));
                b.addActionListener(e -> {
                    playerSel = index;
                    repaint();
                });
                panelControles.add(b);
            }
        }

        JButton btnSalir = new JButton("Cerrar");
        btnSalir.addActionListener(e -> dispose());
        panelControles.add(btnSalir);

        add(new PanelDibujo(), BorderLayout.CENTER);
        add(panelControles, BorderLayout.SOUTH);
    }

    class PanelDibujo extends JPanel {

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            int hBase = getHeight() - 80;

            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.setColor(Color.BLACK);

            if (tipo == 1) {
                g2.drawString("Activity E" + (playerSel + 1) + " - Rosa: Dado / Azul: Movido", 60, 30);
            } else if (tipo == 2) {
                g2.drawString("Throughput", 60, 30);
            } else {
                g2.drawString("Number in system", 60, 30);
            }

            int maxVal = (tipo == 1) ? 8 : 70;
            int salto = (tipo == 1) ? 1 : 10;

            int escalaY = (tipo == 1) ? 40 : 5;

            for (int i = 0; i <= maxVal; i += salto) {
                int yPos = hBase - (i * escalaY);

                g2.setColor(new Color(230, 230, 230));
                g2.drawLine(60, yPos, 800, yPos);

                g2.setColor(Color.BLACK);
                g2.setFont(new Font("Arial", Font.PLAIN, 10));
                g2.drawString("" + i, 35, yPos + 5);
            }

            for (int t = 0; t <= 20; t++) {
                int x = 60 + (t * 35);

                g2.setColor(Color.BLACK);
                g2.setFont(new Font("Arial", Font.PLAIN, 10));
                g2.drawString("" + t, x + 5, hBase + 20);
            }

            for (int t = 0; t <= sim.getTurno(); t++) {
                int x = 60 + (t * 35);

                if (tipo == 1) {
                    if (t > 0) {
                        int d = sim.historialDados[t][playerSel];
                        int m = sim.historialMovido[t][playerSel];

                        // Barra del dado
                        g2.setColor(Color.PINK);
                        g2.fillRect(x, hBase - (d * escalaY), 12, d * escalaY);

                        // Barra de movido
                        g2.setColor(Color.BLUE);
                        g2.fillRect(x + 12, hBase - (m * escalaY), 12, m * escalaY);
                    }

                } else if (tipo == 2) {
                    // Throughput acumulado
                    if (t > 0) {
                        int val = sim.historialThroughput[t];
                        g2.setColor(new Color(0, 150, 255));
                        g2.fillRect(x, hBase - (val * escalaY), 20, val * escalaY);
                    }

                } else if (tipo == 3) {
                    // Number in system
                    int val = sim.historialEnSistema[t];
                    g2.setColor(new Color(100, 150, 255));
                    g2.fillRect(x, hBase - (val * escalaY), 20, val * escalaY);
                }
            }

            g2.setColor(Color.BLACK);
            g2.drawLine(60, hBase, 800, hBase);
            g2.drawLine(60, 50, 60, hBase);
        }
    }
}