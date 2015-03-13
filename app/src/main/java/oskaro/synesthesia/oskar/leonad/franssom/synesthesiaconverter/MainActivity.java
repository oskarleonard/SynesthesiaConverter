package oskaro.synesthesia.oskar.leonad.franssom.synesthesiaconverter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import oskaro.google.samples.apps.iosched.ui.widget.SlidingTabLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/*
    This is the main activity which houses the viewpager and the sliding tabs.
*/

public class MainActivity extends ActionBarActivity {

    //Lets make the names of the folders final incase i want to change their names later i can do it in one place
    public static final String SYNESTHESIA = "SynesthesiaApp";
    public static final String EPUB = "EPUB";
    public static final String HTML = "HTML";
    public static final String COVER = "COVER";

    //A shared pref variable containing information about the user activity
    public static final String SP_ACTIVITY = "SharePrefUserPhone";
    private static final String SPS_FIRST_TIME = "FIRST_TIME";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.startViewPager);
        viewPager.setAdapter(new StartViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(1);//open app in the middle tab

        /*Create the necessary folder structure on the SD card. Needs to be checked
         every time in case the user has deleted the structure, Books will be CORRUPTED */
        createFolderStructure();

        //Only do these things during the first lauch
        SharedPreferences settings = this.getSharedPreferences(SP_ACTIVITY, 0);
        if (settings.getBoolean(SPS_FIRST_TIME, true)) {
            //Only get the users phone size the first time the app launches.
            firstTimeInstallation();
        }

        //Give the viewPager to the slidingTabLayout
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.startSlidingTabs);
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsFrameActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //Create the folder structure for the app
    private void createFolderStructure() {

        File dir = Environment.getExternalStorageDirectory();

        File synesthesia = new File(dir, SYNESTHESIA);
        File epub = new File(dir, SYNESTHESIA + "/" + EPUB);
        File html = new File(dir, SYNESTHESIA + "/" + HTML);
        File cover = new File(dir, SYNESTHESIA + "/" + COVER);

        // Create PDF Structure If Not Exists
        if (!synesthesia.exists()) {
            synesthesia.mkdirs();
        }

        // If file does not exists, then create it
        if (!epub.exists()) {
            epub.mkdirs();
        }

        // If file does not exists, then create it
        if (!html.exists()) {
            html.mkdirs();
        }

        // If file does not exists, then create it
        if (!cover.exists()) {
            cover.mkdirs();
        }

    }


    /* Get the users display resolution and save it to  a shared Preference. This will only run the first time
        the user enters the app. It is used later to optimize pictures for certain displays, i.e. book covers inside the  adapter etc.
     */
    private void firstTimeInstallation() {

        moveFromAssetsToSdCard();

        SharedPreferences settings = this.getSharedPreferences(SP_ACTIVITY, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(SPS_FIRST_TIME, false);
        editor.apply();

    }

    //Move the books i ship with the app to the sd card to let the use access them
    private void moveFromAssetsToSdCard() {
        //Get filenames for all the books inside Assets/books
        File dir = Environment.getExternalStorageDirectory();
        AssetManager assetManager = getAssets();
        String[] files = null;
        try {files = assetManager.list("books");
        } catch (IOException e) {e.printStackTrace();}
        //Copy each book to the sd card using inputstream and outputstream. Finally close them!
        InputStream inputStream = null;
        OutputStream outputStream = null;
        for (String fileName : files){
            try {
                inputStream = assetManager.open("books/"+fileName);
                //Move to correct subfolder in the sdcard
                if(fileName.contains("html")) {
                    outputStream = new FileOutputStream(new File(dir, SYNESTHESIA + "/" + HTML + "/" + fileName));
                }else if (fileName.contains("epub")){
                    outputStream = new FileOutputStream(new File(dir, SYNESTHESIA + "/" + EPUB + "/" + fileName));
                }else if (fileName.contains("jpg")) {
                    outputStream = new FileOutputStream(new File(dir, SYNESTHESIA + "/" + COVER + "/" + fileName));
                }


                //Much faster to read arrays of bytes than one at a time
                byte[] bufferData = new byte[1024];
                // bytesRead contains how many bytes that was actually read from the IS to the bufferData.
                int bytesRead = inputStream.read(bufferData); //InputSteam will try to fill up the bufferData with byte data
                //while there is something to read in the inputStrem (the Asset file)
                //read it and write it to the outputSteam (the new file on sd card)
                while (bytesRead != -1) {
                    outputStream.write(bufferData, 0, bytesRead); //add the bufferData data to the "new file"
                    bytesRead = inputStream.read(bufferData); // keep on reading and filling the dynamic byte araay until it returns -1
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (outputStream != null) {
                    try {
                        // outputStream.flush();
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

    }

    //Change to private here, was public before
    private static class StartViewPagerAdapter extends FragmentPagerAdapter {
        final int PAGES = 3;
        private String tabTitles[] = new String[]{"TEST", "HOME", "LIB"};

        public StartViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new GameFragment();
                case 1:
                    return new HomeFragment();
                case 2:
                    return new HtmlFragment();
                default:
                    return new HomeFragment();
            }
        }

        @Override
        public int getCount() {
            return PAGES;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }

}
