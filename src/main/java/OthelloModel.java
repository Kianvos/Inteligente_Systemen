import java.util.ArrayList;

public class OthelloModel extends GameModel {
    private final char BLACK = 'O';
    private final char WHITE = 'X';


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
        ArrayList<Integer> availableWhite = getAvailableMoves(WHITE);
        for (Integer availableW : availableWhite) {
            System.out.println(availableW);
        }
    }


    @Override
    public boolean checkWinner(char player) {
        return false;
    }

    @Override
    public boolean checkTie() {
        return false;
    }

    public char[] move(int idx, char[] currentBoard, char currentPlayer) {
        char tmp = 'X';
        if (currentPlayer == 'X') {
            tmp = 'O';
        }
        currentBoard[idx] = currentPlayer;
        for (int i = 0; i < currentBoard.length; i++) {
            if (currentBoard[i] == '-') {
                currentBoard[i] = '\u0000';
            }
        }
        moveT(idx, currentBoard, currentPlayer);
        ArrayList<Integer> availableMoves = getAvailableMoves(tmp);
        for (Integer availableMove : availableMoves) {
            currentBoard[availableMove] = '-';
        }

        return currentBoard;
    }

    public char[] moveT(int idx, char[] gameBoard, char player) {
        int size = getSize();
        int row = Math.floorDiv(idx, size);
        int col = (idx - size * row);
        for (int i = 0; i < size; i++) {
            int tmpRow = row + OFFSET_ROW[i];
            int tmpCol = col + OFFSET_COL[i];
            boolean hasOpponentBetween = false;

            while (tmpRow >= 0 && tmpRow < size && tmpCol >= 0 && tmpCol < size) {
                int tmpIdx = tmpCol + tmpRow * size;
                if (isEmptyColumn(tmpIdx)) {
                    break;
                }
                if (gameBoard[tmpIdx] == player){
                    hasOpponentBetween = true;
                }
                if(gameBoard[tmpIdx] == player && hasOpponentBetween){
                    int effectRow = row + OFFSET_ROW[i];
                    int effectCol = col + OFFSET_COL[i];
                    int effectIdx = effectCol + effectRow * size;
                    while (effectRow != tmpRow || effectCol != tmpCol)
                    {
//                        Reversi.getInstance().setEffectedPiece(effectPieceRow, effectPieceCol);
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

    public boolean validMove(int idx) {
        char opponent = 'X';
        if (getCurrentPlayer() == 'X') {
            opponent = 'O';
        }
        return isValidMove(idx, getCurrentPlayer(), opponent);
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
                int idx = i * size + j;
                if (isValidMove(idx, player, opponent)) {
                    availableMoves.add(idx);
                }
            }

        }
        return availableMoves;
    }

    public boolean isValidMove(int idx, char player, char opponent) {
        if (!isEmptyColumn(idx)) {
            return false;
        }

        //26 en 37
        int size = getSize();
        char[] gameBoard = getBoardData();
        int row = Math.floorDiv(idx, size);
        int col = (idx - size * row);
//        System.out.println("Row: " + row + " col: "+ col);
        for (int i = 0; i < size; i++) {
            int tmpRow = row + OFFSET_ROW[i];
            int tmpCol = col + OFFSET_COL[i];

            boolean hasOpponentBetween = false;
            while (tmpRow >= 0 && tmpRow < size && tmpCol >= 0 && tmpCol < size) {
//                System.out.println("TMProw: " + tmpRow + " TMProw: "+ tmpCol);
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
