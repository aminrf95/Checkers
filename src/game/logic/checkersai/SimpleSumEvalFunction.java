package game.logic.checkersai;

import game.logic.CheckersModel;

public class SimpleSumEvalFunction implements EvalFunction {
    @Override
    /**
     * This evaluation function simply sums all of the values on the board.
     * Thus the player who has more pieces is considered to have the advantage.
     * This function follows the convention of normal pieces having a value of 1,
     * and king pieces having a value of 2. This means having more king pieces
     * translates to a higher evaluation than normal pieces.
     */
    public int evaluate(CheckersModel model) {
        int sum = 0;
        int i = 0;
        //Only check squares where pieces could possibly be.
        while(i < CheckersModel.BOARD_ROWS) {
            int j = 0;
            if(i % 2 == 0) j = 1;
            while(j < CheckersModel.BOARD_COLS) {
                sum += model.getPieceAt(i,j);
                j += 2;
            }
            i++;
        }
        return sum;
    }
}
