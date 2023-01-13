package Helper;

import Model.Othello;

import java.util.ArrayList;
import java.util.Random;

public class RandomAI {
    private final Othello othello;
    private int opponent;
    private int AI;


    public RandomAI() {
        this.othello = new Othello();
    }

    public void startGameSettings(int AI, int opponent){
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

    public int AImove() {
        int[] gameBoard = othello.getBoardData().clone();
        int move = randomMove(gameBoard);
        move(move, AI);
        return move;
    }

    public int randomMove(int[] gameBoard) {
        Random random = new Random();
//        System.out.println("AI: " + AI);
        ArrayList<Integer> tmpMoves = othello.getAvailableMoves(gameBoard, AI);
//        System.out.println("MOVES: " + tmpMoves);
        int move = tmpMoves.get(random.nextInt(tmpMoves.size()));
//        System.out.println(move);
        return move;
    }

    public void resetGame(){
        othello.setGameBoard(othello.buildGameBoard());
    }


}
