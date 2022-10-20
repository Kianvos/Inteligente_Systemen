import java.util.Random;

public class AI {
    private int size;
    static int evaluate(char b[], GameModel AiModel) {
        //Bekijk wie wint en geeft op basis daar van punten
        if(AiModel.checkWinner('O')) {
            return + 10;
        }
        else if(AiModel.checkWinner('X')) {
            return - 10;
        }

        //Niemand wint, dus geen punten er bij optellen
        return + 0;
    }

    static int minimax(char[] board, int depth, boolean isMax, GameModel AiModel) {
        int score = evaluate(board, AiModel);

        if (score == 10) {
            return score;
        }

        if (score == -10) {
            return score;
        }

        //Check of moves over zijn op dit bord

        if(isMax) {
            int best = -1000;

            //Als het de zet is van de maximizer
            for (int i = 0; i < board.length; i++) {
                if(board[i] == '\u0000') {
                    board[i] = 'O';
                    best = Math.max(best, minimax(board, depth + 1, !isMax, AiModel));

                    board[i] = '\u0000';
                }
            }
            return best;
        }
        else {
            int best = 1000;

            //Als het de zet is van de maximizer
            for (int i = 0; i < board.length; i++) {
                if(board[i] == '\u0000') {
                    board[i] = 'X';
                    best = Math.max(best, minimax(board, depth + 1, !isMax, AiModel));

                    board[i] = '\u0000';
                }
            }
            return best;
        }



    }


    public AI(int size) {
        this.size = size;
    }

    public int aiNewSet(char[] gameBoard, GameModel model) {
        //Create a copy of the current game model for use in evaluation
        GameModel AiModel = new GameModel(gameBoard.length);

        //Set the current board as the board for the new instance
        AiModel.setGameBoard(gameBoard);

        boolean choice = false;
        int pos = -1;
        while (!choice) {
            int posChoise = getRandom(size*size-1);
            if (gameBoard[posChoise] == '\u0000') {
                pos = posChoise;
                choice = true;
            }
        }
        return pos;
    }

    public int getRandom(int length) {
        return new Random().nextInt(length);
    }

}
