package game.logic.checkersai;

import game.logic.CheckersModel;

public interface EvalFunction {
    public int evaluate(CheckersModel model);
}
