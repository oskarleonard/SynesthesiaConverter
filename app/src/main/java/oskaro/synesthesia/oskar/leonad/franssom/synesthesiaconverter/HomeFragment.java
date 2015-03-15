package oskaro.synesthesia.oskar.leonad.franssom.synesthesiaconverter;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;


/**
 * This is the apps start page, the middle page in the viewpager
 */

public class HomeFragment extends Fragment {

    private Button btnForum, btnMyConverter;
    private ImageView ibLastRead;
    private View theView;
    private TextView tvHomeBookTitle;
    private AppBook appBook;
    public static final String LAST_READ = "lastRead";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        theView = inflater.inflate(R.layout.fragment_home, container, false);
        tvHomeBookTitle = (TextView)theView.findViewById(R.id.tvHomeBookTitle);
        setUpButtons();

        Picasso.with(getActivity()).load("file:///android_asset/books/frame_open_white.jpg").resize(BookShelfAdapter.getBookWidth(getActivity()), BookShelfAdapter.getBookHeight(getActivity())).into(ibLastRead);

        return theView;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sP = getActivity().getSharedPreferences(MainActivity.SP_ACTIVITY, 0);
        appBook = new Gson().fromJson(sP.getString(LAST_READ, null), AppBook.class);

        if(appBook != null){
            tvHomeBookTitle.setText(appBook.getTitle());
            Picasso.with(getActivity()).load(appBook.getCover()).resize(BookShelfAdapter.getBookWidth(getActivity()), BookShelfAdapter.getBookHeight(getActivity())).into(ibLastRead);
        }
    }

    private void setUpButtons() {

        btnForum = (Button) theView.findViewById(R.id.btnForum);
        btnForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forumintent = new Intent(Intent.ACTION_VIEW);
                forumintent.setData(Uri.parse("http://mt.artofmemory.com/forums/synesthesia-training-program-letters-and-numbers-3005.html"));
                startActivity(forumintent);
            }
        });

        btnMyConverter = (Button) theView.findViewById(R.id.btnMyConverter);
        btnMyConverter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Chrome doesnt work good on the converter site so the user should be given the
                // option to choose another browser without disable its setting for "always open with".
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://2epub.com/"));
                Intent chooser = Intent.createChooser(intent, "Select a browser");
                startActivity(chooser);
            }
        });

        ibLastRead = (ImageView) theView.findViewById(R.id.ibLastRead);
        ibLastRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(appBook != null){
                    if(appBook.getEpubPath() != null){
                        appBook.openEpub(getActivity());
                    }else
                        AppBook.openBook(getActivity(), appBook);
                }else{
                    Toast.makeText(getActivity(), "Cannot Open Book", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ibLastRead.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final String[] options = new String[]{"EPUB"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Open as:")
                        .setItems(options, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        if(appBook.getEpubPath()!=null){
                                            appBook.openEpub(getActivity());
                                        }
                                }
                            }
                        });
                builder.create().show();
                return true;
            }
        });


    }


}
