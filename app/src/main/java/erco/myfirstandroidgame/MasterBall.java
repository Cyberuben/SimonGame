package erco.myfirstandroidgame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by ercoa on 26-11-2016.
 */
public class MasterBall implements Ball {

    private Bitmap bitmap;
    private Paint paint;

    // start positions; unit: pixels
    private float xPos = 20;
    private float yPos = 80;

    // unit: pixels per second
    private float xSpeed = 700;
    private float ySpeed = 700;

    // speed booster after bat collision
    private boolean boosterOn = false;
    private float boosterSpeed = 2;
    private long boosterStartTime = 0;
    // unit: milliseconds
    private long boosterPeriod = 1000;


    public MasterBall(SurfaceView surfaceView, float startPositionOffset, int image) {
        paint = new Paint();
        bitmap = BitmapFactory.decodeResource(surfaceView.getResources(), image);
        xPos += startPositionOffset;
        yPos += startPositionOffset;

    }


    public void handleWallCollision(float leftWall, float rightWall, float topWall,
                                    float bottomWall, ScoreBoard scoreBoard) {
        // handle wall collisions
        if (xPos <= leftWall) {
            // left wall collision, you've scored!
            if(xSpeed < 0) {
                xSpeed = -xSpeed;
            }
            scoreBoard.myPoints++;
        }
        if (xPos >= rightWall - bitmap.getWidth()) {
            // right wall collision, adversary has scored!
            if(xSpeed > 0) {
                xSpeed = -xSpeed;
            }
            scoreBoard.adversaryPoints++;
        }
        if (yPos <= topWall) {
            // top wall collision
            if(ySpeed < 0) {
                ySpeed = -ySpeed;
            }
        }
        if (yPos >= bottomWall - bitmap.getHeight()) {
            // bottom wall collision
            if(ySpeed > 0) {
                ySpeed = -ySpeed;
            }
        }
    }


    public void handleBatCollision(float xLeft, float xRight, float yTop, float yBottom) {
        if (yPos > yTop && yPos < yBottom) {
            if (xPos > xLeft && xPos < xRight) {
                // bat collision
                xSpeed = -xSpeed;

                if (!boosterOn) {
                    boosterOn = true;
                    xSpeed *= boosterSpeed;
                    ySpeed *= boosterSpeed;
                    boosterStartTime = System.currentTimeMillis();
                }
            }
        }
    }

    public void checkBoosterStillOn() {
        if (boosterOn) {
            if (System.currentTimeMillis() - boosterStartTime >= boosterPeriod) {
                // turn off booster
                boosterOn = false;
                xSpeed /= boosterSpeed;
                ySpeed /= boosterSpeed;

            }
        }
    }


    public void updatePosition(long fps) {
        xPos += xSpeed / fps;
        yPos += ySpeed / fps;
    }


    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, xPos, yPos, paint);
    }



    public void writeToStream(DataOutputStream daos) throws IOException {
        daos.writeFloat(xPos);
        daos.writeFloat(yPos);
    }


    public void readFromStream(DataInputStream dais) throws IOException {
        throw new UnsupportedOperationException();
    }
}
