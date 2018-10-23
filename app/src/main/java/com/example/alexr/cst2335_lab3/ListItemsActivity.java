package com.example.alexr.cst2335_lab3;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

public class ListItemsActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "1234 ListItemActivity";
    protected static final int PICTURE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);
        Log.i(ACTIVITY_NAME, "In the onCreate() event");


        takeThumbnailPicture();
        checkSwitch();
        checkboxListener();
    }


    private void takeThumbnailPicture(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, PICTURE_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICTURE_REQUEST && resultCode == RESULT_OK){
            updateButtonThumbnail(data.getExtras());
        }
    }

    private void updateButtonThumbnail(Bundle bundle){
        Bitmap imageBitmap = (Bitmap) bundle.get("data");
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), imageBitmap);

            /*  Source: https://stackoverflow.com/questions/17492312/changing-image-of-imagebutton
                Author: Sruit A. Suk
                Date: 2015-05-29
           */
        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setImageBitmap(imageBitmap);
    }

    private void checkSwitch() {
        /*  source: https://android--code.blogspot.com/2015/08/android-switch-button-listener.html
            Author: Unknown
            Date: 2015-08-22
        */
        Switch switchButton = (Switch) findViewById(R.id.switchButton);

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        CharSequence text = getString(R.string.switchOn);
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(ListItemsActivity.this, text, duration);
                        toast.show();
                    }else{
                        CharSequence text = getString(R.string.switchOff);
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(ListItemsActivity.this, text, duration);
                        toast.show();
                    }
                }
            }
        );
    }

    private void checkboxListener(){
        CheckBox checkBox = (CheckBox)findViewById(R.id.checkBox);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ListItemsActivity.this);
                    builder.setMessage(getString(R.string.dialogMessage))
                            .setTitle(getString(R.string.dialogTitle))
                            .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialogInterface, int id){
                                    Intent resultIntent = new Intent();
                                    resultIntent.putExtra("Response", "My information to share");
                                    setResult(RESULT_OK, resultIntent);
                                    finish();
                                }
                            })
                            .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialogInterface, int id){

                                }
                            })
                            .show();
                }
            }
        });
    }

    public void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In the onResume() event");
    }

    public void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In the onStart() event");
    }

    public void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In the onPause() event");
    }

    public void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In the onStop() event");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In the onDestroy() event");
    }

}


