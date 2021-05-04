package game.view;

import game.gametype.Game;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import userinterface.SceneSwitcher;

import java.util.Optional;

public class GameView extends Region {

    SceneSwitcher sceneSwitcher;

    public GameView(SceneSwitcher sceneSwitcher, BoardComponent boardComponent, Game game) {
        this.sceneSwitcher = sceneSwitcher;
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(40,40,40,40));
        getChildren().add(vBox);
        Button quitButton = new Button("Quit");
        HBox controls = new HBox(quitButton);
        controls.setAlignment(Pos.CENTER);
        controls.setSpacing(10);
        vBox.getChildren().addAll(boardComponent,controls);

        quitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Alert confirmQuitAlert = new Alert(
                        Alert.AlertType.NONE,
                        "Are you sure you want to quit?",
                        ButtonType.YES,ButtonType.NO
                );
                confirmQuitAlert.setTitle("Quit game");
                Optional<ButtonType> result = confirmQuitAlert.showAndWait();
                if(result.isPresent() && result.get() == ButtonType.YES) {
                    game.quitGame();
                    sceneSwitcher.setMainMenuScene();
                }
            }
        });
    }
}