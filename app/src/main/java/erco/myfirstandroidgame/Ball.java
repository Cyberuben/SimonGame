package erco.myfirstandroidgame;

import android.graphics.Canvas;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by ercoa on 29-11-2016.
 */
public interface Ball {
    public void handleWallCollision(float leftWall, float rightWall, float topWall,
                                    float bottomWall, ScoreBoard scoreBoard);
    public void handleBatCollision(float xLeft, float xRight, float yTop, float yBottom);
    public void checkBoosterStillOn();
    public void updatePosition(long fps);
    public void draw(Canvas canvas);
    public void writeToStream(DataOutputStream daos) throws IOException;
    public void readFromStream(DataInputStream dais) throws IOException;
}
