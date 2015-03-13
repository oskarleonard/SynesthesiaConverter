package oskaro.synesthesia.oskar.leonad.franssom.synesthesiaconverter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A fragment that holds a test, find all 5s among the 2s. Uses the GameFigureView to display
 * the figures on a canvas, not on a straight line.
 */
public class GameFiveTwoActivity extends ActionBarActivity{

    public static final String SPS_FIVE_TWO = "FiveTwo";
    private LinearLayout gameActivityLL;
    private GameFiveTwoView gameFigureView;
    private Button eight, nine, ten, eleven, twelve, thirteen;
    private Button[] allAnswerButtons;
    private int time = 0;

    private Handler myHandler = new Handler();

    private Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            time++;
            TextView timerTV = (TextView)findViewById(R.id.timerTV);
            timerTV.setText(getTimeString(time));
            myHandler.postDelayed(this, 100);

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        myHandler.removeCallbacks(myRunnable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_five_two);


        gameFigureView = new GameFiveTwoView(this);
        gameFigureView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));


        gameActivityLL = (LinearLayout)findViewById(R.id.gameActivityLL);

        gameActivityLL.addView(gameFigureView);


        myHandler.postDelayed(myRunnable, 500);


        initializeButtons();

    }


    private void initializeButtons(){
        //Initialize the numbers buttons
        eight = (Button)findViewById(R.id.eightBtn);
        nine = (Button)findViewById(R.id.nineBtn);
        ten = (Button)findViewById(R.id.tenBtn);
        eleven = (Button)findViewById(R.id.elevenBtn);
        twelve = (Button)findViewById(R.id.twelveBtn);
        thirteen = (Button)findViewById(R.id.thirteenBtn);

        //Assign unique tags to each button and give them a clickListener.
        allAnswerButtons = new Button[]{eight, nine, ten, eleven, twelve, thirteen};
        int theTag = 8;
        for (Button theButton : allAnswerButtons){
            theButton.setTag(Integer.toString(theTag));
            theButton.setOnClickListener(checkIfCorrect(theButton));
            theTag++;
        }

    }

    View.OnClickListener checkIfCorrect(final Button theButton){
        return new View.OnClickListener(){
            public void onClick(View view){
                if (Integer.toString(gameFigureView.nrOfTimesFive).contains(theButton.getTag().toString())){
                    userWin();
                }else{
                    userLose();
                }
            }
        };
    }

    private void userLose(){
        Toast.makeText(this, "Wrong, there were: " + gameFigureView.nrOfTimesFive, Toast.LENGTH_SHORT).show();

        this.finish();
    }

    private void userWin(){
        SharedPreferences sp = this.getSharedPreferences(GameFragment.SP_HIGHSCORE, 0);
        int highScore = sp.getInt(SPS_FIVE_TWO, -1);

        //If time less than hihscore than add it to SP, Highscore -1 means that the user has reset or havent played yet
        if(time < highScore || highScore == -1){
            Toast.makeText(this, "highScore = " + Math.round(time/10), Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt(SPS_FIVE_TWO, time);
            editor.apply();
        }
        this.finish();
    }

    //Format the timmer so it looks like i want it to.
    private String getTimeString(long millis) {
        int minutes = (int) (millis / 600);
        int seconds = (int) ((millis / 10) % 60);
        int milliseconds = (int) (millis % 10);
        return String.format("%d:%02d.%d", minutes, seconds, milliseconds);
    }


}
