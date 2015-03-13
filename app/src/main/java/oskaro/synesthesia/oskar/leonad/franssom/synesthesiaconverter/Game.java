package oskaro.synesthesia.oskar.leonad.franssom.synesthesiaconverter;

import android.support.v7.app.ActionBarActivity;

/**
 * A Game class, used to store information for the GameAdapter. and to open the correct activity when
 * clicking on an list adapter object.
 */
public class Game {

    private String title;
    private String description;
    private int thumbNail;
    private int highScore;
    private ActionBarActivity actionBarActivity;

    public Game(String title, String description, int thumbNail, int highScore, ActionBarActivity actionBarActivity){
        this.actionBarActivity = actionBarActivity;
        this.title = title;
        this.description = description;
        this.thumbNail = thumbNail;
        this.highScore = highScore;
    }

    public String getDescription() {
        return description;
    }

    public ActionBarActivity getActionBarActivity() {
        return actionBarActivity;
    }
    public int getHighScore() {
        return highScore;
    }
    public int getThumbNail() {
        return thumbNail;
    }
    public String getTitle() {
        return title;
    }

}
