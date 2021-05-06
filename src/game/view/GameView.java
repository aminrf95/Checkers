package game.view;

import game.gametype.Game;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import userinterface.SceneSwitcher;

import java.util.Optional;

public class GameView extends StackPane {

    SceneSwitcher sceneSwitcher;

    public GameView(SceneSwitcher sceneSwitcher, BoardComponent boardComponent, Game game) {
        this.sceneSwitcher = sceneSwitcher;
        this.setAlignment(Pos.CENTER);

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        //vBox.setPadding(new Insets(20));
        getChildren().add(vBox);

        HBox boardWrapper = new HBox(boardComponent);
        boardWrapper.setAlignment(Pos.CENTER);

        MessageBox messageBox = new MessageBox(game.getPlayerColor(), sceneSwitcher, game);
        game.registerMessageBox(messageBox);

        HBox controls = new HBox(messageBox);
        controls.setAlignment(Pos.CENTER);
        controls.setSpacing(10);
        vBox.getChildren().addAll(boardWrapper,controls);
    }
}