package oskaro.synesthesia.oskar.leonad.franssom.synesthesiaconverter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * An listadapter which is populated with game object.
 */

public class GameAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private TextView tvDescription, tvHighScore;
    private List<Game>games;

    public GameAdapter(Context context, List<Game> games){
        this.context = context;
        this.games = games;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return games.size();
    }

    @Override
    public Game getItem(int position) {
        return games.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.game_adapter, parent, false);

        ImageView view = (ImageView)convertView.findViewById(R.id.ivThumbNail);
        tvDescription = (TextView)convertView.findViewById(R.id.tvDescription);
        tvHighScore = (TextView)convertView.findViewById(R.id.tvHighScore);

        //Set the TextViews
        tvDescription.setText(getItem(position).getDescription());
        tvHighScore.setText("HighScore = " + getItem(position).getHighScore() + "s");

        //Set the GameThumbNail
        Picasso.with(inflater.getContext()).load(getItem(position).getThumbNail()).into(view);

        return convertView;
    }


}
