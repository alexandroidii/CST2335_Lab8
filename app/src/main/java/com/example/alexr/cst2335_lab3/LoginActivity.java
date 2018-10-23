package com.example.alexr.cst2335_lab3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DefaultDatabaseErrorHandler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends Activity {

    protected static final String ACTIVITY_NAME = "1234 LoginActivity";

    public void login(View view){

        EditText loginEmailEditText = (EditText)findViewById(R.id.loginEmailText);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.sharedPrefFile), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.defaultEmail),loginEmailEditText.getText().toString());
        editor.commit();

        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME, "In the onCreate() event");
    }

    public void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME, "In the onResume() event");
    }

    public void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In the onStart() event");

        EditText loginEmailEditText = (EditText)findViewById(R.id.loginEmailText);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.sharedPrefFile), Context.MODE_PRIVATE);
        String email = sharedPref.getString(getString(R.string.defaultEmail),getString(R.string.blankDefaultEmail));
        loginEmailEditText.setText(email);
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
