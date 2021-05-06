package game.gametype;

import game.logic.checkersai.CheckersAI;
import game.logic.checkersai.SimpleSumEvalFunction;
import game.logic.CheckersModel;
import game.logic.GameController;
import game.view.BoardComponent;
import game.view.CheckersSquareComponent;
import game.view.MessageBox;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class VSComputerGame extends Game {

    private MessageBox messageBox;
    private boolean controlsDisabled;

    public class AIThread extends Thread {
        @Override
        public void run() {
            while(model.getWinner() == 0 && model.getCurrentPlayer() == -1) {
                String[] move = computerAI.chooseMove(model.clone());
                int[] from = CheckersModel.convertStringCoordinates(move[0]);
                int[] to = CheckersModel.convertStringCoordinates(move[1]);
                try {
                    sleep(1000);
                    controller.selectSquare(from[0],from[1]);
                    sleep(1000);
                    controller.selectSquare(to[0],to[1]);
                    if(!controlsDisabled){
                        setMessage();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    GameController controller;
    CheckersAI computerAI;
    CheckersModel model;
    AIThread aiThread;

    public VSComputerGame(GameController controller, int difficultyLevel) {
        this.controller = controller;
        this.model = controller.getCheckersModel();
        computerAI = new CheckersAI(new SimpleSumEvalFunction(), difficultyLevel, -1);
        BoardComponent boardComponent = controller.getBoardComponent();
        for(int i = 0; i < CheckersModel.BOARD_ROWS; i++) {
            for(int j = 0; j < CheckersModel.BOARD_COLS; j++) {
                CheckersSquareComponent square = boardComponent.getSquare(i,j);
                final int iCoord = i;
                final int jCoord = j;
                square.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if(controlsDisabled || model.getCurrentPlayer() == -1) {return;}
                        controller.selectSquare(iCoord,jCoord);
                        setMessage();
                        aiThread = new AIThread();
                        aiThread.start();
                    }
                });
            }
        }
    }

    @Override
    public void resetGame() {
        try {
            if (aiThread != null) {
                aiThread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        controller.initializeController();
        controlsDisabled = false;
        messageBox.setTurnMessage();
    }

    @Override
    public int getCurrentPlayer() {
        return controller.getCurrentPlayer();
    }

    @Override
    public void quitGame() {

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
        return Color.RED;
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
