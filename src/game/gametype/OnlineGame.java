package game.gametype;

import game.view.BoardComponent;
import game.logic.CheckersModel;
import game.view.CheckersSquareComponent;
import game.logic.GameController;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class OnlineGame extends Game {

    public class ConnectionThread extends Thread {

        @Override
        public void run() {
            int i;
            int j;
            while (true) {
                try {
                    i = in.readInt();
                    j = in.readInt();
                    if(i == -1 || j == -1) {
                        System.out.println("END OF STREAM");
                        break;
                    }
                    controller.selectSquare(i,j);
                } catch (IOException e) {
                    System.out.println("Connection Interrupted");
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
                        if(gameController.getCurrentPlayer() != player) {return;}
                        controller.selectSquare(iCoord,jCoord);
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
        System.out.println("Shutting down connection...");
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
}
