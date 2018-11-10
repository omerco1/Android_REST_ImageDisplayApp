package edu.ucsb.cs.cs184.uname.imageratingexplorer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Donghao on 10/26/2017. Updated by yding37 on 10/31/2018
 */

public class ImageRatingDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "ImageRatingDatabase.db";

    private static ImageRatingDbHelper instance;
    private List<OnDatabaseChangeListener> observers;

    private ImageRatingDbHelper(Context context) {
        super(context, DB_NAME, null, 1);
        observers = new ArrayList<>();
    }


    public static void initialize(Context context) {
        instance = new ImageRatingDbHelper(context);
    }

    public static ImageRatingDbHelper getInstance() {
        return instance;
    }

    public void subscribe(OnDatabaseChangeListener listener) {
        observers.add(listener);
    }

    private boolean tryUpdate(Cursor cursor) {
        try {
            cursor.moveToFirst();
        } catch (SQLiteConstraintException exception) {
            return false;
        } finally {
            cursor.close();
        }
        notifyListeners();
        return true;
    }

    public void deleteDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM Post");
        db.close();
    }

    private void notifyListeners() {
        for (OnDatabaseChangeListener listener : observers) {
            listener.onDbChange();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Create the SQLite table
        db.execSQL(
                "CREATE TABLE Image (Id integer PRIMARY KEY AUTOINCREMENT, Uri text NOT NULL UNIQUE, Url text NOT NULL UNIQUE, Rating real);");
    }

    public void addPost(String uri, String url, int rating) {
        ContentValues insertion = new ContentValues();
        insertion.put("URI", uri);
        insertion.put("URL", url);
        insertion.put("Rating", rating);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("Post", null, insertion);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public interface OnDatabaseChangeListener {
        void onDbChange();
    }
}