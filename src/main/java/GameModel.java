public class GameModel {

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
        this.gameBoard = new char[size * size];
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
        if (!checkPlace(idx)){
            return;
        }
        userSet(idx);

        //Alleen als het spel nog niet geeindigd is.
        //Alleen als er tegen de AI gespeeld wordt.
        if (!isWinner && !isTie && againstAi) {
            int i = ai.aiNewSet(gameBoard);
            userSet(i);
        }
    }

    /**
     * Laat de ai een zet doen
     * @return geeft de index terug waar de ai een zet op wil doen.
     */
    public int aiSet(){
        int i = ai.aiNewSet(gameBoard, this);
        userSet(i);
        return i;
    }


    /**
     * Zet de zet op het bord en handelt de vervolgstappen af.
     * @param idx geeft de index mee waar een zet op gedaan moet worden.
     */
    public void userSet(int idx) {
        //Controleert of er nog plaats is.
        if (!checkPlace(idx)) {
            return;
        }
        gameBoard[idx] = currentPlayer;
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
    public boolean checkPlace(int idx) {
        // Checkt of 'idx' buiten het speelveld valt en of het vakje al bezet is of niet
        if (idx >= 0 && idx < this.gameBoard.length) {
            if (this.gameBoard[idx] == '\u0000') {
                return true;
            }
        }

        return false;
    }

    /**
     * Checkt of de player winnaar is.
     * @param player is degene waarvoor hij controleert of er een winnaar is.
     * @return geeft terug of de speler gewonnen heeft.
     */
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

    /**
     * Checkt of het een gelijkspel is.
     * @return geeft terug of er een gelijkspel is.
     */
    public boolean checkTie() {
        // Check tie/Check of er nog plaats is op het speelveld
        for (int i = 0; i < gameBoard.length; i++) {
            if (gameBoard[i] == '\u0000') {
                return false;
            }
        }
        return true;
    }


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

    /**
     * Het resetten van het spel.
     * @param playAi geeft aan of je tegen de ai speelt
     * @param AiStart geeft aan of de ai mag beginnen
     * @param start welke speler er mag starten
     */
    public void resetGame(boolean playAi, boolean AiStart, char start) {
        gameBoard = new char[size * size];
        currentPlayer = start;
        isWinner = false;
        isTie = false;
        againstAi = playAi;
        startPlayer = start;
        winner = ' ';
        if (AiStart && playAi){
            gameBoard[ai.aiNewSet(gameBoard, this)] = 'O';
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

    /**
     * @return geeft terug wie er begonnen is.
     */
    public char getStartPlayer(){
        return currentPlayer;
    }

    /**
     * @return geeft terug hoe groot het bord is.
     */
    public int getSize() {
        return size;
    }
}
