package oskaro.synesthesia.oskar.leonad.franssom.synesthesiaconverter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;

import java.io.File;
import java.util.ArrayList;


public class SettingsFrameActivity extends ActionBarActivity {

    public static final String SP_SETTINGS = "Settings";
    public static final String SP_SETTINGS_CHOSEN_SCHEMA = "Color_Schema";
    private Spinner spinner;
    private ArrayList<String> navSpinner;
    private SettingsTitleNavigationAdapter adapter;
    private boolean firstOpen = true;
    private Button btnSyncStorage;


    private FrameLayout ff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        setUpButtons();
        spinner = (Spinner)findViewById(R.id.spinner);
        navSpinner = new ArrayList<String>();
            /*Remember, i got an error here when the string was to long and the spinner was displayed
                side by side with the TextView. Dont really understand why this error occurs, it seems
                like its because the spinner gets to long and than effects the TextView. Anyway, this
                is why i added the spinner on a seperate row*/
        navSpinner.add("Josh     ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        navSpinner.add("Magneta ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        navSpinner.add("Oskar    ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        navSpinner.add("RainBow ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        navSpinner.add("Rain     ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        //navSpinner.add("This is a preview of the schema, most common among synesthetic people ");

        SharedPreferences settings = this.getSharedPreferences(SP_SETTINGS, 0);
        adapter = new SettingsTitleNavigationAdapter(this, navSpinner);
        spinner.setAdapter(adapter);


        //Set the User defined Color Schema, If undefined use Josh Cohens Schema
        spinner.setSelection(settings.getInt(SP_SETTINGS_CHOSEN_SCHEMA, 0));


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(firstOpen == false) {

                    SharedPreferences settings = getBaseContext().getSharedPreferences(SP_SETTINGS, 0);
                    SharedPreferences.Editor editor = settings.edit();

                    editor.putInt(SP_SETTINGS_CHOSEN_SCHEMA, i);
                    editor.commit();
                }else
                    firstOpen = false;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void setUpButtons() {
        btnSyncStorage = (Button)findViewById(R.id.btnSyncStorage);
        btnSyncStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncStorage();
            }
        });
    }

    private void syncStorage(){
        BookShelf one  = BookShelf.getShelf(this, HtmlFragment.SHARED_PREF_KEY_INAPP_SHELF1);
        BookShelf two = BookShelf.getShelf(this, HtmlFragment.SHARED_PREF_KEY_INAPP_SHELF2);
        syncBookShelf(one);
        syncBookShelf(two);

    }

    private BookShelf syncBookShelf(BookShelf bookShelf){

        BookShelf newBookShelf = new BookShelf(bookShelf.getName());

        for (int book = 0; book < bookShelf.getBookList().size(); book++) {
            boolean hasHtml = false, hasEpub = false;

            if(bookShelf.getBookList().get(book).getEpubPath()!= null){
                if(new File(Environment.getExternalStorageDirectory(), bookShelf.getBookList().get(book).getEpubPath()).exists()){
                    hasEpub = true;
                }
            }

            if(new File(Environment.getExternalStorageDirectory(), bookShelf.getBookList().get(book).getPath()).exists()){
                hasHtml = true;
            }


            if(hasHtml || hasEpub){
                //Update epub attributes
                if(!hasEpub){bookShelf.getBookList().get(book).setEpubPath(null);}

                //Since Html path acts as the primary key for the book i cannot set this to null
                //if(!hasHtml){bookShelf.getBookList().get(book).setPath(null);}

                newBookShelf.addBookToShelf(bookShelf.getBookList().get(book));
            }
        }
        BookShelf.saveBookShelfToSharedPref(this, newBookShelf, newBookShelf.getName());

        return newBookShelf;
    }
}
