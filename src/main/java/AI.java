
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class AI {

    private final int[] upLeftSide = {0, 1, 2, 3, 4, 5, 6, 7, 8, 16, 24, 32, 40, 48, 56};
    private final int[] upRightSide = {0, 1, 2, 3, 4, 5, 6, 7, 15, 23, 31, 39, 47, 55, 63};
    private final int[] lowLeftSide = {0, 8, 16, 24, 32, 40, 48, 56, 57, 58, 59, 60, 61, 62, 63};
    private final int[] lowRightSide = {7, 15, 23, 31, 39, 47, 55, 56, 57, 58, 59, 60, 61, 62, 63};
    private final int[] aroundCorner = {1, 6, 8, 9, 14, 15, 48, 49, 54, 55, 57, 62};

    private int[] boardScore = {
            100, -10, 5, 2, 2, 5, -10, 100,
            -10, -20, 1, 1, 1, 1, -20, -10,
            5, 1, 1, 1, 1, 1, 1, 5,
            2, 1, 1, 0, 0, 1, 1, 2,
            2, 1, 1, 0, 0, 1, 1, 2,
            5, 1, 1, 1, 1, 1, 1, 5,
            -10, -20, 1, 1, 1, 1, -20, -10,
            100, -10, 5, 2, 2, 5, -10, 100,
    };

    //int score = 0;
    //        int[] boardData = AiModel.getBoardData();
    //        for (int i = 0; i < boardData.length; i++) {
    //            if (boardData[i] == player) {
    //                score += boardScore[i];
    //            } else if (boardData[i] == opponent) {
    //                score -= boardScore[i];
    //            }
    //        }
    //        return score;

    /**
     * Controleert het bord of de maximizer of minimizer heeft gewonnen
     *
     * @param AiModel Kopie van het echte spelbord waar de experimentele zetten op gedaan worden
     * @return score van de zet
     */
    private int evaluate(Model AiModel, int opponent, int player) {
        //Als de maximizer wint, tel dan 10 op bij de score
        if (AiModel.checkWinner() != 0 && AiModel.checkWinner() == player) {
            return +100;
        }

        //Als de minimizer wint, trek dan 10 af van de score
        else if (AiModel.checkWinner() != 0 && AiModel.checkWinner() == opponent) {
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
        //Haal het spelbord op waarmee de functie is aangeroepen
        int[] boardData = AiModel.getBoardData();

        //Stop met puntentelling als de maximale diepte is bereikt
        if (depth <= 0) {
            return heuristics(boardData, player, opponent);
        }
        //Controleer de score van de zet waarmee de functie is aangeroepen
//        int score = evaluate(AiModel, opponent, player);
        //Geef de score terug als de maximizer of minimizer gewonnen heeft
//        if (score != 0) {
//            System.out.println("HASD");
//            return score;
//        }

        //Geef niks terug als er gelijkspel is
//        if (AiModel.checkTie()) {
//            return +0;
//        }

        ArrayList<Integer> moves = isMax ? AiModel.getAvailableMoves(boardData, player) : AiModel.getAvailableMoves(boardData, opponent);

        //Bepaal de hoogst mogelijke score voor de maximizer en minimizer
        int best = isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;
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

        return moves.isEmpty() ? winHeuristics(boardData, player, opponent) : best;
    }

    private int winHeuristics(int[] boardData, int player, int opponent) {
        int playerScore = countScore(boardData, player);
        int opponentScore = countScore(boardData, opponent);
        if (playerScore > opponentScore) {
            return 1000 * (playerScore - opponentScore);
        }
        return -200000;
    }

    private int heuristics(int[] boardData, int player, int opponent) {
        int playerScore = countScore(boardData, player);
        int opponentScore = countScore(boardData, opponent);
        int score = opponentScore - playerScore;
        score += CornerSideHeuristics(boardData, player);
        score -= CornerSideHeuristics(boardData, opponent);
        int count = 0;
        for (int i = 0; i < aroundCorner.length; i++) {
            if (boardData[aroundCorner[i]] == opponent) {
                count += 1;
            }
        }
        score += count << 2;
        return score;
    }

    private int CornerSideHeuristics(int[] boardData, int disc) {
        int score = 0;
        if (boardData[0] == disc) {
            score += 50;
            int count = 0;
            for (int i = 1; i < upLeftSide.length; i++) {
                if (boardData[upLeftSide[i]] == disc) {
                    count++;
                }
            }
            score += count << 2;
        }
        if (boardData[7] == disc) {
            score += 50;
            int count = 0;
            for (int i = 1; i < upRightSide.length; i++) {
                if (boardData[upRightSide[i]] == disc) {
                    count++;
                }
            }
            score += count << 2;
        }
        if (boardData[56] == disc) {
            score += 50;
            int count = 0;
            for (int i = 1; i < lowLeftSide.length; i++) {
                if (boardData[lowLeftSide[i]] == disc) {
                    count++;
                }
            }
            score += count << 2;
        }
        if (boardData[63] == disc) {
            score += 50;
            int count = 0;
            for (int i = 1; i < lowRightSide.length; i++) {
                if (boardData[lowRightSide[i]] == disc) {
                    count++;
                }
            }
            score += count << 2;
        }
        return score;
    }

    private int countScore(int[] boardData, int disc) {
        int count = 0;
        for (int i = 0; i < boardData.length; i++) {
            if (boardData[i] == disc) {
                count++;
            }
        }
        return count;
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
        int bestVal = Integer.MIN_VALUE;
        int bestMove = -1;
        int player = 1;
        player = (opponent == 1) ? 2 : 1;

        ArrayList<Integer> moves = AiModel.getAvailableMoves(boardData, player);

        for (int move : moves) {
            //Doe een zet als de maximizer
            boardData[move] = player;

            if (evaluate(AiModel, opponent, player) == 100) {
                System.out.println("Exited because of evaluation at move " + move);
                boardData[move] = 0;
                return move;
            }

            //Bepaal de score van de zet door een zet te doen als de minimizer
            int moveVal = minimax(AiModel, false, 7, Integer.MIN_VALUE, Integer.MAX_VALUE, opponent, player);

            //Maak het vakje van deze zet weer leeg om andere zetten toe te staan als het bord veranderd is
            boardData[move] = 0;

            //Als de score van de zet groter is dan de hoogst mogelijk score, kies dan deze zet op het echte bord
//            System.out.println(moveVal + " " + bestMove);
            if (moveVal > bestVal) {
//                System.out.println("New best: " + moveVal + " " + bestMove);
                bestMove = move;
                bestVal = moveVal;
            }
        }

//        System.out.println("Final bestMove: " + bestVal + " " + bestMove);
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
        boolean isOthello = model instanceof Othello;

        if (!isOthello) {
            int[] tttScore = {1, 0, 1, 0, 100, 0, 1, 0, 1};
            this.boardScore = tttScore;
        }

        //Maak een kopie van het spelbord die het algoritme kan gebruiken voor simulaties
        Model AiModel = isOthello ? new Othello() : new TicTacToe();
        AiModel.setGameBoard(gameBoard);

        long startTime = System.nanoTime();
        //Bepaal de zet met hoogste score, dus de zet die de grootste kans heeft om een overwinning op te leveren
        int bestMove = findBestMove(AiModel, opponent);

        int pos = -1;
        int player = 1;
        player = (opponent == 1) ? 2 : 1;

        ArrayList<Integer> valid = AiModel.getAvailableMoves(gameBoard, player);

        if (valid.contains(bestMove)) {
            pos = bestMove;
        }
        long endtime = System.nanoTime();
//        System.out.println((double) (endtime - startTime) / 1000000000);

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
