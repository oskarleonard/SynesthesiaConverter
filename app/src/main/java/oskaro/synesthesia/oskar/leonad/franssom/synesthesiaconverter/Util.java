package oskaro.synesthesia.oskar.leonad.franssom.synesthesiaconverter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Oskar on
 */
public class Util {



    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    /**
     * Generate a value suitable for use in {#setId(int)}.
     * This value will not collide with ID values generated at build time by aapt for R.id.
     * @return a generated ID value
     */
    private static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    /*
        Generates a framelayout container so i wont have to make my res/layout messy with empty
        layout containers. All FragmentActivities uses this static method.
     */
    public static FrameLayout getFrameLayoutContainer(Context context){
        FrameLayout ladderFL = new FrameLayout(context);
        ladderFL.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ladderFL.setId(generateViewId());
        return ladderFL;
    }

    //Found this hack on SO, the only thing that worked
    //http://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
    public static void hide_keyboard_from(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
