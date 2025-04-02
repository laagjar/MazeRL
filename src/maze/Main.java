package maze;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Main {
    static int SIZE = 10;
    static int CELL_SIZE = 20;
    static int GOAL_REWARD = 100;
    static int STEP_REWARD = -1;
    static int WALL_PENALTY = -10;
    static double LEARNING_RATE = 0.8;
    static double DISCOUNT_FACTOR = 0.9;
    static double epsilon = 1.0;
    static double EPSILON_DECAY = 0.99;
    static double MIN_EPSILON = 0.1;
    static int EPISODES = 1000;

    static int successfulAttempts = 0;
    static int[][] maze;
    static int[][] visitCounts;
    static double[][][] qTable;
    static Random random = new Random();

    static MazePanel mazePanel;
    static JFrame frame;
    static JTextField infoField;
    static boolean running = false;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StartWindow());
    }

    static int[][] generateMaze(int size) {
        int[][] newMaze = new int[size][size];
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                newMaze[y][x] = random.nextDouble() < 0.2 ? 1 : 0;
            }
        }

        newMaze[0][0] = 0;
        newMaze[size - 1][size - 1] = 2;

        return newMaze;
    }

    static void createAndShowGUI() {
        frame = new JFrame("Maze RL Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(SIZE * CELL_SIZE, SIZE * CELL_SIZE + 56);

        mazePanel = new MazePanel();
        frame.add(mazePanel);

        infoField = new JTextField("Episode: 0, Epsilon: " + epsilon);
        infoField.setEditable(false);
        frame.add(infoField, BorderLayout.SOUTH);

        frame.setVisible(true);

        new Thread(() -> trainAgent()).start();
    }

    static void trainAgent() {
        running = true;
        for (int episode = 0; episode < EPISODES && running; episode++) {
            runEpisode();

            epsilon = Math.max(MIN_EPSILON, epsilon * EPSILON_DECAY);
        }
    }

    static void runEpisode() {
        int[] agentPosition = {0, 0};
        long startTime = System.currentTimeMillis();

        while (maze[agentPosition[1]][agentPosition[0]] != 2 && running) {
            int action = selectAction(agentPosition[0], agentPosition[1]);
            int[] nextState = move(agentPosition[0], agentPosition[1], action);
            int nextX = nextState[0], nextY = nextState[1];
            int reward = getReward(nextX, nextY);
            double maxNextQ = getMaxQ(nextX, nextY);

            qTable[agentPosition[1]][agentPosition[0]][action] = qTable[agentPosition[1]][agentPosition[0]][action] + LEARNING_RATE * (reward + DISCOUNT_FACTOR * maxNextQ - qTable[agentPosition[1]][agentPosition[0]][action]);

            agentPosition[0] = nextX;
            agentPosition[1] = nextY;

            visitCounts[agentPosition[1]][agentPosition[0]]++;

            SwingUtilities.invokeLater(() -> mazePanel.updateAgentPosition(agentPosition[0], agentPosition[1]));

            if (reward == GOAL_REWARD) {
                successfulAttempts++;
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                SwingUtilities.invokeLater(() -> infoField.setText(
                        "Episode: " + successfulAttempts
                                + ", Time: " + String.format("%.2f", duration / 1000.0)
                                + " sec, Epsilon: " + String.format("%.2f", epsilon)
                ));
                break;
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static int selectAction(int x, int y) {
        if (random.nextDouble() < epsilon) {
            return random.nextInt(4);
        } else {
            return getBestAction(x, y);
        }
    }

    static int[] move(int x, int y, int action) {
        int nextX = x, nextY = y;

        switch (action) {
            case 0 -> nextY--;
            case 1 -> nextY++;
            case 2 -> nextX--;
            case 3 -> nextX++;
        }

        if (nextX < 0 || nextY < 0 || nextX >= SIZE || nextY >= SIZE || maze[nextY][nextX] == 1) {
            return new int[]{x, y};
        }

        return new int[]{nextX, nextY};
    }

    static int getReward(int x, int y) {
        if (maze[y][x] == 2) return GOAL_REWARD;
        if (maze[y][x] == 1) return WALL_PENALTY;
        return STEP_REWARD;
    }

    static double getMaxQ(int x, int y) {
        double maxQ = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < 4; i++) {
            maxQ = Math.max(maxQ, qTable[y][x][i]);
        }
        return maxQ;
    }

    static int getBestAction(int x, int y) {
        int bestAction = 0;
        double maxQ = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < 4; i++) {
            if (qTable[y][x][i] > maxQ) {
                maxQ = qTable[y][x][i];
                bestAction = i;
            }
        }
        return bestAction;
    }

    static int getMaxVisits() {
        int maxV = 0;
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                if (visitCounts[y][x] > maxV) {
                    maxV = visitCounts[y][x];
                }
            }
        }
        return maxV;
    }

}
