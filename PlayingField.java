import java.util.Random;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Playingfield.
 * 
 * INCOMPLETE
 * Assignment 6 -- Prisoner's Dilemma -- 2ip90
 * part PlayingField
 * 
 * assignment copyright Kees Huizing
 * 
 * @author NAME
 * @id ID
 * @author NAME
 * @id ID
 */
class PlayingField extends JPanel /* possible implements ... */ {

    private Patch[][] grid;

    private double alpha; // defection award factor

    private Timer timer;

    // random number genrator
    private static final long SEED = 37L; // seed for random number generator; any number goes
    public static final Random RANDOM = new Random(SEED);

    // ...

    /**
     * Calculate and execute one step in the simulation.
     */
    public void step () {
        // ...
    }

    public void setAlpha(double alpha) {
        // ...
    }

    /**
     * Alpha of this playing field..
     * 
     * @return alpha value for this field.
     */
    public double getAlpha() {
        // ...
        return 0.0; // CHANGE THIS
    }

    /**
     * Grid as 2D boolean array.
     * 
     * Precondition: Grid is rectangular, has a non-zero size, and all elements are non-null.
     * 
     * @return 2D boolean array, with true for cooperators and false for defectors
     */
    public boolean[][] getGrid() {
        boolean[][] resultGrid = new boolean[grid.length][grid[0].length];
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                resultGrid[x][y] = grid[x][y].isCooperating();
            }
        }

        return resultGrid;
    }

    /**
     * Set this fields grid.
     * 
     * All patches in the grid become cooperating is the corresponding item in inGrid is true.
     * 
     * @param inGrid 2D array, with true for cooperators and false for defectors.
     */
    public void setGrid(boolean[][] inGrid) {
        // ...
    }
}