package Model;


import AI.TicTacToeAI;
import java.util.ArrayList;

public class TicTacToe extends Model {

    private final int EMPTY = 0;
    private final int PLAYER_X = 1;
    private final int PLAYER_O = 2;

    public TicTacToe() {
        super(3, new TicTacToeAI());
    }

    @Override
    public boolean isFinished() {
        if (checkWinner() != 0){
            return true;
        } else if (checkTie()) {
            return true;
        }
        return false;
    }


    /**
     * Checkt of de player winnaar is.
     * @return geeft terug of de speler gewonnen heeft.
     */
    @Override
    public int checkWinner() {
        int[] gameBoard = getBoardData();
        // Check vertical
        for (int i = 0; i < 3; i++) {
            if (gameBoard[i] == gameBoard[i + 3] && gameBoard[i + 3] == gameBoard[i + 6]) {
                return gameBoard[i];
            }
        }
        // Check horizontal
        for (int i = 0; i < gameBoard.length; i++) {
            if (i % 3 == 2) {
                if (gameBoard[i] == gameBoard[i - 1] && gameBoard[i - 1] == gameBoard[i - 2]) {
                    return gameBoard[i];
                }
            }
        }
        // Check diagonal
        if (gameBoard[0] == gameBoard[4] && gameBoard[4] == gameBoard[8]) {
            return gameBoard[0];
        } else if (gameBoard[2] == gameBoard[4] && gameBoard[4] == gameBoard[6]) {
            return gameBoard[2];
        }
        return EMPTY;
    }

    @Override
    public boolean availabeMovePlayer() {
        return true;
    }

    /**
     * Checkt of het een gelijkspel is.
     *
     * @return geeft terug of er een gelijkspel is.
     */
    @Override
    public boolean checkTie() {
        int[] gameBoard = getBoardData();
        // Check tie/Check of er nog plaats is op het speelveld
        for (int i = 0; i < gameBoard.length; i++) {
            if (gameBoard[i] == EMPTY) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int[] move(int idx, int[] currentBoard, int currentPlayer) {
        currentBoard[idx] = currentPlayer;
        return currentBoard;
    }

    @Override
    public ArrayList<Integer> getAvailableMoves(int[] gameBoard, int player) {
        ArrayList<Integer> availableMoves = new ArrayList<>();

        for (int i = 0; i < gameBoard.length; i++) {
            if (gameBoard[i] == EMPTY) {
                availableMoves.add(i);
            }
        }

        return availableMoves;
    }

    @Override
    public boolean validMove(int idx, int[] gameBoard) {
        return isEmptyColumn(idx, gameBoard);
    }

    @Override
    public int[] buildGameBoard() {
        int size = getSize();
        return new int[size * size];
    }

    /**
     * @return geeft de huidige speler terug
     */
    @Override
    public String getCurrentPlayerString(){
        if (getCurrentPlayer() == PLAYER_X){
            return "X";
        }
        return "O";
    }

    @Override
    public String getStringWinner() {
        if (getWinner() == PLAYER_X){
            return "X";
        }
        return "O";
    }
}
