package game.gametype.checkersai;

import game.logic.CheckersModel;

import java.util.*;

/**
 * This class implements an alpha-beta-pruning search algorithm,
 * with an interchangeable evaluation function and maximum cut-off depth.
 */
public class CheckersAI {

//    class State {
//        int[][] board;
//        int currentPlayer;
//        ArrayList<int[]> actions;
//
//        public State(int[][] board, int currentPlayer) {
//            this.board = board;
//            this.currentPlayer = currentPlayer;
//            findValidAction();
//        }
//
//        //sets actions to a new arraylist containing all moves currentPlayer can make.
//        private void findValidAction() {
//
//        }
//
//        //Returns a new state resulting from the given action.
//        //This method ensures the correct player is set in the new state;
//        //that is, if the currentPlayer of the original state makes a capture
//        //which can be followed up by another capture, then the player in the
//        //new state remains currentPlayer. Otherwise, it is the other players turn,
//        //so the new state's player will be -currentPlayer.
//        public State result(int[] action) {
//            int[][] newBoard = this.board.clone();
//            int i = action[0];
//            int j = action[1];
//            int x = action[2];
//            int y = action[3];
//            //////
//            //get the piece to be moved
//            int piece = newBoard[i][j];
//            //remove piece from i,j
//            newBoard[i][j] = 0;
//            //If piece is at opposite end of board, make piece a king
//            if(piece == CheckersModel.RED_PIECE && x == 7 ||
//                    piece == CheckersModel.BLACK_PIECE && x == 0) {piece *= 2;}
//            //place piece at x,y
//            newBoard[x][y] = piece;
//            //if the move was a jump:
//            if(Math.abs(i-x) == 2 && Math.abs(j-y) == 2) {
//                //remove captured piece
//                newBoard[(i+x)/2][(j+y)/2] = 0;
//                //check if moved piece can jump again:
//                ArrayList<int[]> jumps = findPossibleJumps(x,y);
//                if(!jumps.isEmpty()) {
//                    //It is still the current players turn.
//                    //Return a new state with newBoard and currentPlayer
//                    return new State(newBoard, currentPlayer);
//                }
//            }
//            //currentPlayer's turn is over. Change current player and return the new state
//            return new State(newBoard, -currentPlayer);
//        }
//
//        //Returns a set of all valid jumps the piece at i,j can make
//        private ArrayList<int[]> findPossibleJumps(int i, int j) {
//            ArrayList<int[]> jumps = new ArrayList<>();
//            //for each diagonal, call validJump.
//            //if true, add diagonal coordinate to jumps.
//            for(int x = i-2; x <= i+2; x += 4) {
//                for(int y = j-2; y <= j+2; y += 4) {
//                    if(validJump(i,j,x,y)) {jumps.add(new int[] {x,y});}
//                }
//            }
//            return jumps;
//        }
//
//        //returns true only if the capturing jump i,j -> x,y is valid.
//        private boolean validJump(int i, int j, int x, int y) {
//            //First check if the piece at i,j belongs to currentPlayer
//            if(board[i][j] * currentPlayer <= 0) {return false;}
//            //Ensure piece at i,j can move in the direction of x,y.
//            //If the piece is a king, we do not need to check this condition.
//            if((board[i][j] != CheckersModel.BLACK_KING && board[i][j] != CheckersModel.RED_KING)) {
//                int direction = x - i;
//                if(direction * currentPlayer <= 0) {return false;}
//            }
//            //Ensure x,y is on the board
//            if(!onBoard(x,y)) {return false;}
//            //Ensure x,y is vacant
//            if(board[x][y] != 0) {return false;}
//            //Ensure proper distance between i,j and x,y
//            if(Math.abs(i-x) != 2 || Math.abs(j-y) != 2) {return false;}
//            //Ensure space between i,j and x,y contains opponent's piece
//            //noinspection RedundantIfStatement
//            if(board[(i+x)/2][(j+y)/2] * currentPlayer >= 0) {return false;}
//            //If all of the above conditions pass, return true
//            return true;
//        }
//
//        public boolean terminalState() {
//            return actions.size() == 0;
//        }
//
//        public ArrayList<int[]> getActions() {
//            return actions;
//        }
//
//        public int getCurrentPlayer() {
//            return currentPlayer;
//        }
//
//        public int getEvaluation(EvalFunction e) {
//            return e.evaluate(board);
//        }
//    }

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
