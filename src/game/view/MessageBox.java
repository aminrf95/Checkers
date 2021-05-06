package game.view;

import game.gametype.Game;
import game.logic.CheckersModel;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import userinterface.SceneSwitcher;

/**
 * This class is used to display messages to the player during the game,
 * and to get and handle input related to those messages.
 */
public class MessageBox extends StackPane {

    private final double BUTTON_WIDTH = 50;
    private final double BUTTON_HEIGHT = 10;

    private Game game;
    private Text message;
    private Button yesButton;
    private Button noButton;
    private Button resetButton;
    private Button quitButton;
    private EventHandler<ActionEvent> quitHandler;
    private EventHandler<ActionEvent> resetRequestHandler;
    private EventHandler<ActionEvent> cancelHandler;
    private EventHandler<ActionEvent> acceptHandler;
    private EventHandler<ActionEvent> rejectHandler;

    public MessageBox(Color borderColor, SceneSwitcher sceneSwitcher, Game game) {
        this.setAlignment(Pos.CENTER);
        this.game = game;

        Rectangle background = new Rectangle(405,106,Color.WHITE);
        background.setStroke(borderColor);
        background.setStrokeWidth(5);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);

        message = new Text("");
        StackPane messagePane = new StackPane(message);
        messagePane.setPrefSize(350,100);

        VBox buttonTray = new VBox();
        buttonTray.setAlignment(Pos.CENTER);

        hBox.getChildren().addAll(messagePane,buttonTray);

        yesButton = new Button("Yes");
        yesButton.setPrefSize(BUTTON_WIDTH,BUTTON_HEIGHT);
        noButton = new Button("No");
        noButton.setPrefSize(BUTTON_WIDTH,BUTTON_HEIGHT);
        resetButton = new Button("Reset");
        resetButton.setPrefSize(BUTTON_WIDTH,BUTTON_HEIGHT);
        quitButton = new Button("Quit");
        quitButton.setPrefSize(BUTTON_WIDTH,BUTTON_HEIGHT);

        buttonTray.getChildren().addAll(yesButton,noButton,resetButton,quitButton);

        this.getChildren().addAll(background,hBox);

        setTurnMessage();

        acceptHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                game.sendDecision(true);
                game.resetGame();
                game.disableControls(false);
            }
        };

        rejectHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                game.sendDecision(false);
                setTurnMessage();
                game.disableControls(false);
            }
        };

        cancelHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                setTurnMessage();
                game.disableControls(false);
            }
        };

        resetRequestHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                game.resetRequest();
            }
        };

        quitHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                game.quitGame();
                sceneSwitcher.setMainMenuScene();
            }
        };

        quitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                setQuitConfirmation();
            }
        });

        resetButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                setResetConfirmation();
            }
        });
    }

    public void setWaitingMessage() {
        game.disableControls(true);
        message.setText("Request sent. Waiting for opponent's decision...");
        disableYesNo(true);
        disableResetQuit(true);
    }

    public void setResetRequest() {
        game.disableControls(true);
        message.setText("Your opponent is requesting a reset. Do you accept?");
        yesButton.setOnAction(acceptHandler);
        noButton.setOnAction(rejectHandler);
        disableResetQuit(true);
        disableYesNo(false);
    }

    public void setOpponentLeft() {
        game.disableControls(true);
        message.setText("Your opponent has left the game.\nPress Yes or No to exit.");
        yesButton.setOnAction(quitHandler);
        noButton.setOnAction(quitHandler);
        disableResetQuit(true);
        disableYesNo(false);
    }

    public void setResetAffirm() {
        game.disableControls(false);
        message.setText("Your opponent accepted your reset request.\n Press Yes or No to Continue.");
        yesButton.setOnAction(cancelHandler);
        noButton.setOnAction(cancelHandler);
        disableResetQuit(true);
        disableYesNo(false);
    }

    public void setResetDeny() {
        game.disableControls(false);
        message.setText("Your opponent denied your reset request.\n Press Yes or No to Continue.");
        yesButton.setOnAction(cancelHandler);
        noButton.setOnAction(cancelHandler);
        disableResetQuit(true);
        disableYesNo(false);
    }

    public void setQuitConfirmation() {
        game.disableControls(true);
        message.setText("Are you sure you want to quit the game?");
        yesButton.setOnAction(quitHandler);
        noButton.setOnAction(cancelHandler);
        disableResetQuit(true);
        disableYesNo(false);
    }

    public void setResetConfirmation() {
        game.disableControls(true);
        message.setText("Are you sure you want to reset the game?");
        yesButton.setOnAction(resetRequestHandler);
        noButton.setOnAction(cancelHandler);
        disableResetQuit(true);
        disableYesNo(false);
    }

    public void setGameOverMessage() {
        game.disableControls(true);
        int winner = game.getWinner();
        String winnerString = (winner == CheckersModel.RED_PIECE) ? "Red" : "Black";
        message.setText(winnerString+" wins!\nPress reset to play again or quit to exit.");
        disableResetQuit(false);
        disableYesNo(true);
    }

    public void setTurnMessage() {
        int player = game.getCurrentPlayer();
        String playerString = (player == CheckersModel.RED_PIECE) ? "Red" : "Black";
        message.setText(playerString+"'s turn.");
        disableResetQuit(false);
        disableYesNo(true);
    }

    private void disableYesNo(boolean disable) {
        yesButton.setDisable(disable);
        noButton.setDisable(disable);
    }

    private void disableResetQuit(boolean disable) {
        resetButton.setDisable(disable);
        quitButton.setDisable(disable);
    }

    private void disableControls(boolean disable) {
        game.disableControls(disable);
    }
}
