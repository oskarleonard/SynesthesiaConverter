package oskaro.synesthesia.oskar.leonad.franssom.synesthesiaconverter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * IMPORTANT idIncrementer should be used in the path by the class creating this object!!
 */

public class AppBook implements Parcelable{
    private String path;
    private String title;
    private String type;
    private String cover;
    private String epubPath;

    public AppBook(String title, String type, String cover){
        setTitle(title);
        this.type = type;
        setCover(cover);
        this.path = MainActivity.SYNESTHESIA + "/" + type.toUpperCase()
                + "/" + getTimeStampID() + "." + type.toLowerCase();
    }

    public void openEpub(Context context){
        //Saved last book opened
        SharedPreferences sP = context.getSharedPreferences(MainActivity.SP_ACTIVITY, 0);
        SharedPreferences.Editor editor = sP.edit();
        editor.putString(HomeFragment.LAST_READ, new Gson().toJson(this));
        editor.apply();

        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir, this.getEpubPath());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/epub+zip");

        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context,
                    "Please Install an ePUB Reader, See Play page for more info", Toast.LENGTH_LONG).show();
        }
    }

    public static void openBook(Context context, AppBook appBook){

        //Saved last book opened
        SharedPreferences sP = context.getSharedPreferences(MainActivity.SP_ACTIVITY, 0);
        SharedPreferences.Editor editor = sP.edit();
        editor.putString(HomeFragment.LAST_READ, new Gson().toJson(appBook));
        editor.apply();

        if(appBook.getType().equals("EPUB")){
            File dir = Environment.getExternalStorageDirectory();
            File file = new File(dir, appBook.getPath());
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/epub+zip");

            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context,
                        "Please Install an ePUB Reader, See Play page for more info", Toast.LENGTH_LONG).show();
            }
        }else{
            Intent intent = new Intent(context, InAppReadingActivity.class);
            intent.putExtra("BOOK", appBook);
            context.startActivity(intent);
        }
    }

    public void setTitle(String title) {
        char[] titleChar = title.toCharArray();
        String lineBreakString ="";

        int j = 0;
        for(int x = 0; x < titleChar.length; x++){
            if(j < 12){
                lineBreakString += titleChar[x];
                j++;
            }else{
                lineBreakString += "\n  ";
                lineBreakString += titleChar[x];
                j=0;
            }
            if(x>40)
                break;
        }
        this.title = lineBreakString;
    }


    public void setCover(String cover) {
        //Assign Default Cover if its empty
        if(cover == null){
            this.cover = "file:///android_asset/books/frame_open_white.jpg";
        }else{
            this.cover = cover;
        }
    }


    public String getEpubPath() {
        return epubPath;
    }
    public void setEpubPath(String epubPath) {
        this.epubPath = epubPath;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }

    public String getCover() {
        return cover;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.path, this.title, this.cover, this.type});
    }

    public static final Creator CREATOR = new Parcelable.Creator<AppBook>(){
        @Override
        public AppBook createFromParcel(Parcel source) {
            return new AppBook(source);
        }

        @Override
        public AppBook[] newArray(int size) {
            return new AppBook[size];
        }
    };

    // A constructor for the Parcelable
    private AppBook (Parcel in){
        String [] data = new String[4];
        in.readStringArray(data);
        this.path = data[0];
        this.title = data[1];
        this.cover = data[2];
        this.type = data[3];
    }

    private String getTimeStampID(){
        String stampId = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        //Some epub readers need letters in the title not just numbers.
        // And some phones wont create files with ":" in them, so i cannot use "Title:" STAY with Nr and Letters
        stampId += "Title";
        return stampId;
    }
}
