import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
/**
 * Patch.
 * 
 * INCOMPLETE 
 * Assignment 6 -- Prisoner's Dilemma -- 2ip90
 * part Patch
 * assignment copyright Kees Huizing
 * 
 * @author Alexandru Dicu
 * @id 1837370
 * @author Jules Anseaume
 * @id 1806769
 */

class Patch extends JButton {
    private boolean coop;
    private double score;
    private boolean nextStrategy;

    Patch(double score) {
        this.score = score;
        this.nextStrategy = coop;
        this.setBorder(new LineBorder(Color.black, 0));
        this.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toggleStrategy();
            }
        });
    }

    void setScore(double score) {
        this.score = score;
    }

    void setNextStrategy(boolean nextStrategy) {
        this.nextStrategy = nextStrategy;
    }

    boolean getNextStrategy() {
        return this.nextStrategy;
    }

    /**
     * Determine if this patch is cooperating.
     * 
     * @return true if and only if the patch is cooperating.
     */
    boolean isCooperating() {
        return this.coop;
    }

    
    /**
     * Set strategy to cooperate (C) when isC is true, D otherwise.
     * 
     * @param isC use cooperation strategy.
     */
    void setCooperating(boolean isC) {
        this.coop = isC;
        this.setBackground(isC ? Color.blue : Color.red);
    }
    
    
    /**
     * Toggle strategy between C and D.
     */
    void toggleStrategy() {
        this.setCooperating(!this.coop);
    }
    
    /**
     * Score of this patch in the current round.
     * 
     * @return the score
     */
    double getScore() {
        return this.score;
    }
}