package edu.ucsb.cs.cs184.uname.imageratingexplorer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Donghao on 10/26/2017. Updated by yding37 on 10/31/2018
 */

public class ImageRatingDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "ImageRatingDatabase.db";
    private static final String TABLE_NAME = "Post";
    private static final String sqlCMD = "CREATE TABLE " + TABLE_NAME + " (Id integer PRIMARY KEY AUTOINCREMENT, Url text NOT NULL UNIQUE,  Uri text NOT NULL UNIQUE, Rating real);";
    private static ImageRatingDbHelper instance;
    private List<OnDatabaseChangeListener> observers;
    protected static final int DATABASE_VERSION = 3;

    private ImageRatingDbHelper(Context context) {
        super(context, DB_NAME, null, 3);
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
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.close();
    }

    public void dropTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        //db.close();
    }

    private void notifyListeners() {
        for (OnDatabaseChangeListener listener : observers) {
            listener.onDbChange();
        }
    }

    public float getRatingFor(int myID){
        Log.d("getRatingFor", "im here");

        String selectQuery = "SELECT Rating FROM Post WHERE Id = " + myID;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("column count",  Integer.toString( cursor.getColumnCount()));
        Log.d("column array", cursor.getColumnNames()[0]);



        if (cursor.moveToFirst()) {
            do {
                Log.d("DBID", Integer.toString(cursor.getInt(0)));
                return (cursor.getFloat(cursor.getColumnIndex("Rating")));
            } while (cursor.moveToNext());
        }

        return 0;
    }

    public int getDbId(String url) {
        String selectQuery = "SELECT Id FROM Post WHERE Url=" +"'" + url + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                return (cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        return -1;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        //Create the SQLite table
        db.execSQL(sqlCMD);
        db.setVersion(4);
    }

    public void addPost(String uri, String url, int rating) {
        ContentValues insertion = new ContentValues();


        insertion.put("Url", url);
        insertion.put("Uri", uri);
        insertion.put("Rating", rating);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, insertion);
        db.close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void setRating(int id, float newR){
        SQLiteDatabase db = this.getWritableDatabase();
        String cmd =  "UPDATE Post SET Rating = " + newR + " WHERE Id = " + id;
        db.execSQL(cmd);
        db.close();
    }


    public ArrayList<String> filterUrlByRating(float rating) {
        ArrayList<String> temp = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT Url FROM Post WHERE Rating >= " + rating;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                temp.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return temp;
    }

    public ArrayList<Integer> filterIdByRating(float rating) {
        ArrayList<Integer> temp = new ArrayList<Integer>();
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT Id FROM " + TABLE_NAME + " WHERE Rating >= " + rating;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            //iterate the cursor
            do {
                temp.add(c.getInt(0));
            } while (c.moveToNext());
        }
        return temp;
    }






    public interface OnDatabaseChangeListener {
        void onDbChange();
    }
}