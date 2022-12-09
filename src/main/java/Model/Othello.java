package Model;

import AI.OthelloAI;
import java.util.ArrayList;

// TODO win en gelijk fixen

public class Othello extends Model {

    private final int EMPTY = 0;
    private final int BLACK = 1;
    private final int WHITE = 2;

    private final int SUGGESTED = 3;


    /**
     * offset for row
     */
    private static final int[] OFFSET_ROW = {-1, -1, -1, 0, 0, 1, 1, 1};

    /**
     * offset for column
     */
    private static final int[] OFFSET_COL = {-1, 0, 1, -1, 1, -1, 0, 1};

    public Othello() {
        super(8, new OthelloAI());
    }

    @Override
    public boolean isFinished() {
        int[] currentBoard = getBoardData();

        int currentPlayer = getCurrentPlayer();
        int otherPlayer = (currentPlayer == BLACK) ? WHITE : BLACK;

        ArrayList<Integer> availableMovesCurrentPlayer = getAvailableMoves(currentBoard, currentPlayer);
        ArrayList<Integer> availableMovesOtherPlayer = getAvailableMoves(currentBoard, otherPlayer);

        return (availableMovesCurrentPlayer.size() + availableMovesOtherPlayer.size()) == 0;
    }

    //todo controleer of speler nog minstens 1 steen hebben
    //todo controleer wie er aan het eind van het spel de meeste blokjes te hebben

    @Override
    public int checkWinner() {
        int[] gameBoard = getBoardData();
        int currentPlayer = 0;
        int opponent = 0;

        ArrayList<Integer> blackMoves = this.getAvailableMoves(gameBoard, BLACK);
        ArrayList<Integer> whiteMoves = this.getAvailableMoves(gameBoard, WHITE);

        if (blackMoves.isEmpty() && whiteMoves.isEmpty()) {
            for (int c : gameBoard) {
                if (c == BLACK) {
                    currentPlayer++;
                }
                if (c == WHITE) {
                    opponent++;
                }
            }
            if (currentPlayer == opponent) {
                return EMPTY;
            }


        }
        return (currentPlayer > opponent) ? BLACK : WHITE;
    }

    @Override
    public boolean checkTie() {
        int[] gameBoard = getBoardData();
        int currentPlayer = 0;
        int opponent = 0;
        ArrayList<Integer> blackMoves = this.getAvailableMoves(gameBoard, BLACK);
        ArrayList<Integer> whiteMoves = this.getAvailableMoves(gameBoard, WHITE);

        if (blackMoves.isEmpty() && whiteMoves.isEmpty()) {
            for (int c : gameBoard) {
                if (c == BLACK) {
                    currentPlayer++;
                }
                if (c == WHITE) {
                    opponent++;
                }
            }

            return currentPlayer == opponent;
        }

        return false;
    }

    @Override
    public int[] move(int idx, int[] currentBoard, int currentPlayer) {
        int opponent = (currentPlayer == BLACK) ? WHITE : BLACK;

        currentBoard[idx] = currentPlayer;
        for (int i = 0; i < currentBoard.length; i++) {
            if (currentBoard[i] == SUGGESTED) {
                currentBoard[i] = EMPTY;
            }
        }
        currentBoard = moveColBetweeen(idx, currentBoard, currentPlayer);
        ArrayList<Integer> availableMoves = getAvailableMoves(currentBoard, opponent);

        if (availableMoves.isEmpty()) {
            availableMoves = getAvailableMoves(currentBoard, currentPlayer);
        }

        for (Integer availableMove : availableMoves) {
            currentBoard[availableMove] = SUGGESTED;
        }

        return currentBoard;
    }

