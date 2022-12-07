public class TicTacToeAI extends AI {
    @Override
    public int evaluate(Model AiModel, int player, int opponent) {
        //Als de maximizer wint, tel dan 10 op bij de score
        if (AiModel.checkWinner() != 0 && AiModel.checkWinner() == player) {
            return +10;
        }

        //Als de minimizer wint, trek dan 10 af van de score
        else if (AiModel.checkWinner() != 0 && AiModel.checkWinner() == opponent) {
            return -10;
        }

        if (AiModel.getBoardData()[4] == player && !AiModel.isFinished()){
            return +1;
        }
        //Niemand wint, dus geen punten er bij optellen
        return +0;
    }

}
