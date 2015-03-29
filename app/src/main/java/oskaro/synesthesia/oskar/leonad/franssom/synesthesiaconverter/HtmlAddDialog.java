package oskaro.synesthesia.oskar.leonad.franssom.synesthesiaconverter;


import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Random;


/**
 * A dialog fragments that produces a colorized HTML file from a URL. If everything goes OK, a book will be added
 * to the bookshelf.
 */
public class HtmlAddDialog extends DialogFragment implements FileDialogFragment.FileDialogFragmentListener {

    public static final int DIALOG_COVER_SIZE = 230;
    private Button btnPreviewHtml, btnConvert, btnCreateHtml, btnRan;
    private ImageButton ibCover;
    private WebView preViewHtml;
    private View theView;
    private String htmlContent = "";
    private boolean isDialogActive = true;
    private EditText etURL, etBookTitle;
    private String coverPath, bookType;
    private AppBook appBook;
    private ProgressBar progBarHtml;

    private ConverterDialogListener callback;

    //An interface that lets me call the fragment that called this dialog fragment.
    public interface ConverterDialogListener {
        //This dialog should return a book to calling fragment
        void onFinishEditDialog(AppBook book, int reqCode);
    }

    /**
     * Here i can put styles and themes for the Dialog. No i only use it to
     * know which button to hide.
     */
    public static HtmlAddDialog newInstance(String convert) {
        HtmlAddDialog f = new HtmlAddDialog();

        Bundle args = new Bundle();
        args.putString("convert", convert);
        f.setArguments(args);

        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        theView = inflater.inflate(R.layout.fragment_html_add_dialog, container, false);

        try {
            callback = (ConverterDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement DialogClickListener interface");
        }

        //Disable screen rotation on dialog
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Other
        progBarHtml = (ProgressBar) theView.findViewById(R.id.progBarHtml);

        etURL = (EditText) theView.findViewById(R.id.etURL);
        etURL.setText("");
        etBookTitle = (EditText) theView.findViewById(R.id.etSaveBookTitle);
        setUpButtons();
        setUpWebView();

        bookType = getArguments().getString("convert");

        btnConvert.setEnabled(false);


        return theView;
    }

    private void createAppBookObject() {
        String title = "\n " + etBookTitle.getText().toString();
        appBook = new AppBook(title, bookType, coverPath);
    }

    private String[] splitString(String str, int elements) {
        elements++;
        String[] splitStrings = new String[elements];
        int currentLength = 0;
        int oneFourth = str.length() / elements;


        for (int x = 1; x <= elements; x++) {

            //for the last text chunk add "remainders" which were rounded down when (int)
            if (x == splitStrings.length) {
                //Log.i("THEADD DIALOGER   " , "splitStrings[x-1] = " + splitStrings[x-1].length());

                splitStrings[x-1] = str.substring(currentLength, str.length());

                //Log.i("THEADD----DIALOGERRR " , "oneFourth = " + splitStrings[x-1].length());

            } else {
                splitStrings[x-1] = str.substring(currentLength, oneFourth*x);
            }
            currentLength += oneFourth;
        }
        return splitStrings;
    }

    private boolean createHtmlFile() {
        boolean toReturn = false;
        File dir = Environment.getExternalStorageDirectory();

        Long kk = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        Log.i("Runtime",
                " Total " + Runtime.getRuntime().totalMemory());
        Log.i("Runtime",
                " kk " + kk);
        Log.i("Runtime",
                " Runtime.getRuntime().maxMemory() " + Runtime.getRuntime().maxMemory());

        // Do this : https://developer.android.com/training/run-background-service/index.html
        //https://androidresearch.wordpress.com/2012/03/17/understanding-asynctask-once-and-forever/
        //divide the string by 4 and read it in chunks, each chunk consists of 650 000 elements. Otherwise
        // getBytes(Charset.forName("UTF-16") can cause out.of.memory if string is too big.
        //Important, i have to call this outside the try{} otherwise it will only do 3 loops inside splitString()?????
        String[] bookPieces = splitString(htmlContent, Math.round(htmlContent.length()/650000));

        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            outputStream = new FileOutputStream(new File(dir, appBook.getPath()));
            for (String text : bookPieces) {
                byte[] theBytes = text.getBytes(Charset.forName("UTF-16"));
                inputStream = new ByteArrayInputStream(theBytes);
                byte[] bufferData = new byte[1024];
                int bytesRead = inputStream.read(bufferData);

                while (bytesRead != -1) {
                    outputStream.write(bufferData, 0, bytesRead); //add the bufferData data to the "new file"
                    bytesRead = inputStream.read(bufferData); // keep on reading and filling the dynamic byte araay until it returns -1
                }
                //need to GC the inputsteam myself!!!!
                inputStream = null;

            }
            toReturn = true;

        } catch (Exception e) {

        } finally {
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
        return toReturn;
    }

    private void setUpWebView() {
        preViewHtml = (WebView) theView.findViewById(R.id.webView2);
        preViewHtml.getSettings().setJavaScriptEnabled(true);
        preViewHtml.addJavascriptInterface(new MyJavaScriptInterface(theView.getContext()), "HtmlViewer");

    }


    private void setUpButtons() {

        ibCover = (ImageButton) theView.findViewById(R.id.ibCover);
        Picasso.with(getActivity()).load("file:///android_asset/books/frame_open_white.jpg").resize(DIALOG_COVER_SIZE, DIALOG_COVER_SIZE).into(ibCover);
        ibCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open a File Explorer that shows avaiable jpg files.
                String[] paths = new String[]{"download",
                        "file:///android_asset/books/frame_open_old.jpg",
                        "file:///android_asset/books/frame_open_white.jpg",
                        "file:///android_asset/books/frame_original_haze.jpg",
                        "file:///android_asset/books/frame_wood_gold.jpg"};
                DialogFragment converterDialog = FileDialogFragment.newInstance(".jpg", paths);
                converterDialog.setTargetFragment(HtmlAddDialog.this, 0);
                converterDialog.show(getFragmentManager().beginTransaction(), "DIAL");
            }
        });


        btnConvert = (Button) theView.findViewById(R.id.btnConvert);
        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preViewHtml.loadUrl(converterScript());
            }
        });

        btnPreviewHtml = (Button) theView.findViewById(R.id.btnPreviewHtml);
        btnPreviewHtml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.hide_keyboard_from(theView.getContext(), theView);

                preViewHtml.loadUrl(etURL.getText().toString());
                colorizeHtmlWithJavaScript();
            }
        });


        btnCreateHtml = (Button) theView.findViewById(R.id.btnCreateHtml);
        btnCreateHtml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progBarHtml.getProgress() == progBarHtml.getMax()) {
                    createAppBookObject();
                    createHtmlFile();
                    callback.onFinishEditDialog(appBook, getTargetRequestCode());
                    dismiss();
                } else {
                    Toast.makeText(getActivity(), "Wait until the conversion has finished", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRan = (Button)theView.findViewById(R.id.btnRan);
        btnRan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String [] books = new String[]
                        {"http://www.gutenberg.org/files/16449/16449-h/16449-h.htm",
                        "http://www.gutenberg.org/files/48486/48486-h/48486-h.htm",
                        "https://www.gutenberg.org/files/22336/22336-h/22336-h.htm"};
                etURL.setText(books[new Random().nextInt(books.length)]);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDialogActive = false;
        preViewHtml.destroy();
    }

    //Returns the selected cover for a book.
    @Override
    public void onFinishFileDialogFragment(File selectedFile, int reqCode) {

        //If cover from assets or not
        if(selectedFile.getPath().contains("file:/")){
            String[] thePath = selectedFile.getPath().split("file:/");
            coverPath = "file:///"+thePath[1];
            Picasso.with(getActivity()).load("file:///"+thePath[1]).resize(DIALOG_COVER_SIZE, DIALOG_COVER_SIZE).into(ibCover);
        }else{
            File dir = Environment.getExternalStorageDirectory();
            // Move File (.jpg) from download to Cover folder.
            File newFile = new File(dir, MainActivity.SYNESTHESIA + "/" + MainActivity.COVER + "/" + selectedFile.getName());
            selectedFile.renameTo(newFile);
            coverPath = newFile.toURI().toString();
            //Change to size is always fixed
            Picasso.with(getActivity().getBaseContext()).load(coverPath).resize(DIALOG_COVER_SIZE, DIALOG_COVER_SIZE).into(ibCover);
        }



    }


    //This interface class helps me to return the HTML Source code of the modified HTML file.
    // I.e. the HTML that the webview is currently displaying. I use the interface to interact
    // with my javascript.
    class MyJavaScriptInterface {

        private Context ctx;

        MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }

        @JavascriptInterface
        public void initScript(int progressBar) {
            progBarHtml.setMax(progressBar);
        }

        //Seems like the Garbage Collection isnt working properly with javascript.
        // Somehow the script keeps executing even if the webview/fragment is destroyed.
        // Thats why i need this method so i can abort the loop in my script.
        @JavascriptInterface
        public boolean isActive(int i) {
            if (isDialogActive) {
                progBarHtml.setProgress(i);
            }
            return isDialogActive;
        }

        @JavascriptInterface
        public void showHTML(String html) {
            htmlContent = html;
        }

    }

    private void colorizeHtmlWithJavaScript() {

        preViewHtml.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {

                btnConvert.setEnabled(true);
                //view.loadUrl(converterScript());

            }
        });
    }

    //A method which perform a script on a webview.
    private String converterScript() {
        //Get the chosen colorschema from the settingsActivity
        int colorSchema = getActivity().getSharedPreferences(SettingsFrameActivity.SP_SETTINGS, 0).getInt(SettingsFrameActivity.SP_SETTINGS_CHOSEN_SCHEMA, 0);
        ColorizeSchemeClassObject cSsO = new ColorizeSchemeClassObject(colorSchema);

        List<String> colorar = cSsO.getColorArray(); //size 26

         /*Run a script that give color to each letter. One problem i had were how to get
          var colors to a list but could only get it to a string, so had to convert
           it to an array with the js code later. Seems like duplicate code, and it is.
           But i have no idea how to set var colors to a list or array from my java code.
           \""+colorar+"\";" makes it to a string, and if i just use "+colorar+" than it
            wont load.
        */

        return "javascript:(function() { " +
                "var alpha = 'abcdefghijklmnopqrstuvwxyz0123456789'.split('');" +
                "var alphaBig = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'.split('');" +
                "var colors = \"" + colorar + "\";" +
                "var preColors1 = colors.replace('[', '');" +
                "var preColors2 = preColors1.replace(']', '');" +
                "var colorsArray = preColors2.split(',');" +

                "var theText = '<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>';" +

                "window.HtmlViewer.initScript(alpha.length+alphaBig.length);" +

                "for (i = 0; i < alpha.length; i++) " +
                "{if(window.HtmlViewer.isActive(i))" +
                "{var re = new RegExp( \"(\" + alpha[i] + \"(?![^<>]*>))\", 'g' );" +
                "theText = theText.replace(re, " +
                "'<span style =\"color:'+colorsArray[i]+'\">'+alpha[i]+'</span>');}" +
                "else{break;}" +
                "};" +

                "for (i = 0; i < alphaBig.length; i++) " +
                "{if(window.HtmlViewer.isActive(i+alpha.length-1))" +
                "{var re = new RegExp( \"(\" + alphaBig[i] + \"(?![^<>]*>))\", 'g' );" +
                "theText  = theText.replace(re, " +
                "'<span style =\"color:'+colorsArray[i]+'\">'+alphaBig[i]+'</span>');}" +
                "else{break;}" +
                "};" +


                "if(window.HtmlViewer.isActive(alpha.length+alphaBig.length - 1))" +
                "{window.HtmlViewer.showHTML" +
                "(theText);}" +

                "window.HtmlViewer.isActive(alpha.length+alphaBig.length)" +
                "})()";
    }


}
