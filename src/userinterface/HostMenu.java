package userinterface;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HostMenu extends StackPane {

    public class ServerThread extends Thread {

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(0);
                hostName.setText(InetAddress.getLocalHost().getHostAddress());
                portNumber.setText("" + serverSocket.getLocalPort());
                socket = serverSocket.accept();
                connectButton.setDisable(false);
                hostMenuText.setText("Client found! Press connect to start game.");
            } catch (IOException e) {
            }
        }
    }

    private SceneSwitcher sceneSwitcher;
    private ServerSocket serverSocket;
    private Socket socket;
    private ServerThread serverThread;
    private Text hostMenuText;
    private Text hostName;
    private Text portNumber;
    private Button connectButton;
    private Button searchButton;

    public HostMenu(SceneSwitcher sceneSwitcher) {
        this.sceneSwitcher = sceneSwitcher;
        VBox menuItems = new VBox();
        menuItems.setAlignment(Pos.CENTER);
        menuItems.setPadding(new Insets(40,40,40,40));
        menuItems.setSpacing(10);
        this.getChildren().add(menuItems);
        String instructions = "Press the Search button to initiate connection.";
        hostMenuText = new Text(instructions);
        hostName = new Text("");
        portNumber = new Text("");
        searchButton = new Button("Search");
        connectButton = new Button("Connect");
        connectButton.setDisable(true);
        Button cancelButton = new Button("Cancel");
        HBox controls = new HBox(searchButton,connectButton,cancelButton);
        controls.setAlignment(Pos.CENTER);
        controls.setSpacing(10);
        menuItems.getChildren().addAll(hostMenuText,hostName,portNumber,controls);

        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                search();
            }
        });
        connectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                connect();
            }
        });
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                searchButton.setDisable(false);
                connectButton.setDisable(true);
                hostMenuText.setText("Press the Search button to initiate connection.");
                hostName.setText("");
                portNumber.setText("");
                shutDownConnection();
                sceneSwitcher.setMainMenuScene();
            }
        });
    }



    public void search() {
        serverThread = new ServerThread();
        serverThread.start();
        searchButton.setDisable(true);
        hostMenuText.setText("Share these details with your opponent : ");
    }

    private void shutDownConnection() {
        try {
            if(socket != null && !socket.isClosed()) {socket.close();}
            if(serverSocket!=null && !serverSocket.isClosed()) {serverSocket.close();}
            if(serverThread != null) {serverThread.interrupt();}
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        try {
            socket.getOutputStream().write(0);
            serverSocket.close();
            sceneSwitcher.startHostGame(socket);
        } catch (IOException e) {
            hostMenuText.setText("Client disconnected. Hit cancel and try again.");
        }
    }
}
