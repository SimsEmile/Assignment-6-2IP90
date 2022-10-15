import java.util.*;

/**
 * Algorithm sample for the iterated version of prisoner's dillema.
 */
class Assignment6 {

    Random random = new Random(212798);
    double alpha = 1.159;
    private static final int ROWS = 50; //number of grid rows
    private static final int COLUMNS = 50;  //number of grid columns
    private int[][] a = new int[ROWS + 2][COLUMNS + 2]; //grid displayed as a array of integers

    //array that stores the score for each patch:
    private double[][] score = new double[ROWS + 2][COLUMNS + 2];  

    /**
     * initialise the grid with randomly assigned 1s and 0s.
     * 1 - the patch cooperates
     * 0 - the patch deosn't cooperate
     */
    void fillGrid() {
        for (int i = 1; i <= ROWS; ++i) {
            for (int j = 1; j <= COLUMNS; ++j) {
                boolean coop = random.nextBoolean();
                a[i][j] = (coop ? 1 : 0);
            }
        }
    }

    /**
     * Create imaginary neighbours to the grid so that every array element has neighbours
     * around them.
     */
    void virtualNeighbours() {
        //add neighbours to the corners
        a[0][0] = a[ROWS][COLUMNS];
        a[ROWS + 1][0] = a[1][COLUMNS];
        a[0][COLUMNS + 1] = a[ROWS][1];
        a[ROWS + 1][COLUMNS + 1] = a[1][1];

        //add neighbours to the first and last rows
        for (int j = 1; j <= COLUMNS; ++j) {
            a[0][j] = a[ROWS][j % (COLUMNS + 1)];
            a[ROWS + 1][j] = a[1][j % (COLUMNS + 1)];
        }

        //add neighbours to the first and last columns
        for (int i = 1; i <= ROWS; ++i) {
            a[i][0] = a[i % (ROWS + 1)][COLUMNS];
            a[i][COLUMNS + 1] = a[i % (ROWS + 1)][1];
        }
    }

    /**
     * same as above, but this one is used for the scores, which
     * is a different array type so it needs its own neighbours method.
     * 
     * This might not be necessary in the actual assignment, since we re gonna use the patch class
     * and we can just add a double field and an int field for it, with different constructors
     * for each of those
     * 
     * I don't yet have the best idea how to do that but we'll manage 
     * Worst case scenario we ll just add 2 identical methods like I did here lol.
     */
    void virtualScores() {
        score[0][0] = score[ROWS][COLUMNS];
        score[ROWS + 1][0] = score[1][COLUMNS];
        score[0][COLUMNS + 1] = score[ROWS][1];
        score[ROWS + 1][COLUMNS + 1] = score[1][1];

        for (int j = 1; j <= COLUMNS; ++j) {
            score[0][j] = score[ROWS][j % (COLUMNS + 1)];
            score[ROWS + 1][j] = score[1][j % (COLUMNS + 1)];
        }

        for (int i = 1; i <= ROWS; ++i) {
            score[i][0] = score[i % (ROWS + 1)][COLUMNS];
            score[i][COLUMNS + 1] = score[i % (ROWS + 1)][1];
        }
    }

    /**
     * Prints the grid elements.
     */
    void printGrid() {
        for (int i = 1; i <= ROWS; ++i) {
            for (int j = 1; j <= COLUMNS; ++j) {
                System.out.print(a[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Prints the scores.
     */
    void printScore() {     
        for (int i = 1; i <= ROWS; ++i) {
            for (int j = 1; j <= COLUMNS; ++j) {
                System.out.print(score[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Calculates the score of a patch.
     * @param row the row of the patch
     * @param col the column of the patch
     */
    void calculateScore(int row, int col) {
        //add the values of every patch that surrounds the given patch to the score
        for (int i = row - 1; i <= row + 1; ++i) {
            for (int j = col - 1; j <= col + 1; ++j) {
                score[row][col] += a[i][j];
            }
        }

        //subtract the value of the given patch
        score[row][col] -= a[row][col];

        //multiply by alpha if the patch isn't cooperative
        if (a[row][col] == 0) {
            score[row][col] *= alpha;
        }
    }

    /**
     * Determines the high score of a neighbourhood to which a given patch belongs to.
     * @param row the row of the patch
     * @param col the column of the patch
     * @return the high score of the neighbourhood
     */
    double determineHighScore(int row, int col) {
        double highScore = -1; //initialise the highscore with the lowest possible value
        /*
         * search in the neighbourhood for every score and compare it to the highest found score.
         * if the found score is higher than the last found highScore, assign the score value to the
         * highScore variable.
         */
        for (int i = row - 1; i <= row + 1; ++i) {
            for (int j = col - 1; j <= col + 1; ++j) {
                if (score[i][j] > highScore) {
                    highScore = score[i][j]; 
                }
            }
        }
        return highScore;
    }
    
    /**
     * Determines the strategy that a patch will use in the next round.
     * @param row the row of the patch
     * @param col the column of the patch
     */
    void toggleStrategy(int row, int col) {

        //find the high score of the neighbourhood
        double highScore = determineHighScore(row, col);
        int k = -1; //variable for indexing the array of neighbours with the high score
        int[][] winners = new int[10][2];   //array of neighbours with the high score

        //find the neighbours that have a high score and add them to the array
        for (int i = row - 1; i <= row + 1; ++i) {
            for (int j = col - 1; j <= col + 1; ++j) {
                if (score[i][j] == highScore) {
                    winners[++k][0] = i;
                    winners[k][1] = j;
                }
            }
        }

        int index = k;
        
        //randomly choose one of the winners, if there are more of them
        if (k > 0) {
            index = random.nextInt(0, k); 
        }
        
        a[row][col] = a[winners[index][0]][winners[index][1]];
    }

    /**
     * method for testing the algorithm.
     */
    void run() {
        int numberOfRounds = 1;    //a chosen number of displayed round

        fillGrid();
        virtualNeighbours();
    
        //print 10 rounds
        do {
            //determine the score for each cell
            for (int i = 1; i <= ROWS; ++i) {
                for (int j = 1; j <= COLUMNS; ++j) {
                    calculateScore(i, j);
                }
            }
            virtualScores();
            System.out.println("\n------ ROUND " + numberOfRounds + " ------\n");
            printGrid();
            //determine the strategy for the next round
            for (int i = 1; i <= ROWS; ++i) {
                for (int j = 1; j <= COLUMNS; ++j) {
                    toggleStrategy(i, j);
                }
            }
            virtualNeighbours();
            ++numberOfRounds;
        } while (numberOfRounds <= 10);
    }

    public static void main(String[] args) {
        new Assignment6().run();
    }
}
