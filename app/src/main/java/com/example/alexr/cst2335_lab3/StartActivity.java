package com.example.alexr.cst2335_lab3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "1234 StartActivity";
    protected static final int REQUEST = 5;
    protected static final int CHAT_REQUEST = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Log.i(ACTIVITY_NAME, "In the onCreate() event");
        Button btnLaunchListItems = (Button)findViewById(R.id.btnLaunchListItems);
        btnLaunchListItems.setOnClickListener(
                (View v) -> startActivityForResult(new Intent(this, ListItemsActivity.class), REQUEST)
        );

        Button btnStartChat = (Button)findViewById(R.id.btnStartChat);
        btnStartChat.setOnClickListener(
                (View v) -> {
                    Log.i(ACTIVITY_NAME, "User clicked Start Chat");
                    startActivityForResult(new Intent(this, ChatWindow.class),CHAT_REQUEST);
                }
        );

        Button btnWeatherForecast = (Button)findViewById(R.id.btnWeatherForecast);
        btnWeatherForecast.setOnClickListener(
                (View v) -> {
                    Log.i(ACTIVITY_NAME, "User clicked Weather Forecast");
                    startActivityForResult(new Intent(this, WeatherForecast.class),CHAT_REQUEST);
                }
        );

    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        if (requestCode == REQUEST && responseCode == RESULT_OK) {
            Log.i(ACTIVITY_NAME, "Returned to StartActivity.onActivityResult");
            CharSequence messagePassed = "ListItemsActivity passed: " + data.getStringExtra("Response");
            Toast toast = Toast.makeText(this, messagePassed, Toast.LENGTH_LONG);
            toast.show();
        }else{
            super.onActivityResult( requestCode, responseCode, data );
        }

    }

    public void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME, "In the onResume() event");
    }

    public void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In the onStart() event");
    }

    public void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME, "In the onPause() event");
    }

    public void onStop(){
        super.onStop();
        Log.i(ACTIVITY_NAME, "In the onStop() event");
    }

    public void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In the onDestroy() event");
    }

}
