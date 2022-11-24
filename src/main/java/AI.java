
import java.util.ArrayList;

public class AI {

    private final int[] boardScore = {
            100, -1, 5, 2, 2, 5, -1, 100,
            -1, -10, 1, 1, 1, 1, -10, -1,
            5, 1, 1, 1, 1, 1, 1, 5,
            2, 1, 1, 0, 0, 1, 1, 2,
            2, 1, 1, 0, 0, 1, 1, 2,
            5, 1, 1, 1, 1, 1, 1, 5,
            -1, -10, 1, 1, 1, 1, -10, -1,
            100, -1, 5, 2, 2, 5, -1, 100,
    };

    /**
     * Controleert het bord of de maximizer of minimizer heeft gewonnen
     *
     * @param AiModel Kopie van het echte spelbord waar de experimentele zetten op gedaan worden
     * @return score van de zet
     */
    private int evaluate(Model AiModel, int opponent, int player) {
        //Als de maximizer wint, tel dan 10 op bij de score
        if (AiModel.checkWinner() != 0 && AiModel.checkWinner() != opponent) {
            return +100;
        }

        //Als de minimizer wint, trek dan 10 af van de score
        else if (AiModel.checkWinner() != 0 && AiModel.checkWinner() != player) {
            return -100;
        }

        //Niemand wint, dus geen punten er bij optellen
        return +0;
    }

    /**
     * Bepaalt de score van een zet om te bepalen wat de beste zet is.
     * De score wordt bepaald door AI (de maximizer) en de tegenstander (de minimizer)
     * omzetbeurten een zet te laten doen op elk leeg vakje op het spelbord. Elke keer als deze
     * functie wordt aangeroepen, wordt er eerste gekeken welke speler wint en geeft op basis
     * daarvan punten terug.
     *
     * @param AiModel  Kopie van het echte spelbord waar de experimentele zetten op gedaan worden
     * @param isMax    Of de maximizer aan de beurt is of niet
     * @param depth    Tot hoe diep het algoritme experimentele zetten mag doen
     * @param opponent geeft mee welke speler de tegenstander is.
     * @return De score van het beste mogelijke zet
     */
    private int minimax(Model AiModel, boolean isMax, int depth, int a, int b, int opponent, int player) {
        //Stop met puntentelling als de maximale diepte is bereikt
        if (depth <= 0) {
            return +0;
        }

        //Controleer de score van de zet waarmee de functie is aangeroepen
        int score = evaluate(AiModel, opponent, player);
        //Geef de score terug als de maximizer of minimizer gewonnen heeft
        if (score != 0) {
            return score;
        }

        //Geef niks terug als er gelijkspel is
        if (AiModel.checkTie()) {
            return +0;
        }

        //Haal het spelbord op waarmee de functie is aangeroepen
        int[] boardData = AiModel.getBoardData();
        ArrayList<Integer> moves = isMax ? AiModel.getAvailableMoves(boardData, player) : AiModel.getAvailableMoves(boardData, opponent);

        //Bepaal de hoogst mogelijke score voor de maximizer en minimizer
        int best = isMax ? -10000 : 10000;

        for (int move : moves) {
            if (isMax) {
                //Doe een zet als de maximizer
                boardData[move] = player;

                //Bepaal de beste score door de functie opnieuw aan te roepen en een zet te doen als de minimizer
                best = Math.max(best, minimax(AiModel, false, depth - 1, a, b, opponent, player));

                if (best >= b) {
                    boardData[move] = 0;
                    break;
                }

                a = Math.max(a, best);
            } else {
                //Doe een zet als de minimizer
                boardData[move] = opponent;

                //Bepaal de beste score door de functie opnieuw aan te roepen en een zet te doen als de maximizer
                best = Math.min(best, minimax(AiModel, true, depth - 1, a, b, opponent, player));

                if (best <= a) {
                    boardData[move] = 0;
                    break;
                }

                b = Math.min(b, best);
            }

            boardData[move] = 0;
        }

        return best;

    }

    /**
     * Geeft een lege positie op het bord terug waarbij de kans het grootste
     * is dat het een overwinning oplevert
     *
     * @param AiModel  Kopie van het echte spelbord waar de experimentele zetten op gedaan worden
     * @param opponent geeft mee welke speler de tegenstander is.
     * @return Lege positie op het bord
     */
    private int findBestMove(Model AiModel, int opponent) {
        int[] boardData = AiModel.getBoardData();

        //Bepaal de hoogst mogelijke score die een bord zou kunnen hebben voor de minimizer
        int bestVal = -1000;
        int bestMove = -1;
        int player = 1;
        player = (opponent == 1) ? 2 : 1;

        ArrayList<Integer> moves = AiModel.getAvailableMoves(boardData, player);

        for (int move : moves) {
            //Doe een zet als de maximizer
            boardData[move] = player;

            //Bepaal de score van de zet door een zet te doen als de minimizer
            int moveVal = minimax(AiModel, false, 7, -1000, 1000, opponent, player);

            moveVal += boardScore[move];

            //Maak het vakje van deze zet weer leeg om andere zetten toe te staan als het bord veranderd is
            boardData[move] = 0;

            //Als de score van de zet groter is dan de hoogst mogelijk score, kies dan deze zet op het echte bord
            if (moveVal > bestVal) {
                bestMove = move;
                bestVal = moveVal;
            }
        }

        return bestMove;
    }

    /**
     * Laat de AI een zet doen
     *
     * @param gameBoard Bord van het huidige potje
     * @param opponent  geeft mee welke speler de tegenstander is.
     * @return Een lege positie waar de zet opgedaan word
     */
    public int aiNewSet(int[] gameBoard, int opponent, Model model) {
        //Maak een kopie van het spelbord die het algoritme kan gebruiken voor simulaties
        Model AiModel = (model instanceof Othello) ? new Othello() : new TicTacToe();
        AiModel.setGameBoard(gameBoard);

        //Bepaal de zet met hoogste score, dus de zet die de grootste kans heeft om een overwinning op te leveren
        int bestMove = findBestMove(AiModel, opponent);

        int pos = -1;
        int player = 1;
        player = (opponent == 1) ? 2 : 1;

        ArrayList<Integer> valid = AiModel.getAvailableMoves(gameBoard, player);

        if (valid.contains(bestMove)) {
            pos = bestMove;
        }

        // while (!choice) {
        //     int posChoise = bestMove;

        //     //Controleer of het vakje niet bezet is
        //     if (gameBoard[posChoise] == 0) {
        //         pos = posChoise;
        //         choice = true;
        //     }
        // }

        return pos;
    }
}
