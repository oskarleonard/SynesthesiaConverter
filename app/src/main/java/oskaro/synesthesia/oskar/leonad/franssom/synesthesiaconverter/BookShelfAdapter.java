package oskaro.synesthesia.oskar.leonad.franssom.synesthesiaconverter;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * This adapter populates the Twowayview in reversed order with book objects.
 * It has two static methods so other classes can know how big the books are
 * in the twowayview.
 */

public class BookShelfAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private BookShelf bookShelf;

    public void setBookShelf(BookShelf bookShelf) {
        if(bookShelf==null)
            this.bookShelf = new BookShelf("Shelf is Empty");
        else{
            this.bookShelf = bookShelf;
        }
    }

    public BookShelfAdapter(Context context, BookShelf bookShelf) {
        this.context = context;
        setBookShelf(bookShelf);
        inflater = LayoutInflater.from(this.context);

    }

    @Override
    public int getCount() {
        return bookShelf.getShelfSize();
    }

    @Override
    public AppBook getItem(int position) {
        //Show the the shelf in reversed order, so newly added books will be displayed first
        return bookShelf.getBookByIndext(getCount()-1-position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.book_shelf_adapter, parent, false);
        ImageView view = (ImageView)convertView.findViewById(R.id.ivIcon);
        AppBook theBook = getItem(position);


        //Set the BookTitle
        TextView tvBookTitle = (TextView)convertView.findViewById(R.id.tvBookTitle);
        tvBookTitle.setText(theBook.getTitle());

        /*If Picasso library becomes unstable just use:
        Async Treading and scale using BitmapFactory.Options o2 = new BitmapFactory.Options();
         */
        Picasso.with(inflater.getContext()).load(theBook.getCover()).resize(getBookWidth(context), getBookHeight(context)).into(view);

        return convertView;
    }


    public static int getBookWidth(Context sizeContext){
        WindowManager wm = (WindowManager) sizeContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        //If landscape mode divide the height else divide the width
        if(width>height){
            return (int)Math.round(height/2.4);
        }else{
            return (int)Math.round(width/2.4);
        }

    }

    public static int getBookHeight(Context sizeContext){

        //the height depends on the width, its always 1.17* bigger
        return (int)Math.round(getBookWidth(sizeContext)*1.17);
    }
}
