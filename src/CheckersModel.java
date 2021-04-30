import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class provides a model for a checkers game.
 * An integer matrix is used to represent the game board.
 * In this representation, positive values are associated with the red player,
 * while negative values are associated with the black player. Values of magnitude 1
 * are normal pieces, while values of magnitude 2 are king pieces. For example,
 * -1 represents a normal black piece, whereas 2 represents a red king piece.
 * 0 represents a blank space.
 */
public class CheckersModel {

    /*
    Positive values represent the red player,
    negative values represent the black player.
    Magnitude of 1 represents a normal piece,
    magnitude of 2 represents a king piece.
     */
    public static final int RED_PIECE = 1;
    public static final int RED_KING = 2;
    public static final int BLACK_PIECE = -1;
    public static final int BLACK_KING = -2;

    public static final int BOARD_ROWS = 8;
    public static final int BOARD_COLS = 8;

    private int[][] board;
    private int currentPlayer;
    private int winner;
    private Map<String, Set<String>> possibleMoves;

    public CheckersModel() {
        initializeGame();
    }

    private void initializeGame() {
        board = new int[BOARD_ROWS][BOARD_COLS];
        possibleMoves = new HashMap<>();
        //Initialize the first three rows with red pieces.
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < BOARD_COLS; j++) {
                if((i+j) % 2 == 1) {
                    board[i][j] = RED_PIECE;
                }
            }
        }
        //Initialize the last three rows with black pieces.
        for(int i = 5; i < BOARD_ROWS; i++) {
            for(int j = 0; j < BOARD_COLS; j++) {
                if((i+j) % 2 == 1) {
                    board[i][j] = BLACK_PIECE;
                }
            }
        }
        winner = 0;
        //Red moves first
        currentPlayer = RED_PIECE;
        setPossibleMoves();
    }

    public void resetGame() {
        initializeGame();
    }

    //Convenience method.
    public boolean makeMove(String from, String to) {
        int[] fromCoords = convertStringCoordinates(from);
        int[] toCoords = convertStringCoordinates(to);
        return makeMove(fromCoords[0],fromCoords[1],toCoords[0],toCoords[1]);
    }

    //Makes the entered move if possible, otherwise returns false
    //Moves are entered as strings, from "ij" to "xy"
    public boolean makeMove(int i, int j, int x, int y) {
        //If the game is over, throw an exception
        if(winner != 0) {throw new IllegalStateException("Cannot make move, game has already ended.");}
        String from = "" + i + j;
        String to = "" + x + y;
        //If the move entered is invalid, return false.
        if(!possibleMoves.containsKey(from) || !possibleMoves.get(from).contains(to)) {return false;}
        //Otherwise, get the piece at i,j
        int piece = board[i][j];
        //set remove piece from i,j
        board[i][j] = 0;
        //If piece is at opposite end of board, make piece a king
        if(piece == RED_PIECE && x == 7 || piece == BLACK_PIECE && x == 0) {piece *= 2;}
        //place piece at x,y
        board[x][y] = piece;
        //if the move was a jump:
        if(Math.abs(i-x) == 2 && Math.abs(j-y) == 2) {
            //remove captured piece
            board[(i+x)/2][(j+y)/2] = 0;
            //check if moved piece can jump again:
            Set<String> jumps = findPossibleJumps(x,y);
            if(!jumps.isEmpty()) {
                //It is still the current players turn.
                //Set jumps as possible move, and return true.
                possibleMoves.clear();
                possibleMoves.put(to,jumps);
                return true;
            }
        }
        //currentPlayer's turn is over. Change current player, find possible moves, and return true.
        currentPlayer = -currentPlayer;
        setPossibleMoves();
        return true;
    }

    //This method will find all possible moves for the current player, and update possibleMoves.
    private void setPossibleMoves() {
        //Clear possibleMoves
        possibleMoves.clear();
        //First, look for possible jumps, in order to enforce the forced capture rule.
        for(int i = 0; i < BOARD_ROWS; i++) {
            for(int j = 0; j < BOARD_COLS; j++) {
                //Only check moves for currentPlayer's pieces
                if(board[i][j] * currentPlayer > 0) {
                    Set<String> jumps = findPossibleJumps(i,j);
                    if(!jumps.isEmpty()) {
                        possibleMoves.put(""+i+j,jumps);
                    }
                }
            }
        }
        if(possibleMoves.size() != 0) {return;}
        //If no jumps are possible, it will then look for possible steps
        for(int i = 0; i < BOARD_ROWS; i++) {
            for(int j = 0; j < BOARD_COLS; j++) {
                //Only check moves for currentPlayer's pieces
                if(board[i][j] * currentPlayer > 0) {
                    Set<String> steps = findPossibleSteps(i,j);
                    if(!steps.isEmpty()) {
                        possibleMoves.put(""+i+j,steps);
                    }
                }
            }
        }
        if(possibleMoves.size() != 0) {return;}
        //If no normal moves exist, then this player has lost, and winner is updated.
        winner = -currentPlayer;
    }

    //Returns a set of all valid jumps the piece at i,j can make
    private Set<String> findPossibleJumps(int i, int j) {
        Set<String> jumps = new HashSet<>();
        //for each diagonal, call validJump.
        //if true, add diagonal coordinate to jumps.
        for(int x = i-2; x <= i+2; x += 4) {
            for(int y = j-2; y <= j+2; y += 4) {
                if(validJump(i,j,x,y)) {jumps.add(""+x+y);}
            }
        }
        return jumps;
    }

    //returns true only if the capturing jump i,j -> x,y is valid.
    private boolean validJump(int i, int j, int x, int y) {
        //First check if the piece at i,j belongs to currentPlayer
        if(board[i][j] * currentPlayer <= 0) {return false;}
        //Ensure piece at i,j can move in the direction of x,y.
        //If the piece is a king, we do not need to check this condition.
        if((board[i][j] != BLACK_KING && board[i][j] != RED_KING)) {
            int direction = x - i;
            if(direction * currentPlayer <= 0) {return false;}
        }
        //Ensure x,y is on the board
        if(!onBoard(x,y)) {return false;}
        //Ensure x,y is vacant
        if(board[x][y] != 0) {return false;}
        //Ensure proper distance between i,j and x,y
        if(Math.abs(i-x) != 2 || Math.abs(j-y) != 2) {return false;}
        //Ensure space between i,j and x,y contains opponent's piece
        //noinspection RedundantIfStatement
        if(board[(i+x)/2][(j+y)/2] * currentPlayer >= 0) {return false;}
        //If all of the above conditions pass, return true
        return true;
    }

    //Returns a set of all valid steps the piece at i,j can make.
    //A step is a non-capturing move.
    private Set<String> findPossibleSteps(int i, int j) {
        Set<String> steps = new HashSet<>();
        //for each diagonal, call validStep.
        //if true, add diagonal coordinate to steps.
        for(int x = i-1; x <= i+1; x += 2) {
            for(int y = j-1; y <= j+1; y += 2) {
                if(validStep(i,j,x,y)) {steps.add(""+x+y);}
            }
        }
        return steps;
    }

    //Returns true only if the non-capturing step i,j -> x,y is valid
    private boolean validStep(int i, int j, int x, int y) {
        //First check if the piece at i,j belongs to currentPlayer
        if(board[i][j] * currentPlayer <= 0) {return false;}
        //Ensure piece at i,j can move in the direction of x,y.
        //If the piece is a king, we do not need to check this condition.
        if((board[i][j] != BLACK_KING && board[i][j] != RED_KING)) {
            int direction = x - i;
            if(direction * currentPlayer <= 0) {return false;}
        }
        //Ensure x,y is on the board
        if(!onBoard(x,y)) {return false;}
        //Ensure x,y is vacant
        //noinspection RedundantIfStatement
        if(board[x][y] != 0) {return false;}
        //If all of the above conditions pass, return true
        return true;
    }

    //Checks if (i,j) is a valid coordinate on the board
    private boolean onBoard(int i, int j) {
        return (0 <= i && i < BOARD_ROWS) && (0 <= j && j < BOARD_COLS);
    }

    public Map<String, Set<String>> getPossibleMoves() {
        return possibleMoves;
    }

    //Returns the winner, or 0 if there is no winner yet
    public int getWinner() {
        return winner;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public int getPieceAt(int i, int j) {
        return board[i][j];
    }

    public static int[] convertStringCoordinates(String coordinates) {
        return new int[] {Character.getNumericValue(coordinates.charAt(0)),
                Character.getNumericValue(coordinates.charAt(1))};
    }

    @Override
    //Returns a string representation of the game
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  0 1 2 3 4 5 6 7\n");
        for(int i = 0; i < BOARD_ROWS; i++) {
            sb.append(i + " ");
            for(int j = 0; j < BOARD_COLS; j++) {
                if((i+j)%2==0) {sb.append((char)0x25A0);}
                else if(getPieceAt(i,j) == RED_PIECE) {sb.append('r');}
                else if(getPieceAt(i,j) == RED_KING) {sb.append('R');}
                else if(getPieceAt(i,j) == BLACK_PIECE) {sb.append('b');}
                else if(getPieceAt(i,j) == BLACK_KING) {sb.append('B');}
                else {sb.append('_');}
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
