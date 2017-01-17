package nl.rubenrutten.simongame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button aboutButton = (Button) findViewById(R.id.aboutButton);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
<<<<<<< HEAD
                Highscore score = new Highscore();

                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

                //Setting dialog title
                alertDialog.setTitle("About us");

                //Setting dialog message
                alertDialog.setMessage("Ruben Rutten: developer" + "\n" + "Antonie de Waele: developer");

                //setting OK button
                alertDialog.setButton("OK", new DialogInterface.OnClickListener(){
=======
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.DialogTheme);

                builder.setPositiveButton("Close", new DialogInterface.OnClickListener(){
                    @Override
>>>>>>> origin/master
                    public void onClick(DialogInterface dialog, int which){
                        //Show text after dialog closed
                        Toast.makeText(getApplicationContext(),"Have fun playing", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setMessage("Ruben Rutten: developer\nAntonie de Waele: developer");
                builder.setTitle("About us");
                
                //setting OK button
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        Button startButton = (Button) findViewById(R.id.startGame);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(i);
            }
        });

        Button highscoreButton = (Button) findViewById(R.id.highscoreButton);
        highscoreButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, HighscoreList.class);
                startActivity(i);
            }
        });

    }

}
