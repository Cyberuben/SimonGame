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

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "highscore_layout.db";
    private static final String DB_TABLE_NAME = "highscore_layout";

    private static final String COLOMN_ID = "_id";
    private static final String COLOMN_NAME = "name";
    private static final String COLOMN_SCORE = "score";
    private static final String COLOMN_DATE = "date";


    public HighscoreDBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    public void onCreate(SQLiteDatabase db) {
        String CREATE_HIGHSCORE_TABLE = "CREATE TABLE " + DB_TABLE_NAME +
                "(" +
                COLOMN_ID + " INTEGER PRIMARY KEY," +
                COLOMN_NAME + " TEXT, " +
                COLOMN_SCORE + " INTEGER, " +
                COLOMN_DATE + " TEXT " +
                ")";
        db.execSQL(CREATE_HIGHSCORE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + DB_TABLE_NAME);
        onCreate(db);
    }

    public void addHighscore(Highscore highscore) {
        ContentValues values = new ContentValues();
        values.put(COLOMN_NAME, highscore.getName());
        values.put(COLOMN_SCORE, highscore.getScore());
        values.put(COLOMN_DATE, highscore.getDate().toString());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(DB_TABLE_NAME, null, values);

        db.close();
    }
    //heeft nog aanpassing nodig
    public Cursor getHighscore() {

    public void getHighscore(String name, int score, Date date) {
        String query_all = "SELECT *  FROM " + DB_TABLE_NAME;


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query_all, null);

        cursor.moveToFirst();
        while (cursor.moveToFirst()) {
            Log.i(TAG, cursor.getString(cursor.getColumnIndex(COLOMN_NAME)));
            Log.i(TAG, cursor.getString(cursor.getColumnIndex(COLOMN_SCORE)));
            Log.i(TAG, cursor.getString(cursor.getColumnIndex(COLOMN_DATE)));
        }
        return cursor;
    }
}