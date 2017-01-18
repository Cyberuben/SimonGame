package nl.rubenrutten.simongame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;

import static android.icu.text.MessagePattern.ArgType.SELECT;

/**
 * Created by anton on 13-1-2017.
 */

public class HighscoreDBHandler extends SQLiteOpenHelper {

    private static final String TAG = "HighscoreDBHandler";

    private static final int DB_VERSION = 3;
    private static final String DB_NAME = "highscore_layout.db";
    private static final String DB_TABLE_NAME = "highscore_layout";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_SCORE = "score";
    private static final String COLUMN_DATE = "date";


    public HighscoreDBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //Create database
    public void onCreate(SQLiteDatabase db) {
        String CREATE_HIGHSCORE_TABLE = "CREATE TABLE " + DB_TABLE_NAME +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT, " +
                COLUMN_SCORE + " INTEGER, " +
                COLUMN_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP " +
                ")";
        db.execSQL(CREATE_HIGHSCORE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_NAME);
        onCreate(db);
    }

    public void addHighscore(String name, int score) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_SCORE, score);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(DB_TABLE_NAME, null, values);

        db.close();
    }

    public Cursor getHighscore() {
        String query_all = "SELECT " +
                COLUMN_ID+", "+
                COLUMN_NAME+", "+
                COLUMN_SCORE+", "+
                "strftime('%d-%m-%Y', "+COLUMN_DATE+") AS "+COLUMN_DATE+" "+
                "FROM " + DB_TABLE_NAME+" "+
                "ORDER BY "+COLUMN_SCORE+" DESC LIMIT "+100;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query_all, null);

        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            Log.i(TAG, cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            Log.i(TAG, cursor.getString(cursor.getColumnIndex(COLUMN_SCORE)));
            Log.i(TAG, cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
        }
        db.close();
        return cursor;
    }
}