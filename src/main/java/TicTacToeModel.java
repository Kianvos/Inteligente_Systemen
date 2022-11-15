public class TicTacToeModel extends GameModel {
    public TicTacToeModel() {
        super(3);

    }

    /**
     * Checkt of de player winnaar is.
     *
     * @param player is degene waarvoor hij controleert of er een winnaar is.
     * @return geeft terug of de speler gewonnen heeft.
     */
    public boolean checkWinner(char player) {
        char[] gameBoard = getBoardData();
        // Check vertical
        for (int i = 0; i < 3; i++) {
            if (gameBoard[i] == gameBoard[i + 3] && gameBoard[i + 3] == gameBoard[i + 6] && gameBoard[i] == player) {
                return true;
            }
        }
        // Check horizontal
        for (int i = 0; i < gameBoard.length; i++) {
            if (i % 3 == 2) {
                if (gameBoard[i] == gameBoard[i - 1] && gameBoard[i - 1] == gameBoard[i - 2] && gameBoard[i] == player) {
                    return true;
                }
            }
        }
        // Check diagonal
        if (gameBoard[0] == gameBoard[4] && gameBoard[4] == gameBoard[8] && gameBoard[0] == player) {
            return true;
        } else if (gameBoard[2] == gameBoard[4] && gameBoard[4] == gameBoard[6] && gameBoard[2] == player) {
            return true;
        }
        return false;
    }

    /**
     * Checkt of het een gelijkspel is.
     *
     * @return geeft terug of er een gelijkspel is.
     */
    public boolean checkTie() {
        char[] gameBoard = getBoardData();
        // Check tie/Check of er nog plaats is op het speelveld
        for (int i = 0; i < gameBoard.length; i++) {
            if (gameBoard[i] == '\u0000') {
                return false;
            }
        }
        return true;
    }

    public char[] move(int idx, char[] currentBoard, char currentPlayer) {
        currentBoard[idx] = currentPlayer;
        return currentBoard;
    }

    public boolean validMove(int idx) {
        return isEmptyColumn(idx);
    }

    public char[] buildGameBoard() {
        int size = getSize();
        return new char[size * size];
    }
}
