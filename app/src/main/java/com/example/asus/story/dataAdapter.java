package com.example.asus.story;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by asus on 6/19/2017.
 */

public class dataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<Story> mstory;
    private LayoutInflater inflater;
    private AdapterCallback mAdapterCallback;
    Typeface monstRegular,monstBold;


    public dataAdapter(Context context, ArrayList<Story> story) {
        super();
        this.context = context;
        inflater= LayoutInflater.from(context);
        this.mstory = story;
    }


    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                    .inflate(R.layout.liststrory, parent,false);
        TextView txt = (TextView) view.findViewById(R.id.txtViewer);
        TextView likes = (TextView) view.findViewById(R.id.likes);
        TextView comments = (TextView) view.findViewById(R.id.comments);
        monstRegular = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Regular.ttf");
        monstBold = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Bold.ttf");
        txt.setTypeface(monstBold);
        likes.setTypeface(monstRegular);
        comments.setTypeface(monstRegular);
        MyHolder holder=new MyHolder(view,context);
        return holder;
    }
    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        Story current = mstory.get(position);
        myHolder.captionText.setText(current._caption);
        myHolder.descText = current._desc ;
        myHolder.img.setImageBitmap(convertToBitmap(current.getImage()));
        // load image into imageview using glide
        //URL imgurl = new URL(current.thumbnail)
       /* Glide.with(context).load(current._img)
                .into(myHolder.img);*/

    }

    // return total item from List
    @Override
    public int getItemCount() {
        return mstory.size();
    }



    class MyHolder extends RecyclerView.ViewHolder{

        TextView captionText;
        ImageView img;
        String descText;


        // create constructor to get widget reference
        public MyHolder(View itemView, Context context) {
            super(itemView);
            final Context context1 = context;
            captionText = (TextView) itemView.findViewById(R.id.txtViewer);
            img = (ImageView) itemView.findViewById(R.id.imgView);
            //descText = (TextView) itemView.findViewById(R.id.textView3);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        String s=descText.toString();
                        String t=captionText.getText().toString();

                        img.buildDrawingCache();
                        Bitmap bitmap = img.getDrawingCache();
                        ByteArrayOutputStream stream=new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                        byte[] image=stream.toByteArray();
                        String img_str = Base64.encodeToString(image, 0);

                        Intent i=new Intent(context1,popup.class);
                        i.putExtra("img",img_str);
                        i.putExtra("title",s);
                        i.putExtra("caption",t);

                        context1.startActivity(i);

                       // mAdapterCallback.onclickRecylcer(videoId);
                    } catch (ClassCastException exception) {
                        //Log.d("RecyclerView", "onClicked：" + getLayoutPosition()+videoId);
                    }
                }
            });


        }

    }
    //get bitmap image from byte array

    private Bitmap convertToBitmap(byte[] b) {

            return BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    public static interface AdapterCallback {
        void onclickRecylcer(String VD);
    }
}

