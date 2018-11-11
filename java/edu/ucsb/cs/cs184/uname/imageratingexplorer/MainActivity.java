package edu.ucsb.cs.cs184.uname.imageratingexplorer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.*;


public class MainActivity extends AppCompatActivity {
    ArrayList<String> myArrList = new ArrayList<String>();
    ArrayList<Bitmap> bmArrList = new ArrayList<Bitmap>();
    Context myOnCreateContext;
    ImageRetriever mImageRetriever;
    jsonResponseAdapter jsonAdapt;
    MyAdapter gvAdapter;
    GridView gv;

    ImageRatingDbHelper dbObj;

    FloatingActionButton cam;
    FloatingActionButton gal;



    public void populateDB(View view) {
        Log.d("popHere", "im here");


        mImageRetriever.listImagesRequest(jsonAdapt, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error.toString());
            }
        });

        Log.d("myArr", Integer.toString(jsonAdapt.myArrList.size()));

        //ImageView im = (ImageView) findViewById(R.id.imageView);

        //Picasso.with(myOnCreateContext).load(jsonAdapt.myArrList.get(4)).resize(500, 500).centerCrop().into(im);

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = (TextView) findViewById(R.id.textView);
        //final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        final Context myOnCreateContext = getApplicationContext();
        mImageRetriever = ImageRetriever.getInstance(this);
        RatingBar rb = (RatingBar) findViewById(R.id.ratingBar);



        ImageRatingDbHelper.initialize(this);

        final Bitmap myBmap;

        gv = (GridView) findViewById(R.id.gridView);
        gv.setNumColumns(2);
        gvAdapter = new MyAdapter(this);
        gv.setAdapter(gvAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                //Launch Fragment
                String tempUri = gvAdapter.getItem(position).toString();
                Log.d("tempUri", tempUri);
                int DbId = ImageRatingDbHelper.getInstance().getDbId(tempUri);
                if (DbId == -1) { Log.d("error", "DBID is -1"); }

                Log.d("position", Integer.toString(position));

                myDiaFrag  df = myDiaFrag.newInstance(DbId, tempUri,  ImageRatingDbHelper.getInstance().getRatingFor(DbId));
                df.show(getSupportFragmentManager(), "title");

            }
        });

        jsonAdapt = new jsonResponseAdapter(myOnCreateContext, mImageRetriever, gv, gvAdapter);
        //gridViewArrayAdapter.notifyDataSetChanged();

        rb.setRating(0);
        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                gvAdapter.clearArrayLists();
                gvAdapter.resetArrayLists(rating);
                //gvAdapter.notifyDataSetInvalidated();
                gvAdapter.notifyDataSetChanged();
            }
        });

        cam = (FloatingActionButton) findViewById(R.id.cameraFAB);
        cam.setImageResource(android.R.drawable.ic_menu_camera);
        gal = (FloatingActionButton) findViewById(R.id.galleryFAB);
        gal.setImageResource(android.R.drawable.ic_menu_gallery);

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Camera functionality has been left as an excercise for the grader", Toast.LENGTH_LONG);
                Log.d("Camera FAB" , "Im in cam onCLick listener");
            }
        });

        gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "gallary functionality has been left as an excercise for the grader", Toast.LENGTH_LONG);
//                Toast.makeText(myOnCreateContext, "hi", Toast.LENGTH_SHORT);
                Log.d("Galler FAB" , "Im in gal onCLick listener");

            }
        });

    }

    public void popBM(ArrayList<Bitmap> bmA) {
        bmArrList = bmA;


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ImageRatingDbHelper.getInstance().close();


        //grid view or recycler view to populate images.
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        // Show your dialog here (this is called right after onActivityResult)
    }


    public Bitmap getBitmapFromURL(String src) {


        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            Log.d("Bitmap fail", "there was a problem");
            e.printStackTrace();
            return null;
        }
//        String url = src;
//            HttpURLConnection urlConnection = null;
//            try {
//                URL uri = new URL(url);
//                urlConnection = (HttpURLConnection) uri.openConnection();
//
//                int statusCode = urlConnection.getResponseCode();
//
//                InputStream inputStream = urlConnection.getInputStream();
//                if (inputStream != null) {
//
//                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                    return bitmap;
//                }
//            } catch (Exception e) {
//                Log.d("URLCONNECTIONERROR", e.toString());
//                if (urlConnection != null) {
//                    urlConnection.disconnect();
//                }
//                Log.w("ImageDownloader", "Error downloading image from " + url);
//            } finally {
//                if (urlConnection != null) {
//                    urlConnection.disconnect();
//
//                }
//            }
//            Log.d("returning null", "null");
//            return null;
    }

    public void clearTable(View view) {
        ImageRatingDbHelper.getInstance().deleteDB();
        gvAdapter.myUrlList.clear();
        gvAdapter.myIdArr.clear();
        gvAdapter.notifyDataSetInvalidated();

    }

    public void setOutArr(ArrayList<String> myArrList) {
          for (int i = 0; i < myArrList.size(); i++)
            myArrList.add(myArrList.get(i));
    }





}
