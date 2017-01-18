package nl.rubenrutten.simongame;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by anton on 13-1-2017.
 */

public class Highscore {

    private String name;
    private int score;
    private String date;

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public int getScore(){
        return score;
    }

    public void setScore(int score){
        this.score = score;
    }

    public String getDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }
    public void setDate(String date){this.date = date;}

    @Override
    public String toString(){
        return "Highscore";
    }
}
