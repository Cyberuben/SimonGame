package erco.myfirstandroidgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.*;
/**
 * Created by ercoa on 26-11-2016.
 */
public class AvansPong extends AppCompatActivity {

    private static final String TAG = "AvansPong";

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new GameView(this);
        setContentView(gameView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    class GameView extends SurfaceView implements Runnable {

        // start value unequal to 0 to avoid division by zero at 1st frame
        private long fps = 1;
        private long sendsPerSecond = 0;
        private long receivesPerSecond = 0;
        private long nrFrames = 0;
        private Thread gameThread = null;
        private NetworkReceiveThread networkReceiveThread = new NetworkReceiveThread();
        private NetworkSendThread networkSendThread = new NetworkSendThread();
        private SurfaceHolder ourHolder = getHolder();
        private volatile boolean playing;
        private Canvas canvas;

        private Ball ball1, ball2;
        private Bat myBat;
        private Bat adversaryBat;
        private Background background = new Background();
        // player's finger position on screen
        private float yPrevFinger = 0;

        private static final String MASTER_IP_ADDRESS = "192.168.1.127";
        //private static final String MASTER_IP_ADDRESS = "145.102.67.13";
        private static final String SLAVE_IP_ADDRESS = "192.168.1.172";
        //private static final String SLAVE_IP_ADDRESS = "145.102.75.230";
        private static final int PORT = 4445;
        private static final int SEND_PERIOD = 50;
        private String myIp;
        private boolean isMaster = false;


        public GameView(Context context) {
            super(context);
            myIp = Util.getMyIp(context);
            if(myIp.equals(MASTER_IP_ADDRESS)) {
                isMaster = true;
            }

            if(isMaster) {
                Log.i(TAG, "isMaster");
                ball1 = new MasterBall(this, 5, R.drawable.erco);
                ball2 = new MasterBall(this, 10, R.drawable.robin);
                myBat = new Bat(900, 600);
                adversaryBat = new Bat(100, 600);
            } else {
                Log.i(TAG, "isSlave");
                ball1 = new SlaveBall(this, R.drawable.erco);
                ball2 = new SlaveBall(this, R.drawable.robin);
                myBat = new Bat(100, 600);
                adversaryBat = new Bat(900, 600);
            }
        }


        @Override
        public void run() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Log.e(TAG, "Error: delaying start");
            }
            while (playing) {
                nrFrames++;
                long startFrameTime = System.currentTimeMillis();

                update();
                draw();

                long timeThisFrame = System.currentTimeMillis() - startFrameTime;
                if (timeThisFrame > 0) {
                    fps = 1000 / timeThisFrame;
                }

                // slowly varying printableFps
                if (nrFrames % 100 == 0) {
                    background.setPrintableFps(fps);
                }

            }

        }

        public void update() {

            if(isMaster) {
                updateMasterBall(ball1);
                updateMasterBall(ball2);
            }
        }

        private void updateMasterBall(Ball ball) {
            ball.handleWallCollision(0, getWidth(), 0, getHeight(), background.getScoreBoard());
            // own bat
            ball.handleBatCollision(myBat.getXPos() - 10, myBat.getXPos() + 10,
                    myBat.getYPos(), myBat.getYPos() + myBat.getLength());

            // adversary bat
            ball.handleBatCollision(adversaryBat.getXPos() - 10, adversaryBat.getXPos() + 10,
                    adversaryBat.getYPos(), adversaryBat.getYPos() + adversaryBat.getLength());

            ball.checkBoosterStillOn();
            ball.updatePosition(fps);
        }


        public void draw() {

            // Make sure our drawing surface is valid or it crashes
            if (ourHolder.getSurface().isValid()) {
                // Lock the canvas ready to draw
                canvas = ourHolder.lockCanvas();

                background.draw(canvas, isMaster);
                myBat.draw(canvas);
                adversaryBat.draw(canvas);
                ball1.draw(canvas);
                ball2.draw(canvas);

                // draw everything to the screen
                ourHolder.unlockCanvasAndPost(canvas);
            }

        }

        // If the activity is paused/stopped shutdown the game thread.
        public void pause() {
            Log.i(TAG, "GameView.pause() is called.");
            // setting playing to false breaks the infinite while loops in game thread, network
            // receive thread, and network send thread
            playing = false;
            try {
                // join lets current thread wait for completion of game thread, network receive
                // thread and network send thread
                gameThread.join();
                // can't terminate a thread that is blocking on a udp socket receive; so close
                // socket first
                networkReceiveThread.closeSocket();
                networkReceiveThread.join();
                // network send thread is not blocking, closing socket is not a necessity
                networkSendThread.closeSocket();
                networkSendThread.join();
            } catch (InterruptedException e) {
                Log.e(TAG, "Error: joining thread");
            }

        }

