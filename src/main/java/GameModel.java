public class GameModel {

    final char PLAYER = 'X';
    final char AI = 'O';
    private char[] gameBoard;
    private char currentPlayer;
    private boolean isWinner;
    private boolean isTie;
    private boolean isOnline;
    private char winner;
    private int size;
    private AI ai;

    public GameModel(int size) {
        this.size = size;
        this.gameBoard = new char[size * size];
        this.currentPlayer = PLAYER;
        this.isWinner = false;
        this.isTie = false;
        this.isOnline = false;
        this.winner = ' ';
        this.ai = new AI(size);
    }

    public void sets(int idx) {
        if (!checkPlace(idx)){
            return;
        }
        userSet(idx);
        if (!isWinner && !isTie) {
            aiSet();
        }
    }

    public int aiSet(){
        int i = ai.aiNewSet(gameBoard, this);
        userSet(i);
        return i;
    }

    public void userSet(int idx) {
        if (!checkPlace(idx)) {
            return;
        }
        gameBoard[idx] = currentPlayer;
        isWinner = checkWinner(currentPlayer);
        if (isWinner) {
            winner = currentPlayer;
        }
        isTie = checkTie();
        if(currentPlayer == PLAYER){
            currentPlayer = AI;
        }else {
            currentPlayer = PLAYER;
        }
    }

    public boolean checkPlace(int idx) {
        // Checkt of 'idx' buiten het speelveld valt en of het vakje al bezet is of niet
        if (idx >= 0 && idx < this.gameBoard.length) {
            if (this.gameBoard[idx] == '\u0000') {
                return true;
            }
        }

        return false;
    }

    public boolean checkWinner(char player) {
        // Check vertical
        for (int i = 0; i < 3; i++) {
            if (this.gameBoard[i] == this.gameBoard[i + 3] && this.gameBoard[i + 3] == this.gameBoard[i + 6] && this.gameBoard[i] == player) {
                return true;
            }
        }
        // Check horizontal
        for (int i = 0; i < this.gameBoard.length; i++) {
            if (i % 3 == 2) {
                if (this.gameBoard[i] == this.gameBoard[i - 1] && this.gameBoard[i - 1] == this.gameBoard[i - 2] && this.gameBoard[i] == player) {
                    return true;
                }
            }
        }
        // Check diagonal
        if (this.gameBoard[0] == this.gameBoard[4] && this.gameBoard[4] == this.gameBoard[8] && this.gameBoard[0] == player) {
            return true;
        } else if (this.gameBoard[2] == this.gameBoard[4] && this.gameBoard[4] == this.gameBoard[6] && this.gameBoard[2] == player) {
            return true;
        }
        return false;
    }

    public boolean checkTie() {
        // Check tie/Check of er nog plaats is op het speelveld
        for (int i = 0; i < gameBoard.length; i++) {
            if (gameBoard[i] == '\u0000') {
                return false;
            }
        }
        return true;
    }

    public char[] getBoardData() {
        return gameBoard;
    }

    public void setGameBoard(char[] newGameBoard) {
        gameBoard = newGameBoard;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public boolean isTie() {
        return isTie;
    }

    public boolean isOnline(){
        return isOnline;
    }

    public void toggleIsOnline() {
        this.isOnline = !(this.isOnline);
    }

    public String getWinner() {
        return String.valueOf(winner);
    }

    public void resetGame(boolean AiStart) {
        gameBoard = new char[size * size];
        currentPlayer = PLAYER;
        isWinner = false;
        isTie = false;
        winner = ' ';
        if (AiStart){
            gameBoard[ai.aiNewSet(gameBoard, this)] = AI;
        }
    }

    public int getSize() {
        return size;
    }

    public void quitGame() {
        System.exit(0);
    }
}
