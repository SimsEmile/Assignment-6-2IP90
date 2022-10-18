import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//...

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
class PrisonersDilemma/* possible extends... */ {
    // ...
    /**
     * Build the GUI for the Prisoner's Dilemma application.
     */

    void buildGUI() {
        SwingUtilities.invokeLater(() -> {
            // ...
        });
        JFrame frame = new JFrame("Prisoners Dillema");
        
        PlayingField playingField = new PlayingField();
        playingField.fillInitialGrid();
        playingField.randomizeGrid();

        JPanel panel = new JPanel();
        //headerLabel.setBackground(Color.blue);
        //panel.setBackground(Color.blue);
        JButton startOrPauseButton = new JButton("Go");
        startOrPauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playingField.toggleSimulation();
                startOrPauseButton.setText(playingField.isRunning() ? "Pause" : "Go");
                //playingField.print();
            }
        });
        
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playingField.randomizeGrid();
            }
        });
        
       
        JLabel alphaValue = new JLabel(String.format("alpha: %.2f", playingField.getAlpha()));
        JSlider alphaSlider = new JSlider(JSlider.HORIZONTAL, 0, 3000, 0);
        alphaSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                playingField.setAlpha(alphaSlider.getValue() / 1000d);
                alphaValue.setText(String.format("alpha: %.2f", playingField.getAlpha()));
            }
        });

        JLabel speed = new JLabel(String.format("speed: %d", playingField.getSpeed()));
        JSlider timeSlider = new JSlider(JSlider.HORIZONTAL, 10, 10000, 1000);

        timeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                playingField.setSpeed(timeSlider.getValue());
                speed.setText(String.format("speed: %d", playingField.getSpeed()));
            }
        });
        playingField.setSpeed(1000);
        //playingField.print();
        
        JLabel headerLabel = new JLabel("<html><h1>Prisoner's Dillema</h1></html>",
            SwingConstants.CENTER);

        panel.setOpaque(true);
        panel.add(startOrPauseButton);
        panel.add(resetButton);
        panel.add(alphaValue);
        panel.add(alphaSlider);
        panel.add(speed);
        panel.add(timeSlider);
        frame.setSize(800, 800);
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