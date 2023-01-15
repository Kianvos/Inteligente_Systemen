package Helper;

import Model.Othello;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class RandomAI {
    private final Othello othello;
    private ArrayListFile arrayListFile;
    private int opponent;
    private int AI;

    private enum GAME {
        RANDOM,
        FILE
    }

    private GAME gameStatus;
    private int count;
    private ArrayList<Integer> moves = new ArrayList<>();


    public RandomAI() {
        this.othello = new Othello();
        this.gameStatus = GAME.RANDOM;
        arrayListFile =  new ArrayListFile();
    }

    public void startGameSettings(int AI, int opponent) {
        System.out.println("AI: " + AI + " Opponent: " + opponent);
        this.AI = AI;
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

    public int AImove() throws IOException, ClassNotFoundException {
        int[] gameBoard = othello.getBoardData().clone();
        int move = randomMove(gameBoard);
        move(move, AI);
        return move;
    }

    public int randomMove(int[] gameBoard) throws IOException, ClassNotFoundException {
        int move = 0;
        if (gameStatus == GAME.RANDOM){
            Random random = new Random();
//        System.out.println("AI: " + AI);
            ArrayList<Integer> tmpMoves = othello.getAvailableMoves(gameBoard, AI);
//        System.out.println("MOVES: " + tmpMoves);
            move = tmpMoves.get(random.nextInt(tmpMoves.size()));
        } else if (gameStatus == GAME.FILE) {
            ArrayList<Integer> moves = arrayListFile.ArrayListRead("game_"+count);
            //todo idx gebruiken voor een van de moves
        }

//        System.out.println(move);
        return move;
    }

    public void resetGame() {
        othello.setGameBoard(othello.buildGameBoard());
    }

    public void writeFile() throws IOException {
        arrayListFile.writeArrayListToFile("game_"+count, moves);
        moves = new ArrayList<>();
    }

    public void addMove(int moveIdx) {
        moves.add(moveIdx);
    }

    public void setCount(int count) {
        this.count = count;
    }
}
