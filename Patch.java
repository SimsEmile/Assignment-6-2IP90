import java.util.*;

/**
 * Patch.
 * 
 * INCOMPLETE 
 * Assignment 6 -- Prisoner's Dilemma -- 2ip90
 * part Patch
 * assignment copyright Kees Huizing
 * 
 * @author NAME
 * @id ID
 * @author NAME
 * @id ID
 */
class Patch extends PlayingField {
    public double score = 0;
    protected int x;
    protected int y;
    /**
     * Determine if this patch is cooperating.
     * 
     * @return true if and only if the patch is cooperating.
     */
    boolean isCooperating() {
        return true; // CHANGE THIS
    }
    
    /**
     * Set strategy to cooperate (C) when isC is true, D otherwise.
     * 
     * @param isC use cooperation strategy.
     */
    public void setCooperating(boolean isC) {
        isC = RANDOM.nextBoolean();
    }
    
    /**
     * Toggle strategy between C and D.
     */
    public void toggleStrategy() {
        double maxScore = this.score;
        int[][] Neighbours = GetNeighbours();
        for (int i = 0;i<Neighbours.length; i++){
            if (grid[Neighbours[i][0]][Neighbours[i][1]].score > maxScore){

            }
        }

    }
    
    /**
     * Score of this patch in the current round.
     * 
     * @return the score
     */
    double getScore() {
        int[][] Neighbours;
        Neighbours = GetNeighbours();
        if (this.isCooperating() == false){
            for (int i=0; i<Neighbours.length;i++){
                
            }
        }
        return score; // CHANGE THIS
    }
    public int[][] GetNeighbours(){
        int[][] Neighbours = new int[8][2];
        return Neighbours;
    }
}