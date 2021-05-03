package game.view;

import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * This class represents the board for a checkers game. It is composed of an 8x8
 * grid of CheckersSquareComponents, and provides access to the square located at i,j.
 */
public class BoardComponent extends Region {

    public static final int BOARD_ROWS = 8;
    public static final int BOARD_COLS = 8;
    private CheckersSquareComponent[][] squares;

    public BoardComponent() {
        squares = new CheckersSquareComponent[BOARD_ROWS][BOARD_COLS];
        Rectangle boardOutLine = new Rectangle(100 * BOARD_ROWS,100 * BOARD_COLS);
        boardOutLine.setStrokeWidth(10);
        boardOutLine.setStroke(Color.GOLD);
        getChildren().add(boardOutLine);
        //Create checkerboard pattern
        for(int i = 0; i < BOARD_ROWS; i++) {
            for(int j = 0; j < BOARD_COLS; j++) {
                CheckersSquareComponent square;
                if((i+j) % 2 == 0) {
                     square = new CheckersSquareComponent(Color.RED);
                }
                else {
                    square = new CheckersSquareComponent(Color.BLACK);
                }
                square.setTranslateX(j * 100);
                square.setTranslateY(i * 100);
                squares[i][j] = square;
                getChildren().add(square);
            }
        }
    }

    public CheckersSquareComponent getSquare(int i, int j) {
        return squares[i][j];
    }

}
