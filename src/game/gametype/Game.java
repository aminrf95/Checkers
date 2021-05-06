package game.gametype;

import game.view.MessageBox;
import javafx.scene.paint.Color;

public abstract class Game {

    public abstract void quitGame();

    public abstract void resetGame();

    public abstract int getCurrentPlayer();

    public abstract void registerMessageBox(MessageBox messageBox);

    public abstract int getWinner();

    public abstract void sendDecision(boolean d);

    public abstract void resetRequest();

    public abstract void disableControls(boolean disable);

    public abstract Color getPlayerColor();
}
