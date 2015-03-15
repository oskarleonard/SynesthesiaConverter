package oskaro.synesthesia.oskar.leonad.franssom.synesthesiaconverter;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import org.lucasr.twowayview.TwoWayView;

import java.io.File;


/**
 * A fragment containing a view of two bookshelfs, the bookshelfs are displayed using the TwoWayView widget.
 * You can add a book to either bookshelf using the HtmlAddDialogFragment.
 */
public class HtmlFragment extends Fragment implements HtmlAddDialog.ConverterDialogListener, FileDialogFragment.FileDialogFragmentListener {

    private int shelfOne = 1;
    private int shelfTwo = 2;
    private View theView;
    private TwoWayView twvInAppLibOne, twvInAppLibTwo;
    private Button btnAddInApp, btnAddInAppTwo;
    private AppBook theBook;
    private BookShelf shelfToEdit;

    public static final String SHARED_PREF_KEY_INAPP_SHELF1 = "inAppShelf1";
    public static final String SHARED_PREF_KEY_INAPP_SHELF2 = "inAppShelf2";

    @Override
    public void onResume() {
        super.onResume();
        upDateTwoWayView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        theView = inflater.inflate(R.layout.fragment_html, container, false);

        twvInAppLibOne = (TwoWayView) theView.findViewById(R.id.twvInAppLibOne);
        twvInAppLibTwo = (TwoWayView) theView.findViewById(R.id.twvInAppLibTwo);


        setUpButtons();

        return theView;
    }

    private void upDateTwoWayView() {

        //First lib
        final BookShelf shelf = BookShelf.getShelf(getActivity(), SHARED_PREF_KEY_INAPP_SHELF1);


        twvInAppLibOne.setAdapter(new BookShelfAdapter(theView.getContext(), shelf));


        twvInAppLibOne.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openBook((AppBook) twvInAppLibOne.getItemAtPosition(position));
            }
        });

        //Long press item in list to Open/Edit or delete it
        twvInAppLibOne.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                shelfToEdit = shelf;
                longPressOptions((AppBook) twvInAppLibOne.getItemAtPosition(position), SHARED_PREF_KEY_INAPP_SHELF1, shelfOne);
                return true;
            }
        });

        //Second lib
        final BookShelf bookShelfTwo = BookShelf.getShelf(getActivity(), SHARED_PREF_KEY_INAPP_SHELF2);

        twvInAppLibTwo.setAdapter(new BookShelfAdapter(theView.getContext(), bookShelfTwo));

        twvInAppLibTwo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openBook((AppBook) twvInAppLibTwo.getItemAtPosition(position));
            }
        });

        twvInAppLibTwo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                shelfToEdit = bookShelfTwo;
                longPressOptions((AppBook) twvInAppLibTwo.getItemAtPosition(position), SHARED_PREF_KEY_INAPP_SHELF2, shelfTwo);
                return true;
            }
        });
    }

    private void openBook(AppBook appBook) {
        AppBook.openBook(getActivity(), appBook);
    }

    private void addEpubPath(int shelfNr) {
        //Show files in the downloads directory, Epub files OK.
        String[] paths = new String[]{"download"};
        DialogFragment converterDialog = FileDialogFragment.newInstance(".epub", paths);
        converterDialog.setTargetFragment(HtmlFragment.this, shelfNr);
        converterDialog.show(getFragmentManager().beginTransaction(), "DIAL");
    }

    private void longPressOptions(final AppBook appBook, final String sharedPrefKey, final int shelfNr) {
        theBook = appBook;

        boolean hasEpub = true;
        String openOrCreate = "OPEN EPUB";
        if(appBook.getEpubPath()==null){
            openOrCreate = "ADD EPUB";
            hasEpub = false;
        }
        final boolean hasEpubFinal = hasEpub; // Since i need to access it within an innerclass, it needs to be final

        final String[] options = new String[]{openOrCreate, "DELETE"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Edit")
                .setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                if (hasEpubFinal) {
                                    if(new File(Environment.getExternalStorageDirectory(), appBook.getEpubPath()).exists()){
                                        appBook.openEpub(getActivity());
                                    }else{
                                        Toast.makeText(getActivity(), "File is missing", Toast.LENGTH_SHORT).show();
                                        appBook.setEpubPath(null); //Reset the EpubPath if File is missing.
                                    }
                                    break;
                                } else {
                                    addEpubPath(shelfNr);
                                    break;
                                }
                            case 1:
                                //Delete book from list AND on sd card
                                BookShelf shelf = BookShelf.getShelf(getActivity(), sharedPrefKey);
                                boolean b = shelf.deleteBook(appBook);
                                BookShelf.saveBookShelfToSharedPref(getActivity(), shelf, sharedPrefKey);
                                upDateTwoWayView();
                                break;
                            case 2:
                                //Edit Name
                        }
                    }
                });
        builder.create().show();
    }


    private void setUpButtons() {
        btnAddInApp = (Button) theView.findViewById(R.id.btnAddInApp);
        btnAddInApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment converterDialog = HtmlAddDialog.newInstance(MainActivity.HTML);
                converterDialog.setTargetFragment(HtmlFragment.this, shelfOne);
                converterDialog.show(getFragmentManager().beginTransaction(), "DIAL");
            }
        });

        btnAddInAppTwo = (Button) theView.findViewById(R.id.btnAddInAppTwo);
        btnAddInAppTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment converterDialog = HtmlAddDialog.newInstance(MainActivity.HTML);
                converterDialog.setTargetFragment(HtmlFragment.this, shelfTwo);
                converterDialog.show(getFragmentManager().beginTransaction(), "DIAL");
            }
        });
    }


    @Override
    public void onFinishEditDialog(AppBook book, int reqCode) {
        //To which shelf should the book be added.
        if (reqCode == shelfOne) {
            BookShelf b = BookShelf.getShelf(getActivity(), SHARED_PREF_KEY_INAPP_SHELF1);
            b.addBookToShelf(book);
            BookShelf.saveBookShelfToSharedPref(getActivity(), b, SHARED_PREF_KEY_INAPP_SHELF1);
            upDateTwoWayView();
        } else if (reqCode == shelfTwo) {
            BookShelf b = BookShelf.getShelf(getActivity(), SHARED_PREF_KEY_INAPP_SHELF2);
            b.addBookToShelf(book);
            BookShelf.saveBookShelfToSharedPref(getActivity(), b, SHARED_PREF_KEY_INAPP_SHELF2);
            upDateTwoWayView();
        }

    }

    @Override
    public void onFinishFileDialogFragment(File selectedFile, int reqCode) {

        //if epub file was selected
        if(selectedFile.getName().toLowerCase().endsWith(".epub")){

            File dir = Environment.getExternalStorageDirectory();
            String theDir = MainActivity.SYNESTHESIA + "/" + MainActivity.EPUB + "/" + selectedFile.getName();
            //Move selectedFile file to Epub dir
            File newFile = new File(dir, theDir);
            selectedFile.renameTo(newFile);
            theBook.setEpubPath(theDir);

            if (reqCode == shelfOne) {
                shelfToEdit.updateBook(theBook, getActivity(), SHARED_PREF_KEY_INAPP_SHELF1);
            }else{
                shelfToEdit.updateBook(theBook, getActivity(), SHARED_PREF_KEY_INAPP_SHELF2);
            }


        }else{
            //Some Other File, ERROR
        }
    }
}
