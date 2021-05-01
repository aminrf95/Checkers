import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * This class handels scene switching.
 */
public class MainController {

    private Stage primaryStage;
    private Scene mainMenuScene;

    public MainController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        mainMenuScene = new Scene(new MainMenu(this));
        setMainMenuScene();
    }

    public void setGameScene() {
        LocalGameController gameController = new LocalGameController(this);
        StackPane boardContainer = new StackPane(gameController.getBoardComponent());
        boardContainer.setAlignment(Pos.CENTER);
        boardContainer.setPadding(new Insets(40,40,40,40));
        Scene gameScene = new Scene(boardContainer);
        primaryStage.setScene(gameScene);
    }

    public void setMainMenuScene() {
        primaryStage.setScene(mainMenuScene);
    }
}
