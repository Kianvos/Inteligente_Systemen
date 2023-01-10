package AI;

import Model.Model;
import Model.Othello;

import java.util.ArrayList;

//https://courses.cs.washington.edu/courses/cse573/04au/Project/mini1/RUSSIA/Final_Paper.pdf
public class OthelloAI extends AI {
    private final int[] cornersIdx = {0, 7, 56, 63};

    public OthelloAI() {
        super();
    }

    private enum GamePhase {
        EARLY_GAME,
        MID_GAME,
        END_GAME
    }

    /**
     * Controleert het bord of de maximizer of minimizer heeft gewonnen
     *
     * @param AiModel Kopie van het echte spelbord waar de experimentele zetten op gedaan worden
     * @return score van de zet
     */
    public int evaluate(Model AiModel, int player, int opponent) {
        int[] boardData = AiModel.getBoardData();
        ArrayList<Integer> playerMoves = AiModel.getAvailableMoves(boardData, player);
        ArrayList<Integer> opponentMoves = AiModel.getAvailableMoves(boardData, opponent);

        int playerMovesSize = playerMoves.size();
        int opponentMovesSize = opponentMoves.size();
        if (AiModel.isFinished(playerMoves, opponentMoves)) {
            return 1000 * differenceEvaluate(boardData, player, opponent);
        }
        GamePhase gamePhase = getGamePhase(boardData);
        int score = 0;
        score += 100 * cornerCapturedEvaluate(boardData, player, opponent);
        if (gamePhase == GamePhase.EARLY_GAME) {
            score += 5 * positionsOfMovesEvaluate(playerMovesSize, opponentMovesSize);
        } else if (gamePhase == GamePhase.MID_GAME) {
            score += 2 * positionsOfMovesEvaluate(playerMovesSize, opponentMovesSize) + differenceEvaluate(boardData, player, opponent) + 10 * parityEvaluate(boardData);
        } else if (gamePhase == GamePhase.END_GAME) {
            score += 10 * positionsOfMovesEvaluate(playerMovesSize, opponentMovesSize) + 50 * differenceEvaluate(boardData, player, opponent) + 50 * parityEvaluate(boardData);
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
     * @return de percentage van de stenen die geplaatst zijn door de AI.AI.
     */
    private int differenceEvaluate(int[] boardData, int player, int opponent) {
        int playerCount = Othello.countScore(boardData, player);
        int opponentCount = Othello.countScore(boardData, opponent);
        if (playerCount + opponentCount != 0) {
            return 100 * (playerCount - opponentCount) / (playerCount + opponentCount);
        }
        return 0;
    }

    /**
     * Kijkt wie de meeste zetten als mogelijkheid heeft. Als tegenstander 0 heeft is positief, dan ben je zelf vaker aan de beurt.
     * Als de tegenstander maar 1 mogelijkheid heeft, is er grotere kans dat hij slechte zet moet doen.
     *
     * @param playerMoves   geeft het aantal moves van de AI.AI mee.
     * @param opponentMoves geeft de tegenstander mee.
     * @return de verhouding van mogelijke moves tussen de AI.AI en de opponent
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
     * @return de verhouding van de hoeken tussen de AI.AI en de opponent
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
     * Telt op hoeveel disks er op het speelbord staan. Van de AI.AI of van de opponent.
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
