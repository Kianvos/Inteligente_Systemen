import java.util.ArrayList;

abstract public class Model {

    private boolean againstAi;
    private int[] gameBoard;
    private int currentPlayer;
    private int startPlayer;
    private boolean isWinner;
    private boolean isTie;
    private boolean isOnline;
    private int winner;
    private int size;
    private AI ai;

    private final int EMPTY = 0;

    private final int PLAYER_ONE = 1;

    private final int PLAYER_TWO = 2;

    private final int SUGGESTED = 3;


    public Model(int size) {
        this.size = size;
        this.currentPlayer = PLAYER_ONE;
        this.gameBoard = buildGameBoard();
        this.isWinner = false;
        this.isTie = false;
        this.isOnline = false;
        this.winner = EMPTY;
        this.ai = new AI();
    }

    /**
     * Als een speler een zet heeft gedaan doet de AI eventueel daarna meteen een zet.
     *
     * @param idx geeft de index mee waar een zet op gedaan is.
     */
    public void sets(int idx) {
        if (!isEmptyColumn(idx, gameBoard)) {
            return;
        }
        userSet(idx);

        //Alleen als het spel nog niet geëindigd is.
        //Alleen als er tegen de AI gespeeld wordt.
        if (!isWinner && !isTie && againstAi) {
            aiSet(PLAYER_ONE);
        }
    }

    /**
     * Laat de ai een zet doen
     *
     * @param opponent geeft mee welke speler de tegenstander is.
     * @return geeft de index terug waar de ai een zet op wil doen.
     */
    public int aiSet(int opponent) {
        int i = ai.aiNewSet(gameBoard, opponent, this);
        userSet(i);
        return i;
    }


    /**
     * Zet de zet op het bord en handelt de vervolgstappen af.
     *
     * @param idx geeft de index mee waar een zet op gedaan moet worden.
     */
    public void userSet(int idx) {
        //Controleert of er nog plaats is.
        if (!validMove(idx, gameBoard)) {
            return;
        }
        gameBoard = move(idx, gameBoard, currentPlayer);
        if (isFinished()) {
            winner = checkWinner();
            if (winner == PLAYER_ONE || winner == PLAYER_TWO) {
                isWinner = true;
            }
        }

        isTie = checkTie();
        //Veranderd wie er aan de beurt is.
        changeTurn();

        if (!availabeMovePlayer()) {
            changeTurn();
        }
    }


    public void changeTurn() {
        currentPlayer = (currentPlayer == PLAYER_ONE) ? PLAYER_TWO : PLAYER_ONE;
    }

    /**
     * Controleert of er nog plaats is waar de nieuwe set gedaan wordt.
     *
     * @param idx de index waar de zet op gedaan wordt.
     * @return geeft terug of er nog plaats is op het bord.
     */
    public boolean isEmptyColumn(int idx, int[] gameBoard) {
        // Checkt of 'idx' buiten het speelveld valt en of het vakje al bezet is of niet
        if (idx >= 0 && idx < gameBoard.length) {
            if (gameBoard[idx] == EMPTY || gameBoard[idx] == SUGGESTED) {
                return true;
            }
        }

        return false;
    }

    abstract public int[] move(int idx, int[] currentBoard, int currentPlayer);
    abstract public boolean validMove(int idx, int[] gameBoard);
    abstract public ArrayList<Integer> getAvailableMoves(int[] gameBoard, int player);
    abstract public boolean availabeMovePlayer();

    abstract public int checkWinner();
    abstract public boolean checkTie();
    abstract public boolean isFinished();


    /**
     * @return geeft de huidige bord terug.
     */
    public int[] getBoardData() {
        return gameBoard;
    }

    /**
     * Vervangt het huidige bord met de nieuwe.
     *
     * @param newGameBoard geeft de nieuwe bord status mee
     */
    public void setGameBoard(int[] newGameBoard) {
        gameBoard = newGameBoard;
    }

    /**
     * @return geeft terug of er een winnaar is.
     */
    public boolean isWinner() {
        return isWinner;
    }

    /**
     * @return geeft terug of er een gelijkspel is.
     */
    public boolean isTie() {
        return isTie;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void toggleIsOnline() {
        this.isOnline = !(this.isOnline);
    }

    /**
     * @return geeft de winnaar terug.
     */
    public int getWinner() {
        return winner;
    }

    /**
     * @return geeft de winnaar in een string terug.
     */
    abstract public String getStringWinner();
    abstract public int[] buildGameBoard();

    /**
     * Het resetten van het spel.
     *
     * @param playAi  geeft aan of je tegen de ai speelt
     * @param AiStart geeft aan of de ai mag beginnen
     * @param start   welke speler er mag starten
     */
    public void resetGame(boolean playAi, boolean AiStart, int start) {
        currentPlayer = start;
        gameBoard = buildGameBoard();
        isWinner = false;
        isTie = false;
        againstAi = playAi;
        startPlayer = start;
        winner = EMPTY;
        if (AiStart && playAi) {
            gameBoard[ai.aiNewSet(gameBoard, PLAYER_ONE, this)] = PLAYER_TWO;
        }
    }


    /**
     * @return geeft aan of je tegen ai speelt
     */
    public boolean getAgainstAi() {
        return againstAi;
    }

    /**
     * @return geeft de huidige speler terug
     */
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    abstract char getCurrentPlayerChar();

    /**
     * @return geeft terug wie er begonnen is.
     */
    public int getStartPlayer() {
        return startPlayer;
    }

    /**
     * @return geeft terug hoe groot het bord is.
     */
    public int getSize() {
        return size;
    }
}
