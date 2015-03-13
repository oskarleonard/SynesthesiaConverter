package oskaro.synesthesia.oskar.leonad.franssom.synesthesiaconverter;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * This is the fragments which is displayed in the viewpager. It shows all the available games.
 */
public class GameFragment extends Fragment {

    public static final String SP_HIGHSCORE = "highScoreGames";
    private ListView listView;
    private View theView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        theView = inflater.inflate(R.layout.fragment_game, container, false);

        listView = (ListView)theView.findViewById(R.id.listView);

        return theView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateListView();
    }

    private void updateListView(){
        List<Game>gameList = gameList();

        listView.setAdapter(new GameAdapter(getActivity(), gameList));


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //listView.getItemAtPosition(position);

                Game m = (Game)listView.getItemAtPosition(position);

                Intent intent = new Intent(getActivity(), m.getActionBarActivity().getClass());
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                longPressOptions((Game)listView.getItemAtPosition(position));
                return true;
            }
        });
    }

    private void longPressOptions(final Game game) {

        final String[] options = new String[]{"RESET"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("RESET High Score")
                .setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                SharedPreferences sp = getActivity().getSharedPreferences(SP_HIGHSCORE, 0);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putInt(game.getTitle(), -1);
                                editor.apply();
                                updateListView();
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    private List<Game> gameList() {
        SharedPreferences sp = getActivity().getSharedPreferences(SP_HIGHSCORE, 0);
        List<Game>gameList = new ArrayList<>();
        int highScore = Math.round(sp.getInt(GameFiveTwoActivity.SPS_FIVE_TWO, 0)/10);
        Game FiveTwo = new Game(GameFiveTwoActivity.SPS_FIVE_TWO, "Find all the 5's among the 2's", R.drawable.game_five_two, highScore, new GameFiveTwoActivity());

        gameList.add(FiveTwo);

        return gameList;
    }




}
