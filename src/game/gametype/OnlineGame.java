package game.gametype;

import game.view.BoardComponent;
import game.logic.CheckersModel;
import game.view.CheckersSquareComponent;
import game.logic.GameController;
import game.view.MessageBox;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

public class OnlineGame extends Game {

    private MessageBox messageBox;

    private class ConnectionThread extends Thread {

        @Override
        public void run() {
            int i;
            int j;
            while (true) {
                try {
                    i = in.readInt();
                    j = in.readInt();
                    if(i == -1 || j == -1) {
                        messageBox.setOpponentLeft();
                        break;
                    }
                    else if(i == RESET_SIGNAL || j == RESET_SIGNAL) {
                        messageBox.setResetRequest();
                    }
                    else if(i == AFFIRM_RESET || j == AFFIRM_RESET) {
                        resetGame();
                        messageBox.setResetAffirm();
                    }
                    else if(i == DENY_RESET || j == DENY_RESET) {
                        messageBox.setResetDeny();
                    }
                    else {
                        controller.selectSquare(i, j);
                        setMessage();
                    }
                } catch (IOException e) {
                    messageBox.setOpponentLeft();
                    shutDownConnection();
                    break;
                }
            }
        }
    }

    private GameController controller;
    private int player;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private ConnectionThread connectionThread;
    private boolean controlsDisabled;

    private final int RESET_SIGNAL = -2;
    private final int AFFIRM_RESET = -3;
    private final int DENY_RESET = -4;

    public OnlineGame(GameController gameController, Socket socket, int player) {
        this.player = player;
        this.socket = socket;
        controller = gameController;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        connectionThread = new ConnectionThread();
        BoardComponent boardComponent = controller.getBoardComponent();
        for(int i = 0; i < CheckersModel.BOARD_ROWS; i++) {
            for(int j = 0; j < CheckersModel.BOARD_COLS; j++) {
                CheckersSquareComponent square = boardComponent.getSquare(i,j);
                final int iCoord = i;
                final int jCoord = j;
                square.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if(controlsDisabled || gameController.getCurrentPlayer() != player) {return;}
                        controller.selectSquare(iCoord,jCoord);
                        setMessage();
                        try {
                            out.writeInt(iCoord);
                            out.writeInt(jCoord);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
        connectionThread.start();
    }

    public void shutDownConnection() {
        //System.out.println("Shutting down connection...");
        connectionThread.interrupt();
        try {
            if(in!=null) {
                in.close();
            }
            if(out!=null) {
                out.close();
            }
            if(!socket.isClosed()) {
                socket.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void quitGame() {
        shutDownConnection();
    }

    @Override
    public void resetRequest() {
        try {
            out.writeInt(RESET_SIGNAL);
            out.writeInt(RESET_SIGNAL);
            messageBox.setWaitingMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void resetGame() {
        controller.initializeController();
        controlsDisabled = false;
        setMessage();
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
        try {
            if (d) {
                out.writeInt(AFFIRM_RESET);
                out.writeInt(AFFIRM_RESET);
            } else {
                out.writeInt(DENY_RESET);
                out.writeInt(DENY_RESET);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disableControls(boolean disable) {
        controlsDisabled = disable;
    }

    @Override
    public Color getPlayerColor() {
        if(this.player == CheckersModel.RED_PIECE) {return Color.RED;}
        else return Color.BLACK;
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
