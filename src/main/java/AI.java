
public class AI {
    // public AI() {}

    /**
     * Controleert het bord of de maximizer of minimizer heeft gewonnen
     * @param AiModel Kopie van het echte spelbord waar de experimentele zetten op gedaan worden
     * @return score van de zet
     */
    static int evaluate(Model AiModel, char opponent, char player) {

        //Als de maximizer wint, tel dan 10 op bij de score
        if(AiModel.checkWinner(player)) {
            return + 10;
        }

        //Als de minimizer wint, trek dan 10 af van de score
        else if(AiModel.checkWinner(opponent)) {
            return - 10;
        }

        //Niemand wint, dus geen punten er bij optellen
        return + 0;
    }

    /**
     * Bepaalt de score van een zet om te bepalen wat de beste zet is.
     * De score wordt bepaald door AI (de maximizer) en de tegenstander (de minimizer)
     * omzetbeurten een zet te laten doen op elk leeg vakje op het spelbord. Elke keer als deze
     * functie wordt aangeroepen, wordt er eerste gekeken welke speler wint en geeft op basis
     * daarvan punten terug.
     * @param AiModel Kopie van het echte spelbord waar de experimentele zetten op gedaan worden
     * @param isMax Of de maximizer aan de beurt is of niet
     * @param depth Tot hoe diep het algoritme experimentele zetten mag doen
     * @param opponent geeft mee welke speler de tegenstander is.
     * @return De score van het beste mogelijke zet
     */
    static int minimax(Model AiModel, boolean isMax, int depth, char opponent, char player) {

        //Stop met puntentelling als de maximale diepte is bereikt
        if (depth <= 0) {
            return + 0;
        }

        //Controleer de score van de zet waarmee de functie is aangeroepen
        int score = evaluate(AiModel, opponent, player);
        //Geef de score terug als de maximizer of minimizer gewonnen heeft
        if (score == 10 || score == -10) {
            return score;
        }

        //Geef niks terug als er gelijkspel is
        if (AiModel.checkTie()) {
            return + 0;
        }

        //Haal het spelbord op waarmee de functie is aangeroepen
        char[] board = AiModel.getBoard();

        //Bepaal de hoogst mogelijke score voor de maximizer en minimizer
        int best;
        if(isMax) {
            best = -1000;
        }
        else {
            best = 1000;
        }

        //Controleer het bord op lege vakjes
        for (int i = 0; i < board.length; i++) {
            if(board[i] == '\u0000') {
                if(isMax) {
                    //Doe een zet als de maximizer
                    board[i] = player;

                    //Bepaal de beste score door de functie opnieuw aan te roepen en een zet te doen als de minimizer
                    best = Math.max(best, minimax(AiModel, false, depth - 1, opponent, player));
                }
                else {
                    //Doe een zet als de minimizer
                    board[i] = opponent;

                    //Bepaal de beste score door de functie opnieuw aan te roepen en een zet te doen als de maximizer
                    best = Math.min(best, minimax(AiModel, true, depth - 1, opponent, player));
                }

                //Maak het vakje weer leeg om andere zetten mogelijk te maken
                board[i] = '\u0000';
            }
        }

        return best;

    }

    /**
     * Geeft een lege positie op het bord terug waarbij de kans het grootste
     * is dat het een overwinning oplevert
     * @param AiModel Kopie van het echte spelbord waar de experimentele zetten op gedaan worden
     * @param opponent geeft mee welke speler de tegenstander is.
     * @return Lege positie op het bord
     */
    static int findBestMove(Model AiModel, char opponent) {

        char[] board = AiModel.getBoard();

        //Bepaal de hoogst mogelijke score die een bord zou kunnen hebben voor de minimizer
        int bestVal = -1000;
        int bestMove = -1;
        char player = 'X';
        if (opponent == 'X'){
            player = 'O';
        }
        System.out.println("\n\n\n"+player+", "+opponent+"\n\n\n");
        //Zoek naar een leeg vakje
        for (int i = 0; i < board.length; i++) {
            if (board[i] == '\u0000') {
                //Doe een zet als de maximizer
                board[i] = player;

                //Bepaal de score van de zet door een zet te doen als de minimizer
                int moveVal = minimax(AiModel, false, 10, opponent, player);

                //Maak het vakje van deze zet weer leeg om andere zetten toe te staan als het bord veranderd is
                board[i] = '\u0000';

                //Als de score van de zet groter is dan de hoogst mogelijk score, kies dan deze zet op het echte bord
                if (moveVal > bestVal) {
                    bestMove = i;
                    bestVal = moveVal;
                }
            }
        }

        return bestMove;
    }

    /**
     * Laat de AI een zet doen
     * @param gameBoard Bord van het huidige potje
     * @param opponent geeft mee welke speler de tegenstander is.
     * @return Een lege positie waar de zet opgedaan word
     */
    public int aiNewSet(char[] gameBoard, char opponent) {

        //Maak een kopie van het spelbord die het algoritme kan gebruiken voor simulaties
        Model AiModel = new Model(gameBoard.length);
        AiModel.setBoard(gameBoard);

        //Bepaal de zet met hoogste score, dus de zet die de grootste kans heeft om een overwinning op te leveren
        int bestMove = findBestMove(AiModel, opponent);

        //Laat de AI een zet doen
        boolean choice = false;
        int pos = -1;
        while (!choice) {
            int posChoise = bestMove;

            //Controleer of het vakje niet bezet is
            if (gameBoard[posChoise] == '\u0000') {
                pos = posChoise;
                choice = true;
            }
        }
        return pos;
    }
}
