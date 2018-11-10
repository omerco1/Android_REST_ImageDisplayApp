package edu.ucsb.cs.cs184.uname.imageratingexplorer;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageDownloaderAdapter implements Target {

    Uri myUri;
    String url;

    public ImageDownloaderAdapter(String url) {
        this.url = url;

    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + url);
        try {
            file.createNewFile();
            FileOutputStream ostream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
            ostream.flush();
            ostream.close();
            myUri= (Uri.fromFile(file));

        } catch (IOException e) {
            Log.e("IOException", e.getLocalizedMessage());
        }

    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}
