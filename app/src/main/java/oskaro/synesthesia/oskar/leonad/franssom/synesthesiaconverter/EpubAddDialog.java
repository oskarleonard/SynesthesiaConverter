package oskaro.synesthesia.oskar.leonad.franssom.synesthesiaconverter;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.squareup.picasso.Picasso;

import java.io.File;


/**
 * This dialog is used by the EpubFragment
 * A dialog that lets the user add epub files to an epub shelf. Uses a another dialog fragment
 * that opens a custom fileExplorer to choose cover for the books.
 */

public class EpubAddDialog extends DialogFragment implements FileDialogFragment.FileDialogFragmentListener{

    private View theView;
    private Button btnEpubFile, btnEpubAddDone;
    private ImageButton ibEpubCover;
    private File epubFile, coverFile;
    private EditText etEpubTitle;
    private EpubAddDialogListener listener;


    public static EpubAddDialog newInstance() {
        EpubAddDialog fi = new EpubAddDialog();

        return fi;
    }

    public interface EpubAddDialogListener{
        void onFinishEpubAddDialog(AppBook appBook, int reqCode);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        theView = inflater.inflate(R.layout.fragment_add_epub_dialog, container, false);
        etEpubTitle = (EditText)theView.findViewById(R.id.etEpubTitle);

        listener = (EpubAddDialogListener)getTargetFragment();

        btnEpubAddDone = (Button)theView.findViewById(R.id.btnEpubAddDone);
        btnEpubAddDone.setEnabled(false);

        setUpButtons();

        return theView;
    }

    private void setUpButtons() {

        btnEpubAddDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File dir = Environment.getExternalStorageDirectory();
                String coverPath = null;

                //Move jpg file
                if(coverFile != null){
                    File newFileCover = new File (dir, MainActivity.SYNESTHESIA+"/"+MainActivity.COVER+"/"+coverFile.getName());
                    coverFile.renameTo(newFileCover);
                    coverPath = coverFile.toURI().toString();
                }

                //Create an AppBook obj
                AppBook appBook = new AppBook(etEpubTitle.getText().toString(), MainActivity.EPUB, coverPath);

                //Move epub file to Epub dir (the path of the AppBook obj)
                File newFile = new File (dir, appBook.getPath());
                epubFile.renameTo(newFile);

                listener.onFinishEpubAddDialog(appBook, getTargetRequestCode());
                dismiss();
            }
        });

        btnEpubFile = (Button)theView.findViewById(R.id.btnEpubFile);
        btnEpubFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show files in the downloads directory, Epub files OK.
                String[] paths = new String[]{"download"};
                DialogFragment converterDialog = FileDialogFragment.newInstance(".epub", paths);
                converterDialog.setTargetFragment(EpubAddDialog.this, 0);
                converterDialog.show(getFragmentManager().beginTransaction(), "DIAL");
            }
        });

        ibEpubCover = (ImageButton)theView.findViewById(R.id.ibEpubCover);
        ibEpubCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show files in the downloads & Cover directories, jpg files OK.
                String[] paths = new String[]{"download", "SynesthesiaTool/COVER"};
                DialogFragment converterDialog = FileDialogFragment.newInstance(".jpg", paths);
                converterDialog.setTargetFragment(EpubAddDialog.this, 0);
                converterDialog.show(getFragmentManager().beginTransaction(), "DIAL");
            }
        });
    }

    private void colorButton(Button button, int color){
        Drawable drawable = button.getBackground();
        PorterDuffColorFilter filter =
                new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        drawable.setColorFilter(filter);
    }

    @Override
    public void onFinishFileDialogFragment(File selectedFile, int regCode) {
        if(selectedFile.getName().toLowerCase().endsWith(".epub")){
            //epub file was selected
            epubFile = selectedFile;
            //Enable the DONE buton
            colorButton(btnEpubFile, Color.GREEN);
            //Cannot have selectedFile.getName here
            btnEpubFile.setText(selectedFile.getName());
            btnEpubAddDone.setEnabled(true);
        }else if(selectedFile.getName().toLowerCase().endsWith(".jpg")){
            //cover was chosen
            coverFile = selectedFile;
            Picasso.with(getActivity().getBaseContext()).load(coverFile.toURI().toString()).resize(200, 200).into(ibEpubCover);
        }
    }
}
