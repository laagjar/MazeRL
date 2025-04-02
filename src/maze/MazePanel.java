package maze;

import javax.swing.*;
import java.awt.*;

import static maze.Main.*;

public class MazePanel extends JPanel {
    private int agentX = 0;
    private int agentY = 0;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int maxVisits = getMaxVisits();
        if (maxVisits == 0) maxVisits = 1;

        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                if (maze[y][x] == 1) {
                    g.setColor(Color.BLACK);
                } else if (maze[y][x] == 2) {
                    g.setColor(Color.GREEN);
                } else {
                    float visits = visitCounts[y][x];
                    float norm = visits / maxVisits;

                    if (norm < 0.0f) norm = 0.0f;
                    if (norm > 1.0f) norm = 1.0f;

                    float red, green, blue;

                    if (norm <= 0.5f) {
                        float t = norm / 0.5f;
                        red   = 1.0f;
                        green = t;
                        blue  = 0.0f;
                    }
                    else {
                        float t = (norm - 0.5f) / 0.5f;
                        red   = 1.0f - t;
                        green = 1.0f;
                        blue  = 0.0f;
                    }

                    g.setColor(new Color(red, green, blue));
                }

                g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                g.setColor(Color.GRAY);
                g.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }

        g.setColor(Color.BLUE);
        g.fillOval(
                agentX * CELL_SIZE + CELL_SIZE / 4,
                agentY * CELL_SIZE + CELL_SIZE / 4,
                CELL_SIZE / 2,
                CELL_SIZE / 2
        );
    }

    public void updateAgentPosition(int x, int y) {
        this.agentX = x;
        this.agentY = y;
        repaint();
    }
}