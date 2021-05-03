import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class handles scene switching.
 */
public class MainController {

    private Stage primaryStage;
    private Scene mainMenuScene;
    private Scene hostMenuScene;
    private Scene joinMenuScene;

    public MainController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        mainMenuScene = new Scene(new MainMenu(this));
        hostMenuScene = new Scene(new HostMenu(this));
        joinMenuScene = new Scene(new JoinMenu(this));
        setMainMenuScene();
    }

    public void setGameScene(BoardComponent boardComponent) {
        StackPane boardContainer = new StackPane(boardComponent);
        boardContainer.setAlignment(Pos.CENTER);
        boardContainer.setPadding(new Insets(40,40,40,40));
        Scene gameScene = new Scene(boardContainer);
        primaryStage.setScene(gameScene);
    }

    public void setOnlineGameScene(boolean host) {
        GameController gameController = new GameController();
        Socket socket = null;
        int player;
        if(host) {
            System.out.println("HOST");
            player = 1;
            try {
                ServerSocket serverSocket = new ServerSocket(7777);
                socket = serverSocket.accept();
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Client");
            player = -1;
            try {
                socket = new Socket("localhost", 7777);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        OnlineGame onlineGame = new OnlineGame(gameController, socket, player);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                onlineGame.shutDownConnection();
            }
        });
        setGameScene(gameController.getBoardComponent());
    }

    public void setMainMenuScene() {
        primaryStage.setScene(mainMenuScene);
    }

    public void setJoinMenuScene() {
        primaryStage.setScene(joinMenuScene);
    }

    public void setHostMenuScene() {
        primaryStage.setScene(hostMenuScene);
    }

    public void startLocalGame() {
        GameController gameController = new GameController();
        LocalGame localGame = new LocalGame(gameController);
        setGameScene(gameController.getBoardComponent());
    }

    public void startClientGame(Socket socket) throws IOException {
        System.out.println("Client");
        int player = -1;
        GameController gameController = new GameController();
        OnlineGame onlineGame = new OnlineGame(gameController, socket, player);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                onlineGame.shutDownConnection();
            }
        });
        setGameScene(gameController.getBoardComponent());
    }

    public void startHostGame(Socket socket) throws IOException {
        System.out.println("Host");
        int player = 1;
        GameController gameController = new GameController();
        OnlineGame onlineGame = new OnlineGame(gameController, socket, player);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                onlineGame.shutDownConnection();
            }
        });
        setGameScene(gameController.getBoardComponent());
    }
}
