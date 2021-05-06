package game.gametype;

import game.view.BoardComponent;
import game.logic.CheckersModel;
import game.view.CheckersSquareComponent;
import game.logic.GameController;
import game.view.MessageBox;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.Optional;

public class LocalGame extends Game {

    GameController controller;
    private MessageBox messageBox;
    private boolean controlsDisabled;

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
                        if(controlsDisabled) {return;}
                        controller.selectSquare(iCoord,jCoord);
                        setMessage();
                    }
                });
            }
        }
    }

    @Override
    public void quitGame() {
    }

    @Override
    public void resetGame() {
        controller.initializeController();
        controlsDisabled = false;
        messageBox.setTurnMessage();
    }

    @Override
    public int getCurrentPlayer() {
        return controller.getCurrentPlayer();
    }

    @Override
    public void registerMessageBox(MessageBox messageBox) {
        this.messageBox = messageBox;
    }

    @Override
    public int getWinner() {
        return controller.getCheckersModel().getWinner();
    }

    @Override
    public void sendDecision(boolean d) {

    }

    @Override
    public void resetRequest() {
        resetGame();
    }

    @Override
    public void disableControls(boolean disable) {
        controlsDisabled = disable;
    }

    @Override
    public Color getPlayerColor() {
        return Color.GOLD;
    }

    private void setMessage() {
        if(getWinner() != 0) {
            messageBox.setGameOverMessage();
        }
        else {
            messageBox.setTurnMessage();
        }
    }
}
