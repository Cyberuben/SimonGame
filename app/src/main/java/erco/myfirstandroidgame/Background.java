package erco.myfirstandroidgame;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Canvas;

/**
 * Created by ercoa on 26-11-2016.
 */
public class Background {
    private Paint wallPaint = new Paint();
    private long printableFps = 0;
    private long printableSendsPerSecond = 0;
    private long printableReceivesPerSecond = 0;
    // game points
    private ScoreBoard scoreBoard = new ScoreBoard();

    public void draw(Canvas canvas, boolean isMaster) {
        canvas.drawColor(Color.argb(255, 26, 128, 182));

        wallPaint.setColor(Color.argb(255, 255, 255, 0));
        wallPaint.setTextSize(45);
        canvas.drawText(isMaster ? "Master" : "Slave", 20, 40, wallPaint);
        canvas.drawText("fps: " + printableFps + ", sendsPerSec: " + printableSendsPerSecond +
                ", receivesPerSec: " + printableReceivesPerSecond, 20, 80, wallPaint);
        canvas.drawText("my points: " + scoreBoard.myPoints + ", adversary points: " +
                scoreBoard.adversaryPoints, 20, 120, wallPaint);
    }

    public void setPrintableFps(long printableFps) {
        this.printableFps = printableFps;
    }

    public void setPrintableSendsPerSecond(long printableSendsPerSecond) {
        this.printableSendsPerSecond = printableSendsPerSecond;
    }

    public void setPrintableReceivesPerSecond(long printableReceivesPerSecond) {
        this.printableReceivesPerSecond = printableReceivesPerSecond;
    }

    public ScoreBoard getScoreBoard() {
        return scoreBoard;
    }
}
