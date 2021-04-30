import javafx.geometry.Pos;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * This class represents a single square on a checkers board.
 * Its methods allow the client to set what type of piece is
 * located on this square, if any.
 */
public class CheckersSquareComponent extends StackPane {

    Rectangle square;
    Circle crown;
    Circle piece;

    public CheckersSquareComponent(Color squareColor) {
        setAlignment(Pos.CENTER);
        square = new Rectangle(100,100,squareColor);
        piece = new Circle(30);
        piece.setStroke(Color.GOLD);
        piece.setStrokeWidth(2);
        crown = new Circle(25);
        crown.setFill(Color.TRANSPARENT);
        crown.setStroke(Color.GOLD);
        crown.setStrokeWidth(1.5);
        getChildren().addAll(square,piece,crown);
        setBlank();
    }

    public void setBlank() {
        piece.setVisible(false);
        crown.setVisible(false);
    }

    public void setRedPiece() {
        setHighlighted(false);
        piece.setFill(Color.RED);
        piece.setVisible(true);
        crown.setVisible(false);
    }

    public void setRedKing() {
        setRedPiece();
        crown.setVisible(true);
    }

    public void setBlackPiece() {
        setHighlighted(false);
        piece.setFill(Color.BLACK);
        piece.setVisible(true);
        crown.setVisible(false);
    }

    public void setBlackKing() {
        setBlackPiece();
        crown.setVisible(true);
    }

    public void setHighlighted(boolean highlighted) {
        if(highlighted) {
            piece.setStroke(Color.WHITE);
            crown.setStroke(Color.WHITE);
        }
        else {
            piece.setStroke(Color.GOLD);
            crown.setStroke(Color.GOLD);
        }
    }

}
