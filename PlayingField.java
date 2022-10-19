import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.JPanel;
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
 * @author Alexandru Dicu
 * @id 1837370
 * @author Jules Anseaume
 * @id 1806769
 */
class PlayingField extends JPanel {

    private double alpha; // defection award factor

    private Timer timer;

    // random number genrator
    private static final long SEED = 37L; // seed for random number generator; any number goes
    public static final Random RANDOM = new Random(SEED);

    private static final int ROWS = 50; //number of grid rows

    private static final int COLUMNS = 50;  //number of grid columns

    private static final int NEIGHBOURS = 9;   //total number of patches in a neighbourhood

    private Patch[][] grid = new Patch[ROWS + 2][COLUMNS + 2];

    private boolean rule = false;

    public PlayingField() {
        this.setLayout(new GridLayout(ROWS, COLUMNS));
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                step();
            }
        });
        timer.setInitialDelay(0);
    }

    int getDisplayTime() {
        return timer.getDelay();
    }

    void setDisplayTime(int ms) {
        timer.setDelay(ms);
    }

    Timer getTimer() {
        return this.timer;
    }

    void toggleSimulation() {
        if (timer.isRunning()) {
            timer.stop();
        } else {
            timer.start();
        }
    }

    boolean isRunning() {
        return timer.isRunning();
    }
    /**
     * Sets the rule to the (boolean rule) given
     *
     * @param rule, 
     */
    void setRule(boolean rule) {
        this.rule = rule;
    }

    /**
     *
     * @returns rule, used for
     */
    boolean getRule() {
        return this.rule;
    }
    
    /**
     * Create imaginary neighbours to the grid so that every array element has neighbours
     * around them.
     */
    void addVirtualNeighbours() {
        //add neighbours to the corners
        grid[0][0] = grid[ROWS][COLUMNS];
        grid[ROWS + 1][0] = grid[1][COLUMNS];
        grid[0][COLUMNS + 1] = grid[ROWS][1];
        grid[ROWS + 1][COLUMNS + 1] = grid[1][1];

        //add neighbours to the first and last rows
        for (int j = 1; j <= COLUMNS; ++j) {
            grid[0][j] = grid[ROWS][j];
            grid[ROWS + 1][j] = grid[1][j];
        }

        //add neighbours to the first and last columns
        for (int i = 1; i <= ROWS; ++i) {
            grid[i][0] = grid[i][COLUMNS];
            grid[i][COLUMNS + 1] = grid[i][1];
        }
    }

    Neighbours[] getNeighbourhood(int row, int col) {
        Neighbours[] neighbourhood = new Neighbours[NEIGHBOURS];
        int index = -1;
        for (int i = row - 1; i <= row + 1; ++i) {
            for (int j = col - 1; j <= col + 1; ++j) {
                neighbourhood[++index] = new Neighbours(i, j, grid[i][j].isCooperating());
            }
        }
        return neighbourhood;
    }

    double calculateScore(int row, int col) {
        double score = 0.0;
        Neighbours[] neighbours = getNeighbourhood(row, col);
        for (int i = 0; i < neighbours.length; ++i) {
            if (grid[neighbours[i].getRow()][neighbours[i].getColumn()].isCooperating()) {
                ++score;
            } 
        }
        return (grid[row][col].isCooperating() ? score - 1 : score * getAlpha());
    }

    double getHighScore(int row, int col) {
        double highScore = -1;
        double score;
        Neighbours[] neighbours = getNeighbourhood(row, col);

        for (int i = 0; i < neighbours.length; ++i) {
            score = grid[neighbours[i].getRow()][neighbours[i].getColumn()].getScore();
            if (score > highScore) {
                highScore = score;
            }
        }
        return highScore;
    }

    void setStrategy(int row, int col) {
        int k = -1; //variable for indexing the array of possible winners
        double highScore = getHighScore(row, col);
        double score;
        Neighbours[] neighbours = getNeighbourhood(row, col);
        Neighbours[] winners = new Neighbours[NEIGHBOURS];

        for (int i = 0; i < neighbours.length; ++i) {
            int x = neighbours[i].getRow();  //variable for simplifying a neighbour's row
            int y = neighbours[i].getColumn();  //variable for simplifying a neighbour's column
            boolean strategy = neighbours[i].getStrategy(); //the neighbour's strategy
            score = grid[x][y].getScore();
            if (score == highScore) {
                winners[++k] = new Neighbours(x, y, strategy);
            }
        }
        int index = k;  //the index of the chosen winner

        //randomize the winner index if there are more winners
        if (k > 0) {
            index = RANDOM.nextInt(k + 1);
        }
        
        //set the next strategy of the patch
        if (getRule()) {
            if (grid[row][col].getScore() == highScore) {
                grid[row][col].setNextStrategy(grid[row][col].isCooperating());
            } else {
                grid[row][col].setNextStrategy(winners[index].getStrategy());
            }
        } else {
            grid[row][col].setNextStrategy(winners[index].getStrategy());
        }
    }

    void initializeGrid() {
        for (int x = 1; x <= ROWS; ++x) {
            for (int y = 1; y <= COLUMNS; ++y) {
                grid[x][y] = new Patch(0.0);  //initializing the patch
                this.add(grid[x][y]);  //adding the patch to the field
            }
        }
    }

    void randomizeGrid() {
        for (int x = 1; x <= ROWS; ++x) {
            for (int y = 1; y <= COLUMNS; ++y) {
                //set a random strategy for the patches
                grid[x][y].setCooperating(RANDOM.nextBoolean());
            }
        }
        addVirtualNeighbours();
    }

    void colorisePatches() {
        for (int x = 1; x <= ROWS; ++x) {
            for (int y = 1; y <= COLUMNS; ++y) {
                grid[x][y].setBackground(
                    (grid[x][y].isCooperating() ? Color.blue : Color.red)
                );
            }
        }
    }

    void determineScores() {
        for (int x = 1; x <= ROWS; ++x) {
            for (int y = 1; y <= COLUMNS; ++y) {
                grid[x][y].setScore(calculateScore(x, y));
            }
        }
        addVirtualNeighbours();
    }

    void confirmNewStrategies() {
        for (int x = 1; x <= ROWS; ++x) {
            for (int y = 1; y <= COLUMNS; ++y) {
                if (grid[x][y].isCooperating() != grid[x][y].getNextStrategy()) {
                    grid[x][y].toggleStrategy();
                    grid[x][y].setBackground(
                        (grid[x][y].isCooperating() ? Color.cyan : Color.orange)
                    );
                }
            }
        }
    }
    
    void determineNextRound() {
        for (int x = 1; x <= ROWS; ++x) {
            for (int y = 1; y <= COLUMNS; ++y) {
                setStrategy(x, y);
            }
        }
        confirmNewStrategies();
        addVirtualNeighbours();
    }
    /**
     * Calculate and execute one step in the simulation.
     */
    public void step() {
        colorisePatches();
        determineScores();
        determineNextRound();
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    /**
     * Alpha of this playing field..
     * 
     * @return alpha value for this field.
     */
    public double getAlpha() {
        return this.alpha;
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
        for (int x = 0; x <= ROWS + 1; x++) {
            for (int y = 0; y <= COLUMNS + 1; y++) {
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
        for (int x = 1; x <= ROWS; ++x) {
            for (int y = 1; y <= COLUMNS; ++y) {
                inGrid[x][y] = RANDOM.nextBoolean();
            }
        }
        inGrid[0][0] = inGrid[ROWS][COLUMNS];
        inGrid[ROWS + 1][0] = inGrid[1][COLUMNS];
        inGrid[0][COLUMNS + 1] = inGrid[ROWS][1];
        inGrid[ROWS + 1][COLUMNS + 1] = inGrid[1][1];

        //add neighbours to the first and last rows
        for (int j = 1; j <= COLUMNS; ++j) {
            inGrid[0][j] = inGrid[ROWS][j];
            inGrid[ROWS + 1][j] = inGrid[1][j];
        }

        //add neighbours to the first and last columns
        for (int i = 1; i <= ROWS; ++i) {
            inGrid[i][0] = inGrid[i][COLUMNS];
            inGrid[i][COLUMNS + 1] = inGrid[i][1];
        }
    }
}

class Neighbours {
    private int row;
    private int col;
    private boolean coop;

    Neighbours(int row, int col, boolean coop) {
        this.row = row;
        this.col = col;
        this.coop = coop;
    }

    int getRow() {
        return this.row;
    }

    int getColumn() {
        return this.col;
    }

    boolean getStrategy() {
        return this.coop;
    }
}
