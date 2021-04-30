import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class CheckersApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        LocalGameController gameController = new LocalGameController();

        BoardComponent boardComponent = gameController.getBoardComponent();
        StackPane pane = new StackPane(boardComponent);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(40,40,40,40));
        Scene gameScene = new Scene(pane);

        primaryStage.setScene(gameScene);
        primaryStage.setTitle("Checkers");
        primaryStage.show();
    }
}
