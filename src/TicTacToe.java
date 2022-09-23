import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;


//TODO REMOVE PUBLIC variables EN CREATE METHODS
public class TicTacToe {
    final char playerX = 'X';
    final char playerO = 'O';

    private char[] gameBoard;
    private char winner;
    private boolean gameEnded;
    private gui GUI;

    public TicTacToe(gui GUI) {
        this.GUI = GUI;
        this.gameBoard = new char[9];
        this.winner = 0;
        this.gameEnded = false;
    }

    public void startGame() {
        while (!this.gameEnded) {
            newSet();
            gameState();

            if (!this.gameEnded) {
                aiNewSet();
            }

            gameState();
            System.out.println(this);
        }
        
        if (this.winner == playerX) {
            this.GUI.updateTextField("Je hebt gewonnen.");
            System.out.println("Je hebt gewonnen.");
        } else if (this.winner == playerO) {
            this.GUI.updateTextField("Je hebt verloren");
            System.out.println("Je hebt verloren");
        } else {
            this.GUI.updateTextField("Je hebt gewonnen.");
            System.out.println("Je hebt gewonnen.");
        }
    }


    public void newSet() {
        boolean isDone = false;
        while (!isDone) {
            Thread.yield();
            if (this.GUI.getButtonPressed()) {
                this.GUI.setButtonPressed(false);
                int pos = this.GUI.getSet();
                if (checkPlace(pos)) {
                    this.gameBoard[pos] = playerX;
                    this.GUI.updateButton(pos, playerX);
                    isDone = true;
                } else {
                    System.out.println("Hier kan je hem niet plaatsen.");
                }
            }
        }
    }

    public boolean checkPlace(int pos) {

        // Checkt of 'pos' buiten het speelveld valt en of het vakje al bezet is of niet

        if (pos >= 0 && pos < this.gameBoard.length) {
            if (this.gameBoard[pos] == '\u0000') {
                return true;
            }
        }

        return false;
    }

    public void gameState() {
        if (checkWinner(playerX)) {
            this.gameEnded = true;
            this.winner = playerX;
        } else if (checkWinner(playerO)) {
            this.gameEnded = true;
            this.winner = playerO;
        } else {
            this.gameEnded = checkTie();
        }
    }

    public boolean checkWinner(int player) {

        // TODO: Checken voor de winner kan vast beter maar ik kom er op het moment niet uit

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
        // TODO: Zorg dat checkTie naar voren kijkt of er nog een mogelijkheid is om te winnen of niet

        for (int i = 0; i < gameBoard.length; i++) {
            if (gameBoard[i] == '\u0000') {
                    return false;
            }
        }

        return true;
    }

    public void aiNewSet() {
        boolean choice = false;
        while (!choice) {
            int posChoise = getRandom(8);
            if (this.gameBoard[posChoise] == '\u0000') {
                this.gameBoard[posChoise] = playerO;
                this.GUI.updateButton(posChoise, playerO);
                choice = true;
            }
        }
    }

    public int getRandom(int length) {
        return new Random().nextInt(length);
    }

    public void resetGame(){
        this.gameBoard = new char[9];
        this.winner = 0;
        this.gameEnded = false;
        this.GUI.resetBoard();
        this.startGame();
    }

    public String toString() {
        StringBuilder test = new StringBuilder();
        // String format = "%1$-3s|%2$-3s|%3$-3s";
        for (int i = 0; i < this.gameBoard.length; i++) {
                if (i % 3 == 0) {
                    test.append(" " + this.gameBoard[i] + " | " + this.gameBoard[i+ 1] + " | " + this.gameBoard[i + 2]);
                }
                
                // test.append(centerString(3, this.gameBoard[i][0])).append("|").append(centerString(3, this.gameBoard[i][1])).append("|").append(centerString(3, this.gameBoard[i][2]));
                // "|%1$-10s|%2$-10s|%3$-20s|\n";

            if (i % 3 == 0) {
                test.append("\n");
            }
        }

        return test.toString();
    }

    public static void main(String[] args) {
        int width = 450;
        gui GUI = new gui("Tic Tac Toe", width, width+150);
        TicTacToe game = new TicTacToe(GUI);
        game.startGame();
    }
}
