import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import userinterface.SceneSwitcher;

public class CheckersApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        SceneSwitcher sceneSwitcher = new SceneSwitcher(primaryStage);
        primaryStage.setTitle("Checkers");
        primaryStage.setWidth(1000);
        primaryStage.setHeight(1000);
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                windowEvent.consume();
            }
        });
        primaryStage.show();
    }
}
