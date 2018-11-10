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

    public ArrayList<String> myUrlList = new ArrayList<String>();
    private ArrayList<Integer> myIdArr = new ArrayList<Integer>();
    ArrayList<Bitmap> myBMArr;

    public int getDbId(int position) {
        return myIdArr.get(position);
    }


    public MyAdapter(Context context) {
        myContext = context;
    }

//    public void updateListRating(float r){
////        uriList = ImageRatingDatabaseHelper.GetInstance().getImageOverRating(r);
////        idList = ImageRatingDatabaseHelper.GetInstance().getIdForCurrent(r);
//    }

    public void updateValues(String url) {
        this.myUrlList.add(url);
        Log.d("notNull", this.myUrlList.get(0));

    }

    @Override
    public int getCount() {
        if (myUrlList == null) {
            return 0;
        } else {
            return myUrlList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return myUrlList.get(position);
    }



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
            im.setLayoutParams(new GridView.LayoutParams(300, 300));
            im.setScaleType(ImageView.ScaleType.CENTER_CROP);
            im.setPadding(5, 5, 5, 5);
        } else {
            im = (ImageView) convertView;
        }

        Picasso.with(myContext).load(myUrlList.get(position)).resize(300, 300).centerCrop().into(im);

        return im;
    }



}