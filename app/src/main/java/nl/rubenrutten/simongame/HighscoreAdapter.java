package nl.rubenrutten.simongame;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.Date;

/**
 * Created by anton on 18-1-2017.
 */

public class HighscoreAdapter extends CursorAdapter {

    public HighscoreAdapter(Context context, Cursor c){
        super(context, c,0);
    }

    public View newView(Context context, Cursor cursor, ViewGroup viewGroup){
        View view = LayoutInflater.from(context).inflate(R.layout.highscorerow, viewGroup, false);
        return view;
    }
    //bind textviews with listview
    public void bindView(View view, Context context, Cursor cursor){
        TextView player = (TextView) view.findViewById(R.id.player);
        TextView highscore = (TextView) view.findViewById(R.id.highscore);
        TextView date = (TextView) view.findViewById(R.id.date);

        player.setText(cursor.getString(cursor.getColumnIndex("name")));
        highscore.setText(String.format("%,d", cursor.getInt(cursor.getColumnIndex("score"))));
        date.setText(cursor.getString(cursor.getColumnIndex("date")));
    }
}
