import java.util.Random;

public class AI {
    private int size;

    public AI(int size) {
        this.size = size;
    }

    /**
     * Geeft de score van het bord terug
     * @param AiModel Kopie van het bord
     * @return Score die wordt bepaald op basis van wie wint of gelijkspel
     */
    static int evaluate(GameModel AiModel) {

        //Als de maximizer wint, tel dan 10 op bij de score
        if(AiModel.checkWinner('O')) {
            return + 10;
        }

        //Als de minimizer wint, trek dan 10 af van de score
        else if(AiModel.checkWinner('X')) {
            return - 10;
        }

        //Niemand wint, dus geen punten er bij optellen
        return + 0;
    }

    static int minimax(GameModel AiModel, boolean isMax, int depth) {

        //Stop met puntentelling als de maximale diepte is geraakt
        if (depth <= 0) {
            return + 0;
        }

        //Controleer de score van het huidige bord
        int score = evaluate(AiModel);

        //Geef de score terug als de maximizer gewonnen heeft
        if (score == 10) {
            return score;
        }

        //Geef de score terug als de minimizer gewonnen heeft
        if (score == -10) {
            return score;
        }


        //Geef niks terug als er gelijkspel is
        if (AiModel.checkTie()) {
            return 0;
        }

        //Haal het spelbord op van deze simulatie
        char[] boardData = AiModel.getBoardData();

        int best;
        if(isMax) {
            best = -1000;

            for (int i = 0; i < boardData.length; i++) {
                if(boardData[i] == '\u0000') {
                    boardData[i] = 'O';
                    best = Math.max(best, minimax(AiModel, !isMax, depth - 1));
                    boardData[i] = '\u0000';
                }
            }
        }
        else {
            best = 1000;

            for (int i = 0; i < boardData.length; i++) {
                if(boardData[i] == '\u0000') {
                    boardData[i] = 'X';
                    best = Math.min(best, minimax(AiModel, !isMax, depth - 1));
                    boardData[i] = '\u0000';
                }
            }
        }
        return best;

    }

    static int findBestMove(GameModel AiModel) {

        char[] boardData = AiModel.getBoardData();

        int bestVal = -1000;
        int bestMove = -1;

        for (int i = 0; i < boardData.length; i++) {
            if (boardData[i] == '\u0000') {
                boardData[i] = 'O';

                int moveVal = minimax(AiModel, false, 5);
                boardData[i] = '\u0000';

                if (moveVal > bestVal) {
                    bestMove = i;
                    bestVal = moveVal;
                }
            }
        }

        return bestMove;
    }
    public int aiNewSet(char[] gameBoard) {

        //Maak een kopie van het spelbord die het algoritme kan gebruiken voor simulaties
        GameModel AiModel = new GameModel(gameBoard.length);
        AiModel.setGameBoard(gameBoard);

        //Bepaal de beste zet
        int bestMove = findBestMove(AiModel);


        boolean choice = false;
        int pos = -1;
        while (!choice) {
            int posChoise = bestMove;
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
