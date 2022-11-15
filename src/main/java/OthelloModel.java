import java.util.ArrayList;

public class OthelloModel extends GameModel {
    private final char BLACK = 'X';
    private final char WHITE = 'O';


    /**
     * offset for row
     */
    private static final int[] OFFSET_ROW = {-1, -1, -1, 0, 0, 1, 1, 1};

    /**
     * offset for column
     */
    private static final int[] OFFSET_COL = {-1, 0, 1, -1, 1, -1, 0, 1};

    public OthelloModel() {
        super(8);

        char[] gameBoardStatus = getBoardData();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int idx = i * 8 + j;
                System.out.print(" " + gameBoardStatus[idx]);
            }
            System.out.println();
        }
    }


    @Override
    public boolean checkWinner(char player) {
        ArrayList<Integer> indexes = getAvailableMoves(BLACK);
        for (Integer index : indexes) {
            System.out.println(index);
        }
        return false;
    }

    @Override
    public boolean checkTie() {
        return false;
    }

    /**
     * Zoekt naar alle mogelijke zetten
     *
     * @param player zoekt hij de mogelijke setten
     * @return geeft de indexes terug waar op een zet gedaan kan worden.
     */

    public ArrayList<Integer> getAvailableMoves(char player) {
        ArrayList<Integer> availableMoves = new ArrayList<>();
        int size = getSize();
        char opponent;
        if (player == BLACK) {
            opponent = WHITE;
        } else {
            opponent = BLACK;
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (isValidMove((i * size + j), player, opponent)) {
                    availableMoves.add(i * size + j);
                }
            }

        }
        return availableMoves;
    }

    public boolean isValidMove(int idx, char player, char opponent) {
        if (!checkPlace(idx)) {
            return false;
        }
        int size = getSize();
        char[] gameBoard = getBoardData();

        int tmpRow = Math.floorDiv(idx, size);
        int tmpCol = idx - size * tmpRow;
        boolean isValid = false;
        for (int i = 0; i < size; i++) {
            boolean hasOpponentBetween = false;
            while (tmpRow >= 0 && tmpCol < size && tmpCol >= 0 && tmpRow < size) {
                if (gameBoard[idx] == opponent) {
                    hasOpponentBetween = true;
                } else if (gameBoard[idx] == player && hasOpponentBetween) {
                    isValid = true;
                    break;
                }
                tmpRow += OFFSET_ROW[i];
                tmpCol += OFFSET_COL[i];
            }
            if (isValid) {
                break;
            }
        }

        return isValid;
    }

    public char[] buildGameBoard() {
        int size = getSize();
        char[] gameBoard = new char[size * size];
        gameBoard[27] = BLACK;
        gameBoard[28] = WHITE;
        gameBoard[35] = WHITE;
        gameBoard[36] = BLACK;
        return gameBoard;
    }
}
