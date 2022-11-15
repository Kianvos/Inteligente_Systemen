
public class Model {

    private boolean againstAi;
    private char[] board;
    private char currentPlayer;
    private boolean isWinner;
    private boolean isTie;
    private boolean isOnline;
    private char winner;
    private int size;
    private AI ai;

    public Model(int size) {
        this.size = size;
        this.board = new char[size * size];
        this.isWinner = false;
        this.isTie = false;
        this.isOnline = false;
        this.winner = ' ';
        this.ai = new AI();
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

        //Alleen als het spel nog niet geëindigd is.
        //Alleen als er tegen de AI gespeeld wordt.
        if (!isWinner && !isTie && againstAi) {
            aiSet('X');
            // int i = ai.aiNewSet(board, 'X');
            // userSet(i);
        }
    }

    /**
     * Laat de ai een zet doen
     * @param opponent geeft mee welke speler de tegenstander is.
     * @return geeft de index terug waar de ai een zet op wil doen.
     */
    public int aiSet(char opponent){
        int i = ai.aiNewSet(board, opponent);
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
        board[idx] = currentPlayer;
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
        if (idx >= 0 && idx < this.board.length) {
            if (this.board[idx] == '\u0000') {
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
            if (this.board[i] == this.board[i + 3] && this.board[i + 3] == this.board[i + 6] && this.board[i] == player) {
                return true;
            }
        }
        // Check horizontal
        for (int i = 0; i < this.board.length; i++) {
            if (i % 3 == 2) {
                if (this.board[i] == this.board[i - 1] && this.board[i - 1] == this.board[i - 2] && this.board[i] == player) {
                    return true;
                }
            }
        }
        // Check diagonal
        if (this.board[0] == this.board[4] && this.board[4] == this.board[8] && this.board[0] == player) {
            return true;
        } else if (this.board[2] == this.board[4] && this.board[4] == this.board[6] && this.board[2] == player) {
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
        for (int i = 0; i < board.length; i++) {
            if (board[i] == '\u0000') {
                return false;
            }
        }
        return true;
    }


    /**
     * @return geeft de huidige bord terug.
     */
    public char[] getBoard() {
        return board;
    }

    /**
     * Vervangt het huidige bord met de nieuwe.
     * @param newBoard geeft de nieuwe bord status mee
     */
    public void setBoard(char[] newBoard) {
        board = newBoard;
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
        board = new char[size * size];
        currentPlayer = start;
        isWinner = false;
        isTie = false;
        againstAi = playAi;
        winner = ' ';
        if (AiStart && playAi){
            board[ai.aiNewSet(board, 'X')] = 'O';
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
        return currentPlayer;
    }

    /**
     * @return geeft terug hoe groot het bord is.
     */
    public int getSize() {
        return size;
    }
}
