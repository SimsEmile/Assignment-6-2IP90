import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Prisoners Dilemma application.
 * 
 * INCOMPLETE
 * 
 * assignment copyright Kees Huizing
 * 
 * @author Alexandru Dicu
 * @id 1837370
 * @author Jules Anseaume
 * @id 1806769
 */
class PrisonersDilemma {
    
    private static final int MIN_ALPHA = 0; //minimum value for alpha slider
    private static final int MAX_ALPHA = 300;  //maximum value for alpha slider, times 100
    private static final int DEFAULT_ALPHA = 0; //default value for alpha slider

    private static final int MIN_DISPLAY_TIME = 10; //minimum amount time in miliseconds
    private static final int MAX_DISPLAY_TIME = 5000; //maximum amount time in miliseconds
    private static final int DEFAULT_DISPLAY_TIME = 1000; //default amount of time in miliseconds

    public static final int FRAME_LENGTH = 900; //frame length in pixels
    public static final int FRAME_WIDTH = 900;  //frame width in pixels

    /**
     * Build the GUI for the Prisoner's Dilemma application.
     */
    void buildGUI() {
        SwingUtilities.invokeLater(() -> {
        });
        
        PlayingField playingField = new PlayingField();

        playingField.initializeGrid();
        playingField.randomizeGrid();

        JPanel panel = new JPanel();
        JButton goOrPauseButton = new JButton("Go");

        goOrPauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playingField.toggleSimulation();
                goOrPauseButton.setText(playingField.isRunning() ? "Pause" : "Go");
            }
        });

        JButton resetButton = new JButton("Reset");

        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playingField.randomizeGrid();
            }
        });
        
        JButton updateRule = new JButton("Update rule");

        updateRule.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playingField.setRule(!playingField.getRule());
                updateRule.setText(playingField.getRule() ? "Rule enabled" : "Rule disabled");
            }
        });

        JLabel alphaValue = new JLabel(String.format("alpha: %.2f", playingField.getAlpha()));
        JSlider alphaSlider = new JSlider(JSlider.HORIZONTAL, MIN_ALPHA, MAX_ALPHA, DEFAULT_ALPHA);

        alphaSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                playingField.setAlpha(alphaSlider.getValue() / 100d);
                alphaValue.setText(String.format("alpha: %.2f", playingField.getAlpha()));
            }
        });

        JLabel speed = new JLabel(
            String.format("Display time: %d", playingField.getDisplayTime()) + " ms"
        );
        JSlider timeSlider = new JSlider(
            JSlider.HORIZONTAL, MIN_DISPLAY_TIME, MAX_DISPLAY_TIME, DEFAULT_DISPLAY_TIME
        );

        timeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                playingField.setDisplayTime(timeSlider.getValue());
                speed.setText(
                    String.format("Display time: %d", playingField.getDisplayTime()) + " ms"
                );
            }
        });
        

        JFrame frame = new JFrame("Prisoners Dilemma");
        JLabel headerLabel = new JLabel("<html><h1>Prisoner's Dilemma</h1></html>",
            SwingConstants.CENTER);

        panel.add(goOrPauseButton);
        panel.add(resetButton);
        panel.add(updateRule);
        panel.add(alphaValue);
        panel.add(alphaSlider);
        panel.add(speed);
        panel.add(timeSlider);
        frame.setSize(FRAME_WIDTH, FRAME_LENGTH);
        frame.add(headerLabel, BorderLayout.NORTH);
        frame.add(playingField, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // ...

    public static void main(String[] a) {
        new PrisonersDilemma().buildGUI();
    }
}