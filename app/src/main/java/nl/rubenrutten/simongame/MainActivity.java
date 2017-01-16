package nl.rubenrutten.simongame;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
                Highscore score = new Highscore();

                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

                //Setting dialog title
                alertDialog.setTitle("About us");

                //Setting dialog message
                alertDialog.setMessage("Ruben Rutten: developer" + "\n" + "Antonie de Waele: developer");

                //setting OK button
                alertDialog.setButton("OK", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        //Show text after dialog closed
                        Toast.makeText(getApplicationContext(),"Have fun playing", Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog.show();
            }
        });
    }

}
