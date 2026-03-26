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

        // Botones para elegir estacion en grafica de actividad
        JPanel panelControles = new JPanel();
        if (tipo == 1) {
            panelControles.add(new JLabel("Estacion: "));
            for (int i = 0; i < 10; i++) {
                int index = i;
                JButton b = new JButton("" + (index + 1));
                b.addActionListener(e -> { playerSel = index; repaint(); });
                panelControles.add(b);
            }
        }
        JButton btnSalir = new JButton(">");
        btnSalir.addActionListener(e -> dispose());
        panelControles.add(btnSalir);

        add(new PanelDibujo(), BorderLayout.CENTER);
        add(panelControles, BorderLayout.SOUTH);
    }

    // Clase para el dibujo de barras y escalas
    class PanelDibujo extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            int hBase = getHeight() - 80;

            // Tope de graficas
            int maxVal = (tipo == 1) ? 10 : (tipo == 2) ? 70 : 70;
            int salto = (tipo == 1) ? 1 : 5;

            // Dibujar lineas y numeros de escala
            for (int i = 0; i <= maxVal; i += salto) {
                int yPos = hBase - (tipo == 1 ? i * 35 : tipo == 2 ? i * 5 : i * 5);
                g2.setColor(new Color(230, 230, 230));
                g2.drawLine(60, yPos, 800, yPos);
                g2.setColor(Color.BLACK);
                g2.drawString("" + i, 30, yPos + 5);
            }

            g2.setColor(Color.BLACK);
            g2.drawLine(60, hBase, 800, hBase);

            // Barras por turno
            int inicioTurno = (tipo == 3) ? 0 : 1;
            for (int t = inicioTurno; t <= 20; t++) {
                int x = 60 + (t * 35);
                g2.setColor(Color.BLACK);
                g2.drawString("" + t, x + 5, hBase + 20);

                if (t <= sim.getTurno()) {
                    if (tipo == 1 && t > 0) {
                        int d = sim.historialDados[t][playerSel];
                        int m = sim.historialMovido[t][playerSel];
                        g2.setColor(Color.PINK); g2.fillRect(x, hBase - (d * 35), 12, d * 35);
                        g2.setColor(Color.BLUE); g2.fillRect(x + 12, hBase - (m * 35), 12, m * 35);
                    } else if (tipo == 2 && t > 0) {
                        int val = sim.historialThroughput[t];
                        g2.setColor(new Color(0, 150, 255)); g2.fillRect(x, hBase - (val * 5), 20, val * 5);
                    } else if (tipo == 3) {
                        int val = sim.historialEnSistema[t];
                        g2.setColor(new Color(100, 150, 255)); g2.fillRect(x, hBase - (val * 5), 20, val * 5);
                    }
                }
            }
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.setColor(Color.BLACK);
            if(tipo == 1) g2.drawString("Estacion " + (playerSel+1) + " (Rosa: Dado, Azul: Movido)", 60, 30);
            else if(tipo == 2) g2.drawString("Throughput", 60, 30);
            else g2.drawString("Number in system", 60, 30);
        }
    }
}