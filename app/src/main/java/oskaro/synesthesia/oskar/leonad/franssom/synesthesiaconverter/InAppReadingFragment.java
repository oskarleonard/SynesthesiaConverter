package oskaro.synesthesia.oskar.leonad.franssom.synesthesiaconverter;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import java.io.File;


/**
 * Just for now to test out how to divide content among webviews
 */

public class InAppReadingFragment extends Fragment {


    private ActionBar actionBar;
    private View theView;
    private WebView inAppWebView;
    private Button btnNext, btnBack;
    private Menu m;
    private GestureDetector gd;
    private int bookSize;
    private AppBook appBook;

    public static InAppReadingFragment newInstance(AppBook appBook){
        InAppReadingFragment inAppReadingFragment = new InAppReadingFragment();
        Bundle args = new Bundle();
        args.putParcelable("Book", appBook);
        inAppReadingFragment.setArguments(args);
        return inAppReadingFragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        m = menu;
        inflater.inflate(R.menu.menu_in_app_reading, menu);
        MenuItem item = m.findItem(R.id.item_title);
        item.setTitle(appBook.getTitle());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Blank", "onDestroy");
        inAppWebView.setWebViewClient(null);
        inAppWebView.destroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        //disable Screen rotation during reading mode
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Log.i("Blank", "onCreateView");

        //Get the support actionbar from this fragments activity than hide it
        actionBar = ((ActionBarActivity) this.getActivity()).getSupportActionBar();

        // Inflate the layout for this fragment
        theView = inflater.inflate(R.layout.fragment_in_app_reading, container, false);

        //Get the book file of the AppBook object
        appBook = getArguments().getParcelable("Book");
        Log.i("Blank", appBook.getCover());
        File htmlBook = new File(Environment.getExternalStorageDirectory(), appBook.getPath());

        setUpGestureDetector();
        setUpWebView(htmlBook);
        setUpButtons();

        return theView;
    }

    private void setUpWebView(File htmlBook) {

        inAppWebView = (WebView)theView.findViewById(R.id.inAppWebView);
        inAppWebView.setInitialScale(500);
        Log.i("INAPDDD", "Scale " + inAppWebView.getScale());
        inAppWebView.loadUrl(htmlBook.toURI().toString());
        inAppWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gd.onTouchEvent(event);
            }
        });

        inAppWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // webView.getContentHeight doesn really return the whole scrollable height because it could be scaled up or down.
                //I have to use getScale because there is not an easy way to invoce the onScaleChanged See:
                // http://stackoverflow.com/questions/16079863/how-get-webview-scale-in-android-4
                // Although i could do smt like this:
                // http://stackoverflow.com/questions/19897751/how-can-i-get-the-current-scale-of-a-webviewandroid
                bookSize = Math.round(inAppWebView.getContentHeight() * view.getScale() / view.getHeight());
                updatePageNumber();
            }
        });


    }

    private void setUpGestureDetector(){
        gd = new GestureDetector(getActivity(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });

        gd.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if(actionBar.isShowing()){
                    actionBar.hide();
                }else{
                    actionBar.show();
                }

                //If return false, than the it will scroll after a double click, spookie.
                return true;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return false;
            }
        });
    }

    private void setUpButtons() {
        btnNext = (Button)theView.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Scroll Whole Page
                //inAppWebView.pageDown(false);

                inAppWebView.scrollBy(0, (int)Math.round(inAppWebView.getMeasuredHeight()/1.02)); //Same as pageDown(false), but no animated scroll
                updatePageNumber();

                Log.i("STIRRE", "Scroll = " + inAppWebView.getScrollY());
            }
        });

        btnBack = (Button)theView.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inAppWebView.pageUp(false);
                updatePageNumber();
            }
        });

    }

    private void updatePageNumber(){
        MenuItem item = m.findItem(R.id.item_page_nr);

        item.setTitle(String.valueOf(inAppWebView.getScrollY()/inAppWebView.getMeasuredHeight() + "/" + String.valueOf(bookSize)));
    }

}
