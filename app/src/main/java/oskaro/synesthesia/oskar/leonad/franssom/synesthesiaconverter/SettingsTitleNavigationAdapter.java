package oskaro.synesthesia.oskar.leonad.franssom.synesthesiaconverter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * This is a custom adapter for the settings spinner. It is from this spinner that the
 * user selects its preferred color schema. The spinner text is colorized to give a short overview on
 * how each schema looks like.
 */

public class SettingsTitleNavigationAdapter extends BaseAdapter {

    private final String TAG = this.getClass().getSimpleName();

    private TextView txtTitle;
    private ArrayList<String> spinnerNavItem;
    private Context context;
    private ColorizeSchemeClassObject cSsO;

    public SettingsTitleNavigationAdapter(Context context,
                                          ArrayList<String> spinnerNavItem) {
        this.spinnerNavItem = spinnerNavItem;
        this.context = context;


        Log.i(TAG, "SettingsTitleNavigationAdapter Constructor");
    }

    @Override
    public int getCount() {
        return spinnerNavItem.size();
    }

    @Override
    public Object getItem(int index) {
        return spinnerNavItem.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.spinner_items, null);
        }

        cSsO = new ColorizeSchemeClassObject(position);

        txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);

        txtTitle.setText(colorizeText(spinnerNavItem.get(position)));
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.spinner_items, null);
        }

        cSsO = new ColorizeSchemeClassObject(position);

        Log.i(TAG, "This is the value of position int: " + position);


        txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);

        txtTitle.setText(colorizeText(spinnerNavItem.get(position)));


        return convertView;

    }

    //Give each character the selected color
    private SpannableString colorizeText(String text) {
        SpannableString spannableColor = new SpannableString(text);
        int x = 1;
        for (char letter : text.toCharArray()) {
            spannableColor.setSpan(new ForegroundColorSpan(Color.parseColor(cSsO.ReturnCharColor(letter).substring(0,7))), x - 1, x, 0);
            x++;
        }
        return spannableColor;
    }


}

