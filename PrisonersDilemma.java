import javax.swing.SwingUtilities;
import javax.swing.*;
import java.awt.*;

/**
 * Prisoners Dilemma application.
 * 
 * INCOMPLETE
 * 
 * assignment copyright Kees Huizing
 * 
 * @author NAME
 * @id ID
 * @author NAME
 * @id ID
 */
class PrisonersDilemma extends PlayingField {
    // ...

    /**
     * Build the GUI for the Prisoner's Dilemma application.
     */
    void buildGUI() {
        SwingUtilities.invokeLater(() -> {
        });
        JFrame frame = new JFrame("");
            frame.setSize(800, 600);
            JLabel underLabel = new JLabel("<html><h1>Go?</h1></html>");
            underLabel.setBackground(new Color(255,255,255));
            underLabel.setOpaque(true);
            frame.add(underLabel,BorderLayout.SOUTH);
            JPanel panel = new JPanel(new GridLayout(50, 50));
            frame.add(panel);
            
            


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        }

    // ...

    public static void main(String[] a) {
        new PrisonersDilemma().buildGUI();
    }
}