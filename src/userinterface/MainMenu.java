package userinterface;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class MainMenu extends StackPane {

    private SceneSwitcher sceneSwitcher;
    private Text title;
    private VBox menuItems;
    private final double BUTTON_FONT_SIZE = 50;
    private final double BUTTON_WIDTH = 500;
    private final double BUTTON_HEIGHT = 100;

    public MainMenu(SceneSwitcher sceneSwitcher) {
        this.sceneSwitcher = sceneSwitcher;
        this.setAlignment(Pos.CENTER);

        menuItems = new VBox();
        menuItems.setAlignment(Pos.CENTER);
        menuItems.setSpacing(10);

        title = new Text("Checkers");
        title.setFont(new Font(100));

        Button playButton = new Button("Play Game");
        playButton.setFont(new Font(BUTTON_FONT_SIZE));
        playButton.setPrefSize(BUTTON_WIDTH,BUTTON_HEIGHT);

        Button hostButton = new Button("Host Game");
        hostButton.setFont(new Font(BUTTON_FONT_SIZE));
        hostButton.setPrefSize(BUTTON_WIDTH,BUTTON_HEIGHT);

        Button joinButton = new Button("Join Game");
        joinButton.setFont(new Font(BUTTON_FONT_SIZE));
        joinButton.setPrefSize(BUTTON_WIDTH,BUTTON_HEIGHT);

        Button vsComputerButton = new Button("VS Computer");
        vsComputerButton.setFont(new Font(BUTTON_FONT_SIZE));
        vsComputerButton.setPrefSize(392,BUTTON_HEIGHT);


        Button exitButton = new Button("Exit");
        exitButton.setFont(new Font(BUTTON_FONT_SIZE));
        exitButton.setPrefSize(BUTTON_WIDTH,BUTTON_HEIGHT);

        ObservableList<Integer> difficultyList = FXCollections.observableList(Arrays.asList(1,2,3,4,5,6,7,8,9,10));
        ChoiceBox<Integer> difficulty = new ChoiceBox<>(difficultyList);
        difficulty.setTooltip(new Tooltip("Sets the difficulty level of the AI."));
        difficulty.setPrefWidth(100);
        difficulty.setPrefHeight(100);
        difficulty.setValue(5);
        HBox vsComputerControls = new HBox(vsComputerButton,difficulty);
        vsComputerControls.setSpacing(10);
        vsComputerControls.setAlignment(Pos.CENTER);

        menuItems.getChildren().addAll(title,playButton,hostButton,joinButton,vsComputerControls,exitButton);
        this.getChildren().add(menuItems);

        //Set button actions
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {sceneSwitcher.startLocalGame();}
        });

        hostButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {sceneSwitcher.setHostMenuScene();}
        });

        joinButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {sceneSwitcher.setJoinMenuScene();}
        });

        vsComputerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {sceneSwitcher.startVSComputerGame(difficulty.getValue());}
        });

        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Platform.exit();
            }
        });
    }
}
