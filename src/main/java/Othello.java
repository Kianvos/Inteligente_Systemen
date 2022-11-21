import java.util.ArrayList;

public class Othello extends Model {
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

    public Othello() {
        super(8);
    }


    //todo controleer of speler nog minstens 1 steen hebben
    //todo controleer wie er aan het eind van het spel de meeste blokjes te hebben
    //todo getAvailableMoves werkt nog niet helemaal zoals het moet, voornamelijk isValidMove naar kijken
    //todo tussenliggende items in zet doet het nog niet helemaal.

    @Override
    public boolean checkWinner(char player) {
        char[] gameBoard = getBoardData();
        int currentPlayer = 0;
        int opponent = 0;
        ArrayList<Integer> X = this.getAvailableMoves(gameBoard, 'X');
        ArrayList<Integer> O = this.getAvailableMoves(gameBoard, 'O');

        if(X.isEmpty() && O.isEmpty()){
            for (char c:gameBoard
                 ) {
                if(c == player){
                    currentPlayer++;
                }
                if(c != player && c != '\u0000'){
                    opponent++;
                }
            }
            return currentPlayer > opponent;
        }
        return false;
    }

    @Override
    public boolean checkTie() {
        char[] gameBoard = getBoardData();
        int currentPlayer = 0;
        int opponent = 0;
        ArrayList<Integer> X = this.getAvailableMoves(gameBoard, 'X');
        ArrayList<Integer> O = this.getAvailableMoves(gameBoard, 'O');

        if(X.isEmpty() && O.isEmpty()){
            for (char c:gameBoard
            ) {
                if(c == 'X'){
                    currentPlayer++;
                }
                if(c == 'O'){
                    opponent++;
                }
            }
            return currentPlayer == opponent;
        }
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
        currentBoard =  moveColBetweeen(idx, currentBoard, currentPlayer);
        getAvailableMoves(currentBoard, tmp);

        return currentBoard;
    }

    public char[] moveColBetweeen(int idx, char[] gameBoard, char player) {
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
                    while (effectRow != tmpRow || effectCol != tmpCol)
                    {
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

    public boolean validMove(int idx, char[] gameBoard) {
        char opponent = 'X';
        if (getCurrentPlayer() == 'X') {
            opponent = 'O';
        }
        return isValidMove(idx, getCurrentPlayer(), opponent, gameBoard);
    }

    /**
     * Zoekt naar alle mogelijke zetten
     *
     * @param player zoekt hij de mogelijke setten
     * @return geeft de indexes terug waar op een zet gedaan kan worden.
     */

    public ArrayList<Integer> getAvailableMoves(char[] gameBoard, char player) {
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
                if (isValidMove(idx, player, opponent, gameBoard)) {
                    availableMoves.add(idx);
                    gameBoard[idx] = '-';
                }
            }

        }
        return availableMoves;
    }

    public boolean isValidMove(int idx, char player, char opponent, char[] gameBoard) {
        if (!isEmptyColumn(idx)) {
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
