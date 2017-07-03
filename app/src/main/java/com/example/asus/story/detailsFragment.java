package com.example.asus.story;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by asus on 6/28/2017.
 */

public class detailsFragment extends Fragment {
    TextView next1;
    EditText title,desc;
    ImageView img;
    private Bitmap bp = null;
    Uri choosenImage;
    private byte[] photo;
    private DatabaseHandler db;
    Spinner spinner;
    ArrayList<Categories> cat;
    Typeface monstRegular,monstBold;
    public detailsFragment(){

    }
    private Context context;
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        this.context=context;
        db = new DatabaseHandler(context);
         cat = new ArrayList<>(db.getAllCategory());
        Log.d("ARR","ARR"+cat);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v1 = inflater.inflate(R.layout.details_frag , container, false);
        next1 = (TextView) v1.findViewById(R.id.next1);
        title = (EditText) v1.findViewById(R.id.title);
        desc = (EditText) v1.findViewById(R.id.desc);
        img = (ImageView)getActivity().findViewById(R.id.upimg);
        monstRegular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Montserrat-Regular.ttf");
//         monstBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Montserrat-Bold.ttf");
        next1.setTypeface(monstRegular);
        title.setTypeface(monstBold);
        desc.setTypeface(monstRegular);
        next1.setCompoundDrawablePadding(10);
        spinner = (Spinner) v1.findViewById(R.id.spinner);

        List<String> list = new ArrayList<String>();
        for(int i = 0; i<cat.size();i++) {
            list.add(cat.get(i).toString());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list){

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(monstRegular);
                return v;
            }

            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(monstRegular);
                return v;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        next1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //NEXT FRAGMENT
                    Log.d("DTA", "SAVER" + ((Main2Activity) getActivity()).maindatasaver);

                    if (img.getDrawable() == null || bp == null) {
                        Toast.makeText(getActivity(), "Picture Please", Toast.LENGTH_SHORT).show();
                        img.invalidate();
                    } else if (title.getText().toString().trim().equals("")) {
                        Toast.makeText(getActivity(), "Title Please", Toast.LENGTH_SHORT).show();
                        title.setError("Title is required!");
                    } else if (desc.getText().toString().trim().equals("")) {
                        Toast.makeText(getActivity(), "Description Please", Toast.LENGTH_SHORT).show();
                        desc.setError("Desc is required!");
                    } else {

                        String Savetitle, Savedesc;
                        photo = profileImage(bp);
                        Log.d("PHOTO", "PIC" + photo);
                        if (photo != null) {
                            Savetitle = title.getText().toString();
                            Savedesc = desc.getText().toString();
                            String selected_cat = spinner.getSelectedItem().toString();
                            long cid = db.getCategoryId(selected_cat);

                            ((Main2Activity) getActivity()).maindatasaver.setStoryData(Savetitle, Savedesc, photo);
                            ((Main2Activity) getActivity()).maindatasaver.set_cat_id(cid);
                        }
                        ((Main2Activity) getActivity()).viewPager.setCurrentItem(1, true);

                   /* long sid = db.addstory(new Story(Savetitle, photo, Savedesc));
                    long cid = db.getCategoryId(selected_cat);
                    Log.d("CID"+cid,"SID"+sid);
                    long tid = db.createTransaction(sid,cid);
                    Log.d("tid",""+tid);
                    Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_SHORT).show(); */
                    img.setEnabled(false);
                   //        img.setImageResource(R.drawable.uploadimage);
                        title.setText("");
                        desc.setText("");
                }
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
               selectImage();
            }
        });


        //return inflater.inflate(R.layout.details_frag, container, false);

        return v1;
    }

    public void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        //Log.d("BP in SelectImage", bp.toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //boolean result=Utility.checkPermission(Profile.this);
                if (options[item].equals("Take Photo")) {       //  startActivityForResult(intent, REQUEST_CAMERA);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                  /*
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, 2);
                   */
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    intent.putExtra("crop", "true");
                    intent.putExtra("scale", true);
                    intent.putExtra("outputX",255);
                    intent.putExtra("outputY",255);
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, 3);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
              /*  //from camera
                if (resultCode == RESULT_OK) {
                bp = (Bitmap) data.getExtras().get("data");
                    image.setImageBitmap(bp);
                Log.d("IMAGE CAM",image.toString());
                }
               */
                //FOR crop
                if (resultCode == RESULT_OK) {
                    choosenImage = data.getData();
                    performCrop();
                }

            case 2:
                //from gallery
               /*
                if (resultCode == RESULT_OK) {
                    choosenImage = data.getData();

                    if (choosenImage != null) {

                        bp = decodeUri(choosenImage, 400);
                        image.setImageBitmap(bp);
                        Log.d("IMAGE GAL", image.toString());
                        break;
                    }
                }
                 */
                if (resultCode == RESULT_OK) {
                    choosenImage = data.getData();
                    performCrop();
                }
            case 3:
                if (resultCode == RESULT_OK) {
                    // get the returned data
                    Bundle extras = data.getExtras();
                    // get the cropped bitmap
                    bp = extras.getParcelable("data");
                //    ImageView img = (ImageView)getActivity().findViewById(R.id.upimg);
                    img.setImageBitmap(bp);
                    // get the returned data
                }
        }
    }

    private void performCrop() {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(choosenImage,"image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 255);
            cropIntent.putExtra("outputY", 255);

            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent,3);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT);

            toast.show();
        }
    }
    //Convert and resize our image to 400dp for faster uploading our images to DB
    protected Bitmap decodeUri(Uri selectedImage, int REQUIRED_SIZE) {

        try {

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage), null, o);

            // The new size we want to scale to
            // final int REQUIRED_SIZE =  size;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage), null, o2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Convert bitmap to bytes
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private byte[] profileImage(Bitmap b) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();

    }
}
