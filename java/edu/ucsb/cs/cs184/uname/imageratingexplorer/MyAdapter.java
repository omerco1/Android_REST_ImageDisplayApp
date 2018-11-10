package edu.ucsb.cs.cs184.uname.imageratingexplorer;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends BaseAdapter {
    private Context myContext;
    private ImageView im;

    public ArrayList<String> uriList = new ArrayList<String>();
    private ArrayList<Integer> idList = new ArrayList<Integer>();
    ArrayList<Bitmap> myBMArr;

    public MyAdapter(Context context) {
        myContext = context;
    }

//    public void updateListRating(float r){
////        uriList = ImageRatingDatabaseHelper.GetInstance().getImageOverRating(r);
////        idList = ImageRatingDatabaseHelper.GetInstance().getIdForCurrent(r);
//    }

    public void updateValues(String url) {
        this.uriList.add(url);
        Log.d("notNull", this.uriList.get(0));

    }

    @Override
    public int getCount() {
        if (uriList == null) {
            return 0;
        } else {
            return uriList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return uriList.get(position);
    }

    public int getID(int position) { return idList.get(position); }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View gridView = convertView;

        //Set image view
        if (convertView == null) {
//            LayoutInflater inflater = (LayoutInflater) myContext
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            im = new ImageView(myContext);
            im.setLayoutParams(new GridView.LayoutParams(200, 200));
            im.setScaleType(ImageView.ScaleType.CENTER_CROP);
            im.setPadding(5, 5, 5, 5);

        } else {
            im = (ImageView) convertView;
        }

        Picasso.with(myContext).load(uriList.get(position)).resize(200, 200).centerCrop().into(im);

        return im;
    }



}