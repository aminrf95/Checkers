import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.Map;
import java.util.Set;

/**
 * This class provides a controller for a local game of checkers.
 * The game is run only on a single machine, and the players take turns
 * moving their pieces by clicking on the GUI.
 */
public class LocalGameController {

    private BoardComponent boardComponent;
    private CheckersModel checkersModel;
    private String selectedSquare;
    private Map<String, Set<String>> possibleMoves;

    public LocalGameController() {
        boardComponent = new BoardComponent();
        checkersModel = new CheckersModel();
        selectedSquare = null;
        for(int i = 0; i < CheckersModel.BOARD_ROWS; i++) {
            for(int j = 0; j < CheckersModel.BOARD_COLS; j++) {
                CheckersSquareComponent square = boardComponent.getSquare(i,j);
                final int iCoord = i;
                final int jCoord = j;
                square.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        selectSquare(iCoord,jCoord);
                    }
                });
            }
        }
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
        if(possibleMoves.containsKey(squareCoordinates)) {
            //We need to verify that the piece at i,j can be moved.
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
                selectedSquare = null;
            }
            else {
                selectedSquare = null;
            }
        }
    }

    //Makes the entered move on the checkersModel, and updates the view.
    private void commitMove(String from, String to) {
        //clear the highlights from last turn
        highlightAvailable(false);
        checkersModel.makeMove(from,to);
        updateView();
        if(checkersModel.getWinner() != 0) {
            System.out.println("GAME OVER");
        }
        //get the possible moves, and highlight movable pieces.
        possibleMoves = checkersModel.getPossibleMoves();
        highlightAvailable(true);
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
        if(possibleMoves.containsKey(selectedSquare)) {
            for(String s : possibleMoves.get(selectedSquare)) {
                int[] coordinates = CheckersModel.convertStringCoordinates(s);
                boardComponent.getSquare(coordinates[0],coordinates[1]).setMarker(mark);
            }
        }
    }

    public BoardComponent getBoardComponent() {
        return boardComponent;
    }

    public CheckersModel getCheckersModel() {
        return checkersModel;
    }
}
