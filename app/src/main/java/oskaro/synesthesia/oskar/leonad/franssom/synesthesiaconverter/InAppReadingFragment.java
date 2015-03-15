package oskaro.synesthesia.oskar.leonad.franssom.synesthesiaconverter;


import android.content.SharedPreferences;
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
import android.webkit.WebChromeClient;
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
    private int zoom = 0;

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

        switch (id){
            case R.id.zoomIn:
                zoom+=30;
                inAppWebView.setInitialScale(zoom);
                return true;
            case R.id.zoomOut:
                zoom-=30;
                inAppWebView.setInitialScale(zoom);
                return true;
            case R.id.item_bookmark:
                inAppWebView.scrollTo(0, getActivity().getSharedPreferences(MainActivity.SP_ACTIVITY, 0).getInt(appBook.getPath(),0));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        inAppWebView.stopLoading();
        inAppWebView.destroy();

        SharedPreferences sp = getActivity().getSharedPreferences(MainActivity.SP_ACTIVITY, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(appBook.getPath(), inAppWebView.getScrollY());

        Log.i("onDestroy", "onDestroy zoom " + zoom);

        editor.putInt("ZOOM", zoom);
        editor.apply();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        //disable Screen rotation during reading mode
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Get the support actionbar from this fragments activity than hide it
        actionBar = ((ActionBarActivity) this.getActivity()).getSupportActionBar();

        // Inflate the layout for this fragment
        theView = inflater.inflate(R.layout.fragment_in_app_reading, container, false);

        //Get the book file of the AppBook object
        appBook = getArguments().getParcelable("Book");
        File htmlBook = new File(Environment.getExternalStorageDirectory(), appBook.getPath());

        setUpGestureDetector();
        setUpWebView(htmlBook);
        setUpButtons();

        return theView;
    }

    private void setUpWebView(File htmlBook) {


        inAppWebView = (WebView)theView.findViewById(R.id.inAppWebView);

        zoom = getActivity().
                getSharedPreferences(MainActivity.SP_ACTIVITY, 0).getInt("ZOOM", 100 * (int)inAppWebView.getScale());

        //Set users preferable zoom.
        inAppWebView.setInitialScale(zoom);


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
