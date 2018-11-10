package edu.ucsb.cs.cs184.uname.imageratingexplorer;

/**
 * Created by Yi Ding 11/01.
 *
 * Much of this is based on the Volley documentation -- Android's REST api library.
 * https://developer.android.com/training/volley/
 */

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class ImageRetriever {
    public static final String REQ_LIST_IMG_METADATA = "https://picsum.photos/list";

    private static ImageRetriever mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mContext;
    public ArrayList<String> myArr = new ArrayList<String>();
    public ArrayList<Bitmap> bmArr = new ArrayList<Bitmap>();

    private ImageRetriever(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static synchronized ImageRetriever getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ImageRetriever(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }


    /**
     * This function will list all available images from Lorem Picsum and calls
     * successListener with the json object if successful
     */
    public void listImagesRequest(Response.Listener<JSONArray> successListener,
                                   Response.ErrorListener errorListener) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, REQ_LIST_IMG_METADATA, null, successListener, errorListener);
        mInstance.addToRequestQueue(jsonArrayRequest);
    }


    public Uri saveToInternalStorage(Bitmap bitmapImage, Context context, String filename){
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir


        File mypath=new File(directory,filename);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //return directory.getAbsolutePath();
        return Uri.fromFile(mypath);
    }


    ArrayList<Uri> uriFname = new ArrayList<Uri>();

    public static void imageDownload(Context ctx, String url) {


    }

    //target to save
    public static Target getTarget(final String url){


        Target target = new Target(){

            public Uri myUri;

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        return target;
    }




//    public static class ImageDownloadAsync extends AsyncTask<String,Void,Bitmap> {
//
//        Context context;
//        ImageView imageView;
//        Bitmap bm;
//
//        public ImageDownloadAsync(Context context, ImageView im) {
//            this.context = context;
//            this.imageView = im;
//        }
//
//        public Bitmap returnBM() {
//            return this.bm;
//        }
//
//        @Override
//        protected Bitmap doInBackground(String... Params) {
//            String myString = Params[0];
//            try {
//                URL url = new URL(myString);
//                Bitmap myBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                bm = myBitmap;
//                return myBitmap;
//            } catch (IOException e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            super.onPostExecute(bitmap);
//            //imageView.setImageBitmap(bitmap);
//        }
//    }




}