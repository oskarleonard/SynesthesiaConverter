package oskaro.synesthesia.oskar.leonad.franssom.synesthesiaconverter;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.FrameLayout;


public class InAppReadingActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout ff = Util.getFrameLayoutContainer(this);
        setContentView(ff);

        if (savedInstanceState == null) {
            AppBook appBook = getIntent().getParcelableExtra("BOOK");
            getSupportFragmentManager().beginTransaction()
                    .add(ff.getId(), InAppReadingFragment.newInstance(appBook))
                    .commit();
        }

    }


}
