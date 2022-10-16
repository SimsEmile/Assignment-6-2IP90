import java.util.Random;
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
class Patch {
    private boolean coop;
    private int row;
    private int col;
    private double score;

    Patch(int x, int y, double score, boolean coop) {
        this.row = x;
        this.col = y;
        this.score = score;
        this.coop = coop;
    }

    void setScore(double score) {
        this.score = score;
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

    boolean getCoop() {
        return this.coop;
    }
}