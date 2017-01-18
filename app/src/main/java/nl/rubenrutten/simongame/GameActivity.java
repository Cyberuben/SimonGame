package nl.rubenrutten.simongame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private EditText highscoreName;

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

        // Trigger SimonGame to know a button was hit
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
            // Allow buttons to be pressed
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

            // Change round text and disable buttons for the highlighting stage
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

            // Highlight a specific button
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

            // Show game over dialog with highscore name box
            public void onGameOver(String reason) {
                gameoverReason = reason;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this, R.style.DialogTheme);

                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.gameover_dialog, null);

                        builder.setView(dialogView);

                        builder.setTitle(getString(R.string.gameoverTitle));

                        builder.setPositiveButton(getString(R.string.mainMenu),new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick (DialogInterface dialog,int which){
                                submitHighscore();
                                finish();
                            }
                        });

                        builder.setNegativeButton(getString(R.string.retry),new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick (DialogInterface dialog,int which){
                                simonGame.reset();
                                simonGame.start();
                                submitHighscore();
                            }
                        });

                        AlertDialog alertDialog = builder.create();

                        TextView gameoverReasonText = (TextView)dialogView.findViewById(R.id.gameoverReason);
                        TextView scoreText = (TextView)dialogView.findViewById(R.id.score);
                        scoreText.setText(String.format(getString(R.string.scoreText), simonGame.getScore()));
                        highscoreName = (EditText)dialogView.findViewById(R.id.highscoreName);

                        switch(gameoverReason) {
                            case "quit":
                                gameoverReasonText.setText(getString(R.string.gameoverQuit));
                                break;
                            case "wrong":
                                gameoverReasonText.setText(getString(R.string.gameoverWrong));
                                break;
                            case "timeout":
                                gameoverReasonText.setText(getString(R.string.gameoverTimeout));
                                break;
                        }
                        alertDialog.show();
                    }
                });
            }
        };

        simonGame = new SimonGame(listener);

        simonGame.start();
    }

    // Update score text
    public void updateScore() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Highlight button
                scoreText.setText(String.format(getString(R.string.scoreText), simonGame.getScore()));
            }
        });
    }

    // Submit highscore on closing of the dialog, only when a name was entered
    public void submitHighscore() {
        if(highscoreName.getText().length() > 0) {
            HighscoreDBHandler highscores = new HighscoreDBHandler(getApplicationContext());
            highscores.addHighscore(highscoreName.getText().toString(), simonGame.getScore());
        }
    }
}
