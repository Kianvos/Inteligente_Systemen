package AI;

import Model.Model;
import Model.Othello;
import Model.TicTacToe;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

abstract public class AI {
    private HashMap<Integer, Integer> table;

    public AI() {
        this.loadTranspositionTable("./data/transposition-table");
    }

    /**
     * Laad de opgeslagen transposition table uit het opgegeven bestand in.
     * Als het bestand niet bestaat wordt deze aangemaakt en wordt er een lege transposition table geretourneerd.
     *
     * @param filePath het pad naar het bestand.
     * @return De transposition table uit het opgegeven bestand.
     **/
    private void loadTranspositionTable(String filePath) {
        File file = new File(filePath);

        if (!file.exists()) {
            try {
                boolean exists = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            table = new HashMap<>();
        } else {
            try (FileInputStream f = new FileInputStream(file);
                 ObjectInputStream s = new ObjectInputStream(f)) {
                //noinspection unchecked
                table = (HashMap<Integer, Integer>) s.readObject();
            } catch (IOException | ClassNotFoundException e) {
//                e.printStackTrace();
                table = new HashMap<>();
            }
        }
    }

    public void saveTranspositionTable(String filePath) {
        File file = new File(filePath);

        try (FileOutputStream f = new FileOutputStream(file);
             ObjectOutputStream s = new ObjectOutputStream(f)) {
            s.writeObject(table);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Geeft een lege positie op het bord terug waarbij de kans het grootste
     * is dat het een overwinning oplevert
     * /**
     * Laat de AI.AI een zet doen
     *
     * @param gameBoard Bord van het huidige potje
     * @param opponent  geeft mee welke speler de tegenstander is.
     * @return Een lege positie waar de zet opgedaan word
     */
    public int aiNewSet(int[] gameBoard, int opponent, Model model) {
        // System.out.println(table);

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
        int hash = Arrays.hashCode(boardData);
        final int[] startBoardData = Arrays.copyOf(boardData.clone(), boardData.length);

        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;
        int AI = (opponent == 1) ? 2 : 1;
        ArrayList<Integer> moves = AiModel.getAvailableMoves(boardData, AI);

        if (table.get(hash) != null) {
            System.out.println("Found move");
            return (int) table.get(hash);
        }

        for (int move : moves) {
            AiModel.setGameBoard(AiModel.move(move, boardData, AI));

            int score = minimax(AiModel, false, 3, Integer.MIN_VALUE, Integer.MAX_VALUE, AI, opponent);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
            boardData = startBoardData.clone();
            AiModel.setGameBoard(startBoardData);
        }

        this.table.put(hash, bestMove);

        return bestMove;
    }

    /**
     * Bepaalt de score van een zet om te bepalen wat de beste zet is.
     * De score wordt bepaald door AI.AI (de maximizer) en de tegenstander (de minimizer)
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

    public HashMap<Integer, Integer> getTable() {
        return table;
    }

    abstract public int evaluate(Model AiModel, int player, int opponent);
}
