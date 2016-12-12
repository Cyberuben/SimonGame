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
public class SlaveBall implements Ball {

    private static final String TAG = "AvansPong";

    private Bitmap bitmap;
    private Paint paint;

    // start positions; unit: pixels
    private float xPos;
    private float yPos;

    public SlaveBall(SurfaceView surfaceView, int image) {
        paint = new Paint();
        bitmap = BitmapFactory.decodeResource(surfaceView.getResources(), image);

    }


    public void handleWallCollision(float leftWall, float rightWall, float topWall,
                                    float bottomWall, ScoreBoard scoreBoard) {
        throw new UnsupportedOperationException();
    }


    public void handleBatCollision(float xLeft, float xRight, float yTop, float yBottom) {
        throw new UnsupportedOperationException();
    }

    public void checkBoosterStillOn() {
        throw new UnsupportedOperationException();
    }


    public void updatePosition(long fps) {
        throw new UnsupportedOperationException();
    }


    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, xPos, yPos, paint);
    }


    public void writeToStream(DataOutputStream daos) throws IOException {
        daos.writeFloat(xPos);
        daos.writeFloat(yPos);
    }

    public void readFromStream(DataInputStream dais) throws IOException {
        xPos = dais.readFloat();
        yPos = dais.readFloat();
    }
}
