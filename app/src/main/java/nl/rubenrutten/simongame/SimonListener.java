package nl.rubenrutten.simongame;

/**
 * Created by Ruben on 12-01-17.
 */

public interface SimonListener {
    public void onGameOver(String reason);
    public void onExpectInput();
    public void onHighlight(int id);
    public void onNextRound(int round);
}