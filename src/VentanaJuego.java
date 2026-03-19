import javax.swing.*;
import java.awt.*;

public class VentanaJuego extends JFrame {
    private Simulador sim = new Simulador();
    private JButton btnAccion;

    public VentanaJuego() {
        setTitle("The Dice Game 1 - Simulación de Producción FIM");
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

        // Botones en Español
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

        JButton btnReset = new JButton("REINICIAR");
        btnReset.addActionListener(e -> {
            sim.reiniciarSimulacion();
            btnAccion.setText("TIRAR DADOS (ROLL)");
            btnAccion.setEnabled(true);
            repaint();
        });

        JPanel pnlBotones = new JPanel();
        pnlBotones.add(btnAccion);
        pnlBotones.add(btnReset);
        add(canvas);
        add(pnlBotones, BorderLayout.SOUTH);
    }

    private void dibujarTodo(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(235, 235, 225));
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Posiciones para 10 estaciones (E1 a E10)
        int[][] pos = {
                {50,60}, {230,60}, {410,60}, {590,60}, {770,60},
                {770,300}, {590,300}, {410,300}, {230,300}, {50,300}
        };

        for (int i = 0; i < sim.getEstaciones().length; i++) {
            Estacion e = sim.getEstaciones()[i];
            int x = pos[i][0], y = pos[i][1];

            dibujarDado(g2, x, y, e.getUltimoDado());

            Cola c = e.getCola();
            for (int j = 0; j < c.getTamanoActual(); j++) {
                // Gris = Estaban (Viejas), Azul = Recién llegadas (Nuevas)
                g2.setColor(c.getDatoEn(j) == 1 ? Color.GRAY : new Color(0, 150, 255));
                g2.fillOval(x + 50 + (j%5)*13, y + (j/5)*13, 11, 11);
            }

            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.drawString("E" + (i + 1), x, y - 10);
        }

        // RESERVA DE ENTRADA
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString("PRODUCTO TOTAL INTRODUCIDO AL SISTEMA:", 50, 550);
        g2.setColor(Color.DARK_GRAY);
        // Dibujamos el total de entrada como bolitas en el suelo
        for(int i = 0; i < (sim.getTurno() * 4); i++) {
            g2.fillOval(50 + (i%50)*12, 570 + (i/50)*12, 9, 9);
        }

        // BARRA LATERAL GUINDA
        g2.setColor(new Color(74, 16, 16));
        g2.fillRect(1000, 0, 300, getHeight());

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawString("Rondas", 1050, 400);
        g2.setFont(new Font("Arial", Font.BOLD, 50));
        g2.drawString("" + sim.getTurno(), 1080, 460);

        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("Producción Final: " + sim.getTerminados(), 1020, 100);
        g2.drawString("En Proceso (WIP): " + ( (sim.getTurno()*4) - sim.getTerminados() ), 1020, 140);
    }

    private void dibujarDado(Graphics2D g2, int x, int y, int v) {
        g2.setColor(Color.RED);
        g2.fillRoundRect(x, y, 40, 40, 8, 8);
        g2.setColor(Color.WHITE);
        if (v == 0) return;
        if (v==1 || v==3 || v==5) g2.fillOval(x+16, y+16, 8, 8);
        if (v>1) { g2.fillOval(x+6, y+6, 8, 8); g2.fillOval(x+26, y+26, 8, 8); }
        if (v>3) { g2.fillOval(x+26, y+6, 8, 8); g2.fillOval(x+6, y+26, 8, 8); }
        if (v==6) { g2.fillOval(x+6, y+16, 8, 8); g2.fillOval(x+26, y+16, 8, 8); }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaJuego().setVisible(true));
    }
}