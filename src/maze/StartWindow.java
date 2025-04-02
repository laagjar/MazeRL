package maze;

import javax.swing.*;
import java.awt.*;

import static maze.Main.*;

public class StartWindow extends JFrame {
    public StartWindow() {
        setTitle("Maze RL Settings");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 2));

        JTextField sizeField = addField("Labyrinth size", SIZE);
        JTextField cellSizeField = addField("Cell size", CELL_SIZE);
        JTextField goalRewardField = addField("Goal reward", GOAL_REWARD);
        JTextField stepRewardField = addField("Step reward", STEP_REWARD);
        JTextField wallPenaltyField = addField("Wall penalty", WALL_PENALTY);
        JTextField learningRateField = addField("Learning rate", LEARNING_RATE);
        JTextField discountFactorField = addField("Discount factor", DISCOUNT_FACTOR);
        JTextField epsilonField = addField("Epsilon", epsilon);
        JTextField epsilonDecayField = addField("Epsilon decay", EPSILON_DECAY);
        JTextField minEpsilonField = addField("Minimal epsilon", MIN_EPSILON);
        JTextField episodesField = addField("Episodes", EPISODES);

        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("Stop");

        startButton.addActionListener(e -> {
            SIZE = Integer.parseInt(sizeField.getText());
            CELL_SIZE = Integer.parseInt(cellSizeField.getText());
            GOAL_REWARD = Integer.parseInt(goalRewardField.getText());
            STEP_REWARD = Integer.parseInt(stepRewardField.getText());
            WALL_PENALTY = Integer.parseInt(wallPenaltyField.getText());
            LEARNING_RATE = Double.parseDouble(learningRateField.getText());
            DISCOUNT_FACTOR = Double.parseDouble(discountFactorField.getText());
            epsilon = Double.parseDouble(epsilonField.getText());
            EPSILON_DECAY = Double.parseDouble(epsilonDecayField.getText());
            MIN_EPSILON = Double.parseDouble(minEpsilonField.getText());
            EPISODES = Integer.parseInt(episodesField.getText());

            maze = generateMaze(SIZE);
            qTable = new double[SIZE][SIZE][4];
            visitCounts = new int[SIZE][SIZE];
            createAndShowGUI();
        });

        stopButton.addActionListener(e -> {
            running = false;
            if (frame != null) frame.dispose();
        });

        add(startButton);
        add(stopButton);

        pack();
        setVisible(true);
    }

    private JTextField addField(String label, Object value) {
        add(new JLabel(label));
        JTextField field = new JTextField(value.toString());
        add(field);
        return field;
    }
}