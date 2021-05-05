package game.gametype;

import game.logic.checkersai.CheckersAI;
import game.logic.checkersai.SimpleSumEvalFunction;
import game.logic.CheckersModel;
import game.logic.GameController;
import game.view.BoardComponent;
import game.view.CheckersSquareComponent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.Arrays;

public class VSComputerGame extends Game {

    public class AIThread extends Thread {
        @Override
        public void run() {
            while(model.getWinner() == 0 && model.getCurrentPlayer() == -1) {
                String[] move = computerAI.chooseMove(model.clone());
                int[] from = CheckersModel.convertStringCoordinates(move[0]);
                int[] to = CheckersModel.convertStringCoordinates(move[1]);
                System.out.println(Arrays.toString(from));
                System.out.println(Arrays.toString(to));
                try {
                    sleep(1000);
                    controller.selectSquare(from[0],from[1]);
                    sleep(1000);
                    controller.selectSquare(to[0],to[1]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    GameController controller;
    CheckersAI computerAI;
    CheckersModel model;

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
                        if(model.getCurrentPlayer() == -1) {return;}
                        controller.selectSquare(iCoord,jCoord);
//                        while(model.getWinner() == 0 && model.getCurrentPlayer() == -1) {
//                            getAIMove();
//                        }
                        AIThread aiThread = new AIThread();
                        aiThread.start();
                    }
                });
            }
        }
    }

    private void getAIMove() {
        String[] move = computerAI.chooseMove(model.clone());
        int[] from = CheckersModel.convertStringCoordinates(move[0]);
        int[] to = CheckersModel.convertStringCoordinates(move[1]);
        System.out.println(Arrays.toString(from));
        System.out.println(Arrays.toString(to));
        controller.selectSquare(from[0],from[1]);
        controller.selectSquare(to[0],to[1]);
    }

    @Override
    public void quitGame() {

    }
}