        // If the activity is started then start the game thread.
        public void resume() {
            Log.i(TAG, "GameView.resume() is called.");
            playing = true;
            gameThread = new Thread(this);
            gameThread.start();

            networkReceiveThread.start();
            networkSendThread.start();
        }

        // SurfaceView implements a touch screen listener, so override to detect screen touches
        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {

            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

                // Player has touched the screen
                case MotionEvent.ACTION_DOWN:
                    yPrevFinger = motionEvent.getY();
                    break;

                // Player has removed finger from screen
                case MotionEvent.ACTION_UP:
                    break;

                case MotionEvent.ACTION_MOVE:
                    float yCurrentFinger = motionEvent.getY();
                    myBat.setYPos(myBat.getYPos() + yCurrentFinger - yPrevFinger);
                    yPrevFinger = yCurrentFinger;
                    break;
            }
            return true;
        }


        class NetworkSendThread extends Thread {

            private DatagramSocket sendSock;
            @Override
            public void run() {
                try {
                    sendSock = new DatagramSocket();

                    InetAddress ipAddress;
                    ipAddress = isMaster ? InetAddress.getByName(SLAVE_IP_ADDRESS) :
                            InetAddress.getByName(MASTER_IP_ADDRESS);
                    Log.i(TAG, "network send thread is running");

                    while (playing && !sendSock.isClosed()) {
                        long startSendTime = System.currentTimeMillis();

                        // make sending sufficiently slow; in this way it is ensured that receiving
                        // can keep up so that it is not reading outdated state from the buffer
                        try {
                            Thread.sleep(SEND_PERIOD);
                        } catch (InterruptedException e) {
                            Log.e(TAG, "Error: delaying send socket");
                        }

                        final ByteArrayOutputStream baos=new ByteArrayOutputStream();
                        final DataOutputStream daos=new DataOutputStream(baos);
                        myBat.writeToStream(daos);
                        if(isMaster) {
                            ball1.writeToStream(daos);
                            ball2.writeToStream(daos);
                            background.getScoreBoard().writeToStream(daos);
                        }
                        daos.close();
                        byte[] sendData = baos.toByteArray();
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, PORT);
                        sendSock.send(sendPacket);
                        // Log.i(TAG, "send to socket");

                        long timeThisSend = System.currentTimeMillis() - startSendTime;
                        if (timeThisSend > 0) {
                            sendsPerSecond = 1000 / timeThisSend;
                        }

                        // slowly varying printable network-sends-per-second
                        if (nrFrames % 100 == 0) {
                            background.setPrintableSendsPerSecond(sendsPerSecond);
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Error: sendSock");
                    e.printStackTrace();
                }
            }

            public void closeSocket() {
                sendSock.close();
            }
        }


        class NetworkReceiveThread extends Thread {
            private DatagramSocket receiveSock;

            @Override
            public void run() {
                try {
                    receiveSock = new DatagramSocket(PORT);
                    byte[] buf = new byte[64];
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    Log.i(TAG, "network receive thread is running");

                    while (playing && !receiveSock.isClosed()) {
                        long startReceiveTime = System.currentTimeMillis();

                        // blocking call
                        receiveSock.receive(packet);
                        // Log.i(TAG, "received from socket");
                        byte[] receivedData = packet.getData();
                        final ByteArrayInputStream bais=new ByteArrayInputStream(receivedData);
                        final DataInputStream dais=new DataInputStream(bais);
                        adversaryBat.setYPos(dais.readFloat());
                        if(!isMaster) {
                            ball1.readFromStream(dais);
                            ball2.readFromStream(dais);
                            background.getScoreBoard().reverseReadFromStream(dais);
                        }
                        dais.close();

                        long timeThisReceive = System.currentTimeMillis() - startReceiveTime;
                        if (timeThisReceive > 0) {
                            receivesPerSecond = 1000 / timeThisReceive;
                        }

                        // slowly varying printable network-receives-per-second
                        if (nrFrames % 100 == 0) {
                            background.setPrintableReceivesPerSecond(receivesPerSecond);
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Error: receiveSock");
                    e.printStackTrace();
                }
            }

            public void closeSocket() {
                // Any thread currently blocked in receive(java.net.DatagramPacket) will throw a
                // SocketException, if close() is called on the socket. This is normal behavior,
                // see https://docs.oracle.com/javase/7/docs/api/java/net/DatagramSocket.html#close()
                receiveSock.close();
            }
        }
    }
}
