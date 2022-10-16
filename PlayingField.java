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
 * @author Alexandru Dicu
 * @id 1837370
 * @author Jules Anseaume
 * @id 1806769
 */
class PlayingField extends JPanel /* possible implements ... */ {

    private Patch[][] grid;
    private boolean[][] strategy;

    private double alpha; // defection award factor

    private Timer timer;

    private static final int ROWS = 50; //number of grid rows

    private static final int COLUMNS = 50;  //number of grid columns

    private static final int NEIGHBOURS = 9;

    // random number genrator
    private static final long SEED = 37L; // seed for random number generator; any number goes
    public static final Random RANDOM = new Random(SEED);

    // ...

    /**
     * Create imaginary neighbours to the grid so that every array element has neighbours
     * around them.
     */

    void virtualNeighbours() {
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
                neighbourhood[++index] = new Neighbours(i, j, strategy[i][j]);
            }
        }
        return neighbourhood;
    }

    double calculateScore(int row, int col) {
        double score = 0;
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
        int k = -1;
        double highScore = getHighScore(row, col);
        double score;
        Neighbours[] neighbours = getNeighbourhood(row, col);
        Neighbours[] winners = new Neighbours[NEIGHBOURS];

        for (int i = 0; i < neighbours.length; ++i) {
            int x = neighbours[i].getRow();
            int y = neighbours[i].getColumn();
            boolean strategy = neighbours[i].getStrategy();
            score = grid[x][y].getScore();
            if (score == highScore) {
                winners[++k] = new Neighbours(x, y, strategy);
            }
        }
        int index = k;
        if (k > 0) {
            index = RANDOM.nextInt(k);
        }
        
        if (grid[row][col].isCooperating() != winners[index].getStrategy()) {
            grid[row][col].toggleStrategy();
        }
    }

    void print() {
        for (int x = 1; x <= ROWS; ++x) {
            for (int y = 1; y <= COLUMNS; ++y) {
                if (grid[x][y].getCoop()) {
                    System.out.print(1 + " ");
                } else {
                    System.out.print(0 + " ");
                }
            }
            System.out.println();
        }
    }

    void fillInitialGrid() {
        for (int x = 0; x <= ROWS + 1; ++x) {
            for (int y = 0; y <= COLUMNS + 1; ++y) {
                grid[x][y] = new Patch(x, y, 0.0, strategy[x][y]);
            }
        }
    }

    void determineScores(Patch[][] grid) {
        for (int x = 1; x <= ROWS; ++x) {
            for (int y = 1; y <= COLUMNS; ++y) {
                grid[x][y].setScore(calculateScore(x, y));
            }
        }
        virtualNeighbours();
    }

    void determineNextStrategies(Patch[][] grid) {
        for (int x = 1; x <= ROWS; ++x) {
            for (int y = 1; y <= COLUMNS; ++y) {
                setStrategy(x, y);
            }
        }
        virtualNeighbours();
    }
    
    /**
     * Calculate and execute one step in the simulation.
     */
    public void step() {
        // ...
        strategy = new boolean[ROWS + 2][COLUMNS + 2];
        grid = new Patch[ROWS + 2][COLUMNS + 2];

        setGrid(strategy);
        
        fillInitialGrid();
        print();

        System.out.println("round 2");
        
        determineScores(grid);
        determineNextStrategies(grid);
        print();
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
        return 1.1; // CHANGE THIS
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

    public static void main(String[] args) {
        new PlayingField().step();
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