    public int[] moveColBetweeen(int idx, int[] gameBoard, int player) {
        int size = getSize();
        int row = Math.floorDiv(idx, size);
        int col = (idx - size * row);
        for (int i = 0; i < size; i++) {
            int tmpRow = row + OFFSET_ROW[i];
            int tmpCol = col + OFFSET_COL[i];
            boolean hasOpponentBetween = false;

            while (tmpRow >= 0 && tmpRow < size && tmpCol >= 0 && tmpCol < size) {
                int tmpIdx = tmpCol + tmpRow * size;
                if (isEmptyColumn(tmpIdx, gameBoard)) {
                    break;
                }
                if (gameBoard[tmpIdx] == player) {
                    hasOpponentBetween = true;
                }
                if (gameBoard[tmpIdx] == player && hasOpponentBetween) {
                    int effectRow = row + OFFSET_ROW[i];
                    int effectCol = col + OFFSET_COL[i];
                    while (effectRow != tmpRow || effectCol != tmpCol) {
                        int effectIdx = effectCol + effectRow * size;
                        gameBoard[effectIdx] = player;
                        effectRow += OFFSET_ROW[i];
                        effectCol += OFFSET_COL[i];
                    }

                    break;
                }
                tmpRow += OFFSET_ROW[i];
                tmpCol += OFFSET_COL[i];
            }
        }
        return gameBoard;
    }

    @Override
    public boolean validMove(int idx, int[] gameBoard) {
        int opponent = (getCurrentPlayer() == BLACK) ? WHITE : BLACK;
        return isValidMove(idx, getCurrentPlayer(), opponent, gameBoard);
    }

    /**
     * Zoekt naar alle mogelijke zetten
     *
     * @param player zoekt hij de mogelijke setten
     * @return geeft de indexes terug waar op een zet gedaan kan worden.
     */
    @Override
    public ArrayList<Integer> getAvailableMoves(int[] gameBoard, int player) {
        ArrayList<Integer> availableMoves = new ArrayList<>();
        int size = getSize();
        int opponent = (player == BLACK) ? WHITE : BLACK;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int idx = i * size + j;
                if (isValidMove(idx, player, opponent, gameBoard)) {
                    availableMoves.add(idx);
                }
            }

        }
        return availableMoves;
    }

    public boolean isValidMove(int idx, int player, int opponent, int[] gameBoard) {
        if (!isEmptyColumn(idx, gameBoard)) {
            return false;
        }

        int size = getSize();
        int row = Math.floorDiv(idx, size);
        int col = (idx - size * row);

        for (int i = 0; i < size; i++) {
            int tmpRow = row + OFFSET_ROW[i];
            int tmpCol = col + OFFSET_COL[i];

            boolean hasOpponentBetween = false;
            while (tmpRow >= 0 && tmpRow < size && tmpCol >= 0 && tmpCol < size) {
                int tmpIdx = tmpCol + tmpRow * size;
                if (gameBoard[tmpIdx] == opponent) {
                    hasOpponentBetween = true;
                } else if (gameBoard[tmpIdx] == player && hasOpponentBetween) {
                    return true;
                } else {
                    break;
                }
                tmpRow += OFFSET_ROW[i];
                tmpCol += OFFSET_COL[i];
            }
        }
        return false;
    }

    @Override
    public boolean availabeMovePlayer() {
        int currentPlayer = getCurrentPlayer();

        ArrayList<Integer> availableMovesCurrentPlayer = getAvailableMoves(getBoardData(), currentPlayer);

        return availableMovesCurrentPlayer.size() > 0;
    }

    @Override
    public int[] buildGameBoard() {
        int size = getSize();
        int[] gameBoard = new int[size * size];
        gameBoard[27] = WHITE;
        gameBoard[28] = BLACK;
        gameBoard[35] = BLACK;
        gameBoard[36] = WHITE;
        ArrayList<Integer> availableMoves = getAvailableMoves(gameBoard, getCurrentPlayer());
        for (Integer availableMove : availableMoves) {
            gameBoard[availableMove] = SUGGESTED;
        }
        return gameBoard;
    }

    /**
     * Telt op hoeveel disks jij op het spelbord hebt staan.
     *
     * @param boardData geeft het speelbord van dat moment mee
     * @param disc      geeft mee voor welke je wil kijken hoeveel er zijn.
     * @return het aantal disks wat je hebt
     */
    public static int countScore(int[] boardData, int disc) {
        int count = 0;
        for (int i = 0; i < boardData.length; i++) {
            if (boardData[i] == disc) {
                count++;
            }
        }
        return count;
    }

    /**
     * @return geeft de huidige speler terug
     */
    @Override
    public String getCurrentPlayerString() {
        if (getCurrentPlayer() == BLACK) {
            return "Zwart";
        }
        return "Wit";
    }

    @Override
    public String getStringWinner() {
        if (getWinner() == BLACK) {
            return "Zwart";
        }
        return "Wit";
    }
}
