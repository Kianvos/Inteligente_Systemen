import java.util.ArrayList;
import java.util.Arrays;

//https://courses.cs.washington.edu/courses/cse573/04au/Project/mini1/RUSSIA/Final_Paper.pdf
public class AI {
    private final int[] cornersIdx = {0, 7, 56, 63};

    private enum GamePhase {
        EARLY_GAME,
        MID_GAME,
        END_GAME
    }

    /**
     * Geeft een lege positie op het bord terug waarbij de kans het grootste
     * is dat het een overwinning oplevert
     * /**
     * Laat de AI een zet doen
     *
     * @param gameBoard Bord van het huidige potje
     * @param opponent  geeft mee welke speler de tegenstander is.
     * @return Een lege positie waar de zet opgedaan word
     */
    public int aiNewSet(int[] gameBoard, int opponent, Model model) {
        boolean isOthello = model instanceof Othello;

        //Cast naar de goede instantie
        Model AiModel = isOthello ? (Othello) model : (TicTacToe) model;

        //Bepaal de zet met hoogste score, dus de zet die de grootste kans heeft om een overwinning op te leveren
        int bestMove = findBestMove(AiModel, opponent);

        int player = (opponent == 1) ? 2 : 1;

        ArrayList<Integer> valid = AiModel.getAvailableMoves(gameBoard, player);
        int pos = valid.get(0);

        if (valid.contains(bestMove)) {
            pos = bestMove;
        }
        return pos;
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
        int[] boardData = AiModel.getBoardData().clone();
        final int[] startBoardData = Arrays.copyOf(boardData.clone(), boardData.length);

        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;
        int AI = (opponent == 1) ? 2 : 1;
        ArrayList<Integer> moves = AiModel.getAvailableMoves(boardData, AI);
        for (int move : moves) {
            AiModel.setGameBoard(AiModel.move(move, boardData, AI));

            int score = minimax(AiModel, false, 5, Integer.MIN_VALUE, Integer.MAX_VALUE, AI, opponent);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
            boardData = startBoardData.clone();
            AiModel.setGameBoard(startBoardData);
        }
        System.out.println(bestMove);
        return bestMove;
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
    private int minimax(Model AiModel, boolean isMax, int depth, int a, int b, int AI, int opponent) {
        int[] boardData = AiModel.getBoardData().clone();
        final int[] startBoardData = Arrays.copyOf(boardData.clone(), boardData.length);
        ArrayList<Integer> availableMoves = isMax ? AiModel.getAvailableMoves(boardData, AI) : AiModel.getAvailableMoves(boardData, opponent);

        if (depth == 0 || availableMoves.isEmpty()) {
            return evaluate(AiModel, AI, opponent);
        }

        int bestScore = isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        if (isMax) {
            for (Integer move : availableMoves) {
                for (int i = 0; i < boardData.length; i++) {
                    if (boardData[i] != startBoardData[i]) {
                        System.out.print(i);
                    }
                }
                System.out.println();
                AiModel.setGameBoard(AiModel.move(move, boardData, AI));
                int score = minimax(AiModel, false, depth - 1, a, b, AI, opponent);
                bestScore = Math.max(bestScore, score);

                boardData = startBoardData.clone();
                AiModel.setGameBoard(startBoardData);

                if (bestScore >= b) {
                    break;
                }

                a = Math.max(a, bestScore);

            }
        } else {
            for (Integer move : availableMoves) {
                AiModel.setGameBoard(AiModel.move(move, boardData, opponent));
                int score = minimax(AiModel, true, depth - 1, a, b, AI, opponent);
                bestScore = Math.min(bestScore, score);

                boardData = startBoardData.clone();
                AiModel.setGameBoard(startBoardData);

                if (bestScore <= a) {
                    break;
                }

                b = Math.min(b, bestScore);

            }
        }
        return bestScore;
    }

    /**
     * Controleert het bord of de maximizer of minimizer heeft gewonnen
     *
     * @param AiModel Kopie van het echte spelbord waar de experimentele zetten op gedaan worden
     * @return score van de zet
     */
    private int evaluate(Model AiModel, int player, int opponent) {
        int[] boardData = AiModel.getBoardData();
        if (AiModel.isFinished()) {
            return 1000 * differenceEvaluate(boardData, player, opponent);
        }
        int playerMoves = AiModel.getAvailableMoves(boardData, player).size();
        int opponentMoves = AiModel.getAvailableMoves(boardData, opponent).size();
        GamePhase gamePhase = getGamePhase(boardData);
        int score = 0;
        score += 1000 * cornerCapturedEvaluate(boardData, player, opponent);
        if (gamePhase == GamePhase.EARLY_GAME) {
            score += 50 * positionsOfMovesEvaluate(playerMoves, opponentMoves);
        } else if (gamePhase == GamePhase.MID_GAME) {
            score += 20 * positionsOfMovesEvaluate(playerMoves, opponentMoves) + 10 * differenceEvaluate(boardData, player, opponent) + 100 * parityEvaluate(boardData);
        } else if (gamePhase == GamePhase.END_GAME) {
            score += 100 * positionsOfMovesEvaluate(playerMoves, opponentMoves) + 500 * differenceEvaluate(boardData, player, opponent) + 500 * parityEvaluate(boardData);
        }

        return score;
    }


    /**
     * Bekijkt in welke fase van de game je zit.
     *
     * @param boardData geeft het speelbord van dat moment mee.
     * @return in welke fase van de game je zit. Daar wordt de evaluatie op aangepast.
     */
    private GamePhase getGamePhase(int[] boardData) {
        int discs = 0;
        for (int i = 0; i < boardData.length; i++) {
            if (boardData[i] == 1 || boardData[i] == 2) {
                discs++;
            }
        }
        if (discs < 20) {
            return GamePhase.EARLY_GAME;
        } else if (discs < 50) {
            return GamePhase.MID_GAME;
        } else {
            return GamePhase.END_GAME;
        }
    }


    /**
     * Bekijkt hoeveel stenen geplaatst zijn door de player en door de tegenstander.
     *
     * @param boardData geeft het speelbord van dat moment mee
     * @param player    geeft de player mee.
     * @param opponent  geeft de tegenstander mee.
     * @return de percentage van de stenen die geplaatst zijn door de AI.
     */
    private int differenceEvaluate(int[] boardData, int player, int opponent) {
        int playerCount = countScore(boardData, player);
        int opponentCount = countScore(boardData, opponent);
        if (playerCount + opponentCount != 0) {
            return 100 * (playerCount - opponentCount) / (playerCount + opponentCount);
        }
        return 0;
    }

    /**
     * Kijkt wie de meeste zetten als mogelijkheid heeft. Als tegenstander 0 heeft is positief, dan ben je zelf vaker aan de beurt.
     * Als de tegenstander maar 1 mogelijkheid heeft, is er grotere kans dat hij slechte zet moet doen.
     *
     * @param playerMoves   geeft het aantal moves van de AI mee.
     * @param opponentMoves geeft de tegenstander mee.
     * @return de verhouding van mogelijke moves tussen de AI en de opponent
     */
    private int positionsOfMovesEvaluate(int playerMoves, int opponentMoves) {
        if (playerMoves + opponentMoves != 0) {
            return 100 * (playerMoves - opponentMoves) / (playerMoves + opponentMoves);
        }
        return 0;
    }

    /**
     * Kijkt naar de hoeken.
     *
     * @param boardData geeft het speelbord van dat moment mee
     * @param player    geeft de player mee.
     * @param opponent  geeft de tegenstander mee.
     * @return de verhouding van de hoeken tussen de AI en de opponent
     */
    private int cornerCapturedEvaluate(int[] boardData, int player, int opponent) {
        int playerCorner = 0;
        int opponentCorner = 0;
        for (int corner : cornersIdx) {
            if (boardData[corner] == player) {
                playerCorner++;
            } else if (boardData[corner] == opponent) {
                opponentCorner++;
            }
        }
        if (playerCorner + opponentCorner != 0) {
            return 100 * (playerCorner - opponentCorner) / (playerCorner + opponentCorner);

        }
        return 0;
    }


    /**
     * Kijkt of er een restwaarde is als je deelt door 2
     *
     * @param boardData geeft het speelbord van dat moment mee
     * @return Of je een extra zet hebt ten opzichte van de tegenstander
     */
    private int parityEvaluate(int[] boardData) {
        int remain = 64 - getTotalCount(boardData);
        return remain % 2 == 0 ? -1 : 1;
    }

    /**
     * Telt op hoeveel disks jij op het spelbord hebt staan.
     *
     * @param boardData geeft het speelbord van dat moment mee
     * @param disc      geeft mee voor welke je wil kijken hoeveel er zijn.
     * @return het aantal disks wat je hebt
     */
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
     * Telt op hoeveel disks er op het speelbord staan. Van de AI of van de opponent.
     *
     * @param boardData geeft het speelbord van dat moment mee
     * @return het aantal disks wat er op het speelbord staat
     */
    private int getTotalCount(int[] boardData) {
        int count = 0;
        for (int i = 0; i < boardData.length; i++) {
            if (boardData[i] == 1 || boardData[i] == 2) {
                count++;
            }
        }
        return count;
    }
}
