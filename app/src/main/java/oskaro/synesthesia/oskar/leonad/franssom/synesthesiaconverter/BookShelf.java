package oskaro.synesthesia.oskar.leonad.franssom.synesthesiaconverter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.google.gson.Gson;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A bookshelf class which act as a AppBook manager. It contains a list of books, this shelf is later
 * saved in shared preferences.
 */

public class BookShelf {
    public static final String SHARED_PREF_BOOKSHELF = "BookShelf";
    private String name;
    private List<AppBook> bookList = new ArrayList<AppBook>();

    public BookShelf(String name) {
        this.name = name;
    }

    public BookShelf(BookShelf original){
        this.name = original.name;
        this.bookList = original.bookList;
    }

    public static void saveBookShelfToSharedPref(Context context,BookShelf shelfToSave, String shelfKey){
        Gson gson = new Gson();
        SharedPreferences sp = context.getSharedPreferences(BookShelf.SHARED_PREF_BOOKSHELF, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(shelfKey, gson.toJson(shelfToSave));
        editor.apply();
    }

    public static BookShelf getShelf(Context context, String shelfKey){
        SharedPreferences sp = context.getSharedPreferences(BookShelf.SHARED_PREF_BOOKSHELF, 0);
        BookShelf bookShelfOne = new Gson().fromJson(sp.getString(shelfKey, null), BookShelf.class);
        //If shelf is null, which it will be first time, create a bookshelf. This could be in my
        // first time setUp. But maybe i want the user to be able to add more than two bookshelfs
        // per page. Than this approach is better.
        if(bookShelfOne == null){
            bookShelfOne = new BookShelf(shelfKey);
            saveBookShelfToSharedPref(context, bookShelfOne, shelfKey);
        }

        return bookShelfOne;
    }

    public void reverseShelfOrder(){
        Collections.reverse(bookList);
    }

    public void updateBook(AppBook book, Context context, String shelfKey){
        for (int x=0;x<bookList.size();x++){
            if (bookList.get(x).getPath().equals(book.getPath())){
                bookList.set(x,book);
                saveBookShelfToSharedPref(context, this, shelfKey);
                break;
            }
        }
    }

    public AppBook getBookByIndext(int index){
        if(bookList != null && index < bookList.size())
            return bookList.get(index);
        return null;
    }

    public boolean addBookToShelf(AppBook book){
        //Never add a book which if other book has path
        if(getBookByPath(book.getPath()) == null){
            bookList.add(book);
        }
        return false;
    }

    public boolean deleteBook(AppBook appBook){
        for (AppBook book : bookList){
            if(book.getPath().equals(appBook.getPath())){
                //Delete HTML File
                File file = new File(Environment.getExternalStorageDirectory(), appBook.getPath());
                file.delete();

                //Delete Epub File if exists
                if(appBook.getEpubPath() != null){
                    File epub = new File(Environment.getExternalStorageDirectory(), appBook.getEpubPath());
                    epub.delete();
                }
                //Delete Cover file
                if(!appBook.getCover().contains("frame")){
                    //Since the address to the cover is stored as URI i need to create the URI to get
                    // a hold of the file
                    File fileCover = new File(URI.create(appBook.getCover()));
                    fileCover.delete();
                }

                //Remove book from list
                bookList.remove(book);
                return true;
            }
        }
        return false;
    }

    //Returns a book from a title, othervise returns null (ie. no book by that title)
    public AppBook getBookByPath(String path){
        for(AppBook book:bookList){
            if(book.getPath().equals(path)){
                return book;
            }
        }
        return null;
    }

    public int getShelfSize() {
        return bookList.size();
    }

    public List<AppBook> getBookList() {
        return bookList;
    }

    public String getName() {
        return name;
    }
}


