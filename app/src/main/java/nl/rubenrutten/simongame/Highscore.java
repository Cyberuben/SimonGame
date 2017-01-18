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

    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    public int getScore(){return score;}
    public void setScore(int score){this.score = score;}

    @Override
    public String toString(){return "Highscore";}

}
