package Helper;

import Model.Othello;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class RandomAI {
    private final Othello othello;
    private ArrayListFile arrayListFile;
    private int gameIndex;
    private int opponent;
    private int AI;

    private enum GAME {
        RANDOM,
        FILE
    }

    private GAME gameStatus;
    private int count;
    private ArrayList<Integer> moves = new ArrayList<>();

    boolean reset;


    public RandomAI() {
        this.othello = new Othello();
//        this.gameStatus = GAME.RANDOM;
        this.gameStatus = GAME.RANDOM;
        // Start op 1 omdat index 0 is de int die de player represent
        this.gameIndex = 1;
        this.reset = true;
        arrayListFile = new ArrayListFile("./data/");
    }

    public void startGameSettings(int AI, int opponent) throws IOException, ClassNotFoundException {
        System.out.println("AI: " + AI + " Opponent: " + opponent);
        this.AI = AI;
        // Start op 1 omdat index 0 is de int die de player represent
        this.gameIndex = 1;

        if (gameStatus == GAME.RANDOM){
            this.moves.add(this.AI);
        }else if (gameStatus == GAME.FILE){
            moves = arrayListFile.ArrayListRead("game_"+(count+1));
        }
        this.opponent = opponent;
    }

    public int getOpponent() {
        return opponent;
    }

    public void move(int idx, int player) {
        int[] gameBoard = othello.getBoardData().clone();
        int[] newGameBoard = othello.move(idx, gameBoard, player).clone();
        othello.setGameBoard(newGameBoard);
        othello.changeTurn();
    }

    public int AImove() {
        int[] gameBoard = othello.getBoardData().clone();
        int move = randomMove(gameBoard);
        move(move, AI);
        this.gameIndex += 1;
        return move;
    }

    public int randomMove(int[] gameBoard) {
        int move = 0;
        if (gameStatus == GAME.RANDOM){
            Random random = new Random();
//        System.out.println("AI: " + AI);
            ArrayList<Integer> tmpMoves = othello.getAvailableMoves(gameBoard, AI);
//        System.out.println("MOVES: " + tmpMoves);
            move = tmpMoves.get(random.nextInt(tmpMoves.size()));
        } else if (gameStatus == GAME.FILE) {
//            ArrayList<Integer> moves = arrayListFile.ArrayListRead("game_"+(count+1));
            // idx gebruiken voor een van de moves
            move = moves.get(this.gameIndex);
            System.out.println(move);
//            System.out.println("Loaded move: " + move + " From index: " + this.gameIndex);
        }

//        System.out.println(move);
        return move;
    }

    public void resetGame() {
        othello.setGameBoard(othello.buildGameBoard());
    }

    public void writeFile() throws IOException {
        System.out.println(moves +  " Moves");
        arrayListFile.writeArrayListToFile("game_"+count, moves);
        moves = new ArrayList<>();
    }

    public void addMove(int moveIdx) {
        moves.add(moveIdx);
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int Ai() { return this.AI; }

    public boolean isFile() { return this.gameStatus == GAME.FILE; }
}
