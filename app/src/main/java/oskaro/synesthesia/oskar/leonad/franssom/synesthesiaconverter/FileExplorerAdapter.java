package oskaro.synesthesia.oskar.leonad.franssom.synesthesiaconverter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by Oskar on 2015-01-28.
 */

public class FileExplorerAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private File[] files;
    private String extension;

    public FileExplorerAdapter(Context context, File[] files, String extension) {
        this.files = files;
        this.extension = extension;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return files.length;
    }

    @Override
    public File getItem(int position) {
        return files[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.file_explorer_adapter, parent, false);
        ImageView ivFileIcon = (ImageView) convertView.findViewById(R.id.ivFileIcon);
        TextView tvItemName = (TextView) convertView.findViewById(R.id.tvItemName);
        TextView tvItemSize = (TextView) convertView.findViewById(R.id.tvItemSize);

        File currentFile = getItem(position);

        tvItemName.setText(currentFile.getName());

        if (currentFile.isDirectory()) {
            ivFileIcon.setImageResource(R.drawable.ic_picture_dir);
            tvItemSize.setText("Items: " + currentFile.listFiles().length);
        } else if (currentFile.getName().toLowerCase().endsWith(extension)) {
            //If picture file, than show a thumbnail of it, else show that its OK
            if(currentFile.getName().toLowerCase().endsWith(".jpg")){

                //If cover from assets or not
                if(currentFile.getPath().contains("file:/")){
                    String[] thePath = currentFile.getPath().split("file:/");
                    Picasso.with(inflater.getContext()).load("file:///"+thePath[1]).resize(250, 250).into(ivFileIcon);
                }else{
                    //Picasso helps here in case the jpg files is too big. Picasso takes care of memory management.
                    Picasso.with(inflater.getContext()).load(currentFile.toURI().toString()).resize(250, 250).into(ivFileIcon);
                }

            }else{
                ivFileIcon.setImageResource(R.drawable.ic_file_ok);
            }

            tvItemSize.setText("Size: " + (currentFile.length()/1024) + "kb");
        }else{
            ivFileIcon.setImageResource(R.drawable.ic_file_error);
        }

        return convertView;
    }
}
