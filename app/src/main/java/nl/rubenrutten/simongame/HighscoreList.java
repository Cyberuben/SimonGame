package nl.rubenrutten.simongame;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by anton on 17-1-2017.
 */

public class HighscoreList extends AppCompatActivity{

    HighscoreDBHandler myDB;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscore_layout);

        ListView listContent = (ListView)findViewById(R.id.highscoreListview);
        myDB = new HighscoreDBHandler(this);

        ArrayList<String> list = new ArrayList<>();
        Cursor data = myDB.getHighscore();

        HighscoreAdapter highscoreAdapter = new HighscoreAdapter(this, data);
        listContent.setAdapter(highscoreAdapter);


        if(data.getCount()== 0){
            Toast.makeText(HighscoreList.this, "nothing in the database", Toast.LENGTH_LONG).show();
        }
    }
}

