import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;


//TODO REMOVE PUBLIC variables EN CREATE METHODS
public class TicTacToe implements ActionListener {
    final char playerX = 'X';
    final char playerO = 'O';

    private boolean buttonPressed;
    private int set;
    private char[][] gameBoard;
    private char winner;
    private boolean gameEnded;
    private gui GUI;

    public TicTacToe(gui GUI) {
        this.GUI = GUI;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.GUI.button[i][j].addActionListener(this);
            }
        }
        this.gameBoard = new char[3][3];
        this.winner = 0;
        this.gameEnded = false;
        this.buttonPressed = false;
        this.set = 0;
    }

    public void startGame() {
        while (!this.gameEnded) {
            newSet();
            gameState();
            if (!this.gameEnded) {
                aiNewSet();
            }
            gameState();
            System.out.println(this.toString());
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
        do {
            Thread.yield();
            if (this.buttonPressed) {
                this.buttonPressed = false;
                int row = this.set / 3;
                int col = this.set % 3;
                System.out.println(row + " " + col);
                System.out.println(row + " " + col);
                if (checkPlace(row, col)) {
                    this.gameBoard[row][col] = playerX;
                    this.GUI.updateButton(row, col, playerX);
                    isDone = true;
                } else {
                    System.out.println("Hier kan je hem niet plaatsen.");
                }
            }
        } while (!isDone);

    }

    public boolean checkPlace(int row, int col) {
        //Als het buiten het speelveld valt
        if (row > 2 || row < 0 || col > 2 || col < 0) {
            return false;
        }
        if (this.gameBoard[row][col] == '\u0000') {
            return true;
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
        //Check horizontal
        for (int i = 0; i < gameBoard.length; i++) {
            if (gameBoard[i][0] == gameBoard[i][1] && gameBoard[i][1] == gameBoard[i][2] && gameBoard[i][0] == player) {
                return true;
            } else if (gameBoard[0][i] == gameBoard[1][i] && gameBoard[1][i] == gameBoard[2][i] && gameBoard[0][i] == player) {
                return true;
            }
        }
        if (gameBoard[0][0] == gameBoard[1][1] && gameBoard[1][1] == gameBoard[2][2] && gameBoard[0][0] == player) {
            return true;
        } else if (gameBoard[0][2] == gameBoard[1][1] && gameBoard[1][1] == gameBoard[2][0] && gameBoard[0][2] == player) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkTie() {
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                if (gameBoard[i][j] == '\u0000') {
                    return false;
                }
            }
        }
        return true;
    }

    public void aiNewSet() {
        boolean choice = false;
        while (!choice) {
            int rowChoice = getRandom(3);
            int colChoice = getRandom(3);
            if (this.gameBoard[rowChoice][colChoice] == '\u0000') {
                this.gameBoard[rowChoice][colChoice] = playerO;
                this.GUI.updateButton(rowChoice, colChoice, playerO);
                choice = true;
            }
        }
    }

    public int getRandom(int length) {
        return new Random().nextInt(length);
    }

    public String toString() {
        StringBuilder test = new StringBuilder();
        String format = "%1$-3s|%2$-3s|%3$-3s";
        for (int i = 0; i < this.gameBoard.length; i++) {
            test.append(" " + this.gameBoard[i][0] + " | " + this.gameBoard[i][1] + " | " + this.gameBoard[i][2]);
//            test.append(centerString(3, this.gameBoard[i][0])).append("|").append(centerString(3, this.gameBoard[i][1])).append("|").append(centerString(3, this.gameBoard[i][2]));
            //"|%1$-10s|%2$-10s|%3$-20s|\n";
            if (i != this.gameBoard.length - 1) {
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

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (e.getSource() == this.GUI.button[i][j]) {
                    this.buttonPressed = true;
                    this.set = i * 3 + j;
                }
            }
        }
    }
}
