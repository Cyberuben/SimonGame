package nl.rubenrutten.simongame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by Ruben on 16-01-17.
 */

public class GameActivity extends AppCompatActivity {
    private SimonGame simonGame;
    private SimonListener listener;

    private Button[] buttons;
    private Button stopButton;
    private TextView bottomText;

    private int round;
    private int highlitButton;
    private String gameoverReason;

    private TextView scoreText;

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
        scoreText = (TextView)findViewById(R.id.scoreText);

        stopButton = (Button)findViewById(R.id.stop);

        for(int i = 0; i < 4; i++) {
            buttons[i].setClickable(false);
        }

        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simonGame.hit(0);
                updateScore();
            }
        });

        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simonGame.hit(1);
                updateScore();
            }
        });

        buttons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simonGame.hit(2);
                updateScore();
            }
        });

        buttons[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simonGame.hit(3);
                updateScore();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simonGame.stop();
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

                updateScore();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    // Disable buttons
                    for(int i = 0; i < 4; i++) {
                        buttons[i].setClickable(false);
                    }

                    // Set text at the bottom to round number
                    bottomText.setText(String.format(getString(R.string.roundNumber), round));
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
                gameoverReason = reason;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this, R.style.DialogTheme);

                    builder.setPositiveButton(getString(R.string.mainMenu),new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick (DialogInterface dialog,int which){
                            finish();
                        }
                    });

                    builder.setNegativeButton(getString(R.string.retry),new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick (DialogInterface dialog,int which){
                        simonGame.reset();
                        simonGame.start();
                        }
                    });

                    switch(gameoverReason) {
                        case "quit":
                            builder.setMessage(getString(R.string.gameoverQuit));
                            break;
                        case "wrong":
                            builder.setMessage(getString(R.string.gameoverWrong));
                            break;
                        case "timeout":
                            builder.setMessage(getString(R.string.gameoverTimeout));
                            break;
                    }

                    builder.setTitle(getString(R.string.gameoverTitle));

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    }
                });
            }
        };

        simonGame = new SimonGame(listener);

        simonGame.start();
    }

    public void updateScore() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            // Highlight button
            scoreText.setText(String.format(getString(R.string.scoreText), simonGame.getScore()));
            }
        });
    }
}
