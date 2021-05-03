package game.gametype;

import game.view.BoardComponent;
import game.logic.CheckersModel;
import game.view.CheckersSquareComponent;
import game.logic.GameController;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;

import java.util.Optional;

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

    @Override
    public void quitGame() {

    }
}
