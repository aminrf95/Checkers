package userinterface;

import game.gametype.LocalGame;
import game.gametype.OnlineGame;
import game.logic.GameController;
import game.view.BoardComponent;
import game.view.GameView;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


import java.io.IOException;
import java.net.Socket;

/**
 * This class handles scene switching.
 */
public class SceneSwitcher {

    private Stage primaryStage;
    private Scene mainMenuScene;
    private Scene hostMenuScene;
    private Scene joinMenuScene;

    public SceneSwitcher(Stage primaryStage) {
        this.primaryStage = primaryStage;
        mainMenuScene = new Scene(new MainMenu(this));
        hostMenuScene = new Scene(new HostMenu(this));
        joinMenuScene = new Scene(new JoinMenu(this));
        setMainMenuScene();
    }

    public void setGameScene(GameView gameView) {
        Scene gameScene = new Scene(gameView);
        primaryStage.setScene(gameScene);
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
        GameView gameView = new GameView(this, gameController.getBoardComponent(), localGame);
        setGameScene(gameView);
    }

    public void startClientGame(Socket socket) throws IOException {
        int player = -1;
        GameController gameController = new GameController();
        OnlineGame onlineGame = new OnlineGame(gameController, socket, player);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                onlineGame.shutDownConnection();
            }
        });
        GameView gameView = new GameView(this, gameController.getBoardComponent(), onlineGame);
        setGameScene(gameView);
    }

    public void startHostGame(Socket socket) throws IOException {
        int player = 1;
        GameController gameController = new GameController();
        OnlineGame onlineGame = new OnlineGame(gameController, socket, player);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                onlineGame.shutDownConnection();
            }
        });
        GameView gameView = new GameView(this, gameController.getBoardComponent(), onlineGame);
        setGameScene(gameView);
    }
}
