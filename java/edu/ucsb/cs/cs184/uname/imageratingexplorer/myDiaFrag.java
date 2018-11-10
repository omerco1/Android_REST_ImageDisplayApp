package edu.ucsb.cs.cs184.uname.imageratingexplorer;

import android.media.Rating;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class myDiaFrag extends DialogFragment {

    int id;
    String imageURL;
    float rate;
    ImageView im;
    RatingBar rb;



    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    static myDiaFrag newInstance(int param0, String param1, float param2) {
        myDiaFrag f = new myDiaFrag();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("id",param0);
        args.putString("imageURL", param1);
        args.putFloat("Rating", param2);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            id = getArguments().getInt("id");
            imageURL = getArguments().getString("imageURL");
            rate = getArguments().getFloat("Rating");
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.dialog_layout, container, false);
        View tv = v.findViewById(R.id.text);
        getDialog().setTitle("title");

        im = (ImageView) v.findViewById(R.id.fragImageView);
        rb = (RatingBar) v.findViewById(R.id.fragRatingBar);

        rb.setRating(rate);
        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,  boolean fromUser) {

                ImageRatingDbHelper.getInstance().setRating(id , rating);
            }
        });


        Picasso.with(getActivity()).load(imageURL).resize(1500, 1500).centerInside().into(im);

        return v;
    }




}
