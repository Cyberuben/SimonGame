package nl.rubenrutten.simongame;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Ruben on 16-01-17.
 */

public class GameActivity extends AppCompatActivity {
    private SimonGame simonGame;
    private SimonListener listener;

    private Button[] buttons;
    private TextView bottomText;

    private int round;
    private int highlitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simongame);

        buttons = new Button[4];

        buttons[0] = (Button)findViewById(R.id.green);
        buttons[1] = (Button)findViewById(R.id.red);
        buttons[2] = (Button)findViewById(R.id.blue);
        buttons[3] = (Button)findViewById(R.id.yellow);

        bottomText = (TextView)findViewById(R.id.bottomText);

        for(int i = 0; i < 4; i++) {
            buttons[i].setClickable(false);
        }

        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simonGame.hit(0);
            }
        });

        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simonGame.hit(1);
            }
        });

        buttons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simonGame.hit(2);
            }
        });

        buttons[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simonGame.hit(3);
            }
        });

        listener = new SimonListener() {
            public void onExpectInput() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Set buttons to enabled
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                for(int i = 0; i < 4; i++) {
                                    buttons[i].setClickable(true);
                                }
                            }
                        }, 1000);
                    }
                });
            }

            public void onNextRound(int _round) {
                round = _round;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Disable buttons
                        for(int i = 0; i < 4; i++) {
                            buttons[i].setClickable(false);
                        }

                        // Set text at the bottom to round number
                        bottomText.setText("ROUND #"+round);
                    }
                });
            }

            public void onHighlight(int input) {
                highlitButton = input;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Highlight button
                        buttons[highlitButton].setSelected(true);

                        // Remove the highlight after a few seconds
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                buttons[highlitButton].setSelected(false);
                            }
                        }, 750);
                    }
                });
            }

            public void onGameOver(String reason) {
                // Show dialog
                Log.i("GameActivity", reason);
            }
        };

        simonGame = new SimonGame(listener);

        simonGame.start();
    }
}
