package nl.rubenrutten.simongame;

import java.util.Date;

/**
 * Created by anton on 13-1-2017.
 */

public class Highscore {

    private String name;
    private int score;
    private Date date;

    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    public int getScore(){return score;}
    public void setScore(int score){this.score = score;}

    public Date getDate(){return date;}
    public void setDate(Date date){this.date = date;}

    @Override
    public String toString(){return "Highscore";}

}
