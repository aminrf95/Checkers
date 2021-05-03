package userinterface;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class MainMenu extends Region {

    private SceneSwitcher sceneSwitcher;
    private Text title;
    private BorderPane mainPane;
    private VBox menuItems;

    public MainMenu(SceneSwitcher sceneSwitcher) {
        this.sceneSwitcher = sceneSwitcher;
        mainPane = new BorderPane();
        mainPane.setPadding(new Insets(40,40,40,40));
        getChildren().add(mainPane);
        title = new Text("Checkers");
        title.setFont(new Font(20));
        mainPane.setTop(title);
        menuItems = new VBox();
        menuItems.setAlignment(Pos.CENTER);
        menuItems.setSpacing(10);
        mainPane.setCenter(menuItems);
        Button playButton = new Button("Play Game");
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                sceneSwitcher.startLocalGame();
            }
        });
        Button hostButton = new Button("Host Game");
        hostButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                sceneSwitcher.setHostMenuScene();
            }
        });
        Button joinButton = new Button("Join Game");
        joinButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                sceneSwitcher.setJoinMenuScene();
            }
        });
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Platform.exit();
            }
        });
        menuItems.getChildren().addAll(playButton,hostButton,joinButton,exitButton);
    }
}
