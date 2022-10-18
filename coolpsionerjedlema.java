import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Prisoners Dilemma application.
 * 
 * assignment copyright Kees Huizing
 * 
 * @author Mihnea-Theodor Visoiu
 * @ID 1791346
 * @author Vlad Petru Barbulescu
 * @ID 1850288
 */

class PrisonersDilemma {
    PlayingField field;

    /**
     * Build the GUI for the Prisoner's Dilemma application.
     */
    void buildGUI() {
        //Initialize the window
        JFrame frame = new JFrame("Prisoner's Dilemma");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 800);
        
        field = new PlayingField();
        frame.add(field);

        //Define and configure the Go and Pause button
        JButton goPauseButton = new JButton("Go");
        goPauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                field.togglePlaybackSimulation();
                goPauseButton.setText(field.isSimulationRunning() ? "Pause" : "Go");
            }
        });
        
        //Define and configure the Reset button
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                field.randomizeGrid();
            }
        });

        //Define and configure the Alpha slider + labels stating the min, current and max values
        JPanel alphaSliderPanel = new JPanel();
        
        JLabel alphaSliderText = new JLabel(String.format("Current alpha: %.2f",
            field.getAlpha()));
        alphaSliderPanel.add(alphaSliderText);

        JSlider alphaSlider = new JSlider(JSlider.HORIZONTAL,
            (int) PlayingField.MIN_ALPHA * 100, 
            (int) PlayingField.MAX_ALPHA * 100,
            (int) PlayingField.MIN_ALPHA * 100);
        alphaSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                field.setAlpha(alphaSlider.getValue() / 100d);
                alphaSliderText.setText(String.format("Current alpha: %.2f", 
                    field.getAlpha()));
            }
        });
        Hashtable<Integer, JLabel> alphaSliderLabels = new Hashtable<Integer, JLabel>();
        alphaSliderLabels.put((int) PlayingField.MIN_ALPHA * 100,
            new JLabel(Double.toString(PlayingField.MIN_ALPHA)));
        alphaSliderLabels.put((int) PlayingField.MAX_ALPHA * 100,
            new JLabel(Double.toString(PlayingField.MAX_ALPHA)));
        alphaSlider.setLabelTable(alphaSliderLabels);
        alphaSlider.setPaintLabels(true);
        alphaSliderPanel.add(alphaSlider);

        //Define and configure the animation speed slider + labels stating the min, 
        //current and max values
        JPanel animationSpeedSliderPanel = new JPanel();
        
        JLabel animationSpeedSliderText = new JLabel("Current animation speed: " 
            + field.getPlaybackSpeed() + "ms");
        animationSpeedSliderPanel.add(animationSpeedSliderText);

        JSlider animationSpeedSlider = new JSlider(JSlider.HORIZONTAL,
            PlayingField.MIN_ANIMATION_SPEED, 
            PlayingField.MAX_ANIMATION_SPEED,
            field.getPlaybackSpeed());
        animationSpeedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                field.setPlaybackSpeed(animationSpeedSlider.getValue());
                animationSpeedSliderText.setText("Current animation speed: " 
                    + field.getPlaybackSpeed() + "ms");
            }
        });
        Hashtable<Integer, JLabel> animationSpeedSliderLabels = new Hashtable<Integer, JLabel>();
        animationSpeedSliderLabels.put(PlayingField.MIN_ANIMATION_SPEED,
            new JLabel(PlayingField.MIN_ANIMATION_SPEED + "ms"));
        animationSpeedSliderLabels.put(PlayingField.MAX_ANIMATION_SPEED,
            new JLabel(PlayingField.MAX_ANIMATION_SPEED + "ms"));
        animationSpeedSlider.setLabelTable(animationSpeedSliderLabels);;
        animationSpeedSlider.setPaintLabels(true);
        animationSpeedSliderPanel.add(animationSpeedSlider);
    
        //Define and configure the alternative update rule checkbox
        JCheckBox alternativeUpdateRuleCheckBox = new JCheckBox("Use alternative update rule");
        alternativeUpdateRuleCheckBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                field.setUpdateRule(alternativeUpdateRuleCheckBox.isSelected());
            }
        });
        
        //Place all the buttons, sliders and labels
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(1, 0));
        buttonsPanel.add(goPauseButton);
        buttonsPanel.add(alternativeUpdateRuleCheckBox);
        buttonsPanel.add(resetButton);

        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new GridLayout(0, 1));

        controlsPanel.add(alphaSliderPanel);
        controlsPanel.add(animationSpeedSliderPanel);
        controlsPanel.add(buttonsPanel);
        frame.add(controlsPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    public static void main(String[] a) {
        new PrisonersDilemma().buildGUI();
    }
}