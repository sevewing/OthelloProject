public class Evaluation {
    private static int[][] board;
    private static int current;
    private static int opponent;
    private static int[] countDisc;
    private static int sizeIndex;
    private static int sumDisc;
    private static int sumBoard;


    public static int evaluationValue(GameState s) {
        board = s.getBoard();
        current = s.getPlayerInTurn();
        opponent = current == 1 ? 2 : 1;
        countDisc = s.countTokens();
        sizeIndex = board.length - 1;
        sumDisc = countDisc[0] + countDisc[1];
        sumBoard = board.length * board.length;

        return evaluationWeight();
    }

    private static int evaluationWeight() {
        int value = 0;
        if (sumDisc > sumBoard / 4) {
            value += 200 * cornerWeight();
            value += 30 * edgeWeight();
        }
//        if (sumDisc < sumBoard / 2) {
        value += 40 * aveAssignmentWeight();
//        }
        if (sumDisc > sumBoard / 3) {
            value += 10 * countDiscWeight();
        }

        value -= 20 * cornerDefensiveWeight();
        // for depth 6
//        value -= 60 * edgeDefensiveWeight();
        return value;
    }

    /**
     * Stable positions , corner
     *
     * @return count
     */
    private static int cornerWeight() {
        // Advantages: The corners are the most stable positions.
        int count = 0;
        if (board[0][0] == current) {
            count++;
        }
        if (board[sizeIndex][sizeIndex] == current) {
            count++;
        }
        if (board[0][sizeIndex] == current) {
            count++;
        }
        if (board[sizeIndex][0] == current) {
            count++;
        }
        return count;
    }

    /**
     * Stable positions ,edge (without Corner)
     *
     * @return count
     */
    private static int edgeWeight() {
//         Advantages: The edges of the board are the most stable positions.
        int count = 0;
        for (int i = 1; i <= sizeIndex - 1; i++) {
            if (board[i][1] == current) {
                count++;
            }
            if (board[i][sizeIndex - 1] == current) {
                count++;
            }
            if (board[1][i] == current) {
                count++;
            }
            if (board[sizeIndex - 1][i] == current) {
                count++;
            }
        }
        return count;
    }


    /**
     * Try to make it have an averanged assignment
     *
     * @return count
     */
    private static int aveAssignmentWeight() {
        //Advantages: Ensure that you are placing discs in a variety of locations around the board.
        //Try to have one piece in each col/row
        int count = 0;
        for (int row = 0; row <= sizeIndex; row++) {
            int countRow = 0;
            int countCol = 0;
            for (int col = 0; col <= sizeIndex; col++) {
                if (board[row][col] == current) {
                    countRow++;
                }
                if (board[col][row] == current) {
                    countCol++;
                }
            }
            if (countRow > 0) {
                count++;
            }
            if (countCol > 0) {
                count++;
            }
        }
        return count;
    }

    /**
     * Prone to win
     *
     * @return count
     */
    private static int countDiscWeight() {
        //Advantages: Calculate the numbers
        int count = 0;

        if (current == 1)
            count = countDisc[0];
        if (current == 2)
            count = countDisc[1];
        return count;
    }

    /**
     * Current get the place that easy to be outflanked (corners)
     *
     * @return count
     */
    private static int cornerDefensiveWeight() {
        // Disadvantages: Corner is empty, but the one next to it is the currentPlayer
        int count = 0;
        if (board[0][0] == 0 && (board[1][1] == current || board[0][1] == current || board[1][0] == current)) {
            count++;
        }
        if (board[sizeIndex][sizeIndex] == 0 && (board[sizeIndex - 1][sizeIndex - 1] == current || board[sizeIndex][sizeIndex - 1] == current || board[sizeIndex - 1][sizeIndex] == current)) {
            count++;
        }
        if (board[0][sizeIndex] == 0 && (board[1][sizeIndex - 1] == current || board[0][sizeIndex - 1] == current || board[1][sizeIndex] == current)) {
            count++;
        }
        if (board[sizeIndex][0] == 0 && (board[sizeIndex - 1][1] == current || board[sizeIndex - 1][0] == current || board[sizeIndex][1] == current)) {
            count++;
        }
        return count;
    }

    /**
     * Opponent get the edge (include corner)
     *
     * @return count
     */
    private static int edgeDefensiveWeight() {
        int count = 0;
        for (int i = 0; i <= sizeIndex; i++) {
            if (board[i][0] == opponent) {
                count++;
            }
            if (board[i][sizeIndex] == opponent) {
                count++;
            }
            if (board[0][i] == opponent) {
                count++;
            }
            if (board[sizeIndex][i] == opponent) {
                count++;
            }
        }
        return count;
    }


}
