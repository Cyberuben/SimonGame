package erco.myfirstandroidgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by ercoa on 26-11-2016.
 */
public class Bat {

    private Paint paint;

    // unit: pixels
    private float length = 600;
    private float xPos;
    private float yPos;

    public Bat(float xStart, float yStart) {
        paint = new Paint();
        xPos = xStart;
        yPos = yStart;
    }


    public void draw(Canvas canvas) {
        paint.setColor(Color.argb(255, 255, 255, 0));
        paint.setStrokeWidth(20);
        canvas.drawLine(xPos, yPos, xPos, yPos + length, paint);
    }


    public float getXPos() {
        return xPos;
    }


    public float getYPos() {
        return yPos;
    }

    public void setYPos(float yPos) {
        this.yPos = yPos;
    }

    public float getLength() {
        return length;
    }

    public void writeToStream(DataOutputStream daos) throws IOException {
        daos.writeFloat(yPos);
    }

}
