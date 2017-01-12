package nl.rubenrutten.simongame;

/**
 * Created by Ruben on 12-01-17.
 */

import android.util.Log;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static nl.rubenrutten.simongame.SimonGameState.EXPECT_INPUT;
import static nl.rubenrutten.simongame.SimonGameState.HIGHLIGHTING;
import static nl.rubenrutten.simongame.SimonGameState.IDLE;

enum SimonGameState {
    IDLE,
    HIGHLIGHTING,
    EXPECT_INPUT,
    GAME_OVER
};

public class SimonGame {
    private SimonListener listener;
    ArrayList<Integer> sequence = new ArrayList<>();

    private int score = 0;
    private int timeoutLength = 10000;
    private int currentIndex = 0;
    private TimerTask timeoutTask;

    private SimonGameState gameState;

    private Timer timer = new Timer();

    SimonGame(SimonListener a_listener) {
        listener = a_listener;
        gameState = IDLE;

        timeoutTask = new TimeoutTask(this);
    }

    public void start() {
        newRound();
    }

    public void newRound() {
        currentIndex = 0;
        sequence.add((int)Math.round(Math.random() * 4));

        gameState = HIGHLIGHTING;

        listener.onNextRound(sequence.size());

        highlightNextButton(0);
    }

    public void setState(SimonGameState _state) {
        setState(_state, "");
    }

    public void setState(SimonGameState _state, String reason) {
        gameState = _state;

        switch(gameState) {
            case EXPECT_INPUT:
                listener.onExpectInput();
                timer.schedule(timeoutTask, timeoutLength);
                break;
            case GAME_OVER:
                listener.onGameOver(reason);
                timer.cancel();
                break;
            default:
                Log.w("SimonGame", "Not implemented state");
                break;
        }
    }

    public void highlightNextButton(int index) {
        TimerTask highlightNext = new HighlightTask(index, this);

        timer.schedule(highlightNext, 1000);
    }

    public SimonListener getListener() {
        return listener;
    }

    public ArrayList<Integer> getSequence() {
        return sequence;
    }

    public void stop() {
        if(gameState == HIGHLIGHTING || gameState == EXPECT_INPUT) {
            timer.cancel();
            listener.onGameOver("quit");
        }
    }

    public void hit(int input) {
        timer.cancel();
        if(sequence.get(currentIndex) == input) {
            currentIndex++;
            score += currentIndex;

            if(currentIndex == sequence.size()) {
                newRound();
            }else{
                timer.schedule(timeoutTask, timeoutLength);
            }

            return;
        }else{
            listener.onGameOver("wrong");
        }
    }
}

