abstract public class GameModel {

    private boolean againstAi;
    private char[] gameBoard;
    private char currentPlayer;
    private char startPlayer;
    private boolean isWinner;
    private boolean isTie;
    private boolean isOnline;
    private char winner;
    private int size;
    private AI ai;

    public GameModel(int size) {
        this.size = size;
        this.gameBoard = buildGameBoard();
        this.isWinner = false;
        this.isTie = false;
        this.isOnline = false;
        this.winner = ' ';
        this.ai = new AI(size);
    }



    /**
     * Als een speler een zet heeft gedaan doet de AI eventueel daarna meteen een zet.
     * @param idx geeft de index mee waar een zet op gedaan is.
     */
    public void sets(int idx) {
        if (!isEmptyColumn(idx)){
            return;
        }
        userSet(idx);

        //Alleen als het spel nog niet geëindigd is.
        //Alleen als er tegen de AI gespeeld wordt.
        if (!isWinner && !isTie && againstAi) {
            int i = ai.aiNewSet(gameBoard, 'X');
            userSet(i);
        }
    }

    /**
     * Laat de ai een zet doen
     * @param opponent geeft mee welke speler de tegenstander is.
     * @return geeft de index terug waar de ai een zet op wil doen.
     */
    public int aiSet(char opponent){
        int i = ai.aiNewSet(gameBoard, opponent);
        userSet(i);
        return i;
    }


    /**
     * Zet de zet op het bord en handelt de vervolgstappen af.
     * @param idx geeft de index mee waar een zet op gedaan moet worden.
     */
    public void userSet(int idx) {
        //Controleert of er nog plaats is.
        if (!validMove(idx, gameBoard)){
            return;
        }
        gameBoard = move(idx, gameBoard, currentPlayer);
        isWinner = checkWinner(currentPlayer);
        if (isWinner) {
            winner = currentPlayer;
        }

        isTie = checkTie();
        //Veranderd wie er aan de beurt is.
        if(currentPlayer == 'X'){
            currentPlayer = 'O';
        }else {
            currentPlayer = 'X';
        }
    }


    /**
     * Controleert of er nog plaats is waar de nieuwe set gedaan wordt.
     * @param idx de index waar de zet op gedaan wordt.
     * @return geeft terug of er nog plaats is op het bord.
     */
    public boolean isEmptyColumn(int idx) {
        // Checkt of 'idx' buiten het speelveld valt en of het vakje al bezet is of niet
        if (idx >= 0 && idx < gameBoard.length) {
            if (gameBoard[idx] == '\u0000' || gameBoard[idx] == '-') {
                return true;
            }
        }

        return false;
    }

    abstract public char[] move(int idx, char[] currentBoard, char currentPlayer);

    abstract public boolean validMove(int idx, char[] gameBoard);

    abstract public boolean checkWinner(char player);

    abstract public boolean checkTie();



    /**
     * @return geeft de huidige bord terug.
     */
    public char[] getBoardData() {
        return gameBoard;
    }

    /**
     * Vervangt het huidige bord met de nieuwe.
     * @param newGameBoard geeft de nieuwe bord status mee
     */
    public void setGameBoard(char[] newGameBoard) {
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

    public boolean isOnline(){
        return isOnline;
    }

    public void toggleIsOnline() {
        this.isOnline = !(this.isOnline);
    }

    /**
     * @return geeft de winnaar terug.
     */
    public String getWinner() {
        return String.valueOf(winner);
    }

    abstract public char[] buildGameBoard();

    /**
     * Het resetten van het spel.
     * @param playAi geeft aan of je tegen de ai speelt
     * @param AiStart geeft aan of de ai mag beginnen
     * @param start welke speler er mag starten
     */
    public void resetGame(boolean playAi, boolean AiStart, char start) {
        gameBoard = buildGameBoard();
        currentPlayer = start;
        isWinner = false;
        isTie = false;
        againstAi = playAi;
        startPlayer = start;
        winner = ' ';
        if (AiStart && playAi){
            gameBoard[ai.aiNewSet(gameBoard, 'X')] = 'O';
        }
    }


    /**
     * @return geeft aan of je tegen ai speelt
     */
    public boolean getAgainstAi(){
        return againstAi;
    }

    /**
     * @return geeft de huidige speler terug
     */
    public char getCurrentPlayer(){
        return currentPlayer;
    }

    public void setCurrentPlayer(char currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * @return geeft terug wie er begonnen is.
     */
    public char getStartPlayer(){
        return startPlayer;
    }

    /**
     * @return geeft terug hoe groot het bord is.
     */
    public int getSize() {
        return size;
    }
}
