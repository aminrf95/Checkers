package userinterface;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.Socket;

public class JoinMenu extends Region {

    private SceneSwitcher sceneSwitcher;
    private BorderPane mainPane;
    private VBox menuItems;

    public JoinMenu(SceneSwitcher sceneSwitcher) {
        this.sceneSwitcher = sceneSwitcher;
        mainPane = new BorderPane();
        this.getChildren().add(mainPane);
        menuItems = new VBox();
        menuItems.setAlignment(Pos.CENTER);
        menuItems.setPadding(new Insets(40,40,40,40));
        menuItems.setSpacing(10);
        mainPane.setCenter(menuItems);
        String instructions = "Enter connection details :";
        Text joinMenuText = new Text(instructions);

        HBox hostNameDetails = new HBox();
        hostNameDetails.setSpacing(24);
        Label hostNameLabel = new Label("Enter Hostname : ");
        TextField hostNameField = new TextField();
        hostNameDetails.getChildren().addAll(hostNameLabel,hostNameField);

        HBox portNumberDetails = new HBox();
        portNumberDetails.setSpacing(10);
        Label portNumberLabel = new Label("Enter port number : ");
        TextField portNumberField = new TextField();
        portNumberDetails.getChildren().addAll(portNumberLabel,portNumberField);

        HBox controls = new HBox();
        controls.setAlignment(Pos.CENTER);
        controls.setSpacing(10);
        Button connectButton = new Button("Connect");
        Button cancelButton = new Button("Cancel");
        controls.getChildren().addAll(connectButton,cancelButton);

        menuItems.getChildren().addAll(joinMenuText,hostNameDetails,portNumberDetails,controls);

        connectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                hostNameLabel.setTextFill(Color.BLACK);
                portNumberLabel.setTextFill(Color.BLACK);
                String hostName = "";
                int portNumber = -1;
                try {
                    hostName = hostNameField.getText();
                    if(hostName.length() == 0) {throw new IllegalStateException();}
                    portNumber = Integer.parseInt(portNumberField.getText());
                    Socket socket = new Socket(hostName,portNumber);
                    socket.getInputStream().read();
                    sceneSwitcher.startClientGame(socket);
                } catch (IOException e) {
                    hostNameLabel.setTextFill(Color.RED);
                    portNumberLabel.setTextFill(Color.RED);
                    joinMenuText.setText("Could not connect to "+hostName+" on port "+portNumber+". Try again : ");
                } catch (IllegalStateException e) {
                    hostNameLabel.setTextFill(Color.RED);
                    joinMenuText.setText("Enter a valid Hostname. Try again : ");
                } catch (NumberFormatException e) {
                    portNumberLabel.setTextFill(Color.RED);
                    joinMenuText.setText("Enter a valid number. Try again : ");
                }

            }
        });
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                hostNameLabel.setTextFill(Color.BLACK);
                portNumberLabel.setTextFill(Color.BLACK);
                hostNameField.clear();
                portNumberField.clear();
                joinMenuText.setText(instructions);
                sceneSwitcher.setMainMenuScene();
            }
        });
    }
}
