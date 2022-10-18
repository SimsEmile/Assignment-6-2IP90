import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

/**
 * Patch.
 * 
 * Assignment 6 -- Prisoner's Dilemma -- 2ip90
 * part Patch
 * assignment copyright Kees Huizing
 * 
 * @author Mihnea-Theodor Visoiu
 * @ID 1791346
 * @author Vlad Petru Barbulescu
 * @ID 1850288
 */

class Patch extends JButton {
    private boolean oldCooperating;
    private boolean cooperating;
    private boolean cooperatingWhenScoreCalculated;
    private double score = 0.0d;
    private Patch[] neighbors = new Patch[8];

    /**
     * Initializes the patch by setting its size, border, and the action listener for toggling the
     * patch's strategy upon pressing being pressed.
     */
    public Patch() {
        this.setSize(this.getPreferredSize().height, this.getPreferredSize().height);
        this.setBorder(new LineBorder(Color.WHITE, 0));
        this.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toggleStrategy();
            }
        });
    }

    /**
     * Determine if this patch is cooperating.
     * 
     * @return true if and only if the patch is cooperating.
     */
    boolean isCooperating() {
        return cooperating;
    }
    
    /**
     * Set strategy to cooperate (C) when isC is true, D otherwise.
     * 
     * @param isC use cooperation strategy.
     */
    void setCooperating(boolean isC) {
        oldCooperating = cooperating;
        cooperating = isC;
        updateStyle();
    }
    
    /**
     * Brings oldCooperating and cooperating on the same value.
     * Mosly used when initializing a patch as oldCooperating isn't yet set to
     * the same value as cooperating.
     */
    void forceSyncOldCooperating() {
        //Doing it "twice" (1st when initializing). Thus we also update the UI.
        setCooperating(isCooperating()); 
    }

    /**
     * Toggle strategy between C and D.
     */
    void toggleStrategy() {
        setCooperating(!isCooperating());
    }
    
    /**
     * Score of this patch in the current round.
     * 
     * @return the score
     */
    double getScore() {
        return score; 
    }

    /**
     * Calculates this patch's score, given an alpha.
     * 
     * @param alpha The alpha to be used for the calculation.
     */
    void calculateScore(double alpha) {
        cooperatingWhenScoreCalculated = isCooperating();

        int cooperatingNeighbors = 0;

        for (int i = 0; i < neighbors.length; i++) {
            if (neighbors[i].isCooperating()) {
                cooperatingNeighbors++;
            }
        }

        if (cooperating) {
            score = cooperatingNeighbors;
        } else {
            score = cooperatingNeighbors * alpha;
        }

        //this.setText(Long.toString(Math.round(score)));
    }

    /**
     * Returns the strategy when the score was calculated. 
     * Helps prevent a "cascade" of strategy changes when updating the strategy for the entire
     * field.
     * 
     * @return The strategy when the score was calculated.
     */
    public boolean getStrategyWhenScoreWasCalculated() {
        return cooperatingWhenScoreCalculated;
    }

    /**
     * Updates how the patch is displayed, given the current (and past) strategy.
     */
    void updateStyle() {
        if (oldCooperating == cooperating) { //Strategy remained the same between rounds
            if (cooperating) {
                this.setBackground(Color.BLUE);
            } else {
                this.setBackground(Color.RED);
            }
        } else { //Strategy has changed between rounds
            if (cooperating) { //Indicates the new strategy
                this.setBackground(new Color(51, 153, 255)); //Light blue
            } else {
                this.setBackground(Color.ORANGE);
            }
        }
    }

    /**
     * Method which sets this patch's neighbors.
     * 
     * @param neighbors The neighbors
     */
    public void setNeighbors(Patch[] neighbors) {
        this.neighbors = neighbors;
    }

    /**
     * Method which returns only the subset of the neighbors + the patch itself which scored
     * the highest.
     * 
     * @return List of highest scoring neighbors.
     */
    public ArrayList<Patch> getHighestScoringNeighbors() {
        ArrayList<Patch> results = new ArrayList<Patch>();
        results.add(this);

        for (int i = 0; i < neighbors.length; i++) {
            if (results.get(0).getScore() < neighbors[i].getScore()) {
                results.clear(); //Reset the list if there is a new maximum score
                results.add(neighbors[i]);
            } else if (results.get(0).getScore() == neighbors[i].getScore()) {
                results.add(neighbors[i]);
            }
        }

        return results;
    }
}