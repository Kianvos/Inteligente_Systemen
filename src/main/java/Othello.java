import java.util.ArrayList;

//TODO win en gelijk fixen

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
        super(8);
    }

    public boolean isFinished() {
        int[] currentBoard = getBoardData();

        for (int i = 0; i < currentBoard.length; i++) {
            if (currentBoard[i] == EMPTY){
                break;
            }
            return true;
        }
        int currentPlayer = getCurrentPlayer();
        int otherPlayer = BLACK;
        if (currentPlayer == BLACK) {
            otherPlayer = WHITE;
        }
        ArrayList<Integer> availableMovesCurrentPlayer = getAvailableMoves(getBoardData(), currentPlayer);
        ArrayList<Integer> availableMovesOtherPlayer = getAvailableMoves(getBoardData(), otherPlayer);

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
            for (int c : gameBoard
            ) {
                if (c == BLACK) {
                    currentPlayer++;
                }
                if (c != BLACK && c != EMPTY) {
                    opponent++;
                }
            }
            if (currentPlayer > opponent){
                return BLACK;
            } else {
                return WHITE;
            }
        }
        return EMPTY;
    }

    @Override
    public boolean checkTie() {
        int[] gameBoard = getBoardData();
        int currentPlayer = 0;
        int opponent = 0;
        ArrayList<Integer> blackMoves = this.getAvailableMoves(gameBoard, BLACK);
        ArrayList<Integer> whiteMoves = this.getAvailableMoves(gameBoard, WHITE);

        if (blackMoves.isEmpty() && whiteMoves.isEmpty()) {
            for (int c : gameBoard
            ) {
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


    public int[] move(int idx, int[] currentBoard, int currentPlayer) {
        int opponent = BLACK;
        if (currentPlayer == BLACK) {
            opponent = WHITE;
        }

        currentBoard[idx] = currentPlayer;
        for (int i = 0; i < currentBoard.length; i++) {
            if (currentBoard[i] == SUGGESTED) {
                currentBoard[i] = EMPTY;
            }
        }
        currentBoard = moveColBetweeen(idx, currentBoard, currentPlayer);
        ArrayList<Integer> availableMoves = getAvailableMoves(currentBoard, opponent);
        if (availableMoves.size() == 0){
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

    public boolean validMove(int idx, int[] gameBoard) {
        int opponent = BLACK;
        if (getCurrentPlayer() == BLACK) {
            opponent = WHITE;
        }
        return isValidMove(idx, getCurrentPlayer(), opponent, gameBoard);
    }

    /**
     * Zoekt naar alle mogelijke zetten
     *
     * @param player zoekt hij de mogelijke setten
     * @return geeft de indexes terug waar op een zet gedaan kan worden.
     */

    public ArrayList<Integer> getAvailableMoves(int[] gameBoard, int player) {
        ArrayList<Integer> availableMoves = new ArrayList<>();
        int size = getSize();
        int opponent;
        if (player == BLACK) {
            opponent = WHITE;
        } else {
            opponent = BLACK;
        }
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

    public boolean availabeMovePlayer() {
        int currentPlayer = getCurrentPlayer();

        ArrayList<Integer> availableMovesCurrentPlayer = getAvailableMoves(getBoardData(), currentPlayer);

        return availableMovesCurrentPlayer.size() > 0;
    }

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
     * @return geeft de huidige speler terug
     */
    public char getCurrentPlayerChar() {
        if (getCurrentPlayer() == BLACK) {
            return '⚫';
        }
        return '○';
    }

    public String getStringWinner() {
        if (getWinner() == BLACK) {
            return "⚫";
        }
        return "○";
    }
}
