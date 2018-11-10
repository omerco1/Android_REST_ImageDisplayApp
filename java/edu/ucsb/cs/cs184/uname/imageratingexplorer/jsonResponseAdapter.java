package edu.ucsb.cs.cs184.uname.imageratingexplorer;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.volley.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class jsonResponseAdapter implements Response.Listener<JSONArray> {
    ArrayList<String> myArrList = new ArrayList<String>();
    ArrayList<Bitmap> myBM = new ArrayList<Bitmap>();
    ArrayList<Uri> myURIS = new ArrayList<Uri>();
    ArrayList<String> fnames = new ArrayList<String>();
    Context OnCreateContext;
    ImageRetriever mImageRetriever;
    ImageView im;
    GridView gv;
    ImageRatingDbHelper RDH = ImageRatingDbHelper.getInstance();
    MyAdapter gvAdapt;

    public jsonResponseAdapter(Context myOnCreateContext, ImageRetriever mImageRetriever, GridView gv, MyAdapter gvAdapt) {
        this.mImageRetriever = mImageRetriever;
        this.OnCreateContext = myOnCreateContext;
        this.im = im;
        this.gv = gv;
        this.gvAdapt = gvAdapt;
    }

    @Override
    public void onResponse(JSONArray response) {
        try {
            Log.d("Number of values",
                    Integer.toString(response.length()));
            Log.d("Sample Metadata",
                    ((JSONObject)response.get(100)).toString(2));


             myArrList = filterJSON(response);
            Log.d("FILTER", myArrList.get(31));
            Log.d("below filter",  Integer.toString(  myArrList.size()));


            //TODO GET URI AND POST IT TO DB HERE
            for (int i = 0; i < myArrList.size(); i++) {
                //RDH.addPost("", myArrList.get(i), 0);
//                Log.d("notNull", myArrList.get(i));
                gvAdapt.updateValues(myArrList.get(i));

            }
            gvAdapt.notifyDataSetInvalidated();
//            gvAdapt.notifyDataSetChanged();


//            Picasso.with(OnCreateContext)
//                    .load(myArrList.get(4)).resize(200, 200)
//                    .into(im);


//            for (int i = 0; i < myArrList.size(); i++ ) {
//                ImageDownloaderAdapter imgAD = new ImageDownloaderAdapter(myArrList.get(i));
//
//
//                //ERRROR HERE
////                Picasso.with(myOnCreateContext)
//////                        .load(myArrList.get(i))
//////                        .into(imgAD);
//
//                myURIS.add(imgAD.myUri);
//                Log.d("imgAD", imgAD.myUri.toString());
//            }
//
//            Log.d("myURIS", Integer.toString(myURIS.size()));
//            Log.d("exURI", myURIS.get(0).toString());


//                    for (int i = 0; i < myArrList.size(); i++ ) {
//                        bmArrList.add(getBitmapFromURL(myArrList.get(i)));
//                    }

            // im.setImageBitmap(bmArrList.get(2));
//                    ImageView im = (ImageView) findViewById(R.id.imageView);
//                    ImageRetriever.ImageDownloadAsync task = new ImageRetriever.ImageDownloadAsync(myOnCreateContext, im);
//                    for (int i = 0; i < mImageRetriever.myArr.size(); i++) {
//
//                        task.execute(mImageRetriever.myArr.get(i));
//                        mImageRetriever.bmArr.add(task.returnBM());
//
//                    }
//
//                    ArrayList<Uri> uriArr = new ArrayList<Uri>();
//                    for (int i = 0; i < mImageRetriever.bmArr.size(); i++) {
//
//                        uriArr.add(mImageRetriever.saveToInternalStorage(mImageRetriever.bmArr.get(i), myOnCreateContext, fnames.get(i)));
//                    }

        } catch (Exception e) {
            Log.d("myError" , "theres an errro");
            Log.d("stack", e.getMessage());
        }

    }

    public ArrayList<String> filterJSON(JSONArray rawJSON) {
        JSONArray newJSON = new JSONArray();
        ArrayList<String> myArr = new ArrayList<String>();

        try {
            for (int i = 0; i < rawJSON.length(); i++ ) {
                String h = ((JSONObject) rawJSON.get(i)).get("height").toString();
                int height = Integer.parseInt(h);
                if (height < 1024) {

                    String id = ((JSONObject) rawJSON.get(i)).get("id").toString();
                    String width = ((JSONObject) rawJSON.get(i)).get("width").toString();
                    String fname = ((JSONObject) rawJSON.get(i)).get("filename").toString();
                    String url = "https://picsum.photos/" + width + "/" + h + "/?image=" + id;
                    myArr.add(url);
                    fnames.add(fname);
                }
                else
                    continue;
            }

        } catch (Exception e) {

        }
        Log.d("FILTER", Integer.toString(myArr.size()));
        return myArr;
    }


}
