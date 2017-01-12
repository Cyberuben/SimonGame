package nl.rubenrutten.simongame;

import java.util.ArrayList;
import java.util.TimerTask;

/**
 * Created by Ruben on 12-01-17.
 */

public class HighlightTask extends TimerTask {
    private int index;
    private SimonGame gameInstance;

    public HighlightTask(int _index, SimonGame _instance) {
        index = _index;
        gameInstance = _instance;
    }

    public void run() {
        ArrayList<Integer> sequence = gameInstance.getSequence();

        gameInstance.getListener().onHighlight(sequence.get(index));

        index++;
        if(index < sequence.size()) {
            gameInstance.highlightNextButton(index);
        }else{
            gameInstance.setState(SimonGameState.EXPECT_INPUT);
        }
    }
}
