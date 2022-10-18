import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Playingfield.
 * 
 * Assignment 6 -- Prisoner's Dilemma -- 2ip90
 * part PlayingField
 * 
 * assignment copyright Kees Huizing
 * 
 * @author Mihnea-Theodor Visoiu
 * @ID 1791346
 * @author Vlad Petru Barbulescu
 * @ID 1850288
 */

class PlayingField extends JPanel {
    private Patch[][] grid;
    public static final int GRID_SIZE_ROW = 50; //Making it public as it's a useful constant
    public static final int GRID_SIZE_COL = 50; //Making it public as it's a useful constant

    private double alpha; // defection award factor
    public static final double MIN_ALPHA = 0.0d; //Making it public as it's a useful constant 
    public static final double MAX_ALPHA = 3.0d; //Making it public as it's a useful constant

    public static final int MIN_ANIMATION_SPEED = 10; //In milliseconds
    public static final int MAX_ANIMATION_SPEED = 10000; //In milliseconds

    private Timer timer;

    private boolean useAlternativeRule = false;

    // random number genrator
    private static final long SEED = 37L; // seed for random number generator; any number goes
    public static final Random RANDOM = new Random(SEED);

    /**
     * Initializes the field with GRID_SIZE_ROW rows x GRID_SIZE_COL patches, randomizes them
     * and prepares the animation timer.
     */
    public PlayingField() {
        this.setLayout(new GridLayout(GRID_SIZE_ROW, GRID_SIZE_COL));
        initializeGrid(GRID_SIZE_ROW, GRID_SIZE_COL);
        assignNeighbors();
        randomizeGrid();
        setAlpha(MIN_ALPHA);

        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                step();
            }
        });
        timer.setInitialDelay(0);
    }

    /**
     * Calculate and execute one step in the simulation.
     */
    public void step() {
        calculateAllScores();
        updateAllStrategies();
    }

    /**
     * Set a new alpha value for this field.
     * 
     * @param alpha The new alpha value
     */
    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    /**
     * Alpha of this playing field..
     * 
     * @return alpha value for this field.
     */
    public double getAlpha() {
        return alpha;
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
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                grid[x][y].setCooperating(inGrid[x][y]);
            }
        }
    }

    /**
     * Initializes a new grid with sizeX x sizeY patches.
     * 
     * @param sizeX Number of rows for the grid
     * @param sizeY Number of columns for the grid
     */

    public void initializeGrid(int sizeX, int sizeY) {
        grid = new Patch[sizeX][sizeY];
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                grid[x][y] = new Patch();
                this.add(grid[x][y]);
            }
        }
    }

    /**
     * Randomize all grid's patches' strategies.
     */
    public void randomizeGrid() {
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                grid[x][y].setCooperating(RANDOM.nextBoolean());
                //We use forceSyncOldCooperating() to make sure that both Patch.cooperating and
                //Patch.oldCooperating are the same. This prevents inconsistencies when resetting
                //mid-simulation.
                grid[x][y].forceSyncOldCooperating();
            }
        }
    }

    /**
     * Compute each patch's neighbors and store them in each patch.
     */
    public void assignNeighbors() {
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                Patch[] neighbors = new Patch[8];
                neighbors[0] = grid
                    [(x + GRID_SIZE_ROW - 1) % GRID_SIZE_ROW]
                    [(y + GRID_SIZE_COL - 1) % GRID_SIZE_COL];
                neighbors[1] = grid
                    [(x + GRID_SIZE_ROW - 1) % GRID_SIZE_ROW]
                    [(y + GRID_SIZE_COL + 0) % GRID_SIZE_COL];
                neighbors[2] = grid
                    [(x + GRID_SIZE_ROW - 1) % GRID_SIZE_ROW]
                    [(y + GRID_SIZE_COL + 1) % GRID_SIZE_COL];
                neighbors[3] = grid
                    [(x + GRID_SIZE_ROW + 0) % GRID_SIZE_ROW]
                    [(y + GRID_SIZE_COL - 1) % GRID_SIZE_COL];
                neighbors[4] = grid
                    [(x + GRID_SIZE_ROW + 0) % GRID_SIZE_ROW]
                    [(y + GRID_SIZE_COL + 1) % GRID_SIZE_COL];
                neighbors[5] = grid
                    [(x + GRID_SIZE_ROW + 1) % GRID_SIZE_ROW]
                    [(y + GRID_SIZE_COL - 1) % GRID_SIZE_COL];
                neighbors[6] = grid
                    [(x + GRID_SIZE_ROW + 1) % GRID_SIZE_ROW]
                    [(y + GRID_SIZE_COL + 0) % GRID_SIZE_COL];
                neighbors[7] = grid
                    [(x + GRID_SIZE_ROW + 1) % GRID_SIZE_ROW]
                    [(y + GRID_SIZE_COL + 1) % GRID_SIZE_COL];

                grid[x][y].setNeighbors(neighbors);
            }
        }
    }

    /**
     * Method which calculates the scores across all patches.
     */
    public void calculateAllScores() {
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                grid[x][y].calculateScore(alpha);
            }
        }
    }

    /**
     * Method which updates the strategy of all patches, according to the given strategy.
     */
    public void updateAllStrategies() {
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                //This array contains only the subset of all the neighbors + the given patch
                //which scored the highest
                ArrayList<Patch> highScoringNeighbors = grid[x][y].getHighestScoringNeighbors();
                int neighborIndex = RANDOM.nextInt(highScoringNeighbors.size());

                if (useAlternativeRule) { //Alternative update rule case
                    if (grid[x][y].getScore() 
                        == highScoringNeighbors.get(neighborIndex).getScore()) {
                        //The player's score IS the highest in the neighborhood.
                        //We set its strategy to the same one, mostly to update
                        //the UI in regards to the patch colors.
                        grid[x][y].setCooperating(grid[x][y].isCooperating());
                    } else {
                        //The player's score isn't the highest in the neighborhood, falling
                        //back to the regular rule set, changing its strategy.
                        grid[x][y].setCooperating(
                            highScoringNeighbors
                            .get(neighborIndex)
                            .getStrategyWhenScoreWasCalculated());
                    }
                } else { //Regular rule update case
                    grid[x][y].setCooperating(
                        highScoringNeighbors
                        .get(neighborIndex)
                        .getStrategyWhenScoreWasCalculated());
                }
            }
        }
    }

    /** 
     * Method which returns the current animation speed.
     * 
     * @return Current animation speed, in ms of display time per frame.
    */
    public int getPlaybackSpeed() {
        return timer.getDelay();
    }

    /**
     * Method which sets a new animatin speed.
     * 
     * @param ms New animation speed, in ms of display time per frame.
     */
    public void setPlaybackSpeed(int ms) {
        timer.setDelay(ms);
    }


    /**
     * Method which toggles the playback of the simulation.
     */
    public void togglePlaybackSimulation() {
        if (timer.isRunning()) {
            timer.stop();
        } else {
            timer.start();
        }
    }

    /**
     * Method which returns the current playback status of the animation.
     * 
     * @return True - if the animation is running; False otherwise.
     */
    public boolean isSimulationRunning() {
        return timer.isRunning();
    }

    /**
     * Method which sets which rule set should be used for updating the strategy.
     * 
     * @param useAlternativeRule Set to true if the alternative Part B.4 rule is to be used, false
     *      for the regular ruleset.
     */
    public void setUpdateRule(boolean useAlternativeRule) {
        this.useAlternativeRule = useAlternativeRule;
    }
}