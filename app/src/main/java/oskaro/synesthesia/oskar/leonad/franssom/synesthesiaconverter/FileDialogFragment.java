package oskaro.synesthesia.oskar.leonad.franssom.synesthesiaconverter;


import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;

/**
 * A file dialog fragment that
 */
public class FileDialogFragment extends DialogFragment {

    private View theView;
    private ListView listViewFiles;

    FileDialogFragmentListener listener;


    public interface FileDialogFragmentListener{
        void onFinishFileDialogFragment(File selectedFile, int regCoe);
    }

    /**Here i can put styles and themes for the Dialog. No i only use it to
     * know which button to hide.*/
    public static FileDialogFragment newInstance(String extenstion, String[] direc) {
        FileDialogFragment f = new FileDialogFragment();

        Bundle args = new Bundle();
        args.putStringArray("directories", direc);
        args.putString("extension", extenstion);
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        theView = inflater.inflate(R.layout.fragment_file_dialog, container, false);
        listViewFiles = (ListView)theView.findViewById(R.id.listViewFiles);


        listener = (FileDialogFragmentListener)getTargetFragment();

        String extension = getArguments().getString("extension");
        String[] paths = getArguments().getStringArray("directories");


        listFilesInDialog(stringPathsToFiles(paths), extension);

        return theView;
    }

    //List files in a dialog. Will exit and call onFinishFileDialogFragment
    // when a file with the expected extension is clicked. The clicked file
    // will be sent to the parent.
    private void listFilesInDialog(File [] fileArr, final String extension){

        final File[] files =  fileArr;

        listViewFiles.setAdapter(new FileExplorerAdapter(getActivity(), files, extension));
        listViewFiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(files[position].isDirectory()){
                    listFilesInDialog(files[position].listFiles(), extension);
                }else if(files[position].getName().toLowerCase().endsWith(extension)){
                    listener.onFinishFileDialogFragment(files[position], getTargetRequestCode());
                    dismiss();
                }else{
                    Toast.makeText(theView.getContext(), "Please choose " + extension, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Create a fileArray from paths. The files will be used in a listview adapter.
    private File[] stringPathsToFiles(String[] paths){
        File dir = Environment.getExternalStorageDirectory();
        File[] toReturn = new File[paths.length];
        for(int x = 0; x < paths.length; x++){
            toReturn[x] = new File(dir, paths[x]);
        }
        return toReturn;
    }

}
