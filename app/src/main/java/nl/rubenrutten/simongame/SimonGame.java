package nl.rubenrutten.simongame;

/**
 * Created by Ruben on 12-01-17.
 */

import android.util.Log;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static nl.rubenrutten.simongame.SimonGameState.EXPECT_INPUT;
import static nl.rubenrutten.simongame.SimonGameState.GAME_OVER;
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

    // TimeoutTask is the task that ends the game if the player didn't play fast enough
    private TimerTask timeoutTask;

    private SimonGameState gameState;

    private Timer timer = new Timer();

    SimonGame(SimonListener a_listener) {
        listener = a_listener;
        gameState = IDLE;
    }

    public void start() {
        // Only start a new game if there isn't currently a game being played
        if(gameState == IDLE) {
            reset();

            newRound();
        }
    }

    public void newRound() {
        // Add new index to the sequence and start highlighting the buttons
        currentIndex = 0;
        sequence.add((int)Math.floor(Math.random() * 4));

        gameState = HIGHLIGHTING;

        listener.onNextRound(sequence.size());

        highlightNextButton(0);
    }

    public void setState(SimonGameState _state) {
        setState(_state, "");
    }

    public void setState(SimonGameState _state, String reason) {
        // Set the game state. In case of a game over, also set the reason.
        gameState = _state;

        switch(gameState) {
            case EXPECT_INPUT:
                listener.onExpectInput();
                timeoutTask = new TimeoutTask(this);
                timer.schedule(timeoutTask, timeoutLength);
                break;
            case GAME_OVER:
                listener.onGameOver(reason);
                timeoutTask.cancel();
                break;
            default:
                Log.w("SimonGame", "Not implemented state");
                break;
        }
    }

    public void highlightNextButton(int index) {
        // Highlight a button only during the HIGHLIGHTING state (to prevent buttons from being
        // highlit after a player stopped the game
        if(gameState == HIGHLIGHTING) {
            TimerTask highlightNext = new HighlightTask(index, this);

            timer.schedule(highlightNext, 1000);
        }
    }

    public SimonListener getListener() {
        return listener;
    }

    public ArrayList<Integer> getSequence() {
        return sequence;
    }

    public void stop() {
        // Stop the game, only available when the game is actively being played
        if(gameState == HIGHLIGHTING || gameState == EXPECT_INPUT) {
            timeoutTask.cancel();
            setState(GAME_OVER, "quit");
        }
    }

    public void reset() {
        // Reset function to make sure everything is set back to default values
        sequence.clear();
        currentIndex = 0;
        score = 0;
        gameState = IDLE;
    }

    public void hit(int input) {
        // Register a hit of the player
        if(sequence.get(currentIndex) == input) {
            currentIndex++;
            score += currentIndex;

            if(currentIndex == sequence.size()) {
                timeoutTask.cancel();
                timer.purge();
                newRound();
            }else{
                timeoutTask.cancel();
                timer.purge();
                timeoutTask = new TimeoutTask(this);
                timer.schedule(timeoutTask, timeoutLength);
            }

            return;
        }else{
            listener.onGameOver("wrong");
        }
    }

    public int getScore() {
        return score;
    }
}

