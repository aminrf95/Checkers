import javafx.application.Application;
import javafx.stage.Stage;
import userinterface.SceneSwitcher;

public class CheckersApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        SceneSwitcher sceneSwitcher = new SceneSwitcher(primaryStage);
        primaryStage.setTitle("Checkers");
        primaryStage.show();
    }
}
