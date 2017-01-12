package nl.rubenrutten.simongame;

import java.util.ArrayList;
import java.util.TimerTask;

/**
 * Created by Ruben on 12-01-17.
 */

public class TimeoutTask extends TimerTask {
    private SimonGame gameInstance;

    public TimeoutTask(SimonGame _instance) {
        gameInstance = _instance;
    }

    public void run() {
        gameInstance.setState(SimonGameState.GAME_OVER, "timeout");
    }
}
