import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogEvent;
import javafx.scene.input.MouseEvent;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * This class provides a controller for a local game of checkers.
 * The game is run only on a single machine, and the players take turns
 * moving their pieces by clicking on the GUI.
 */
public class GameController {

    private BoardComponent boardComponent;
    private CheckersModel checkersModel;
    private String selectedSquare;
    private Map<String, Set<String>> possibleMoves;

    public GameController() {
        //One-time setup of boardComponent
        boardComponent = new BoardComponent();
        checkersModel = new CheckersModel();
        //sets up a new game.
        initializeController();
    }

    //Sets up a new game.
    public void initializeController() {
        checkersModel.resetGame();
        selectedSquare = null;
        updateView();
        possibleMoves = checkersModel.getPossibleMoves();
        highlightAvailable(true);
    }


    //This method will update the boardComponent according to the
    //current state of the checkersModel.
    public void updateView() {
        for(int i = 0; i < CheckersModel.BOARD_ROWS; i++) {
            for(int j = 0; j < CheckersModel.BOARD_COLS; j++) {
                CheckersSquareComponent square = boardComponent.getSquare(i,j);
                int piece = checkersModel.getPieceAt(i,j);
                if(piece == CheckersModel.RED_PIECE) {square.setRedPiece();}
                else if(piece == CheckersModel.RED_KING) {square.setRedKing();}
                else if(piece == CheckersModel.BLACK_PIECE) {square.setBlackPiece();}
                else if(piece == CheckersModel.BLACK_KING) {square.setBlackKing();}
                else {square.setBlank();}
            }
        }
    }

    //Handles square/piece selection.
    public void selectSquare(int i, int j) {
        //Clear any previously marked squares.
        markPossible(false);
        String squareCoordinates = ""+i+j;
        //We need to check if the piece at i,j can be moved.
        if(possibleMoves.containsKey(squareCoordinates)) {
            selectedSquare = squareCoordinates;
            //mark all possible moves from i,j
            markPossible(true);
        }
        else if(selectedSquare != null) {
            //A valid piece has already been selected.
            //We need to check if the square at i,j is a valid destination.
            if(possibleMoves.get(selectedSquare).contains(squareCoordinates)) {
                //This is a valid move.
                commitMove(selectedSquare,squareCoordinates);
            }
            selectedSquare = null;
        }
    }

    //Makes the entered move on the checkersModel, and updates the view.
    private void commitMove(String from, String to) {
        //clear the highlights from last turn
        highlightAvailable(false);
        checkersModel.makeMove(from,to);
        updateView();
        if(checkersModel.getWinner() != 0) {
            endGame();
        }
        //get the possible moves, and highlight movable pieces.
        possibleMoves = checkersModel.getPossibleMoves();
        highlightAvailable(true);
    }

    private void endGame() {
        String winnerString;
        if(checkersModel.getWinner() == CheckersModel.RED_PIECE) {winnerString = "Red";}
        else if(checkersModel.getWinner() == CheckersModel.BLACK_PIECE) {winnerString = "Black";}
        else {throw new IllegalStateException();}
        System.out.println(winnerString+" wins!");
    }

    //sets the highlight on all movable pieces.
    private void highlightAvailable(boolean highlight) {
        for(String piece : possibleMoves.keySet()) {
            int[] coordinates = CheckersModel.convertStringCoordinates(piece);
            boardComponent.getSquare(coordinates[0],coordinates[1]).setHighlighted(highlight);
        }
    }

    //Marks or unmarks all possible moves from the current selected square.
    private void markPossible(boolean mark) {
        if(selectedSquare!=null){
            int[] selectedSquareCoords = CheckersModel.convertStringCoordinates(selectedSquare);
            boardComponent.getSquare(selectedSquareCoords[0],selectedSquareCoords[1]).setMarker(mark);
        }
        if(possibleMoves.containsKey(selectedSquare)) {
            for(String s : possibleMoves.get(selectedSquare)) {
                int[] coordinates = CheckersModel.convertStringCoordinates(s);
                boardComponent.getSquare(coordinates[0],coordinates[1]).setMarker(mark);
            }
        }
    }

    public int getCurrentPlayer() {
        return checkersModel.getCurrentPlayer();
    }

    public BoardComponent getBoardComponent() {
        return boardComponent;
    }

    public CheckersModel getCheckersModel() {
        return checkersModel;
    }
}
