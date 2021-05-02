import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class LocalGame extends Game {

    GameController controller;

    public LocalGame(GameController gameController) {

        controller = gameController;
        BoardComponent boardComponent = controller.getBoardComponent();
        for(int i = 0; i < CheckersModel.BOARD_ROWS; i++) {
            for(int j = 0; j < CheckersModel.BOARD_COLS; j++) {
                CheckersSquareComponent square = boardComponent.getSquare(i,j);
                final int iCoord = i;
                final int jCoord = j;
                square.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        controller.selectSquare(iCoord,jCoord);
                    }
                });
            }
        }
    }
}
