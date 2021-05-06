package game.logic.checkersai;

import game.logic.CheckersModel;

import java.util.*;

/**
 * This class implements an alpha-beta-pruning search algorithm,
 * with an interchangeable evaluation function and maximum cut-off depth.
 */
public class CheckersAI {

    private String[] bestMove;
    private EvalFunction evalFunction;
    private int maxDepth;

    public CheckersAI(EvalFunction evalFunction, int maxDepth, int player) {
        this.evalFunction = evalFunction;
        this.maxDepth = maxDepth;
    }

    /**
     * Finds and returns the AI's next move. Initiates alpha-beta search.
     */
    public String[] chooseMove(CheckersModel state) {
        bestMove = null;
        int eval;
        if(state.getCurrentPlayer() == CheckersModel.BLACK_PIECE) {
            eval = minValue(state, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
        else {
            eval = maxValue(state, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
        if(bestMove == null) {
            throw new IllegalStateException("bestMove not set");
        }
        return bestMove;
    }

    private int maxValue(CheckersModel state, int currentDepth, int alpha, int beta) {
        if(currentDepth >= maxDepth || state.getWinner() != 0) {return evalFunction.evaluate(state);}
        ArrayList<String[]> actions = convertActions(state.getPossibleMoves());
        int best = Integer.MIN_VALUE;
        for(String[] action : actions) {
            CheckersModel resultingState = state.clone();
            resultingState.makeMove(action[0],action[1]);
            int resultingEval;
            //since player turns don't necessarily alternate, we need to check the player of state.
            if(resultingState.getCurrentPlayer() == CheckersModel.BLACK_PIECE) {
                resultingEval = minValue(resultingState, currentDepth+1, alpha, beta);
            }
            else {
                resultingEval = maxValue(resultingState, currentDepth+1, alpha, beta);
            }
            if(resultingEval > best) {
                best = resultingEval;
                //Keep track of the best move for top-most call
                if(currentDepth == 0) {
                    bestMove = action;
                }
            }
            if(best >= beta) {return best;}
            alpha = Math.max(alpha,best);
        }
        return best;
    }

    private int minValue(CheckersModel state, int currentDepth, int alpha, int beta) {
        if(currentDepth >= maxDepth || state.getWinner() != 0) {return evalFunction.evaluate(state);}
        ArrayList<String[]> actions = convertActions(state.getPossibleMoves());
        int best = Integer.MAX_VALUE;
        for(String[] action : actions) {
            CheckersModel resultingState = state.clone();
            resultingState.makeMove(action[0],action[1]);
            int resultingEval;
            //since player turns don't necessarily alternate, we need to check the player of state.
            if(resultingState.getCurrentPlayer() == CheckersModel.BLACK_PIECE) {
                resultingEval = minValue(resultingState, currentDepth+1, alpha, beta);
            }
            else {
                resultingEval = maxValue(resultingState, currentDepth+1, alpha, beta);
            }
            if(resultingEval < best) {
                best = resultingEval;
                //Keep track of the best move for top-most call
                if(currentDepth == 0) {
                    bestMove = action;
                }
            }
            if(best <= alpha) {return best;}
            beta = Math.min(beta,best);
        }
        return best;
    }

    private ArrayList<String[]> convertActions(Map<String,Set<String>> map) {
        ArrayList<String[]> out = new ArrayList<>();
        for(String from : map.keySet()) {
            for(String to : map.get(from)) {
                out.add(new String[] {from, to});
            }
        }
        return out;
    }
}